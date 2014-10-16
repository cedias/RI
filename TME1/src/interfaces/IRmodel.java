package interfaces;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import classes.Index;

public interface IRmodel {

	Map<Integer, Double> getScores(HashMap<String, Integer> query) throws IOException;
	TreeMap<Integer, Double> getRanking(HashMap<String, Integer> query) throws IOException;
	
	void setIndex(Index index);
	
	
}
