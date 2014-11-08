package vectorModels;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.sun.jndi.url.dns.dnsURLContext;

import classes.BagOfWords;
import classes.Index;
import classes.SparseVector;
import interfaces.Weighter;

public class TFIDFWeighter implements Weighter {

	Index index;

	/*
	 * w(t,d) = tf(t,d)
	 * w(t,q) = idf(t,q)
	 */

	public TFIDFWeighter(Index index) {
		super();
		this.index = index;
	}


	@Override
	public SparseVector getDocWeightsForDoc(String idDoc) {
		int id = Integer.parseInt(idDoc);
		try {
			SparseVector docW =  index.getTfsForDoc(id);
			for(Double d : docW.values()){
				d = 1+Math.log(d);
			}
			return docW;

		} catch (IOException e) {
			System.out.println("Error on docWeights");
			return null;
		}

	}

	@Override
	public HashMap<Integer, Double> getDocWeightsForStem(String stem) {
		try {

			HashMap<Integer, Double> docW =  index.getTfsForStem(stem);

			if(docW == null){
				return null;
			}

			for(Double d : docW.values()){
				d = 1+Math.log(d);
			}
			return docW;

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
				try {
					res.setValue(bow.get(entry.getKey()), index.getIDFStem(entry.getKey()));
				} catch (IOException e) {
					System.out.println("Error on IDF");
					e.printStackTrace();
				}

		return res;
	}
}
