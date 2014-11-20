package parsing.selector;

import java.util.HashMap;

import classes.Document;
import classes.Stemmer;

public class TitleSelector implements DocumentFieldSelector {

	@Override
	public HashMap<String, Integer> getStemsFromDocument(Document d, Stemmer s) {
		return s.porterStemmerHash(d.getTitre());
	}

}
