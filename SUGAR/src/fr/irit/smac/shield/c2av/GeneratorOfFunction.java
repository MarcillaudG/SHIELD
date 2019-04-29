package fr.irit.smac.shield.c2av;

import fr.irit.smac.shield.model.Generator;

public class GeneratorOfFunction {

	
	private final int NB_VAR_MAX; 
	
	private Generator generator;
	
	public GeneratorOfFunction() {
		this.generator = new Generator();
		this.NB_VAR_MAX = 1000;
		
		initVariable();
	}
	
	public GeneratorOfFunction(int nbVar) {
		this.generator = new Generator();
		this.NB_VAR_MAX = nbVar;
		initVariable();
	}

	/**
	 * Initialize all the variables
	 */
	private void initVariable() {
		for(int i = 0; i < this.NB_VAR_MAX; i++) {
			this.generator.initVariableWithRange();
		}
	}

}
