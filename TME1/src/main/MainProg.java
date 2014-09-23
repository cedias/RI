package main;

import java.io.FileNotFoundException;
import java.util.HashMap;

import classes.Document;
import classes.Stemmer;
import parsing.CisiParser;
import parsing.DocumentIter;

public class MainProg {

	public static void main(String[] args) throws FileNotFoundException {
		
		
		String filename = "cacm/cacm.txt";
		
		DocumentIter dociter = new DocumentIter(filename, new CisiParser());
		HashMap<String, Integer> bagOfWords = new HashMap<String,Integer>(10000);
		Stemmer stemmer = new Stemmer();  
		
		HashMap<String, Integer> stemmedText;
		
		for(Document d : dociter){
			System.out.println(d.getId());
			stemmedText = stemmer.porterStemmerHash(d.getText());
			
			for(String s : stemmedText.keySet())
				System.out.println(s);
			
			break;
			
		}
		

	}

}
