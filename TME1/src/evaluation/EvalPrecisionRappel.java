package evaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import classes.Rank;

public class EvalPrecisionRappel implements EvalMeasure {

	int nbLevels;



	public EvalPrecisionRappel(int nbLevels) {
		super();
		this.nbLevels = nbLevels;
	}

	public EvalPrecisionRappel() {
		this(10);
	}

	@Override
	public List<Double> eval(IRList l) {

		ArrayList<Rank> results = l.results;
		HashSet<Integer> relDocs = l.query.releventDocuments;
		ArrayList<Double> eval = new ArrayList<Double>();

		final double level = 1/(nbLevels+0.0);
		int numLevel = 1;

		int relCount = 0;
		int irrelCount = 0;
		double maxPrecision = -1;

		for(Rank r : results){
			if(relDocs.contains(r.doc))
				relCount++;
			else
				irrelCount++;

			double rap = relCount/(relDocs.size()+0.0);

			if(rap >= numLevel*level){
				numLevel++;
				eval.add(relCount/(irrelCount+relCount+0.0));
			}

			if(relCount/(irrelCount+relCount+0.0)>1)
			{
				System.err.println("Precision-Rappel > 1");
				System.err.println(eval.toString());
			}

			if(numLevel >= nbLevels)
				break;
		}
		maxPrecision = 0;
		for(int i = eval.size()-1;i>=0;i--)
		{
			if(eval.get(i) < maxPrecision)
				eval.set(i,maxPrecision);
			else
				maxPrecision = eval.get(i);
		}

		return eval;
	}

}
