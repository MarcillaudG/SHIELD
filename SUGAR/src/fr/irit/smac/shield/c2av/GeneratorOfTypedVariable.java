package fr.irit.smac.shield.c2av;

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
	
	
}
