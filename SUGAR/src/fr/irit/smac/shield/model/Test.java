package fr.irit.smac.shield.model;

import fr.irit.smac.shield.c2av.GeneratorOfFunction;

public class Test {

	public static void main(String[] args) {

		Generator gen = new Generator();
		GeneratorOfFunction funGen = new GeneratorOfFunction(gen);
		/*for(int i = 0 ; i < 100; i++) {
			gen.initVariableWithRange(-100,100);
		}*/
		gen.initSetOfVariableWithRange(1000, 0, 100);
		gen.generateAllValues();
		System.out.println(gen.getValueOfVariable("Variable1000"));
		gen.printAllVariables();
		gen.generateAllValues();
		gen.printAllVariables();
	}

}
