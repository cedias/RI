package main;

import graphModels.HITS;
import graphModels.PageRank;
import graphModels.ResearchGraphRank;
import interfaces.IRmodel;
import interfaces.Weighter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import okapi.OkapiModel;

import languageModels.LanguageModel;

import classes.Index;

import evaluation.EvalIRModel;
import evaluation.EvalMeasure;
import evaluation.EvalPrecisionRappel;

import parsing.CisiParser;
import parsing.QueryIter;
import plot.PlotArray;
import vectorModels.SimpleWeighter;
import vectorModels.TFIDFWeighter;
import vectorModels.TFWeighter;
import vectorModels.Vectoriel;

public class MainProg {

	public static void main(String[] args) throws IOException {


		String filename = "cisi/cisi.txt";
		String file = "cisi/cisi";
		Index index = new Index(filename, new CisiParser(filename), "cisi");

		Weighter w = new TFIDFWeighter(index);
		Vectoriel vect = new Vectoriel(index, w,false);
		LanguageModel lang = new LanguageModel(index, 0.5);
		OkapiModel okap = new OkapiModel(index, 1.2, 0.7);
		PageRank pr = new PageRank(index, 0.8, 1000);
		HITS hi = new HITS(index, 50);
		ResearchGraphRank rg = new ResearchGraphRank(index, vect, 10, 10,hi );


		QueryIter queries = new QueryIter(file+".qry", file+".rel", new CisiParser(file+".qry"));


		ArrayList<IRmodel> models = new ArrayList<IRmodel>();
		ArrayList<EvalMeasure> mesures = new ArrayList<EvalMeasure>();

		models.add(vect);
		String modelName = "Hits Model - (50)";
		//models.add(okap);

		mesures.add(new EvalPrecisionRappel(100));
		//mesures.add(new EvalPrecisionMoyenne());
		//Map<Integer, List<List<Double>>> eval = EvalIRModel.Evaluate(models, mesures, queries);
		List<List<Double>> eval = EvalIRModel.EvaluateMean(models, mesures, queries);


		//System.out.println(eval.get(1).size());



		Double[] yvals = eval.get(0).toArray(new Double[eval.get(0).size()]);
		Double[] xvals = EvalIRModel.getRappelLevels(10);



		PlotArray pl = new PlotArray(xvals, yvals, "Rappel", "Précision", modelName);
		pl.plot();
		pl.save("./vect.pdf");




		System.out.println(Arrays.toString(xvals));
		System.out.println(Arrays.toString(yvals));




	}

}
