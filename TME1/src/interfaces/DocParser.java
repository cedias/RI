package interfaces;

import java.io.IOException;

import classes.Document;

public interface DocParser {

	Document getDocument(String text, long address, String filename);

	String getDocumentString(long address) throws IOException;
	String getFilename(); //forces filename attribute;
	void reset();

}
