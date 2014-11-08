package languageModels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import classes.Index;
import classes.Rank;
import interfaces.IRmodel;

public class LanguageModel implements IRmodel {

	private Index index;
	private float lambda;



	public LanguageModel(Index index, float lambda) {
		super();
		this.index = index;
		this.lambda = lambda;
	}

	@Override
	public Map<Integer, Double> getScores(HashMap<String, Integer> query)
			throws IOException {

		for(Entry<String, Integer> e : query.entrySet()){


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
