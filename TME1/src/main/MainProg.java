package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import classes.Document;
import classes.Index;
import classes.Stemmer;
import parsing.CisiParser;
import parsing.DocumentIter;

public class MainProg {

	public static void main(String[] args) throws IOException {
		Index index = new Index("cacm/cacm.txt", new CisiParser(), "cacm");
	}

}
