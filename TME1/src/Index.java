import java.io.RandomAccessFile;
import java.util.HashMap;


public class Index {
	
	private String name;
	private RandomAccessFile index;
	private RandomAccessFile inverted_index;
	private HashMap<String, Long> docs;
	private HashMap<String, Long> stems;
	private HashMap<String,Long> pos;
	

}
