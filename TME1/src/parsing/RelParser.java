package parsing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;



public class RelParser {
	
	
	
	public static HashMap<Integer, HashSet<Integer>> parse(String filename) throws FileNotFoundException {
		
		HashMap<Integer, HashSet<Integer>> rel = new HashMap<Integer, HashSet<Integer>>(100);
		
		Scanner in = new Scanner(new FileReader(filename));
		
		while(in.hasNextLine()){
			String[] line = in.nextLine().trim().replaceAll("\\s+", " ").split(" ");
			
			int queryId = Integer.parseInt(line[0]);
			int docId = Integer.parseInt(line[1]);
			
			
			if(!rel.containsKey(queryId)){
				rel.put(queryId, new HashSet<Integer>());
			}
			
			rel.get(queryId).add(docId);
			
			
		}
		
		in.close();
		return rel;
	}
	
	
}
