package fr.irit.smac.shield.recovac;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fr.irit.smac.shield.model.Variable;

public class FuncEpsilonGen {
	private int nbVariables;
	private Random random;
	private List<Variable> generatedVariables; 
	private List<Double> newValueVariables;
	private List<Double> hVariables;
	private double yEp1;


	public FuncEpsilonGen(int numberV, List<Variable> variables, List<Double> h) {
		this.nbVariables = numberV;
		this.generatedVariables = variables;
		this.hVariables = h;
		this.random = new Random();
		this.newValueVariables = new ArrayList<Double>();
		this.yEp1 = Math.random()*0.5;
	}
	
	//Value after applying the epsilon function 1
	public void generateAllObservedValuesWithFunc() {	
		int nbChange = this.random.nextInt(nbVariables);
		List<Variable> variablesPossiblyChange = new ArrayList<Variable>();
		double hprime;
		double resH;


		for(int i = 0; i < this.generatedVariables.size(); i++) {
			variablesPossiblyChange.add(this.generatedVariables.get(i));
			this.newValueVariables.add(this.generatedVariables.get(i).getValue());
		}
		
		for (int j = 0; j < nbChange && variablesPossiblyChange.size() > 0; j ++) {
			int nbC = this.random.nextInt(variablesPossiblyChange.size());;
						
			hprime = this.hVariables.get(nbC) + this.epsilonValueVariable(this.generatedVariables.get(nbC).getFun().getNbInflu());
			//System.out.println("hprime 1 " + hprime1 );
			
			resH = this.calculateValueOfVariableWithHprime(variablesPossiblyChange.get(nbC), hprime);

			
			this.newValueVariables.set(nbC, resH);
			
			Variable varC = variablesPossiblyChange.get(nbC);
			variablesPossiblyChange.remove(varC);
		}
	}
		
	//Funcion con la primera alfa 2^(-(n^y))
	public double epsilonValueVariable(int nbInfl) {
		double res;
		double auxres;
		
		auxres =  Math.pow(nbInfl, yEp1);
		res = Math.pow(2, -auxres);
		
		return res;
	}
	
	//Funcion con la primera alfa y*e^(-y*x)
	/*public double epsilonValueVariable2(double value) {
		double res;
		double auxres2;
		double auxres3;
		
		//auxres1 = y * value;
		auxres2 =  Math.exp(-1); // xq estamos usando la esperanza del valor aleatorio -> y = 1/elvalor esperando de Vi
		auxres3 = 1 / value;
		res = auxres3 * auxres2;
		//res = y * auxres2;
		
		return res;
	}*/
	
	private double calculateValueOfVariableWithHprime(Variable variable, Double hprime) {
		double value = variable.getValue();
		double min = variable.getMin();
		double max = variable.getMax();

		double res = value;
		double resFun = hprime;
		double secop = Math.log(Math.abs(value - resFun));
		double denom = secop+max;

		if(resFun == 0.0) {
			res = min + new Random().nextDouble()*(max-min);
		}
		else {
			resFun = Math.abs(resFun);
			if (resFun <= value) {
				res = value + (min-value)*secop/denom;
			}
			else {
				res = value + (max-value)*secop/denom;
			}
		}
		return res;
	}
		
	public List<Double> getVariablesObserved(){
		return this.newValueVariables;
	}
	
	public double getY() {
		return this.yEp1;
	}
	
	public void printAllObservedVariables() {
		for (int s = 0; s < newValueVariables.size(); s++) {
			System.out.println(this.newValueVariables.get(s));
		}
	}
	
	public void printAllHprimeVariables() {
		for (int s = 0; s < hVariables.size(); s++) {
			System.out.println(this.hVariables.get(s));
		}
	}
	
	public double getMinValueList(List<Double> variables) {
		return Collections.min(variables);
	}
	
	public double getMaxValueList(List<Double> variables) {
		return Collections.max(variables);
	}
}
