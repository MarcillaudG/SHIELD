package fr.irit.smac.shield.c2av;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.irit.smac.complex.ComposedFunction;
import fr.irit.smac.shield.model.Generator;

public class GeneratorOfComposedFunction {

	private int nbFunction;
	
	private Generator generator;
	
	private Map<String,ComposedFunction> functions;
	
	
	public GeneratorOfComposedFunction(Generator gen) {
		this.generator = gen;
		this.nbFunction = 0;
		this.functions = new TreeMap<>();
	}
	
	/**
	 * Generate a new composed function
	 * @param name
	 * 		Then name of the function
	 * @param input
	 * 		A list of String with float or int
	 * @param outputs
	 * 		A list of String with float or int
	 * @return a new Composed Function
	 */
	public ComposedFunction generateFunction(String name, List<String> input, List<String> outputs, int complexity, int seuilFunction) {
		ComposedFunction fun = new ComposedFunction(name, input, outputs, complexity, seuilFunction);
		this.functions.put(fun.getName(), fun);
		this.nbFunction++;
		return fun;
	}
	
	public void printAllFunctions() {
		for(ComposedFunction cf : this.functions.values()) {
			System.out.println(cf);
		}
	}
	
	public void generateNewValues() {
		this.generator.generateAllValues();
	}

	public double getValueOfVariable(String poll) {
		return this.generator.getValueOfVariable(poll);
	}

	public double getWorstCaseValue(String var) {
		return this.generator.getWorstCaseValue(var);
		
	}

	public ComposedFunction getComposedFunctionWithName(String name) {
		return this.functions.get(name);
	}

	

}
