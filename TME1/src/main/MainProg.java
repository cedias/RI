package main;

import java.io.IOException;
import java.io.RandomAccessFile;

import classes.Index;
import classes.SparseVector;
import parsing.CisiParser;

public class MainProg {

	public static void main(String[] args) throws IOException {
		String filename = "cisi/cisi.txt";
		Index index = new Index(filename, new CisiParser(filename), "cisi");
		SparseVector sv = index.getTfsForDoc(0);
		SparseVector sv2 = index.getTfsForStem("event");

		System.out.println(sv);
		System.out.println(sv2);

	}

}
