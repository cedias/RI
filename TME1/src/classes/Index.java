package classes;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map.Entry;

import parsing.DocParser;
import parsing.DocumentIter;



public class Index {
	
	private String name;
	private String filename;
	private RandomAccessFile index;
	private RandomAccessFile inverted_index;
	private DocParser parser;
	
	private BagOfWords bow;
	private HashMap<Integer, Long> docs; //docs positions
	private HashMap<Integer, Long> stems; //stem positions
	
	
	
	public Index(String filename, DocParser parser, String indexName) throws IOException {
		super();	
		this.name = indexName;
		this.filename = filename;
		this.parser = parser;
		this.bow = new BagOfWords();
		System.out.println(bow.size());
		buildIndexs();	
	}
	

	public Index(String filename, DocParser parser) throws IOException{
		this(filename,parser,filename);
	}
	
	
	private void buildIndexs() throws IOException {
		
		docs = new HashMap<Integer,Long>();
		stems = new HashMap<Integer,Long>();
		
		index = new RandomAccessFile(name+"_index", "rw");
		inverted_index = new RandomAccessFile(name+"_inverted", "rw");
		

		DocumentIter dociter = new DocumentIter(filename, parser);
		Stemmer stemmer = new Stemmer(); //get this out ?
		
		int id;
		int tf;
		HashMap<String, Integer> docStems = new HashMap<String,Integer>(10);
		HashMap<Integer,Integer> stemDocCount = new HashMap<Integer,Integer>(1500);
		
		
		/*
		 * First iteration, building index and counting doc per stems
		 */
		for(Document d: dociter){
			//Format: id:stemId-count stemId-count stemId-count stemId-count...\n
			docStems.putAll(stemmer.porterStemmerHash(d.getText()));
			docStems.putAll(stemmer.porterStemmerHash(d.getTitre()));
			docStems.remove(" * "); //useless key

			//docStems.putAll(d.getKeywords());  -- TODO add keywords
			//docStems.putAll(d.getAuteur(), 1); // -- TODO works for single named author
			
			docs.put(d.getId(), index.getFilePointer());
			index.writeChars(d.getId()+":");
			
			for(Entry<String, Integer> s : docStems.entrySet()){
				id = bow.getId(s.getKey());
				tf = s.getValue();
				index.writeChars(" "+id+"-"+tf);
				
				
				if(stemDocCount.containsKey(id))
					stemDocCount.put(id, stemDocCount.get(id)+1);
				else
					stemDocCount.put(id, 1);
			}
			index.writeChar('\n');
		
			docStems.clear();
		}
		
		/*
		 * Second Iteration, building inverted index
		 */
		dociter = new DocumentIter(filename, parser); //dociter works only once. -- TODO add exception "already used"
		
		for(Document d: dociter){
			//Format: id-stem:id id id id...\n
			//get stems
			//if stems exist -> add doc
			//else add stem + doc and save position in file
			
			
			
		}
	
	}
	


	
}
