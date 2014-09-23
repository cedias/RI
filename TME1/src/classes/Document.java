package classes;

import java.util.List;
import java.util.Date;

public class Document {
	
	int id;
	String titre;
	Date date;
	String auteur;
	List<String> keywords;
	String text;
	String filename;
	Long fileAdress;
	
	
	public Document(int id, String titre, Date date, String auteur,
			List<String> keywords, String text, String filename, Long fileAdress) {
		super();
		this.id = id;
		this.titre = titre;
		this.date = date;
		this.auteur = auteur;
		this.keywords = keywords;
		this.text = text;
		this.filename = filename;
		this.fileAdress = fileAdress;
	}
	
	public int getId() {
		return id;
	}
	public String getTitre() {
		return titre;
	}
	public Date getDate() {
		return date;
	}
	public String getAuteur() {
		return auteur;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public String getText() {
		return text;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public Long getFileAdress() {
		return fileAdress;
	}

}
