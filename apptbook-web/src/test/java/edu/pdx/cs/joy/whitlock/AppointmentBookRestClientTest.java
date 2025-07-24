package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppointmentBookRestClientTest {

  @Test
  void getAppointmentBookPerformsHttpGetWithOwnerParameter() throws ParserException, IOException {
    String owner = "TEST OWNER";
    String description = "TEST DESCRIPTION";

    AppointmentBook book = new AppointmentBook(owner);
    book.addAppointment(new Appointment(description));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(AppointmentBookServlet.OWNER_PARAMETER, owner)))).thenReturn(appointmentBookAsText(book));

    AppointmentBookRestClient client = new AppointmentBookRestClient(http);

    assertThat(client.getAppointmentBook(owner), equalTo(book));
  }

  private HttpRequestHelper.Response appointmentBookAsText(AppointmentBook dictionary) {
    StringWriter writer = new StringWriter();
    new TextDumper(writer).dump(dictionary);

    return new HttpRequestHelper.Response(writer.toString());
  }
}
