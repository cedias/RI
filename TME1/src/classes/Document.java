package classes;

import java.util.List;
import java.util.Date;
import java.util.Set;

public class Document {

	int id;
	String titre;
	Date date;
	String auteur;
	List<String> keywords;
	Set<Integer> links;
	String text;
	String filename;
	Long fileAdress;



	public Document(int id, String titre, Date date, String auteur,
			List<String> keywords, String text,Set<Integer> links, String filename, Long fileAdress) {
		super();
		this.id = id;
		this.titre = titre;
		this.date = date;
		this.auteur = auteur;
		this.keywords = keywords;
		this.text = text;
		this.filename = filename;
		this.fileAdress = fileAdress;
		this.links = links;
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

	public Set<Integer> getLinks(){
		return links;
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

	@Override
	public String toString() {
		return "Document [id=" + id + ", titre=" + titre + ", date=" + date
				+ ", auteur=" + auteur + ", keywords=" + keywords + ", links="
				+ links + ", text=" + text + ", filename=" + filename
				+ ", fileAdress=" + fileAdress + "]";
	}


}
