package main;

import java.io.FileNotFoundException;

import classes.Document;
import parsing.CisiParser;
import parsing.DocumentIter;

public class MainProg {

	public static void main(String[] args) throws FileNotFoundException {
		
		
		String filename = "cacm/cacm.txt";
		
		DocumentIter dociter = new DocumentIter(filename, new CisiParser());
		
		for(Document d : dociter){
			
		}
		

	}

}
