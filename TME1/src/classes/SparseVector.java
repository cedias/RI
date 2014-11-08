package classes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class SparseVector {

	HashMap<Integer, Double> vector = new HashMap<Integer,Double>();
	int size;
	int sumComp;

	public SparseVector(int size) {
		super();
		this.size = size;
	}

	public SparseVector(int size, HashMap<Integer, Integer> stemsRead) {
		this.size = size;

		for(Entry<Integer, Integer> e : stemsRead.entrySet()){
			double val = e.getValue();
			vector.put(e.getKey(), val);
		}

	}

	public void setValue(int index, double value){
		if(index>=size){
			throw new IndexOutOfBoundsException("index "+index+" is out of bounds, sparse vector has size "+size);
		}

		if(value != 0.0){
			vector.put(index, value);
			return;
		}

		throw new IndexOutOfBoundsException("Value Error => "+ value);
	}

	public Double getValue(int index){
		return (vector.containsKey(index))?vector.get(index):0;
	}

	public String toString() {
		return vector.toString();
	}

	public Collection<Double> values() {
		return vector.values();
	}

	public HashMap<Integer, Double> toHashmap(){
		return vector;
	}

	public Double dotProd(SparseVector b){
		double res = 0;

		Set<Integer> keys =  (vector.keySet().size() <= b.vector.keySet().size())? new HashSet<Integer>(vector.keySet()):new HashSet<Integer>(b.vector.keySet());


		for(int key : keys ){
			res += this.getValue(key)*b.getValue(key);
		}
		return res;
	}

	public Double cosSim(SparseVector b){

		double sumA = 0;
		double sumB = 0;


		for(double val :this.values())
			sumA += (val*val);

		for(double val :b.values())
			sumB += (val*val);

		double prod = sumA*sumB;

		if(prod == 0){
			System.err.println("Caution, dividing by 0 on cosSim, One vector might be null");
		}


		return this.dotProd(b)/Math.sqrt(sumA*sumB);
	}


}
