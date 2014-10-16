package classes;

import java.util.HashSet;


public class Query {

	public HashSet<Integer> releventDocuments;
	public Document d;
	
	public Query(Document d, HashSet<Integer> releventDocuments) {
		this.d = d;
		this.releventDocuments = releventDocuments;
		
		if(releventDocuments == null)
			System.err.println("Query #"+d.id+" doesn't have any relevent documents");
	}
	
	public String toString(){
		try {
			return d.toString()+"\n"+releventDocuments.toString();
		} catch (NullPointerException e) {
			return d.toString();
		}
		
	}
	
	
	

}
