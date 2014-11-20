package struct;

import java.io.Serializable;

public class STrainingSample<X,Y>implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3221897092308838680L;
	public X input;
	public Y output; 
	
	public STrainingSample(X xi, Y output)
	{
		this.input = xi;
		this.output = output;
	}
	
	
}
