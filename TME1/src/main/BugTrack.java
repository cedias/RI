package main;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map.Entry;

import parsing.CisiParser;
import parsing.DocumentIter;
import classes.Document;
import classes.Stemmer;


public class BugTrack {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {

		DocumentIter dociter = new DocumentIter("cacm/cacm.txt", new CisiParser("cacm"));
		Stemmer stemmer = new Stemmer();
		HashMap<String, Integer> docStems = new HashMap<String, Integer>();

		int cpt1 = 0;
		int cpt2 = 0;

		for(Document d: dociter){

			docStems.putAll(stemmer.porterStemmerHash(d.getText()));
			docStems.putAll(stemmer.porterStemmerHash(d.getTitre()));
			//docStems.putAll(d.getKeywords());  -- TODO add keywords
			//docStems.putAll(d.getAuteur(), 1); // -- TODO works for single named author
			docStems.remove(" * "); //useless key

			for(Entry<String, Integer> s : docStems.entrySet()){

				if(s.getKey().equals("inventorship"))
						cpt1++;

				System.out.println(cpt1);
			}

			docStems.clear();
		}
		System.out.println("Index created");

		/*
		 * Second Iteration, building inverted index
		 */
		dociter = new DocumentIter("cacm/cacm.txt", new CisiParser("cacm")); //dociter works only once. -- TODO add exception "already used"


		for(Document d: dociter){


			docStems.putAll(stemmer.porterStemmerHash(d.getText()));
			docStems.putAll(stemmer.porterStemmerHash(d.getTitre()));
			//docStems.putAll(d.getKeywords());  -- TODO add keywords
			//docStems.putAll(d.getAuteur(), 1); // -- TODO works for single named author
			docStems.remove(" * "); //useless key



			for(Entry<String, Integer> s : docStems.entrySet()){

				if(s.getKey().equals("inventorship")){
					cpt2++;
					System.out.println(cpt2+"/"+cpt1 );

				}

	}
			docStems.clear();
		}

	}
}


