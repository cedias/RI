package models.graphModels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import models.IRmodel;
import classes.Index;
import classes.Rank;

public class ResearchGraphRank implements IRmodel {

	private Index index;
	private IRmodel seedModel;
	private int seedNumber;
	private int inLinks;
	private RandomWalk graphMod;

	public ResearchGraphRank(Index index, IRmodel seedModel, int seedNumber,
			int inLinks, RandomWalk graphMod) {
		super();
		this.index = index;
		this.seedModel = seedModel;
		this.seedNumber = seedNumber;
		this.inLinks = inLinks;
		this.graphMod = graphMod;
	}



	@Override
	public HashMap<Integer, Double> getScores(HashMap<String, Integer> query)
			throws IOException {
		Set<Integer> seed = getKSeed(query);
		Set<Integer> nodes = new HashSet<Integer>();
		nodes.addAll(seed);

		for(Integer node: seed){
			nodes.addAll(index.getDocLinks(node));
			nodes.addAll(this.Krandom(node));

		}

		return (HashMap<Integer, Double>) graphMod.getNodeScores(nodes);
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

	private Set<Integer> getKSeed(HashMap<String, Integer> query) throws IOException{
		HashSet<Integer> seed = new HashSet<Integer>();
		ArrayList<Rank> ranking = seedModel.getRanking(query);

		for(int i=0;i<seedNumber;i++)
			seed.add(ranking.get(i).doc);

		 return seed;
	}

	private Set<Integer> Krandom(Integer node) throws IOException {

		Set<Integer> nodes = index.getDocInLinks(node);

		if(nodes== null)
			return new HashSet<Integer>();

		if(nodes.size() <= inLinks)
			return nodes;

		Set<Integer> nodesRem = new HashSet<Integer>();

		int remCount = nodes.size()-inLinks;
		Random r = new Random();
		while(true){
			for(Integer n:nodes){
				if(r.nextFloat()>0.9)
					nodesRem.add(n);


				if(nodesRem.size() >= remCount){
					nodes.removeAll(nodesRem);
					return nodes;
				}

			}

		}

	}

}
