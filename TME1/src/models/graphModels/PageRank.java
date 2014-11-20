package models.graphModels;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import classes.Index;
import classes.Pair;

public class PageRank implements RandomWalk {

	Index index;
	double d;
	int nbIter;


	public PageRank(Index index, double d, int nbIter) {
		super();
		this.index = index;
		this.d = d;
		this.nbIter = nbIter;
	}

	@Override
	public Map<Integer, Double> getNodeScores(Set<Integer> nodes) throws IOException {
		int dim = nodes.size();
		HashMap<Integer, Integer> node2Index = new HashMap<Integer,Integer>(nodes.size());
		HashMap<Integer, Integer> index2Node = new HashMap<Integer,Integer>(nodes.size());

		int counter = 0;
		for(Integer i:nodes){
			node2Index.put(i,counter);
			index2Node.put(counter, i);
			counter++;
		}

		double[] scores = new double[dim];
		Arrays.fill(scores, 1/(nodes.size()+0.0)); //init

		double[] pMatrix = new double[dim*dim];
		Arrays.fill(pMatrix, 0);

		for(Integer node:nodes){
			int indI = node2Index.get(node);
			Set<Integer> links = index.getDocLinks(node);
			Pair<Integer,Integer> count = index.getDocLinkCount(node);


			if(links == null)
				continue;

			for(Integer link:links){


				if(!node2Index.containsKey(link))
					continue;


				int indJ = node2Index.get(link);
				pMatrix[indI*dim+indJ] = (1/(count.e2+0.0));
			}
		}

		for(int i=0;i<nbIter;i++){
			double[] oldScores = scores;
			scores = this.iterate(scores,pMatrix);

			if(i != 1 && i != 0  && meanSquareDiff(scores, oldScores) == 0.0)
			{
				System.out.println("Convergence atteinte en "+i+" itérations");
				break;
			}

		}


		HashMap<Integer, Double> map = new HashMap<Integer,Double>();

		for(int i=0;i<scores.length;i++){
			map.put(index2Node.get(i), scores[i]);
		}

		return map;
	}

	private double meanSquareDiff(double[] scores,double[] oldScores){
		double err=0;
		for(int i=0;i<scores.length;i++)
			err += (scores[i]-oldScores[i])*(scores[i]-oldScores[i]);
		err/=scores.length;

		return err;
	}

	private double[] iterate(double[] scores, double[] pMatrix) {
		int dim = scores.length;
		double skipProb = (1-d)/(scores.length+0.0);
		double[] newScores = new double[dim];
		int ind = 0;
		for(int cptCell=0;cptCell<scores.length;cptCell++){
			double sumProd = 0;
			for(int j=0;j<scores.length;j++){
				sumProd += pMatrix[ind]*scores[j];
				ind++;
			}
			newScores[cptCell] = sumProd;
		}

		for(int i=0;i<newScores.length;i++){
			newScores[i]= newScores[i]*d+skipProb;
		}

		return newScores;

	}

	@Override
	public void setIndex(Index index) {
		this.index = index;

	}

	public void setNbIter(int iter){
		this.nbIter = iter;
	}

}
