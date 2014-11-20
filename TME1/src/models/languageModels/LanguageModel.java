package models.languageModels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import models.IRmodel;
import models.vectorModels.TFWeighter;
import classes.BagOfWords;
import classes.Index;
import classes.Rank;
import classes.SparseVector;

public class LanguageModel implements IRmodel {

	private Index index;
	private double lambda;



	public LanguageModel(Index index, double d) {
		super();
		this.index = index;
		this.lambda = d;
	}

	private double calculateScore(SparseVector doc, SparseVector query, SparseVector corpusTf){
		return lambda*Math.log(query.dotProd(doc.divide(doc.getSum())))+(1-lambda)*Math.log(query.dotProd(corpusTf));
	}

	@Override
	public HashMap<Integer, Double> getScores(HashMap<String, Integer> query)
			throws IOException {

		TFWeighter weighter = new TFWeighter(index);
		SparseVector queryVector = weighter.getWeightsForQuery(query);
		HashMap<Integer, Double> scores = new HashMap<Integer,Double>();
		HashMap<Integer, Double> relDocsIndex;
		SparseVector corpusTf = new SparseVector(index.getBow().size());
		BagOfWords bow = index.getBow();

		for(String s: query.keySet()){
			if(!bow.containsKey(s))
				continue;
			corpusTf.setValue(bow.get(s),index.getCorpusTfsForStem(s));
		}
		corpusTf = corpusTf.divide(index.getCorpusSize());

		for(Entry<String, Integer> e : query.entrySet()){
			relDocsIndex = index.getTfsForStem(e.getKey());

			if(relDocsIndex == null)
				continue;

			for(Integer i: relDocsIndex.keySet())
				scores.put(i,this.calculateScore(index.getTfsForDoc(i),queryVector,corpusTf));
		}

		return scores;
	}

	@Override
	public ArrayList<Rank> getRanking(HashMap<String, Integer> query)
			throws IOException {
		HashMap<Integer, Double> scores = getScores(query);

		ArrayList<Rank> ranking = new ArrayList<Rank>();
		
		double min = 0;

		for(Entry<Integer,Double> score: scores.entrySet()){
			ranking.add(new Rank(score.getKey(),score.getValue()));
			
			if(score.getValue() < min)
				min = score.getValue();
		}

		Collections.sort(ranking,Collections.reverseOrder());

		HashSet<Integer> relevant = new HashSet<Integer>(scores.keySet());
		HashSet<Integer> irrelevant = index.docIdSet();
		irrelevant.removeAll(relevant);

		for(Integer idIrr: irrelevant){
			ranking.add(new Rank(idIrr, min-1));
		}

		return ranking;
	}

	@Override
	public void setIndex(Index index) {
		this.index = index;
	}

}
