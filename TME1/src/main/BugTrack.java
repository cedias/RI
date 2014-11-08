package main;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map.Entry;

import parsing.CisiParser;
import parsing.DocumentIter;
import classes.Document;
import classes.SparseVector;
import classes.Stemmer;


public class BugTrack {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {

		SparseVector sv1 = new SparseVector(2);
		SparseVector sv2 = new SparseVector(2);

		sv1.setValue(0, 1);
		sv2.setValue(1, 0);

		System.out.println(sv1.cosSim(sv2));

	}
}


