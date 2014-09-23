package parsing;

import java.util.Date;
import java.util.List;

import classes.Document;

public interface DocParser {

	Document getDocument(String text, long lastAddress, String filename);

	
}
