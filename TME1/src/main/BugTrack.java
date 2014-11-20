package main;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import models.graphModels.HITS;
import models.graphModels.PageRank;
import parsing.CisiParser;
import parsing.DocumentIter;
import parsing.selector.MultiSelector;
import parsing.selector.TextSelector;
import parsing.selector.TitleSelector;
import classes.Document;
import classes.Index;
import classes.SparseVector;
import classes.Stemmer;


public class BugTrack {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String filename = "cisi/cisi.txt";
		MultiSelector dfs = new MultiSelector();
		dfs.add(new TextSelector());
		dfs.add(new TitleSelector());
		
		Index index = new Index(filename, new CisiParser(filename), "cisi",dfs);

		System.out.println(index.getDocLinks(1));
		System.out.println(index.getDocLinkCount(1));
		System.out.println(index.getDocInLinks(1));
		System.out.println(index.getDocInLinks(1).size());

		HITS hi = new HITS(index, 10000);

		HashSet<Integer> nodes2 = new HashSet<Integer>();


		HashSet<Integer> nodes = index.docIdSet();
		Random r = new Random();
		for(Integer i:nodes){
			//if(r.nextDouble() >=0.9)
				nodes2.add(i);

		}

		Map<Integer, Double> res = hi.getNodeScores(nodes2);
		System.out.println(Arrays.toString(res.values().toArray()));
		System.out.println(res.values().size());

/*
		PageRank pr = new PageRank(index, 1, 10000);

		HashSet<Integer> nodes;
		HashSet<Integer> nodes2 = new HashSet<Integer>();


		nodes = index.docIdSet();
		Random r = new Random();
		for(Integer i:nodes){
			if(r.nextDouble() >=0.9)
				nodes2.add(i);

		}

		System.out.println(index.getNbDocs());
		System.out.println("nodes length:" + nodes2.size());

		Map<Integer, Double> res = pr.getNodeScores(nodes2);

		System.out.println(Arrays.toString(res.values().toArray()));
		System.out.println(res.values().size());
	*/


	}
}


