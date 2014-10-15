package vectorModels;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import classes.BagOfWords;
import classes.Index;
import classes.SparseVector;
import interfaces.IRmodel;
import interfaces.Weighter;

public class Vectoriel implements IRmodel {

	Index index;
	Weighter weighter;
	
	public Vectoriel(Index index, Weighter weighter) {
		super();
		this.index = index;
		this.weighter = weighter;
	}
	
	@Override
	public HashMap<Integer, Double> getScores(HashMap<String, Integer> query) throws IOException {
		/*
		 * score: sim(d,q) = Ei docweights(i)*queryweights(i)/Racine(Ei docWeighs(I)^2 * Ej queryweights(j)^2)
		 */
		
		BagOfWords bow = index.getBow();
		HashMap<Integer, Double> scores;
		HashMap<Integer,Integer> stemWeights;
		SparseVector queryWeights = weighter.getWeightsForQuery(query);
		double sumDocWeights = 0;
		
		for(Entry<String, Integer> e : query.entrySet()){
			
			stemWeights = weighter.getDocWeightsForStem(e.getKey());
			
			for(Entry<Integer, Double> doc :stemWeights.toHashmap().entrySet()){
				weighter.getDocWeightsForDoc("")
			}
		
			
		
			
			
		}
		return scores;
	}

	@Override
	public TreeMap<Double, Integer> getRanking(HashMap<String, Integer> query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIndex(Index index) {
		this.index = index;
	}

	

}
