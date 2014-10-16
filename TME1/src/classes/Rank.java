package classes;

public class Rank implements Comparable<Rank>{

	final public int doc;
	final public double score;
	@Override
	
	public int compareTo(Rank o) {
		return new Double(this.score).compareTo(o.score);
	}
	public Rank(int doc, double score) {
		super();
		this.doc = doc;
		this.score = score;
	}
	@Override
	public String toString() {
		return "[" + doc + "-" + score + "]";
	}
	
	
	
	
}
