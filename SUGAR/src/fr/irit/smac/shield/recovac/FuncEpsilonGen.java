package fr.irit.smac.shield.recovac;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.irit.smac.shield.model.Variable;

public class FuncEpsilonGen extends RecovacGenerator{

	public FuncEpsilonGen(int _nbSituations, int _nbVariables) {
		super(_nbSituations, _nbVariables);
	}
	
	//Funcion con la primera alfa 2^(-(n^y))
	public void generateAllObservedValuesWithFunc1 () {
		int maxChange = numberV;
		int nbChange = this.rand.nextInt(maxChange);
		List<Variable> variablesPossiblyChange = new ArrayList<Variable>();
		double auxValue;
		double hprime;
		double resH;
		totalNoise = 0;

		System.out.println("Variables size +" + this.variables.size());
		for(String s : this.variables.keySet()) {
			variablesPossiblyChange.add(this.variables.get(s));
			this.variablesObserved1.add(this.variables.get(s).getValue());
			System.out.println("Aqui&2");
		}
		
		//System.out.println("nbChange " + nbChange);
		for (int j = 0; j < nbChange; j ++) {
			int nbC = 0;
			
			if (!variablesPossiblyChange.isEmpty()) {
				nbC = this.rand.nextInt(variablesPossiblyChange.size());
			}
			auxValue = variablesPossiblyChange.get(nbC).getValue();
			
			
			hprime = this.h.get(nbC) + this.epsilonValueVariable(this.variablesFunction.get(nbC).getVarInfluence());
			resH = this.calculValueOfVariableWithHprime(auxValue, hprime, variablesPossiblyChange.get(nbC).getMin(), variablesPossiblyChange.get(nbC).getMax());
			this.variablesObserved1.set(nbC, resH);
			Variable varC = variablesPossiblyChange.get(nbC);
			variablesPossiblyChange.remove(varC);
		}
		System.out.println("Aqui");
	}
	
	public double epsilonValueVariable(int nbInfl) {
		double res;
		double auxres;
		double y = Math.random()*0.5;
		
		auxres =  Math.pow(nbInfl, y);
		res = Math.pow(2, -auxres);
		
		return res;
	}
	
	private double calculValueOfVariableWithHprime(double variable, Double hprime, double min, double max) {
		double res = variable;
		// resultat de la fonction
		double resFun;
		
		resFun = hprime;

		double secop = Math.pow(variable -resFun,2);

		double denom = secop+1;

		if(resFun == 0.0) {
			res = min + new Random().nextDouble()*(max-min);
		}
		else {
			resFun = Math.abs(resFun);
			if (resFun <= variable) {
				res = variable + (min-variable)*secop/denom;
			}
			else {
				res = variable + (max-variable)*secop/denom;
			}
		}
	
		return res;
	}
	
	public void printAllObservedVariables() {
		for (int s = 0; s < variablesObserved1.size(); s++) {
			System.out.println(this.variablesObserved1.get(s));
		}
	}
	
	public void printAllHprimeVariables() {
		for (int s = 0; s < h.size(); s++) {
			System.out.println(this.h.get(s));
		}
	}

}
