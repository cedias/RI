package evaluation;

import interfaces.IRmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parsing.CisiParser;
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
	
	
	

}
