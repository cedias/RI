package parsing;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import classes.Document;
import classes.Query;

public class QueryIter implements Iterable<Query> {

	String filename;
	DocParser parser;
	private RandomAccessFile raf;
	private static Pattern pattern = Pattern.compile("\\.I ");
    private static Matcher matcher;
	StringBuffer docBuff = new StringBuffer();
	String line;
	long lastAddress;
	long currentAdress;
	boolean start = true;
	boolean end = true;
	HashMap<Integer, HashSet<Integer>> relevents;
	

	
	public QueryIter(String qryFilename,String relFilename, DocParser parser) throws FileNotFoundException {
		super();
		this.filename = qryFilename;
		this.parser = parser;
		this.relevents = GtParser.parse(relFilename); //////////////////////////////WTF TODO
		raf = new RandomAccessFile(filename,"r");
		
	}
	
	public void reset() throws FileNotFoundException{
		raf = new RandomAccessFile(filename,"r");
		this.start = true;
		this.end = true;
	}

	@Override
	public Iterator<Query> iterator() {
		return new Iterator<Query>() {

			@Override
			public boolean hasNext() {
				try{
					if(start){
						line ="";
						matcher = pattern.matcher(line);
						while(!matcher.find()){
							line = raf.readLine();
			
							matcher = pattern.matcher(line);
						}
						
						start = false;
					}
					
					if(!end){
						raf.close();
					}
					lastAddress = raf.getFilePointer();
						
						docBuff.append(line+"\n");
						line = raf.readLine();
						matcher = pattern.matcher(line);
						while(!matcher.find()){
							docBuff.append(line+"\n");
							line = raf.readLine();
							matcher = pattern.matcher(line);
						}
						
						return true;
					
						
				} catch (Exception e) {
					if(end){
						end = false;
						return true;
					}
					return false;
				}
				

				
			}

			@Override
			public Query next() {
				String doc = docBuff.toString();
				docBuff.setLength(0);
				Document d = parser.getDocument(doc,lastAddress,filename);
				return new Query(d, relevents.get(d.getId()));
				
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
				
			}
			
			

		};
	}

}
