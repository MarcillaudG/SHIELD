package fr.irit.smac.shield.c2av;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import fr.irit.smac.shield.model.Generator;

public class GeneratorOfFunction {

	
	private final int NB_VAR_MAX;
	
	private int nbFunction;
	
	private Generator generator;
	
	private Map<String,SyntheticFunction> functions;
	
	private Random rand;
	
	public GeneratorOfFunction() {
		this.NB_VAR_MAX = 1000;
		init();
		initVariable();
		
	}
	
	public GeneratorOfFunction(int nbVar) {
		this.NB_VAR_MAX = nbVar;
		init();
		initVariable();
		
	}
	
	private void init() {
		this.generator = new Generator();
		this.functions = new TreeMap<String,SyntheticFunction>();
		this.nbFunction = 0;
		this.rand = new Random();
	}
	
	
	/**
	 * Initialize all the variables
	 */
	private void initVariable() {
		for(int i = 0; i < this.NB_VAR_MAX; i++) {
			this.generator.initVariableWithRange(-100,100);
		}
	}

	
	public SyntheticFunction generateFunction(int nbVar) {
		Deque<String> variables = new ArrayDeque<String>();

		List<String> variablesRemaining = new ArrayList<String>(this.generator.getAllVariables());
		for(int i = 0; i < nbVar; i++) {
			variables.push(variablesRemaining.remove(rand.nextInt(variablesRemaining.size())));
		}
		SyntheticFunction fun = new SyntheticFunction("Function "+this.nbFunction,this,variables);
		this.functions.put(fun.getName(), fun);
		this.nbFunction++;
		return fun;
	}
	
	public void printAllFunctions() {
		for(SyntheticFunction f : this.functions.values()) {
			System.out.println(f);
		}
	}
	
	public void generateNewValues() {
		this.generator.generateAllValues();
	}

	public double getValueOfVariable(String poll) {
		return this.generator.getValueOfVariable(poll);
	}

}
