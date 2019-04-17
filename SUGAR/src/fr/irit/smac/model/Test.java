package fr.irit.smac.model;

public class Test {

	public static void main(String[] args) {

		Generator gen = new Generator();

		for(int i = 0 ; i < 100; i++) {
			gen.initVariable();
		}
		gen.generateAllValues();
		gen.getValueOfVariable("Variable1000");
		gen.printAllVariables();
	}

}
