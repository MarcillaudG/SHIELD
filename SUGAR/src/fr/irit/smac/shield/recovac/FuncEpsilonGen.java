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
	private List<Double> newValueVariables1;
	private List<Double> newValueVariables2;
	private List<Double> hVariables;
	private double yEp1;


	public FuncEpsilonGen(int numberV, List<Variable> variables, List<Double> h) {
		this.nbVariables = numberV;
		this.generatedVariables = variables;
		this.hVariables = h;
		this.random = new Random();
		this.newValueVariables1 = new ArrayList<Double>();
		this.newValueVariables2 = new ArrayList<Double>();
		this.yEp1 = Math.random()*0.5;
	}
	
	//Value after applying the epsilon function 1
	public void generateAllObservedValuesWithFunc() {	
		int nbChange = this.random.nextInt(nbVariables);
		List<Variable> variablesPossiblyChange = new ArrayList<Variable>();
		double hprime1;
		double hprime2;
		double resH1;
		double resH2;

		for(int i = 0; i < this.generatedVariables.size(); i++) {
			variablesPossiblyChange.add(this.generatedVariables.get(i));
			this.newValueVariables1.add(this.generatedVariables.get(i).getValue());
			this.newValueVariables2.add(this.generatedVariables.get(i).getValue());
		}
		
		for (int j = 0; j < nbChange && variablesPossiblyChange.size() > 0; j ++) {
			int nbC = this.random.nextInt(variablesPossiblyChange.size());;
						
			hprime1 = this.hVariables.get(nbC) + this.epsilonValueVariable1(this.generatedVariables.get(nbC).getFun().getNbInflu());
			hprime2 = this.hVariables.get(nbC) + this.epsilonValueVariable2(variablesPossiblyChange.get(nbC).getValue());
			//System.out.println("hprime 1 " + hprime1 + "hprime2 " + hprime2);
			
			resH1 = this.calculateValueOfVariableWithHprime(variablesPossiblyChange.get(nbC), hprime1);
			resH2 = this.calculateValueOfVariableWithHprime(variablesPossiblyChange.get(nbC), hprime2);
			
			//System.out.println("resh1 " + resH1 + "resh2 " + resH2);
			
			this.newValueVariables1.set(nbC, resH1);
			this.newValueVariables2.set(nbC, resH2);
			
			Variable varC = variablesPossiblyChange.get(nbC);
			variablesPossiblyChange.remove(varC);
		}
	}
		
	//Funcion con la primera alfa 2^(-(n^y))
	public double epsilonValueVariable1(int nbInfl) {
		double res;
		double auxres;
		
		auxres =  Math.pow(nbInfl, yEp1);
		res = Math.pow(2, -auxres);
		
		return res;
	}
	
	//Funcion con la primera alfa y*e^(-y*x)
	public double epsilonValueVariable2(double value) {
		double res;
		double auxres2;
		double auxres3;
		
		//auxres1 = y * value;
		auxres2 =  Math.exp(-1); // xq estamos usando la esperanza del valor aleatorio -> y = 1/elvalor esperando de Vi
		auxres3 = 1 / value;
		res = auxres3 * auxres2;
		//res = y * auxres2;
		
		return res;
	}
	
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
		
	public List<Double> getVariablesObserved1(){
		return this.newValueVariables1;
	}
	
	public List<Double> getVariablesObserved2(){
		return this.newValueVariables2;
	}
	
	public double getY() {
		return this.yEp1;
	}
	
	public void printAllObservedVariables() {
		for (int s = 0; s < newValueVariables1.size(); s++) {
			System.out.println(this.newValueVariables1.get(s));
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
