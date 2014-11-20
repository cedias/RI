package classes;

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
import java.util.Set;

import classes.BagOfWords;
import classes.SparseVector;
import classes.Document;
import classes.Stemmer;
import parsing.DocParser;
import parsing.DocumentIter;
import parsing.selector.DocumentFieldSelector;



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
	private RandomAccessFile links_index;
	private RandomAccessFile invert_links_index;
	private DocParser parser;

	private BagOfWords bow;
	private HashMap<Integer, Long> docs; //docs positions (-1 == number of words)
	private HashMap<Integer, Long> stems; //stem positions
	private HashMap<Integer, Long> docsAdress; //documents address in files
	private HashMap<Integer, Long> linksAdress; //links adresses
	private HashMap<Integer, Long> invlinksAdress; //inverted links adresses

	private DocumentFieldSelector dfs;


	@SuppressWarnings("unchecked")
	public Index(String filename, DocParser parser, String indexName, DocumentFieldSelector dfs) throws IOException{
		super();
		this.name = indexName;
		this.filename = filename;
		this.parser = parser;
		this.dfs = dfs;

		try{
			File bowFile =  new File(name+".bow") ;
			File docsFile =  new File(name+".docs") ;
			File stemsFile =  new File(name+".stems") ;
			File addressFile =  new File(name+".addrs") ;
			File linksFile = new File(name+".links") ;
			File invertLinks = new File(name+".ilinks");
			index = new RandomAccessFile(name+"_index", "rw");
			inverted_index = new RandomAccessFile(name+"_inverted", "rw");
			links_index = new RandomAccessFile(name+"_links", "rw");
			invert_links_index = new RandomAccessFile(name+"_ilinks", "rw");

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

			ois =  new ObjectInputStream(new FileInputStream(linksFile)) ;
			this.linksAdress = (HashMap<Integer, Long>) ois.readObject() ;
			ois.close();

			ois =  new ObjectInputStream(new FileInputStream(invertLinks)) ;
			this.invlinksAdress = (HashMap<Integer, Long>) ois.readObject() ;
			ois.close();
		}
		catch(Exception e){
			this.bow = new BagOfWords();
			buildIndexs();
		}
	}


	public Pair<Integer, Integer> getDocLinkCount(int docId) throws IOException{
		if(!linksAdress.containsKey(docId)){
			System.err.println("doc with id:"+ docId+" has no links");
			return null;
		}

		links_index.seek(linksAdress.get(docId));

		int readInt = links_index.readInt();

		if(readInt != docId)
			System.err.println("read doc id differs from argument");

		int inLinks = links_index.readInt();
		int outLinks = links_index.readInt();

		return new Pair<Integer, Integer>(inLinks, outLinks);

	}

	public Set<Integer> getDocLinks(int docId) throws IOException{
		if(!linksAdress.containsKey(docId)){
			//System.err.println("doc with id:"+ docId+" has no links");
			return null;
		}

		links_index.seek(linksAdress.get(docId));

		int readInt = links_index.readInt();

		if(readInt != docId)
			System.err.println("read doc id differs from argument");

		@SuppressWarnings("unused")
		int inLinks = links_index.readInt();
		@SuppressWarnings("unused")
		int outLinks = links_index.readInt();

		if(readInt != docId)
			System.err.println("Read wrong docLinks");

		HashSet<Integer> linksRead = new HashSet<Integer>();

		while(true){
			readInt = links_index.readInt();

			if(readInt == -1)
				break; //end

			linksRead.add(readInt);

		}
		return linksRead;
	}

	public Set<Integer> getDocInLinks(int docId) throws IOException{
		if(!invlinksAdress.containsKey(docId)){
			//System.err.println("doc with id:"+ docId+" has no incoming links");
			return null;
		}

		invert_links_index.seek(invlinksAdress.get(docId));

		int readInt = invert_links_index.readInt();

		if(readInt != docId)
			System.err.println("read doc id differs from argument");

		HashSet<Integer> linksRead = new HashSet<Integer>();

		while(true){
			readInt = invert_links_index.readInt();

			if(readInt == -1)
				break; //end

			linksRead.add(readInt);

		}
		return linksRead;
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

	public int getNbDocs(){
		return docs.size();
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

	public int getNbDocsWithStem(String stem) throws IOException{
		HashMap<Integer, Double> tf = getTfsForStem(stem);
		int sum = 0;
		sum +=tf.size();
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
		HashMap<Integer,Integer> inLinks = new HashMap<Integer,Integer>(1500);

		docs = new HashMap<Integer,Long>(1500);
		stems = new HashMap<Integer,Long>(1500);
		docsAdress = new HashMap<Integer,Long>(1500);
		linksAdress = new HashMap<Integer,Long>(1500);
		invlinksAdress = new HashMap<Integer,Long>(1500);

		index = new RandomAccessFile(name+"_index", "rw");
		inverted_index = new RandomAccessFile(name+"_inverted", "rw");
		links_index = new RandomAccessFile(name+"_links", "rw");
		invert_links_index = new RandomAccessFile(name+"_ilinks", "rw");

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
			docStems.putAll(dfs.getStemsFromDocument(d, stemmer));
			docStems.remove(" * "); //useless key

			this.addLinksCount(d,inLinks);

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
		docStems.clear();

		HashMap<Integer,Integer> writeCount = new HashMap<Integer,Integer>(1500);
		HashMap<Integer,Integer> linksWriteCount = new HashMap<Integer,Integer>(inLinks.size());



		for(Document d: dociter){

			cpt2++;
			System.out.println("doc:"+cpt2+"/"+cpt);

			//Format: id-stem id-doc tf...-1 (all ints)
			//get stems
			docStems.putAll(dfs.getStemsFromDocument(d, stemmer));
			docStems.remove(" * "); //useless key

			if(inLinks.containsKey(d.getId()))
				this.indexLinks(d,inLinks.get(d.getId()));
			else
				this.indexLinks(d,0);

			this.indexInvertLinks(d,linksWriteCount,inLinks);

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

	public DocumentFieldSelector getDfs() {
		return dfs;
	}


	private void indexInvertLinks(Document d,
			HashMap<Integer, Integer> linksWriteCount,HashMap<Integer, Integer> inLinksCount) throws IOException {

		Set<Integer> links = d.getLinks();

		if(links == null || links.size() == 0){
			System.err.println("Document #"+d.getId() +" has no links");
			return;
		}
		links.remove(d.getId());
		int nodeId = d.getId();

		for(Integer link: links){

			if(linksWriteCount.containsKey(link)){
				int written = linksWriteCount.get(link);
				invert_links_index.seek(invlinksAdress.get(link));
				int readId = invert_links_index.readInt();

				if(readId != link)
					System.err.println("read Id differs");

				invert_links_index.skipBytes(4*written);
				invert_links_index.writeInt(nodeId);
				linksWriteCount.put(link, linksWriteCount.get(link)+1);
			}
			else
			{
				invert_links_index.seek(invert_links_index.length());
				invlinksAdress.put(link, invert_links_index.getFilePointer());
				invert_links_index.writeInt(link);
				invert_links_index.writeInt(nodeId);
				linksWriteCount.put(link, 1);

				for(int i=0; i<inLinksCount.get(link)-1;i++){
					invert_links_index.writeInt(0);
				}

				invert_links_index.writeInt(-1);
			}

		}



	}


	private void addLinksCount(Document d, HashMap<Integer, Integer> inLinks) {
		Set<Integer> links = d.getLinks();

		if(links == null || links.size() == 0){
			System.err.println("Document #"+d.getId() +" has no links");
			return;
		}
		links.remove(d.getId());

		for(Integer link: links){

			if(!inLinks.containsKey(link))
				inLinks.put(link, 1);
			else
				inLinks.put(link, inLinks.get(link)+1);
		}

	}


	private void indexLinks(Document d,int inLinks) throws IOException {
		Set<Integer> links = d.getLinks();

		if(links == null || links.size() == 0){
			System.err.println("Document #"+d.getId() +" has no links");
			return;
		}
		links.remove(d.getId()); // removing self-link

		int docId = d.getId();
		linksAdress.put(docId,links_index.getFilePointer());

		//format: docId inlinks outlinks link1 link2... -1
		links_index.writeInt(docId);
		links_index.writeInt(inLinks);
		links_index.writeInt(links.size());

		for(Integer link: links)
			links_index.writeInt(link);

		links_index.writeInt(-1);

		return;
	}


	public void saveIndex() throws IOException{
		File bowFile =  new File(this.name+".bow") ;
		File docsFile =  new File(this.name+".docs") ;
		File stemsFile =  new File(this.name+".stems") ;
		File addressFile =  new File(this.name+".addrs") ;
		File linksFile = new File(this.name+".links");
		File invLinksFile = new File(this.name+".ilinks");

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

		oos =  new ObjectOutputStream(new FileOutputStream(linksFile)) ;
		oos.writeObject(this.linksAdress) ;
		oos.close();

		oos =  new ObjectOutputStream(new FileOutputStream(invLinksFile)) ;
		oos.writeObject(this.invlinksAdress) ;
		oos.close();
	}

	public HashSet<Integer> docIdSet(){
		HashSet<Integer> res = new HashSet<Integer>(docs.keySet());
		res.remove(-1);
		return res;
	}


}
