package bin;

import index.ImageFeatures;
import index.VIndexFactory;
import io.ImageNetParser;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Computer;

import struct.DataSet;
import struct.STrainingSample;
import utils.PCA;

public class VisualIndexes {

	public static void main(String[] args) throws Exception {
		
		String[] filenames = {"acoustic_guitar", "ambulance", "electric_guitar", "european_fire_salamander","harp", "minivan", "taxi", "tree-frog", "wood-frog"};
		
		List<STrainingSample<double[], String>> listTrain = new ArrayList<STrainingSample<double[], String>>();
		List<STrainingSample<double[], String>> listTest = new ArrayList<STrainingSample<double[], String>>();
		DataSet<double[], String> data;
		//List<ImageFeatures> imgFeat = ImageNetParser.getFeatures(filename);
		
		for(String filename: filenames){
			String file="txt/"+filename+".txt";
			
			List<List<Integer>> AllImgWords = ImageNetParser.getWords(file);
			
			int i=0;
			for(List<Integer> imgWords : AllImgWords){
				
				double[] bow = VIndexFactory.computeBow(imgWords);
				List<STrainingSample<double[], String>> l = (i<800)?listTrain:listTest;
				l.add(new STrainingSample<double[], String>(bow, filename));
				
				i++;
			}
			
			data = new DataSet<double[], String>(listTrain, listTest);
			
			PCA.computePCA(data, 250);
		}
		
	}
}
