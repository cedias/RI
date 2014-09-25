package parsing;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import classes.Document;

public class DocumentIter implements Iterable<Document> {

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
	

	
	@SuppressWarnings("resource")
	public DocumentIter(String filename, DocParser parser) throws FileNotFoundException {
		super();
		this.filename = filename;
		this.parser = parser;
		
		raf = new RandomAccessFile(filename,"r");
		
	}
	

	@Override
	public Iterator<Document> iterator() {
		return new Iterator<Document>() {

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
			public Document next() {
				String doc = docBuff.toString();
				docBuff.setLength(0);
				return parser.getDocument(doc,lastAddress,filename);
				
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
				
			}
			
			

		};
	}

}
