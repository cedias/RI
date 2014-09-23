package parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import classes.Document;

public class CisiParser implements DocParser {
	
	private int id;
	private String titre;
	private Date date;
	private String auteur;
	private List<String> keywords;
	private String text;
	
	@Override
	public Document getDocument(String text, long lastAddress, String filename) {
		parseCisiText(text);
		return new Document(id, titre, date, auteur, keywords, this.text, filename, lastAddress);
	}

	private void parseCisiText(String text){
		
		this.id = getId(text);
		
		
		String[] lines = text.split("\n");
		char currentCat = '#';
		StringBuffer buf = new StringBuffer();
		
		for(String line : lines){
		
			if(line.matches("\\.(T|B|A|K|W|X)")){
				
				switch(currentCat){
					case 'T':
						title = buf.s
						break;
					case 'B':
						break;
					case 'A':
						break;
					case 'K':
						break;
					case 'W':
						break;
					case 'X':
						break;
				}
				
				System.out.println(currentCat + ": "+buf.toString()+"\n");
				
				currentCat = line.charAt(1);
				
				buf.setLength(0);
				continue;
			}
			if(currentCat == '#')
				continue;
			
			buf.append(line);
			
			
		}
		
	
			
			
		
	}
	

	private int getId(String text) {
		try{
			return Integer.parseInt(text.substring(3, text.indexOf('\n')));
		}
		catch(Exception e){
			System.err.println("Error in IDPARSE of terxt: "+text);
			return 0;
		}
	}
	
	

}
