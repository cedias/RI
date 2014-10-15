package interfaces;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

import classes.Index;

public interface IRmodel {

	HashMap<Integer,Double> getScores(HashMap<String, Integer> query) throws IOException;
	TreeMap<Double, Integer> getRanking(HashMap<String, Integer> query) throws IOException;
	
	void setIndex(Index index);
	
	
}
