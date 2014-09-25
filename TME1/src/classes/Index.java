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
	private HashMap<String, Long> stems; //stem positions
	
	
	
	public Index(String filename, DocParser parser, String indexName) throws IOException {
		super();	
		this.name = indexName;
		this.filename = filename;
		this.parser = parser;
		this.bow = new BagOfWords();
		System.out.println(bow.size());
		buildIndex();	
	}
	

	public Index(String filename, DocParser parser) throws IOException{
		this(filename,parser,filename);
	}
	
	
	private void buildIndex() throws IOException {
		
		docs = new HashMap<Integer,Long>();
		docStems = new HashMap<String,Integer>();
		
		index = new RandomAccessFile(name+"_index", "rw");
		inverted_index = new RandomAccessFile(name+"_inverted", "rw");
		

		DocumentIter dociter = new DocumentIter(filename, parser);
		Stemmer stemmer = new Stemmer(); //get this out ?
		
		int id;
		int tf;
		HashMap<String, Integer> stems = new HashMap<String,Integer>(10000);
		
		for(Document d: dociter){
			
			docStems.putAll(stemmer.porterStemmerHash(d.getText()));
			docStems.putAll(stemmer.porterStemmerHash(d.getTitre()));
			docStems.remove(" * "); //useless key

			//stemmedText.putAll(d.getKeywords());  -- TODO add keywords
			//stems.put(d.getAuteur(), 1); // -- TODO works for single named author
			docs.put(d.getId(), index.getFilePointer());
			index.writeChars("ID:"+d.getId()+" ");
			
			for(Entry<String, Integer> s : stems.entrySet()){
				id = bow.getId(s.getKey());
				tf = s.getValue();
				index.writeChars("("+id+";"+tf+") ");
				
			}
			index.writeChar('\n');
		
			docStems.clear();
		}
		
		
		
		
	
	}
}
