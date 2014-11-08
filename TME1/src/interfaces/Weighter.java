package interfaces;

import java.util.HashMap;

import classes.SparseVector;

public interface Weighter {

	SparseVector getDocWeightsForDoc(String idDoc);
	HashMap<Integer, Double> getDocWeightsForStem(String stem);
	SparseVector getWeightsForQuery(HashMap<String, Integer> query);


}
