package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TextDumperParserTest {

  @Test
  void emptyAppointmentBookCanBeDumpedAndParsed() throws ParserException {
    AppointmentBook appointmentBook = new AppointmentBook("Test Appointment Book");
    AppointmentBook read = dumpAndParse(appointmentBook);
    assertThat(read, equalTo(appointmentBook));
  }

  private AppointmentBook dumpAndParse(AppointmentBook book) throws ParserException {
    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(book);

    String text = sw.toString();

    TextParser parser = new TextParser(new StringReader(text));
    return parser.parse();
  }

  @Test
  void dumpedTextCanBeParsed() throws ParserException {
    AppointmentBook appointmentBook = new AppointmentBook("Test Appointment Book");
    appointmentBook.addAppointment(new Appointment("Doctor's Appointment"));
    AppointmentBook read = dumpAndParse(appointmentBook);
    assertThat(read, equalTo(appointmentBook));
  }
}
