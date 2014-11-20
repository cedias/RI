package index;

import java.util.Arrays;
import java.util.List;

public class VIndexFactory {

	
	public static double[] computeBow(List<Integer> words){
		double[] bow = new double[1000];
		Arrays.fill(bow, 0);
		
		for(Integer i: words)
			bow[i]+=1;
		
		for(int i=0; i<bow.length;i++)
			bow[i]/=1000;
		
		return bow;
	}
	
}
