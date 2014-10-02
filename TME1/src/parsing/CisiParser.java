package parsing;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import classes.Document;

public class CisiParser implements DocParser {
	
	private int id;
	private String titre = null;
	private Date date = null;
	private String auteur = null;
	private List<String> keywords = null;
	private String text = null;
	private List<Integer> links = null;
	private String filename;
	
	public CisiParser(String filename){
		this.filename = filename;
	}
	
	@Override
	public Document getDocument(String text, long lastAddress, String filename) {
		parseCisiText(text);
		return new Document(this.id, this.titre, this.date, this.auteur, this.keywords, this.text, this.links, filename, lastAddress);
	}

	private void parseCisiText(String text){
		
		this.id = getId(text);
		
		
		String[] lines = text.split("\n");
		char currentCat = '#';
		StringBuffer buf = new StringBuffer();
		
		for(String line : lines){
		
			if(line.matches("^\\.(T|B|A|K|W|X|N)$")){
				
				switch(currentCat){
					case '#':
						break;
					case 'N': //Cacm
						break;
					case 'T':
						this.titre = buf.toString().trim();
						break;
					case 'B':
						this.date = null;
						break;
					case 'A':
						this.auteur = buf.toString().trim();
						break;
					case 'K':
						this.keywords = Arrays.asList(buf.toString().trim().split(","));
						break;
					case 'W':
						this.text = buf.toString().trim();
						break;
					case 'X':
						links = null;
						break;
				}
				
				
				
				currentCat = line.charAt(1);
				buf.setLength(0);
				continue;
			}
			
			if(currentCat == '#')
				continue;
			
			buf.append(line+'\n');
			
			
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

	@Override
	public String getDocumentString(long address) throws IOException {
		Pattern pattern = Pattern.compile("\\.I ");
		Matcher matcher;
		StringBuffer docBuff = new StringBuffer();
		
		RandomAccessFile raf = new RandomAccessFile(filename,"r");
		raf.seek(address);
		
		String line = raf.readLine();
		docBuff.append(line+"\n");
		
		matcher = pattern.matcher(line);
		while(!matcher.find()){
			line = raf.readLine();
			docBuff.append(line+"\n");
			matcher = pattern.matcher(line);
		}
		this.parseCisiText(docBuff.toString());
		raf.close();
		
		return this.text;
	}

	@Override
	public String getFilename() {
		return filename;
	}
	
	

}
