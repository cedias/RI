package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.IRmodel;
import models.graphModels.HITS;
import models.graphModels.PageRank;
import models.graphModels.ResearchGraphRank;
import models.languageModels.LanguageModel;
import models.okapi.OkapiModel;
import models.vectorModels.TFIDFWeighter;
import models.vectorModels.Vectoriel;
import models.vectorModels.Weighter;
import classes.Index;
import evaluation.EvalIRModel;
import evaluation.EvalMeasure;
import evaluation.EvalPrecisionRappel;
import parsing.CisiParser;
import parsing.QueryIter;
import parsing.selector.DocumentFieldSelector;
import parsing.selector.MultiSelector;
import parsing.selector.TextSelector;
import parsing.selector.TitleSelector;
import plot.PlotArray;

public class MainProg {

	public static void main(String[] args) throws IOException {


		String filename = "cisi/cisi.txt";
		String file = "cisi/cisi";
		
		MultiSelector dfs = new MultiSelector();
		dfs.add(new TextSelector());
		dfs.add(new TitleSelector());
		
		Index index = new Index(filename, new CisiParser(filename), "cisi", dfs);

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
		String modelName = "HITS Model";


		mesures.add(new EvalPrecisionRappel(100));

		List<List<Double>> eval = EvalIRModel.EvaluateMean(models, mesures, queries, dfs);




		Double[] yvals = eval.get(0).toArray(new Double[eval.get(0).size()]);
		Double[] xvals = EvalIRModel.getRappelLevels(100);



		PlotArray pl = new PlotArray(xvals, yvals, "Rappel", "Précision", modelName);
		pl.plot();


		System.out.println(Arrays.toString(xvals));
		System.out.println(Arrays.toString(yvals));




	}

}
