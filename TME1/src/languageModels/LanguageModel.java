package languageModels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import vectorModels.TFWeighter;

import classes.Index;
import classes.Rank;
import classes.SparseVector;
import interfaces.IRmodel;

public class LanguageModel implements IRmodel {

	private Index index;
	private float lambda;



	public LanguageModel(Index index, float lambda) {
		super();
		this.index = index;
		this.lambda = lambda;
	}

	private double calculateScore(SparseVector doc, SparseVector query, SparseVector corpusTf){
		return lambda*Math.log(query.dotProd(doc.divide(doc.getSum())))+(1-lambda)*Math.log(query.dotProd(corpusTf));///////////////////////////TF(CORPUS,TERM)/L(C)
	}

	@Override
	public Map<Integer, Double> getScores(HashMap<String, Integer> query)
			throws IOException {

		TFWeighter weighter = new TFWeighter(index);
		SparseVector queryVector = weighter.getWeightsForQuery(query);
		HashMap<Integer, Double> scores = new HashMap<Integer,Double>();
		HashMap<Integer, Double> relDocsIndex;

		for(Entry<String, Integer> e : query.entrySet()){
			relDocsIndex = index.getTfsForStem(e.getKey());

			SparseVector corpusTf; ///BUILD CA
			for(Integer i: relDocsIndex.keySet())
				scores.put(i,this.calculateScore(index.getTfsForDoc(i),queryVector,corpusTf));



		}


		return null;
	}

	@Override
	public ArrayList<Rank> getRanking(HashMap<String, Integer> query)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIndex(Index index) {
		this.index = index;
	}

}
