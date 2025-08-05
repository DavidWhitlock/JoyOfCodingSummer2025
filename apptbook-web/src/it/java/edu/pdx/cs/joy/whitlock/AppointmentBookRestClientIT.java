package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration test that tests the REST calls made by {@link AppointmentBookRestClient}
 */
@TestMethodOrder(MethodName.class)
class AppointmentBookRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AppointmentBookRestClient newAppointmentBookRestClient() {
    int port = Integer.parseInt(PORT);
    return new AppointmentBookRestClient(HOSTNAME, port);
  }

  @Test
  void test0RemoveAllAppointmentBooks() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    client.removeAllAppointmentBooks();
  }

  @Test
  void test2AddAppointment() throws IOException, ParserException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = "TEST WORD";
    String description = "TEST DEFINITION";
    Appointment appointment = new Appointment(description);
    client.addAppointment(owner, appointment);

    AppointmentBook book = client.getAppointmentBook(owner);
    assertThat(book.getOwnerName(), equalTo(owner));
    assertThat(book.getAppointments().iterator().next().getDescription(), equalTo(description));
  }

  @Test
  void test4EmptyWordThrowsException() {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String emptyString = "";

    RestException ex = assertThrows(RestException.class, () -> client.addAppointment(emptyString, new Appointment(emptyString)));
    assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
    assertThat(ex.getMessage(), containsString(Messages.missingRequiredParameter(AppointmentBookServlet.OWNER_PARAMETER)));  }

}
