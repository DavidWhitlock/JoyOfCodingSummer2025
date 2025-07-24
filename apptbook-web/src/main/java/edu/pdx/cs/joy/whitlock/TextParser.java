package edu.pdx.cs.joy.whitlock;

import edu.pdx.cs.joy.AppointmentBookParser;
import edu.pdx.cs.joy.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser implements AppointmentBookParser<AppointmentBook> {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  public Map<String, String> parseAsMap() throws ParserException {
    Pattern pattern = Pattern.compile("(.*) : (.*)");

    Map<String, String> map = new HashMap<>();

    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {

      for (String line = br.readLine(); line != null; line = br.readLine()) {
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
          throw new ParserException("Unexpected text: " + line);
        }

        String word = matcher.group(1);
        String definition = matcher.group(2);

        map.put(word, definition);
      }

    } catch (IOException e) {
      throw new ParserException("While parsing dictionary", e);
    }

    return map;
  }

  @Override
  public AppointmentBook parse() throws ParserException {
    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {

      AppointmentBook book = null;
      for (String line = br.readLine(); line != null; line = br.readLine()) {
        if (book == null) {
          book = new AppointmentBook(line);

        } else {
          Appointment appointment = new Appointment(line);
          book.addAppointment(appointment);
        }
      }

      if (book != null) {
        return book;

      } else {
        throw new ParserException("No owner line found in the text input");
      }

    } catch (IOException e) {
      throw new ParserException("While parsing dictionary", e);
    }
  }
}
