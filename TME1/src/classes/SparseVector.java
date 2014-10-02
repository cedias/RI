package classes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

public class SparseVector {

	HashMap<Integer, Double> vector = new HashMap<Integer,Double>();
	int size;
	
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
		vector.put(index, value);
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
	
	
}
