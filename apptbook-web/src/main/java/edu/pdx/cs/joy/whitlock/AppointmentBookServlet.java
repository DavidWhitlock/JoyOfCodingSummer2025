package edu.pdx.cs.joy.whitlock;

import com.google.common.annotations.VisibleForTesting;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class AppointmentBookServlet extends HttpServlet
{
    static final String OWNER_PARAMETER = "owner";
    static final String DESCRIPTION_PARAMETER = "description";

    private final Map<String, String> dictionary = new HashMap<>();
    private final Map<String, AppointmentBook> appointmentBooks = new HashMap<>();

    /**
     * Handles an HTTP GET request from a client by writing the definition of the
     * word specified in the "word" HTTP parameter to the HTTP response.  If the
     * "word" parameter is not specified, all of the entries in the dictionary
     * are written to the HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        response.setContentType( "text/plain" );

        String owner = getParameter(OWNER_PARAMETER, request );
        if (owner != null) {
            log("GET " + owner);
            AppointmentBook book = this.appointmentBooks.get(owner);
            if (book == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                PrintWriter pw = response.getWriter();
                TextDumper dumper = new TextDumper(pw);
                dumper.dump(book);
                response.setStatus(HttpServletResponse.SC_OK);
            }

        } else {
            missingRequiredParameter(response, OWNER_PARAMETER);
        }
    }

    /**
     * Handles an HTTP POST request by storing the dictionary entry for the
     * "word" and "definition" request parameters.  It writes the dictionary
     * entry to the HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        response.setContentType( "text/plain" );

        String owner = getParameter(OWNER_PARAMETER, request );
        if (owner == null) {
            missingRequiredParameter(response, OWNER_PARAMETER);
            return;
        }

        String description = getParameter(DESCRIPTION_PARAMETER, request );
        if ( description == null) {
            missingRequiredParameter( response, DESCRIPTION_PARAMETER);
            return;
        }

        log("POST " + owner + " -> " + description);

        AppointmentBook book = getExistingAppointmentBook(owner);
        book.addAppointment(new Appointment(description));

        this.dictionary.put(owner, description);

        PrintWriter pw = response.getWriter();
        pw.println(Messages.definedWordAs(owner, description));
        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK);
    }

    private AppointmentBook getExistingAppointmentBook(String owner) {
        AppointmentBook book = this.appointmentBooks.get(owner);
        if (book == null) {
            book = new AppointmentBook(owner);
            this.appointmentBooks.put(owner, book);
        }
        return book;
    }

    /**
     * Handles an HTTP DELETE request by removing all dictionary entries.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        log("DELETE all dictionary entries");

        this.dictionary.clear();
        this.appointmentBooks.clear();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allDictionaryEntriesDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Writes the definition of the given word to the HTTP response.
     *
     * The text of the message is formatted with {@link TextDumper}
     */
    private void writeDefinition(String word, HttpServletResponse response) throws IOException {
        String definition = this.dictionary.get(word);

        if (definition == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } else {
            PrintWriter pw = response.getWriter();

            Map<String, String> wordDefinition = Map.of(word, definition);
            TextDumper dumper = new TextDumper(pw);
            dumper.dump(wordDefinition);

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * Writes all of the dictionary entries to the HTTP response.
     *
     * The text of the message is formatted with {@link TextDumper}
     */
    private void writeAllDictionaryEntries(HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        TextDumper dumper = new TextDumper(pw);
        dumper.dump(dictionary);

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

    @VisibleForTesting
    String getDefinition(String word) {
        return this.dictionary.get(word);
    }

    @Override
    public void log(String msg) {
      System.out.println(msg);
    }

    public AppointmentBook getAppointmentBook(String owner) {
        return this.appointmentBooks.get(owner);
    }

    public void addAppointment(String owner, String description) {
        getExistingAppointmentBook(owner).addAppointment(new Appointment(description));
    }
}
