package fr.irit.smac.shield.model;

public class Test {

	public static void main(String[] args) {

		Generator gen = new Generator();

		for(int i = 0 ; i < 100; i++) {
			gen.initVariableWithRange(-100,100);
		}
		gen.generateAllValues();
		gen.getValueOfVariable("Variable1000");
		gen.printAllVariables();
	}

}
