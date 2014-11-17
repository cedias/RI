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
	double norm = -1;
	boolean newValues = false;
	int sum = -1;

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
			this.newValues = true;
			vector.put(index, value);
			return;
		}

		throw new IndexOutOfBoundsException("Value Error => "+ value);
	}

	public SparseVector divide(double val){
		SparseVector divided = new SparseVector(this.size);

		for(Entry<Integer, Double> e: this.vector.entrySet()){
			divided.setValue(e.getKey(),e.getValue()/val);
		}
		return divided;
	}

	public int getSum(){
		if(norm != -1 && newValues == false){
			return this.sum;
		}
		this.sum=0;

		for(double val : this.values())
			this.sum+=val;
		return this.sum;
	}

	public double getNorm(){
		if(norm != -1 && newValues == false){
			return this.norm;
		}

		double sum = 0;

		for(double val :this.values())
			sum += (val*val);

		this.norm = Math.sqrt(sum);
		this.newValues = false;

		if(this.norm < 0){
			System.err.println("Norm is less than 0");
		}

		return this.norm;
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
		Double prod =  this.dotProd(b)/(this.getNorm()*b.getNorm());

		if(prod<-1 || prod >1)
			throw new NumberFormatException("cos sim = "+prod +" != [-1 1] \n dot product="+this.dotProd(b)+ "\n norm object = "+this.getNorm()+" norm other ="+b.getNorm());

		return prod;
	}


}
