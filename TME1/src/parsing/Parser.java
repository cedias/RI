package parsing;

import classes.Document;


public abstract class Parser{
	
	
	String filename;
	
	abstract Document getDocument(String text);
	
	String nextDocument(){
		return filename;
		
	}
	
	
}
