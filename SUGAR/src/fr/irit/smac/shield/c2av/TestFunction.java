package fr.irit.smac.shield.c2av;

import fr.irit.smac.shield.exceptions.TooMuchVariableToRemoveException;
import fr.irit.smac.shield.model.Generator;

public class TestFunction {

	public static void main(String args[]) {
		GeneratorOfTypedVariable genTyped = new GeneratorOfTypedVariable();
		genTyped.initSetOfTypedVariable(150, 0, 100, "Type 1");
		genTyped.generateAllFunctions();
		genTyped.generateAllValues();
		GeneratorOfFunction gen = new GeneratorOfFunction(genTyped);
		gen.generateFunction("Function1",100);
		SyntheticFunction fun1 =  gen.getSyntheticFunctionWithName("Function1");
		try {
			SyntheticFunction fun2 = fun1.degradeFunctionInput(20);
			gen.printAllFunctions();
			System.out.println(fun1.computeInput());
			System.out.println(fun2.computeInput());
		} catch (TooMuchVariableToRemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		/*for(int i = 0; i < 50; i ++) {
			genTyped.initSetOfTypedVariable(20, 0, 100, ""+i);
		}
		genTyped.generateAllFunctions();
		genTyped.generateAllValues();

		genTyped.printAllVariables();
		genTyped.generateAllValues();

		genTyped.printAllVariables();*/
	}
}
