package fr.irit.smac.shield.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import fr.irit.smac.shield.exceptions.NotEnoughParametersException;

public class Generator {

	/**
	 * Singleton or intern class
	 */
	private Random rand;

	public static int NB_MAX_VAR;

	public static double MIN_VAR = 0.0;

	public static double MAX_VAR = 1.0;

	protected Map<String,Variable> variables;

	protected int nbVar;

	public Generator() {
		NB_MAX_VAR = 10;
		init();
	}

	public Generator(int nbVarMax) {
		NB_MAX_VAR = nbVarMax;
		init();
	}



	private void init() {
		this.variables = new TreeMap<String,Variable>();
		this.rand = new Random();
		this.nbVar = 1000;

		System.out.println("Success of the initialization");
	}

	

	/**
	 * Return the new value 
	 * 
	 * @param variable
	 * 
	 * @return the value
	 */
	public double getValueOfVariableAfterCalcul(String variable) {
		Variable var = this.variables.get(variable);

		Deque<Double> values = new ArrayDeque<Double>();
		Deque<String> paramTmp = new ArrayDeque<String>(var.getFun().getVariables());
		while(!paramTmp.isEmpty()) {
			values.offer(this.variables.get(paramTmp.poll()).getValue());
		}
		//double res = calculValueOfVariable(var.getValue(), var.getFun(), values, var.getMin(), var.getMax());
		double res = calculValueOfVariable(var,values);
		this.variables.get(variable).setValue(res);
		return res;
	}

	private double calculValueOfVariable(Variable var, Deque<Double> xi) {
		// TODO REFACTOR
		double variable = var.getValue();
		FunctionGen h = var.getFun();
		double max = var.getMax();
		double min = var.getMin();
		
		double res = variable;
		// resultat de la fonction
		double resFun;
		try {
			resFun = h.compute(xi,var);

			double secop = Math.log(Math.abs(variable - resFun));

			double denom = secop+max;

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

		} catch (NotEnoughParametersException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Create a new variable and construct the corresponding function
	 * @param variable
	 */
	@Deprecated
	public void initVariable(String variable) {
		List<String> variablesRemaining = new ArrayList<String>(this.variables.keySet());

		Variable v = new Variable(variable,MIN_VAR,MAX_VAR);

		int nbVar = this.rand.nextInt(Math.min(NB_MAX_VAR, variablesRemaining.size())+1);
		Deque<String> parameters = new ArrayDeque<String>();

		for(int i = 0; i < nbVar && variablesRemaining.size()>0;i++) {
			String param = variablesRemaining.get(this.rand.nextInt(variablesRemaining.size()));
			parameters.push(param);
			Variable var = this.variables.get(param);

			variablesRemaining.remove(param);
			variablesRemaining.removeAll(var.getFun().getVariables());
		}
		v.setFun(FunctionGen.generateFunction(parameters.size(), parameters));

		this.variables.put(variable,v);

	}
	
	/**
	 * Create a new variable and construct the corresponding function
	 * @param variable
	 */
	public void initVariableWithRange(String variable, double min, double max) {
		List<String> variablesRemaining = new ArrayList<String>(this.variables.keySet());

		Variable v = new Variable(variable,min,max);

		int nbVar = this.rand.nextInt(Math.min(NB_MAX_VAR, variablesRemaining.size())+1);
		Deque<Variable> parameters = new ArrayDeque<Variable>();

		for(int i = 0; i < nbVar && variablesRemaining.size()>0;i++) {
			String param = variablesRemaining.get(this.rand.nextInt(variablesRemaining.size()));
			parameters.push(this.variables.get(param));
			Variable var = this.variables.get(param);

			variablesRemaining.remove(param);
			variablesRemaining.removeAll(var.getFun().getVariables());
		}
		v.setFun(FunctionGen.generateFunctionWithRange(parameters.size(), parameters,min,max));

		this.variables.put(variable,v);


	}

	@Deprecated
	public void initVariable() {
		this.initVariable("Variable"+this.nbVar);
		this.nbVar++;
	}
	

	/**
	 * Create a new variable with a name generated
	 */
	public void initVariableWithRange(double min, double max) {
		this.initVariableWithRange("Variable"+this.nbVar,min,max);
		this.nbVar++;
	}

	/**
	 * For all variables, generate the new value
	 */
	public void generateAllValues() {
		for(String s : this.variables.keySet()) {
			this.getValueOfVariableAfterCalcul(s);
		}
	}

	public void printAllVariables() {
		for(String s : this.variables.keySet()) {
			System.out.println(this.variables.get(s));
		}

	}

	public Double getValueOfVariable(String s) {
		return this.variables.get(s).getValue();

	}

	/**
	 * Return the collection with the name of all variables
	 * 
	 * @return the set with all the name
	 * 
	 */
	public Set<String> getAllVariables() {
		return this.variables.keySet();
	}

	/**
	 * Return the value of a variable
	 * 
	 * @param var
	 * 
	 * @return the value
	 */
	public double getValueOfH(String var) {
		return this.variables.get(var).getFun().getLastValue();
	}
}
