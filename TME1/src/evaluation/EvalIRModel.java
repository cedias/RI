package evaluation;

import interfaces.IRmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import parsing.QueryIter;
import classes.Query;
import classes.Rank;
import classes.Stemmer;

public class EvalIRModel {

	public static Map<Integer,List<List<Double>>> Evaluate(List<IRmodel> models, List<EvalMeasure> mesures, QueryIter queries) throws IOException{

		HashMap<Integer,List<List<Double>>> results = new HashMap<Integer,List<List<Double>>>();
		Stemmer st = new Stemmer();

		for(Query query: queries){

			if(query.releventDocuments == null)
			{
				System.err.println("Query #"+query.d.getId()+" doesn't have any relevant documents");
				continue;
			}

			HashMap<String, Integer> queryText = st.porterStemmerHash(query.d.getText()); //TODO -- use all query info ?

			for(IRmodel model: models){
				ArrayList<Rank> ranking = model.getRanking(queryText);
				IRList result = new IRList(query, ranking);

				for(EvalMeasure mesure : mesures){

					if(!results.containsKey(query.d.getId()))
						results.put(query.d.getId(), new ArrayList<List<Double>>(mesures.size()));

					results.get(query.d.getId()).add(mesure.eval(result));

				}

			}

		}


		return results;

	}

	public static List<List<Double>> EvaluateMean(List<IRmodel> models, List<EvalMeasure> mesures, QueryIter queries) throws IOException{

		ArrayList<List<Double>> results = new ArrayList<List<Double>>(mesures.size());
		Stemmer st = new Stemmer();
		int queryCounter = 0;
		List<Double> finalList;


		for(Query query: queries){

			if(query.releventDocuments == null)
			{
				System.err.println("Query #"+query.d.getId()+" doesn't have any relevant documents");
				continue;
			}

			HashMap<String, Integer> queryText = st.porterStemmerHash(query.d.getText()); //TODO -- use all query info ?

			for(IRmodel model: models){
				ArrayList<Rank> ranking = model.getRanking(queryText);
				IRList result = new IRList(query, ranking);

				for(int i=0;i<mesures.size();i++){
					EvalMeasure mesure = mesures.get(i);
					List<Double> eval = mesure.eval(result);


					if(queryCounter == 0){
						results.add(eval);
					}
					else{
						finalList = results.get(i);

						for(int j=0; j<eval.size();j++){
							finalList.set(j, finalList.get(j)+eval.get(j));

						}
					}

				}
			}
			queryCounter++;
		}


		for(List<Double> l: results){
			for(int i=0;i<l.size();i++)
				l.set(i, l.get(i)/queryCounter);

		}

		return results;

	}

	public static Double[] getRappelLevels(int nbLevels){

		Double[] xvals = new Double[nbLevels-1]; //0.0 isn't usefull !

		for(int i=0;i < nbLevels-1;i++){
			xvals[i] = (1.0/nbLevels)*(i+1);
		}

		return xvals;

	}


}
