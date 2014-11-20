package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import classes.Index;
import classes.Rank;

public interface IRmodel {

	Map<Integer, Double> getScores(HashMap<String, Integer> query) throws IOException;
	ArrayList<Rank> getRanking(HashMap<String, Integer> query) throws IOException;
	
	void setIndex(Index index);
	
	
}
