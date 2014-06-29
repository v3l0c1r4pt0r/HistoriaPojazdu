package tk.v3l0c1r4pt0r.cepik;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class ParsingErrorHandler implements ErrorHandler {
		  public void error(SAXParseException e) {
		    log(Level.SEVERE, "Error", e);
		  }
		  public void fatalError(SAXParseException e) {
		    log(Level.SEVERE, "Fatal Error", e);
		  }
		  public void warning(SAXParseException e) {
		    log(Level.WARNING, "Warning", e);
		  }
		  private Logger logger = Logger.getLogger("com.mycompany");
		  private void log(Level level, String message, SAXParseException e) {
		    int line = e.getLineNumber();
		    int col = e.getColumnNumber();
		    String publicId = e.getPublicId();
		    String systemId = e.getSystemId();

		    message = message + ": " + e.getMessage() + ": line=" + line + ", col=" + col + ", PUBLIC="
		        + publicId + ", SYSTEM=" + systemId;

		    logger.log(level, message);
		  }
}
