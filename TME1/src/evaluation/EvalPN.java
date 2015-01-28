package evaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import classes.Rank;

public class EvalPN implements EvalMeasure {

	int n;



	public EvalPN(int n) {
		super();
		this.n = n;
	}

	@Override
	public List<Double> eval(IRList l) {


		ArrayList<Rank> results = l.results;
		HashSet<Integer> relDocs = l.query.releventDocuments;
		ArrayList<Double> eval = new ArrayList<Double>();

		int relCount = 0;
		int irrelCount = 0;

		//System.out.println(results.subList(0, 20).toString());
		for(int i=0;i<n;i++){
			if(relDocs.contains(results.get(i).doc)){

				relCount++;
			}
			else
				irrelCount++;
		}

		eval.add((relCount/(relCount+irrelCount+0.0))*100);
		System.out.println(relCount);
		return eval;


	}

}
