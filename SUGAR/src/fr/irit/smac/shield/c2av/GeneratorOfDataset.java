package fr.irit.smac.shield.c2av;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneratorOfDataset {

	private GeneratorOfTypedVariable gen;
	private FileWriter file;



	public GeneratorOfDataset(String fileName) {
		this.gen = new GeneratorOfTypedVariable();

		this.gen.initSetOfTypedVariable(10, 0, 10, "TYPE1");
		List<String> allVar = new ArrayList<String>(this.gen.getAllVariables());
		for(String var: allVar) {
			this.gen.generateSimilarDataDifferent(var, 3);
		}
		this.gen.generateAllFunctions();

		// Writing the csv
		try {
			this.file = new FileWriter(new File("C:\\\\Users\\\\gmarcill\\\\Desktop\\\\"+fileName+".csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
	private void printLineName() {
		String toPrint = "";
		for(String var : this.gen.getAllVariables()) {
			toPrint += var+";";
		}
		toPrint +="\n";
		try {
			this.file.write(toPrint);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void printLine() {
		String toPrint = "";
		for(String var : this.gen.getAllVariables()) {
			toPrint += this.gen.getVariableWithName(var).getValue()+";";
		}
		toPrint +="\n";
		try {
			this.file.write(toPrint);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void printNbLine(int nb) {
		for(int i = 0; i < nb; i++) {
			this.gen.generateAllValues();
			this.printLine();
		}
	}

	public static void main(String args[]) {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		GeneratorOfDataset genDat = new GeneratorOfDataset("dataset-"+timeStamp);
		genDat.printLineName();
		genDat.printNbLine(200);
		genDat.closeFile();
	}




	private void closeFile() {
		try {
			this.file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}



