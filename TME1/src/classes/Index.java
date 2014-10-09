package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import classes.BagOfWords;
import classes.SparseVector;
import classes.Document;
import classes.Stemmer;
import parsing.DocParser;
import parsing.DocumentIter;



public class Index {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String filename;
	private RandomAccessFile index;
	private RandomAccessFile inverted_index;
	private DocParser parser;
	
	private BagOfWords bow;
	private HashMap<Integer, Long> docs; //docs positions
	private HashMap<Integer, Long> stems; //stem positions
	private HashMap<Integer, Long> docsAdress; //documents address in files
	
	
	
	public Index(String filename, DocParser parser, String indexName) throws IOException{
		super();	
		this.name = indexName;
		this.filename = filename;
		this.parser = parser;
		
		try{
			File bowFile =  new File(filename+".bow") ;
			File docsFile =  new File(filename+".docs") ;
			File stemsFile =  new File(filename+".stems") ;
			File addressFile =  new File(filename+".addrs") ;
			index = new RandomAccessFile(name+"_index", "rw");
			inverted_index = new RandomAccessFile(name+"_inverted", "rw");
			
			ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(bowFile)) ;
			this.bow = (BagOfWords) ois.readObject() ;
			ois.close();
			
			ois =  new ObjectInputStream(new FileInputStream(docsFile)) ;
			this.docs = (HashMap<Integer, Long>) ois.readObject() ;
			ois.close();
			
			ois =  new ObjectInputStream(new FileInputStream(stemsFile)) ;
			this.stems = (HashMap<Integer, Long>) ois.readObject() ;
			ois.close();
			
			ois =  new ObjectInputStream(new FileInputStream(addressFile)) ;
			this.docsAdress = (HashMap<Integer, Long>) ois.readObject() ;
			ois.close();
		}
		catch(Exception e){
			this.bow = new BagOfWords();
			buildIndexs();	
		}
		
	}
	

	public Index(String filename, DocParser parser) throws IOException{
		this(filename,parser,filename);
	}
	
	public SparseVector getTfsForDoc(int docId) throws IOException{
		if(!docs.containsKey(docId)){
			System.err.println("no docs with id:"+ docId);
			return null;
		}
		
		index.seek(docs.get(docId));
		index.readInt(); //reads id; --TODO add check same ID ?
		HashMap<Integer,Integer> stemsRead = new HashMap<Integer,Integer>();
		
		boolean stemType = true;
		int stemId=0;
		int tfCount=0;
		
		while(true){
			
			int readInt = index.readInt();
			
			if(readInt == -1)
				break; //end
			
			if(stemType){
				stemId = readInt;
			}
			else{
				tfCount = readInt;
				stemsRead.put(stemId,tfCount);
			}
			stemType = !stemType ;
			
		}
		
		return new SparseVector(bow.size(), stemsRead);
		
		
	}
	
	public SparseVector getTfsForStem(String stem) throws IOException{
		
		if(!bow.containsKey(stem)){
			return null;
		}
		int stemId = bow.getId(stem);
		System.out.println("stemid:" +stemId);
		if(!docs.containsKey(stemId)){
			System.err.println("no stem with id:"+ stemId);
			return null;
		}
		int readInt;
		inverted_index.seek(stems.get(stemId));
		readInt = inverted_index.readInt(); //reads Stemid;
		HashMap<Integer,Integer> docsRead = new HashMap<Integer,Integer>();
		System.out.println("read:"+readInt);
		boolean docType = true;
		int docId=0;
		int dfCount=0;
		
		while(true){
			
			readInt = index.readInt();
			System.out.println("read:"+readInt);
			if(readInt == -1)
				break; //end
			
			if(docType){
				docId = readInt;
			}
			else{
				dfCount = readInt;
				docsRead.put(docId,dfCount);
			}
			docType = !docType ;
			
		}
		
		return new SparseVector(bow.size(), docsRead);
	}
	
	public String getStringDoc(int docId) throws IOException{
		return parser.getDocumentString(docsAdress.get(docId)) ;
	}
	
	
	private void buildIndexs() throws IOException {
		
		docs = new HashMap<Integer,Long>();
		stems = new HashMap<Integer,Long>();
		docsAdress = new HashMap<Integer,Long>();
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
		int cpt = 0;
		for(Document d: dociter){
			docsAdress.put(d.getId(),d.getFileAdress());
			
			cpt++;
			System.out.println("cpt:"+cpt);
			//Format: id int-id int-tf... -1 
			docStems.putAll(stemmer.porterStemmerHash(d.getText()));
			docStems.putAll(stemmer.porterStemmerHash(d.getTitre()));
			docStems.remove(" * "); //useless key

			//docStems.putAll(d.getKeywords());  -- TODO add keywords
			//docStems.putAll(d.getAuteur(), 1); // -- TODO works for single named author
			
			docs.put(d.getId(), index.getFilePointer());
			index.writeInt(d.getId());
			
			for(Entry<String, Integer> s : docStems.entrySet()){
				id = bow.getId(s.getKey());
				tf = s.getValue();
				index.writeInt(id);
				index.writeInt(tf);
				
				
				if(stemDocCount.containsKey(id))
					stemDocCount.put(id, stemDocCount.get(id)+1);
				else
					stemDocCount.put(id, 1);
			}
			index.writeInt(-1);
		
			docStems.clear();
		}
		System.out.println("Index created");
		
	
		
		/*
		 * Second Iteration, building inverted index
		 */
		dociter = new DocumentIter(filename, parser); //dociter works only once. -- TODO add exception "already used"
		long endAddress = inverted_index.getFilePointer();
		int cpt2=0;
		for(Document d: dociter){
			
			cpt2++;
			System.out.println("cpt:"+cpt2+"/"+cpt);
			
			//Format: id-stem id-doc tf...-1 (all ints)
			//get stems
			docStems.putAll(stemmer.porterStemmerHash(d.getText()));
			docStems.putAll(stemmer.porterStemmerHash(d.getTitre()));
			docStems.remove(" * "); //useless key
			//docStems.putAll(d.getKeywords());  -- TODO add keywords
			//docStems.putAll(d.getAuteur(), 1); // -- TODO works for single named author
			
			
			for(Entry<String, Integer> s : docStems.entrySet()){
				id = bow.getId(s.getKey());
				tf = s.getValue();
				
				
				if(stems.containsKey(id)){ //if stems exist -> add doc
					inverted_index.seek(stems.get(id));
					
					while(inverted_index.readInt() != 0 );
					inverted_index.seek(inverted_index.getFilePointer()-4);
					
					inverted_index.writeInt(d.getId());
					inverted_index.writeInt(tf);
					
				}
				else //else add stem + doc and save position in file
				{
					inverted_index.seek(endAddress);
					stems.put(id,inverted_index.getFilePointer());
					inverted_index.writeInt(id);
					inverted_index.writeInt(d.getId());
					inverted_index.writeInt(tf);
					
					for(int i =0; i<stemDocCount.get(id);i++){
						inverted_index.writeInt(0);
						inverted_index.writeInt(0);
					}
					
					inverted_index.writeInt(-1);
					endAddress = inverted_index.getFilePointer();
					
				}
			}
			
		}
		System.out.println("Inverted Index created");
		System.out.println("saving index");
		this.saveIndex(filename);
	}
	
	public void saveIndex(String filename) throws IOException{
		File bowFile =  new File(filename+".bow") ;
		File docsFile =  new File(filename+".docs") ;
		File stemsFile =  new File(filename+".stems") ;
		File addressFile =  new File(filename+".addrs") ;

		ObjectOutputStream oos =  new ObjectOutputStream(new FileOutputStream(bowFile)) ;
		oos.writeObject(this.bow) ;
		oos.close();
		
		oos =  new ObjectOutputStream(new FileOutputStream(docsFile)) ;
		oos.writeObject(this.docs) ;
		oos.close();
		
		oos =  new ObjectOutputStream(new FileOutputStream(stemsFile)) ;
		oos.writeObject(this.stems) ;
		oos.close();
		
		oos =  new ObjectOutputStream(new FileOutputStream(addressFile)) ;
		oos.writeObject(this.docsAdress) ;
		oos.close();
	}
	

	
}
