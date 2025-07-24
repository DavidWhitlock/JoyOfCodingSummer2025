package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AppointmentBookServletTest {

  @Test
  void ownerParameterIsRequired() throws IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    // Nothing is written to the response's PrintWriter
    verify(pw, never()).println(anyString());
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "The required parameter \"owner\" is missing");
  }

  @Test
  void addAppointmentToAppointmentBook() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    String owner = "TEST OWNER";
    String description = "TEST DESCRIPTION";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn(owner);
    when(request.getParameter(AppointmentBookServlet.DESCRIPTION_PARAMETER)).thenReturn(description);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    assertThat(stringWriter.toString(), containsString(Messages.definedWordAs(owner, description)));

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    AppointmentBook book = servlet.getAppointmentBook(owner);
    assertThat(book.getOwnerName(), equalTo(owner));
    assertThat(book.getAppointments().iterator().next().getDescription(), equalTo(description));
  }

  @Test
  void fetchAppointmentBookForOwner() throws ServletException, IOException, ParserException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    String owner = "TEST OWNER";
    String description = "TEST DESCRIPTION";

    // Add an appointment to the book
    servlet.addAppointment(owner, description);

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn(owner);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    String text = stringWriter.toString();
    TextParser parser = new TextParser(new StringReader(text));
    AppointmentBook book = parser.parse();
    assertThat(book.getOwnerName(), equalTo(owner));
    assertThat(book.getAppointments().iterator().next().getDescription(), equalTo(description));

    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));
  }

  @Test
  void nonExistentAppointmentBookReturnsNotFound() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    String owner = "NON_EXISTENT_OWNER";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AppointmentBookServlet.OWNER_PARAMETER)).thenReturn(owner);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doGet(request, response);

    verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

}
