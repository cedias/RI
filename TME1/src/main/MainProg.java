package main;

import interfaces.Weighter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;

import classes.Document;
import classes.Index;
import classes.Query;
import classes.SparseVector;
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
		
		//System.out.println(w.getDocWeightsForStem("deeper"));
		//System.out.println(index.getBow());
		//System.out.println(vect.getRanking(testQuery).toString());
		
		
		QueryIter queries = new QueryIter("cisi/cisi.qry", "cisi/cisi.rel", new CisiParser("cisi/cisi.qry"));
		
		for(Query query: queries){
			System.out.println(query);
		}

	}

}
