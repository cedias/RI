package vectorModels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
	Boolean normalized = true;
	
	public Vectoriel(Index index, Weighter weighter) {
		super();
		this.index = index;
		this.weighter = weighter;
	}
	
	public Vectoriel(Index index, Weighter weighter,Boolean normalized) {
		super();
		this.index = index;
		this.weighter = weighter;
		this.normalized = normalized;
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
		HashMap<Integer,SparseVector> relDocs = new HashMap<Integer,SparseVector>();
		
		for(Entry<String, Integer> e : query.entrySet()){
			
			stemWeights = weighter.getDocWeightsForStem(e.getKey());
			if(stemWeights != null){
				for(Entry<Integer, Integer> doc : stemWeights.entrySet()){
					if(!relDocs.containsKey(doc.getKey())){ //relevant doc doesn't exist 
						relDocs.put(doc.getKey(), new SparseVector(bow.size()));
					}
					
					relDocs.get(doc.getKey()).setValue(bow.get(e.getKey()), doc.getValue());
						
				}
			}
				
		}
		System.out.println(relDocs);
		scores = new HashMap<Integer, Double>();
		
		for(Entry<Integer,SparseVector> rel: relDocs.entrySet()){
			if(normalized)
				scores.put(rel.getKey(),rel.getValue().cosSim(queryWeights));
			else
				scores.put(rel.getKey(),rel.getValue().dotProd(queryWeights));
		}
		
		return scores;
	}

	@Override
	public TreeMap<Integer, Double> getRanking(HashMap<String, Integer> query) throws IOException {
		HashMap<Integer, Double> scores = getScores(query);
		MapDoubleValueComparator<Integer> comp = new MapDoubleValueComparator<Integer>(scores);
		TreeMap<Integer, Double> ranking = new TreeMap<Integer,Double>(comp);
		ranking.putAll(scores);
		
		
		HashSet<Integer> relevant = new HashSet<Integer>(ranking.keySet());
		HashSet<Integer> irrelevant = index.docIdSet();
		irrelevant.removeAll(relevant);
		
		for(Integer idIrr: irrelevant)
			ranking.put(idIrr,0.0);
		
		return ranking;
	}

	@Override
	public void setIndex(Index index) {
		this.index = index;
	}

	

}
