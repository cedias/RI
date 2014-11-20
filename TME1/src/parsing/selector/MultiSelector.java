package parsing.selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import classes.Document;
import classes.Stemmer;

public class MultiSelector implements DocumentFieldSelector {

	List<DocumentFieldSelector> fs = new ArrayList<DocumentFieldSelector>();
	
	@Override
	public HashMap<String, Integer> getStemsFromDocument(Document d, Stemmer s) {
		HashMap<String, Integer> selected = new HashMap<String, Integer>();
		
		if(fs.size() == 0)
			System.err.println("NO FIELD SELECTOR DUDE!");
		
		for (DocumentFieldSelector selector : fs) {
			selected.putAll(selector.getStemsFromDocument(d, s));
		}
		
		return selected ;
	}
	
	public void add(DocumentFieldSelector ds){
		fs.add(ds);
	}

}
