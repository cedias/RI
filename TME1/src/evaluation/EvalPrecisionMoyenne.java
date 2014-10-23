package evaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import classes.Rank;

public class EvalPrecisionMoyenne implements EvalMeasure {

	@Override
	public List<Double> eval(IRList l) {
		
		ArrayList<Rank> results = l.results;
		HashSet<Integer> relDocs = l.query.releventDocuments;
		ArrayList<Double> eval = new ArrayList<Double>();
		
		int relCount = 0;
		int irrelCount = 0;
		

		for(Rank r : results){
			if(relDocs.contains(r.doc)){
				relCount++;
				eval.add(relCount/(relCount+irrelCount+0.0));	
			}
			else
				irrelCount++;

			
		}
		
		return eval;
		
		
	}
	

}
