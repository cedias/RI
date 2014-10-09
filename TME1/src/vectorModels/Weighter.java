package vectorModels;

import java.util.HashMap;

import classes.SparseVector;

public interface Weighter {

	SparseVector getDocWeightsForDoc(String idDoc);
	SparseVector getDocWeightsForStem(String stem);
	SparseVector getWeightsForQuery(HashMap<String, Integer> query);
	
	
}
