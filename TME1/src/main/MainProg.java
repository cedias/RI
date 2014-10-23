package main;

import interfaces.IRmodel;
import interfaces.Weighter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

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
		Index index = new Index(filename, new CisiParser(filename), "cisi");

		Weighter w = new SimpleWeighter(index);
		Weighter w2 = new TFWeighter(index);
		Weighter w3 = new TFIDFWeighter(index);
		Vectoriel vect = new Vectoriel(index, w2,false);
		QueryIter queries = new QueryIter("cisi/cisi.qry", "cisi/cisi.rel", new CisiParser("cisi/cisi.qry"));

		ArrayList<IRmodel> models = new ArrayList<IRmodel>();
		ArrayList<EvalMeasure> mesures = new ArrayList<EvalMeasure>();

		models.add(vect);

		mesures.add(new EvalPrecisionRappel(100));
		//mesures.add(new EvalPrecisionMoyenne());


		//Map<Integer, List<List<Double>>> eval = EvalIRModel.Evaluate(models, mesures, queries);
		List<List<Double>> eval = EvalIRModel.EvaluateMean(models, mesures, queries);

		Double[] yvals = eval.get(0).toArray(new Double[eval.get(0).size()]);
		Double[] xvals = EvalIRModel.getRappelLevels(100);

		PlotArray pl = new PlotArray(xvals, yvals, "Rappel", "Précision", "PR-102");
		pl.plot();



		System.out.println(Arrays.toString(xvals));
		System.out.println(Arrays.toString(yvals));




	}

}
