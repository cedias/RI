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

		final int level = relDocs.size()/nbLevels;

		int relCount = 0;
		int irrelCount = 0;
		double maxPrecision = 0;

		for(Rank r : results){
			if(relDocs.contains(r.doc))
				relCount++;
			else
				irrelCount++;

			if(relCount%level == 0){
				
				
			}

		}


		return null;
	}

}
