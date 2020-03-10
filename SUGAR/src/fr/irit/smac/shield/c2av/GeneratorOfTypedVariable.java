package fr.irit.smac.shield.c2av;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fr.irit.smac.shield.model.Generator;
import fr.irit.smac.shield.model.Variable;

public class GeneratorOfTypedVariable extends Generator{

	public void initSetOfTypedVariable(int nbVarToBuild, double min,double max, String type) {
		Random rand = new Random();
		for(int i = 0; i < nbVarToBuild; i++) {
			Variable var = new TypedVariable("V"+type+this.getAllVariables().size(), min,max,rand.nextDouble()*(max - min)+min,type);
			this.putNewVariable(var.getName(), var);
		}
	}

	public void generateSimilarData(String dataName, int nb) {
		Variable var = new TypedVariable(this.variables.get(dataName),dataName+"copy:"+nb);
		this.putNewVariable(var.getName(), var);
	}

	public String getCopyOfVar(String s) {
		List<String> copies = new ArrayList<String>();
		for(String var: this.variables.keySet()) {
			if(var.contains(s+"copy")) {
				copies.add(var);
			}
		}
		Collections.shuffle(copies);
		return copies.get(0);
	}

	
}
