package fr.irit.smac.shield.c2av;

import fr.irit.smac.shield.model.Generator;

public class TestFunction {

	public static void main(String args[]) {
		/*GeneratorOfFunction gen = new GeneratorOfFunction();
		for(int i = 0 ; i < 10; i++) {
			gen.generateFunction(10);
		}
		gen.printAllFunctions();*/
		
		GeneratorOfTypedVariable genTyped = new GeneratorOfTypedVariable();
		
		for(int i = 0; i < 50; i ++) {
			genTyped.initSetOfTypedVariable(20, 0, 100, ""+i);
		}
		genTyped.generateAllFunctions();
		genTyped.generateAllValues();
		
		genTyped.printAllVariables();
		genTyped.generateAllValues();
		
		genTyped.printAllVariables();
	}
}
