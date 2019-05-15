package fr.irit.smac.shield.c2av;

public class TestFunction {

	public static void main(String args[]) {
		GeneratorOfFunction gen = new GeneratorOfFunction();
		for(int i = 0 ; i < 10; i++) {
			gen.generateFunction(10);
		}
		gen.printAllFunctions();
	}
}
