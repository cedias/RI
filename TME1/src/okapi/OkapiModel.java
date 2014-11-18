package okapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import vectorModels.TFWeighter;

import classes.BagOfWords;
import classes.Index;
import classes.Rank;
import classes.SparseVector;
import interfaces.IRmodel;

public class OkapiModel implements IRmodel{

	private Index index;
	private double K1;
	private double B;

	public OkapiModel(Index index, double k1, double b) {
		super();
		this.index = index;
		K1 = k1;
		B = b;
	}

	private SparseVector calculateProbIDF(Set<String> terms) throws IOException{
		BagOfWords bow = index.getBow();
		SparseVector probIdf = new SparseVector(bow.size());

		for(String s: terms){
			if(!bow.containsKey(s))
				continue;

			int nb = index.getNbDocsWithStem(s);
			double max = Math.max(0, (bow.size()-nb+0.5)/(nb+0.5));
			probIdf.setValue(bow.get(s), max);
		}
		return probIdf;
	}

	private SparseVector calcultatePondTf(SparseVector tf){
		SparseVector a = tf.mult((K1+1));
		SparseVector b = tf.plus((1-B) + B*(tf.getSum()/(index.getCorpusSize()/index.getBow().size()+0.0)));

		return a.divideAll(b);
	}

	@Override
	public Map<Integer, Double> getScores(HashMap<String, Integer> query)
			throws IOException {

		HashMap<Integer, Double> scores = new HashMap<Integer,Double>();
		HashMap<Integer, Double> relDocsIndex;

		SparseVector probIdf = this.calculateProbIDF(query.keySet());

		for(Entry<String, Integer> e : query.entrySet()){
			relDocsIndex = index.getTfsForStem(e.getKey());

			if(relDocsIndex == null)
				continue;

			for(Integer i: relDocsIndex.keySet())
				scores.put(i,probIdf.dotProd(this.calcultatePondTf(index.getTfsForDoc(i))));
		}

		return scores;

	}

	@Override
	public ArrayList<Rank> getRanking(HashMap<String, Integer> query)
			throws IOException {
		HashMap<Integer, Double> scores = (HashMap<Integer, Double>) getScores(query);

		ArrayList<Rank> ranking = new ArrayList<Rank>();


		for(Entry<Integer,Double> score: scores.entrySet()){
			ranking.add(new Rank(score.getKey(),score.getValue()));
		}

		Collections.sort(ranking,Collections.reverseOrder());

		HashSet<Integer> relevant = new HashSet<Integer>(scores.keySet());
		HashSet<Integer> irrelevant = index.docIdSet();
		irrelevant.removeAll(relevant);

		for(Integer idIrr: irrelevant){
			ranking.add(new Rank(idIrr, -1));
		}

		return ranking;
	}

	@Override
	public void setIndex(Index index) {
		this.index = index;

	}

}
