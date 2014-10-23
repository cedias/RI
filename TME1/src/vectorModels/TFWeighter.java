package vectorModels;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import classes.BagOfWords;
import classes.Index;
import classes.SparseVector;
import interfaces.Weighter;

public class TFWeighter implements Weighter {

	Index index;

	/*
	 * w(t,d) = tf(t,d)
	 * w(t,q) = tf(t,q)
	 */


	public TFWeighter(Index index) {
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
	public HashMap<Integer, Integer> getDocWeightsForStem(String stem) {
		try {
			return index.getTfsForStem(stem);

		} catch (IOException e) {
			System.out.println("Error on docWeights");
			return null;
		}
	}

	@Override
	public SparseVector getWeightsForQuery(HashMap<String, Integer> query) {
		BagOfWords bow = index.getBow();

		SparseVector res = new SparseVector(bow.size());

		for(Entry<String, Integer> entry : query.entrySet())
			if(bow.containsKey(entry.getKey()))
				res.setValue(bow.get(entry.getKey()), entry.getValue());

		return res;
	}

}
