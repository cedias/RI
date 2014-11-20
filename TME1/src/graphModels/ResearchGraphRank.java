package graphModels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import classes.Index;
import classes.Rank;
import interfaces.IRmodel;

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
	public Map<Integer, Double> getScores(HashMap<String, Integer> query)
			throws IOException {
		Set<Integer> seed = getKSeed(query);
		Set<Integer> nodes = new HashSet<Integer>();
		nodes.addAll(seed);

		for(Integer node: seed){

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

	private Set<Integer> getKSeed(HashMap<String, Integer> query) throws IOException{
		HashSet<Integer> seed = new HashSet<Integer>();
		ArrayList<Rank> ranking = seedModel.getRanking(query);

		for(int i=0;i<seedNumber;i++)
			seed.add(ranking.get(i).doc);

		 return seed;
	}

}
