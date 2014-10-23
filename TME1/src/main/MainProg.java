package main;

import interfaces.IRmodel;
import interfaces.Weighter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import classes.Document;
import classes.Index;
import classes.Query;
import classes.SparseVector;
import classes.Stemmer;
import evaluation.EvalIRModel;
import evaluation.EvalMeasure;
import evaluation.EvalPrecisionMoyenne;
import evaluation.EvalPrecisionRappel;
import evaluation.IRList;
import evaluation.RelParser;
import parsing.CisiParser;
import parsing.DocumentIter;
import parsing.QueryIter;
import vectorModels.SimpleWeighter;
import vectorModels.Vectoriel;

public class MainProg {

	public static void main(String[] args) throws IOException {
		/*
		HashMap<String, Integer> testQuery = new HashMap<String,Integer>();
		
		testQuery.put("event", 5);
		testQuery.put("medica", 44);
		testQuery.put("profession", 10);
		testQuery.put("religi", 5);
		testQuery.put("conceptu", 5);
		testQuery.put("proverbi", 5);
		testQuery.put("pass", 1);
*/
		
		String filename = "cisi/cisi.txt";
		Index index = new Index(filename, new CisiParser(filename), "cisi");
		
		Weighter w = new SimpleWeighter(index);
		Vectoriel vect = new Vectoriel(index, w);
		QueryIter queries = new QueryIter("cisi/cisi.qry", "cisi/cisi.rel", new CisiParser("cisi/cisi.qry"));

		ArrayList<IRmodel> models = new ArrayList<IRmodel>();
		ArrayList<EvalMeasure> mesures = new ArrayList<EvalMeasure>();
		
		models.add(vect);
		
		mesures.add(new EvalPrecisionRappel(100));
		mesures.add(new EvalPrecisionMoyenne());
		
		
		Map<Integer, List<List<Double>>> eval = EvalIRModel.Evaluate(models, mesures, queries);
		
		for(Integer key : eval.keySet()){
			System.out.println(eval.get(key));
		}
		
	
		

	}

}
