package evaluation;

import java.util.ArrayList;

import classes.Query;
import classes.Rank;

public class IRList {

	Query query;
	ArrayList<Rank> results;
	
	
	
	public IRList(Query query, ArrayList<Rank> results) {
		super();
		this.query = query;
		this.results = results;
	}
	
	
	
}
