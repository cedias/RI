package parsing.selector;

import java.util.HashMap;

import classes.Document;
import classes.Stemmer;

public class TextSelector implements DocumentFieldSelector {

	@Override
	public HashMap<String, Integer> getStemsFromDocument(Document d, Stemmer s) {
		return s.porterStemmerHash(d.getText());
	}

}
