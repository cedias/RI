package classes;

import interfaces.DocParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import classes.BagOfWords;
import classes.SparseVector;
import classes.Document;
import classes.Stemmer;
import parsing.CisiParser;
import parsing.DocumentIter;



public class Index {

	/**
	 *
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	private String name;
	private String filename;
	private RandomAccessFile index;
	private RandomAccessFile inverted_index;
	private RandomAccessFile normalized_index;
	private DocParser parser;

	private BagOfWords bow;
	private HashMap<Integer, Long> docs; //docs positions (-1 == number of words)
	private HashMap<Integer, Long> stems; //stem positions
	private HashMap<Integer, Long> docsAdress; //documents address in files



	@SuppressWarnings("unchecked")
	public Index(String filename, DocParser parser, String indexName) throws IOException{
		super();
		this.name = indexName;
		this.filename = filename;
		this.parser = parser;

		try{
			File bowFile =  new File(name+".bow") ;
			File docsFile =  new File(name+".docs") ;
			File stemsFile =  new File(name+".stems") ;
			File addressFile =  new File(name+".addrs") ;
			index = new RandomAccessFile(name+"_index", "rw");
			inverted_index = new RandomAccessFile(name+"_inverted", "rw");
			normalized_index = new RandomAccessFile(name+"_normIndex", "rw");

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
		int readInt = index.readInt(); //reads id; --TODO add check same ID ?

		if(readInt != docId)
			System.err.println("Read wrong Stem");

		HashMap<Integer,Integer> stemsRead = new HashMap<Integer,Integer>();

		boolean stemType = true;
		int stemId=0;
		int tfCount=0;

		while(true){
			readInt = index.readInt();

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

	public long getCorpusSize(){
		return docs.get(-1);
	}

	public HashMap<Integer, Double> getTfsForStem(String stem) throws IOException{

		if(!bow.containsKey(stem)){
			System.err.println("no stem: "+stem);
			return null;
		}

		int stemId = bow.getId(stem);

		if(!stems.containsKey(stemId)){
			System.err.println("no stem with id:"+ stemId);
			return null;
		}

		int readInt;
		inverted_index.seek(stems.get(stemId));
		readInt = inverted_index.readInt(); //reads Stemid;

		if(readInt != stemId)
			System.err.println("Read wrong Stem");

		HashMap<Integer,Double> docsRead = new HashMap<Integer,Double>();

		boolean docType = true;

		int docId=0;
		int dfCount=0;

		while(true){

			readInt = inverted_index.readInt();

			if(readInt == -1)
				break; //end

			if(docType){
				docId = readInt;
			}
			else{
				dfCount = readInt;
				docsRead.put(docId,dfCount+0.0);
			}
			docType = !docType ;
		}
		return docsRead;
	}

	public int getCorpusTfsForStem(String stem) throws IOException{
		HashMap<Integer, Double> tf = getTfsForStem(stem);
		int sum = 0;
		for(Double d:tf.values()){
			sum+=d;
		}

		return sum;
	}

	public double getIDFStem(String stem) throws IOException{
		HashMap<Integer,Double> frequencies = getTfsForStem(stem);
		double stemCount = 0;
		for(double i: frequencies.values()){
			stemCount+=i;
		}

		return Math.log(docs.size()/(stemCount));

	}

	public String getStringDoc(int docId) throws IOException{
		return parser.getDocumentString(docsAdress.get(docId)) ;
	}



	public BagOfWords getBow() {
		return bow;
	}



	private void buildIndexs() throws IOException {
		long sizeCorpus = 0;
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

			if(d.getId() == 0){
				System.out.println("id == 0");
				System.exit(1);
			}

			cpt++;
			System.out.println("doc:"+cpt);
			//Format: id int-id int-tf... -1
			docStems.putAll(stemmer.porterStemmerHash(d.getText()));
			docStems.putAll(stemmer.porterStemmerHash(d.getTitre()));
			//docStems.putAll(d.getKeywords());  -- TODO add keywords
			//docStems.putAll(d.getAuteur(), 1); // -- TODO works for single named author
			docStems.remove(" * "); //useless key



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
		parser.reset();
		dociter = new DocumentIter(filename, parser); //dociter works only once. -- TODO add exception "already used"
		long endAddress = 0;
		int cpt2=0;
		HashMap<Integer,Integer> writeCount = new HashMap<Integer,Integer>(1500);
		docStems.clear();

		for(Document d: dociter){

			cpt2++;
			System.out.println("doc:"+cpt2+"/"+cpt);

			//Format: id-stem id-doc tf...-1 (all ints)
			//get stems
			docStems.putAll(stemmer.porterStemmerHash(d.getText()));
			docStems.putAll(stemmer.porterStemmerHash(d.getTitre()));
			//docStems.putAll(d.getKeywords());  -- TODO add keywords
			//docStems.putAll(d.getAuteur(), 1); // -- TODO works for single named author
			docStems.remove(" * "); //useless key



			for(Entry<String, Integer> s : docStems.entrySet()){

				id = bow.get(s.getKey());

				tf = s.getValue();
				sizeCorpus += tf;

				if(stems.containsKey(id)){ //if stems exist -> add doc


					inverted_index.seek(stems.get(id));

					int written = writeCount.get(id);
					int readId = inverted_index.readInt();

					if(readId != id)
						System.err.println("Stem: "+s.getKey()+  " - Id read differs, read "+readId+" expecting "+id +" address="+stems.get(id) +" written:" +written + "/"+stemDocCount.get(id));

					if(written > stemDocCount.get(id))
						System.err.println("written to much: Stem: "+s.getKey() +" ("+written+"/"+stemDocCount.get(id)+ ")");

					inverted_index.skipBytes(4*written*2);
					inverted_index.writeInt(d.getId());
					inverted_index.writeInt(tf);
					writeCount.put(id, written+1);

				}
				else //else add stem + doc and save position in file
				{
					inverted_index.seek(endAddress);
					stems.put(id,inverted_index.getFilePointer());

					inverted_index.writeInt(id);
					inverted_index.writeInt(d.getId());
					inverted_index.writeInt(tf);

					for(int i=0; i<stemDocCount.get(id)-1;i++){
						inverted_index.writeInt(0);
						inverted_index.writeInt(0);
					}
					inverted_index.writeInt(-1);
					writeCount.put(id, 1); //wrote 1
					endAddress = inverted_index.getFilePointer();

				}

			}
			docStems.clear();

		}
		System.out.println("Inverted Index created");
		System.out.println("saving index");

		docs.put(-1, sizeCorpus); //sets number of words in corpus

		this.saveIndex();
	}

	public void saveIndex() throws IOException{
		File bowFile =  new File(this.name+".bow") ;
		File docsFile =  new File(this.name+".docs") ;
		File stemsFile =  new File(this.name+".stems") ;
		File addressFile =  new File(this.name+".addrs") ;

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

	public HashSet<Integer> docIdSet(){
		return new HashSet<Integer>(docs.keySet());
	}


}
