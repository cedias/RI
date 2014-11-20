package models.compModels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.IRmodel;
import sun.awt.geom.AreaOp.AddOp;
import classes.Index;
import classes.Rank;

public class linearCombModels implements IRmodel {
	
	List<IRmodel> models = new ArrayList<IRmodel>();
	List<Double> coef = new ArrayList<Double>();
	private Index index;

	@Override
	public Map<Integer, Double> getScores(HashMap<String, Integer> query)
			throws IOException {
		HashMap <Integer,Double> finalScores = new HashMap<Integer,Double>(); 
		Map<Integer, Double> scores;
		
		int cpt = 0;
		for(IRmodel m : models){
			scores = m.getScores(query);
			
			for(Entry<Integer, Double> e : scores.entrySet()){
				
				if(!finalScores.containsKey(e.getKey()))
					finalScores.put(e.getKey(),coef.get(cpt)*e.getValue()); 
				else
					finalScores.put(e.getKey(),finalScores.get(e.getKey())+coef.get(cpt)*e.getValue()); 
			}
			cpt++;
		}
		
		return finalScores;
	}

	@Override
	public ArrayList<Rank> getRanking(HashMap<String, Integer> query)
			throws IOException {
		
		Map<Integer, Double> scores = getScores(query);

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
	
	public void add(IRmodel mod,double coeff){
		models.add(mod);
		coef.add(coeff);
	}

}
