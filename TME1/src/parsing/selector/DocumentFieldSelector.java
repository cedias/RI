package parsing.selector;

import java.util.HashMap;

import classes.Document;
import classes.Stemmer;

public interface DocumentFieldSelector {

	HashMap<String, Integer> getStemsFromDocument(Document d, Stemmer s);
	
}
