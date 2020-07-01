package fr.irit.smac.shield.c2av;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GeneratorOfDataset {

	private GeneratorOfTypedVariable gen;
	private FileWriter file;


	public GeneratorOfDataset(String fileName) {
		this.gen = new GeneratorOfTypedVariable();

		this.gen.initSetOfTypedVariable(50, -100, 100, "TYPE1");
		List<String> allVar = new ArrayList<String>(this.gen.getAllVariables());
		for(String var: allVar) {
			this.gen.generateSeveralSimilarDataDifferent(var, 3);
		}
		this.gen.generateAllFunctions();

		// Writing the csv
		try {
			this.file = new FileWriter(new File("C:\\\\Users\\\\gmarcill\\\\Desktop\\\\"+fileName+".csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GeneratorOfDataset() {
		this.mock();
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
		/*String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		GeneratorOfDataset genDat = new GeneratorOfDataset("dataset-"+timeStamp);
		genDat.printLineName();
		genDat.printNbLine(1000);
		genDat.closeFile();*/
		
		

		GeneratorOfDataset genDat = new GeneratorOfDataset();
		
		
	}

	public void mock() {
		try {
			Scanner data =  new Scanner(new File("C:\\\\Users\\\\gmarcill\\\\Documents\\\\Dataset\\\\MOCK_DATA_20.csv"));
			FileWriter file = new FileWriter(new File("C:\\\\Users\\\\gmarcill\\\\Desktop\\\\dataset_mock_20_enhanced.csv"));
			int c = 0;
			String line = data.nextLine();
			String line2 ="";
			String[] lineSplit = line.split(",");
			int nbVar = lineSplit.length;
			int nbCopy = 3;
			float coeffs[] = new float[nbCopy * (nbVar-1)];
			Random rand = new Random();
			System.out.println(line);
			System.out.println(nbVar);
			for(int i = 0; i < nbVar-1;i++) {
				line2 +=lineSplit[i]+";";
				for(int j =0; j < nbCopy;j++) {
					line2 += lineSplit[i]+":copy:"+j+";";
					coeffs[i*nbCopy+j] = rand.nextFloat()*10;
				}
			}
			line2 += "\n";
			System.out.println(line2);
			file.write(line2);
			while(data.hasNextLine()) {
				line = data.nextLine();
				lineSplit = line.split(",");
				line2 = "";
				for(int i = 0; i < nbVar-1;i++) {
					line2 +=lineSplit[i]+";";
					for(int j =0; j < nbCopy;j++) {
						line2 += Float.parseFloat(lineSplit[i]) * coeffs[i*nbCopy+j] + ";";
					}
				}
				line2 += "\n";
				file.write(line2);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
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



