package vectorModels;

import java.io.IOException;
import java.util.HashMap;

import classes.Index;
import classes.SparseVector;

public class SimpleWeighter implements Weighter {

	Index index;
	
	/*
	 * w(t,d) = tf(t,d) 
	 * w(t,q) = 1 si t in query 0 sinon
	 */
	
	
	public SimpleWeighter(Index index) {
		super();
		this.index = index;
	}

	@Override
	public SparseVector getDocWeightsForDoc(String idDoc) {
		int id = Integer.parseInt(idDoc);
		try {
			return index.getTfsForDoc(id);
			
			
		} catch (IOException e) {
			System.out.println("Error on docWeights");
			return null;
		}
		
	}

	@Override
	public SparseVector getDocWeightsForStem(String stem) {
		try {
			return index.getTfsForStem(stem);		
			
		} catch (IOException e) {
			System.out.println("Error on docWeights");
			return null;
		}
	}

	@Override
	public SparseVector getWeightsForQuery(HashMap<String, Integer> query) {
		
		return null;
	}

}
