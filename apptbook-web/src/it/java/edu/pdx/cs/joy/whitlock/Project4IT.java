package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.InvokeMainTestCase;
import edu.pdx.cs.joy.UncaughtExceptionInMain;
import edu.pdx.cs.joy.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * An integration test for {@link Project4} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    void test0RemoveAllMappings() throws IOException {
      AppointmentBookRestClient client = new AppointmentBookRestClient(HOSTNAME, Integer.parseInt(PORT));
      client.removeAllAppointmentBooks();
    }

    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project4.class );
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.MISSING_ARGS));
    }

    @Test
    void test2MissingOwnerName() {
        MainMethodResult result = invokeMain( Project4.class, HOSTNAME, PORT );

        assertThat(result.getTextWrittenToStandardError(), containsString("Missing owner"));

        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, equalTo(""));
    }

    @Test
    void test3NoAppointmentsThrowsAppointmentBookRestException() {
        String owner = "WORD";
        try {
            invokeMain(Project4.class, HOSTNAME, PORT, owner);
            fail("Expected a RestException to be thrown");

        } catch (UncaughtExceptionInMain ex) {
            RestException cause = (RestException) ex.getCause();
            assertThat(cause.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        }
    }

    @Test
    void test4AddDefinition() {
        String word = "WORD";
        String definition = "DEFINITION";

        MainMethodResult result = invokeMain( Project4.class, HOSTNAME, PORT, word, definition );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(Messages.definedWordAs(word, definition)));

        result = invokeMain( Project4.class, HOSTNAME, PORT, word );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("WORD's appointment book with 1 appointments"));

    }
}