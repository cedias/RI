package classes;

public final class Pair<X,Y> {

	public final X e1;
	public final Y e2;

	public Pair(X e1, Y e2) {
		super();
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public String toString() {
		return "<" + e1 + ", " + e2 + ">";
	}





}
