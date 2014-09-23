package parsing;

import java.util.Date;
import java.util.List;

import classes.Document;

public class CisiParser implements DocParser {

	
	@Override
	public Document getDocument(String text, long lastAddress, String filename) {
		return new Document(getId(text), getTitre(text), getDate(text), getAuteur(text), getKeywords(text), getText(text), filename, lastAddress);
	}

	private String getText(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<String> getKeywords(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getAuteur(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	private Date getDate(String text) {
		return null;
	}

	private String getTitre(String text) {
		return null;
	}

	private int getId(String text) {
		try{
			return Integer.parseInt(text.substring(2, text.indexOf('\n')));
		}
		catch(Exception e){
			System.err.println("Error in IDPARSE of terxt: "+text);
			return 0;
		}
	}
	
	

}
