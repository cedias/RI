package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import evaluation.EvalPrecisionRappel;

public class MultiIndex {
	public static void main(String[] args) throws IOException {
		
		String filename = "cisi/cisi.txt";
		String file = "cisi/cisi";
		
		Index indexText = new Index(filename, new CisiParser(filename), "cisi_text", new TextSelector());
		Index indexTitle = new Index(filename, new CisiParser(filename), "cisi_title", new TitleSelector());
		
		QueryIter queries = new QueryIter(file+".qry", file+".rel", new CisiParser(file+".qry"));
		
		linearCombModels lcm = new linearCombModels();
		lcm.setIndex(indexTitle);
		lcm.add(new Vectoriel(indexTitle, new TFIDFWeighter(indexTitle)), 0.8);
		lcm.add(new Vectoriel(indexText, new TFIDFWeighter(indexText)), 0.4);
		
		ArrayList<IRmodel> models = new ArrayList<IRmodel>();
		ArrayList<EvalMeasure> mesures = new ArrayList<EvalMeasure>();
		
		mesures.add(new EvalPrecisionRappel(100));
		models.add(lcm);
		
		
		MultiSelector dfs = new MultiSelector();
		dfs.add(new TextSelector());
		dfs.add(new TitleSelector());
		
		
		List<List<Double>> eval = EvalIRModel.EvaluateMean(models, mesures, queries, dfs);


		Double[] yvals = eval.get(0).toArray(new Double[eval.get(0).size()]);
		Double[] xvals = EvalIRModel.getRappelLevels(100);



		PlotArray pl = new PlotArray(xvals, yvals, "Rappel", "Précision", "comblin");
		pl.plot();
		
		
	}
	
}
