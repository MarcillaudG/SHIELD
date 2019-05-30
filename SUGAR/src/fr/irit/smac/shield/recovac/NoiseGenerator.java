package fr.irit.smac.shield.recovac;

import java.util.ArrayList;
import java.util.List;

import fr.irit.smac.shield.model.Variable;

public class NoiseGenerator extends RecovacGenerator{
	protected List<Double> variablesNoise;
	protected double noise;
	protected double totalNoise;
	
	public NoiseGenerator(int _nbSituations, int _nbVariables) {
		super(_nbSituations, _nbVariables, _nbVariables);
		this.init();
	}
	
	public void init() {
		this.variablesNoise = new ArrayList<Double>();
		this.noise = 0.05;
	}
	
	/**
	 * Generate the values of variables when adding a noise value
	 * @param type
	 * 		the type of the situation to which the variable belongs
	 */
	public void generateAllValuesAfterNoise(boolean type) {
		int maxChange = numberCV+1;
		int nbChange = this.rand.nextInt(maxChange);
		List<Variable> variablesPossiblyChange = new ArrayList<Variable>();
		double auxValue;
		totalNoise = 0;
		
		for(String s : this.variables.keySet()) {
			variablesPossiblyChange.add(this.variables.get(s));
			this.variablesNoise.add(this.variables.get(s).getValue());
		}
		
		//System.out.println("nbChange " + nbChange);
		for (int j = 0; j < nbChange; j ++) {
			int nbC = this.rand.nextInt(variablesPossiblyChange.size());
			auxValue = variablesPossiblyChange.get(nbC).getValue();
			this.variablesNoise.set(nbC, this.noiseValueVariable(auxValue, type));
			Variable varC = variablesPossiblyChange.get(nbC);
			variablesPossiblyChange.remove(varC);
		}
	}
	
	/**
	 * Noise value
	 * @param value
	 * 		the value of the variable
	 * @param type
	 * 		the type of the situation to which the variable belongs
	 * @return the new value of the variable after applying the noise
	 */
	public double noiseValueVariable(double value, boolean type) {
		double newValue;
		int aux = this.rand.nextInt(2);
		double r;
		//Add noise to the variable value
		if (aux == 0) {
			if (type == true) {
				r = this.rand.nextDouble()*(noise);
				newValue = value + r;
				//System.out.println("value " + value + "suma + normal" + "con noise " + r + "new Value " + newValue);
			}
			else {
				r = noise + this.rand.nextDouble()*(1 - noise);
				newValue = value + r;
				//System.out.println("value " + value +"suma + abnormal" + "con noise " + r + "new Value " + newValue);
			}	
			
			if (newValue > 1) {
				newValue = 1;
			}
		}
		else {
			if (type == true) {
				r = this.rand.nextDouble()*(noise);
				newValue = value - r;
				//System.out.println("value " + value +"resta - normal"+ "con noise " + r + "new Value " + newValue);
			}
			else {
				r = noise + this.rand.nextDouble()*(1 - noise);
				newValue = value - r;
				//System.out.println("value " + value +"resta - normal"+ "con noise " + r + "new Value " + newValue);
			}	
			
			if (newValue < 0) {
				newValue = 0;
			}
		}
		totalNoise += r;
		
		return newValue;
	}
	
	public void printAllNoiseVariables() {
		for (int s = 0; s < variablesNoise.size(); s++) {
			System.out.println(this.variablesNoise.get(s));
		}
	}
	
	public double getNoiseValue() {
		return this.noise;
	}
	
	
	/*public void executeMethods (boolean type, int index) {
		this.variablesTuple = new ArrayList<VariableTuple>();
		//this.variablesObserved = new ArrayList<Double>();
		this.variablesObserved1 = new ArrayList<Double>();
		this.newValueForVariables();
		this.generateAllValues();
		
		if (type == true) {
			//this.generateAllValuesAfterNoise(true); // type: true = normal, false = abnormal
			this.generateAllValuesAfterNoiseA1();
			this.situationTuple.add(new SituationTuple (index, this.variablesTuple,this.calculateCapacityOfSituation()));
		} 
		else {
			//this.generateAllValuesAfterNoise(false); // type: true = normal, false = abnormal
			this.generateAllValuesAfterNoiseA1();
			this.situationTuple.add(new SituationTuple (index, this.variablesTuple,this.calculateCapacityOfSituation()));
		}	
		this.generateTuple();
		this.printSituationTuple();
	}*/
	
}
