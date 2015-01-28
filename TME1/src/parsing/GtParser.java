package parsing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;



public class GtParser {
	
	
	public static HashMap<Integer, HashSet<Integer>> parse(String filename) throws FileNotFoundException {
		
		HashMap<Integer, HashSet<Integer>> rel = new HashMap<Integer, HashSet<Integer>>(100);
		
		Scanner in = new Scanner(new FileReader(filename));
		
		while(in.hasNextLine()){
			String[] line = in.nextLine().trim().replaceAll("\\s+", " ").split(" ");
	
			if(line[0].startsWith("#") || line.length != 4)
				continue;
			
			int queryId = Integer.parseInt(1+line[0]);
			int docId = Integer.parseInt(toNeatString(line[2]));
			
			
			if(!rel.containsKey(queryId)){
				rel.put(queryId, new HashSet<Integer>());
			}
			
			rel.get(queryId).add(docId);
			
			
		}
		
		in.close();
		return rel;
	}
	
	
	
	private static String toNeatString(String text){
		text = text.replace("/", "");
		text = "1"+text;
		return text;
	}
}
