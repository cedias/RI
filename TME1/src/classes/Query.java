package classes;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Query extends Document {
	
	int id;
	HashMap<String,Integer> query;
	
	
	public Query(int id, String titre, Date date, String auteur,
			List<String> keywords, String text, List<Integer> links,
			String filename, Long fileAdress) {
		super(id, titre, date, auteur, keywords, text, links, filename, fileAdress);
		
	}

}
