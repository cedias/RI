package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import models.IRmodel;
import models.compModels.linearCombModels;
import models.vectorModels.TFIDFWeighter;
import models.vectorModels.Vectoriel;
import parsing.CisiParser;
import parsing.QueryIter;
import parsing.selector.MultiSelector;
import parsing.selector.TextSelector;
import parsing.selector.TitleSelector;
import plot.PlotArray;
import classes.Index;
import evaluation.EvalIRModel;
import evaluation.EvalMeasure;
import evaluation.EvalPN;
import evaluation.EvalPrecisionRappel;

public class DiversityMain {

	public static void main(String[] args) throws IOException {

		String filename = "ImageCLEF2008/ImageCLEF2008_text.txt";
		String file = "ImageCLEF2008/ImageCLEF2008";
		
		Index indexText = new Index(filename, new CisiParser(filename), "img_text", new TitleSelector());
		
		QueryIter queries = new QueryIter(file+"_query.txt", file+"_gt.txt", new CisiParser(file+"_query.txt"));
		
		IRmodel vec = new Vectoriel(indexText, new TFIDFWeighter(indexText));
		
		ArrayList<IRmodel> models = new ArrayList<IRmodel>();
		ArrayList<EvalMeasure> mesures = new ArrayList<EvalMeasure>();
		
		mesures.add(new EvalPN(20));
		models.add((IRmodel) vec);
		
		
		
		List<List<Double>> eval = EvalIRModel.EvaluateMean(models, mesures, queries, new TitleSelector());

		System.out.println(eval.get(0).get(0));
		

	}

}
