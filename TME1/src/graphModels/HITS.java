package graphModels;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import classes.Index;

public class HITS implements RandomWalk{

	private Index index;
	private int nbIter;

	public HITS(Index index,int nbIter) {
		super();
		this.index = index;
		this.nbIter = nbIter;
	}

	@Override
	public Map<Integer, Double> getNodeScores(Set<Integer> nodes)
			throws IOException {

		int dim = nodes.size();
		HashMap<Integer, Integer> node2Index = new HashMap<Integer,Integer>(nodes.size());
		HashMap<Integer, Integer> index2Node = new HashMap<Integer,Integer>(nodes.size());
		int counter = 0;
		for(Integer i:nodes){
			node2Index.put(i,counter);
			index2Node.put(counter, i);
			counter++;
		}
		double [] a = new double[dim];
		Arrays.fill(a, 1);
		double [] h = new double[dim];
		Arrays.fill(h, 1);

		double[] oldA = a;
		for(int i=0;i<nbIter;i++){

			oldA = a;
			a = this.updateAuthority(a,h,nodes,node2Index);
			h = this.updateHub(oldA,h,nodes,node2Index);

			if(i != 1 && i != 0  && meanSquareDiff(a, oldA) == 0.0)
			{
				System.out.println("Convergence atteinte en "+i+" itérations");
				break;
			}
		}


		HashMap<Integer, Double> map = new HashMap<Integer,Double>();

		for(int i=0;i<a.length;i++){
			map.put(index2Node.get(i), a[i]);
		}

		return map;
	}

	private double[] updateHub(double[] a, double[] h, Set<Integer> nodes,
			HashMap<Integer, Integer> node2Index) throws IOException {
		double[] newH = new double[h.length];
		Arrays.fill(newH,0);

		for (Integer node : nodes){
			int scoreIndex = node2Index.get(node);
			Set<Integer> outs = index.getDocLinks(node);

			if(outs == null)
				continue;

			for(Integer out : outs){
				if(!node2Index.containsKey(out))
					continue;
				int aIndex = node2Index.get(out);

				newH[scoreIndex] += a[aIndex];
			}

		}
		this.l2Norm(newH);
		return newH;
	}

	private double[] updateAuthority(double[] a, double[] h,
			Set<Integer> nodes, HashMap<Integer, Integer> node2Index) throws IOException {
		double[] newA = new double[a.length];
		Arrays.fill(newA,0);

		for (Integer node : nodes){
			int scoreIndex = node2Index.get(node);
			Set<Integer> ins = index.getDocInLinks(node);

			if(ins == null)
				continue;

			for(Integer in : ins){
				if(!node2Index.containsKey(in))
					continue;
				int hubIndex = node2Index.get(node);

				newA[scoreIndex] += h[hubIndex];
			}

		}
		this.l2Norm(newA);
		return newA;
	}

	private void l2Norm(double[] a){

		double norm = 0;
		for(int i=0;i<a.length;i++){
			norm+=(a[i]*a[i]);
		}
		norm = Math.sqrt(norm);

		for(int i=0;i<a.length;i++){
			a[i] = (a[i]/norm);
		}
		return;
	}

	private double meanSquareDiff(double[] scores,double[] oldScores){
		double err=0;
		for(int i=0;i<scores.length;i++)
			err += (scores[i]-oldScores[i])*(scores[i]-oldScores[i]);
		err/=scores.length;

		return err;
	}

	@Override
	public void setIndex(Index index) {
		this.index = index;

	}




}
