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
	protected Random rand;

	public static int NB_MAX_VAR;

	public static double MIN_VAR = 0.0;

	public static double MAX_VAR = 10.0;

	protected Map<String,Variable> variables; 
	
	protected Map<String,Variable> variablesActionState;

	protected int nbVar;
	
	protected List<Double> h;

	public Generator() {
		NB_MAX_VAR = 10;
		init();
	}

	public Generator(int nbVarMax) {
		NB_MAX_VAR = nbVarMax;
		init();
	}

	protected void init() {
		this.variables = new TreeMap<String,Variable>(); //tree map?
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
			String auxparam = paramTmp.poll();
			if (auxparam.contains("Context V")) {
				values.offer(this.variables.get(auxparam).getValue());
			}
			else {
				values.offer(this.variablesActionState.get(auxparam).getValue());
			}
		}
		
		double res = calculValueOfVariable(var,values);
		this.variables.get(variable).setValue(res);
		return res;
	}

	protected double calculValueOfVariable(Variable var, Deque<Double> xi) {
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

			double secop = Math.log(Math.abs(variable -resFun));
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
			this.h.add(resFun);
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
	protected void initVariable(String variable) {
		Variable v = new Variable(variable,MIN_VAR,MAX_VAR);
		//System.out.println(v.getName()+ " " +v.getValue());
		
		this.initFunction(v);
	}
	
	/**
	 * Create a new variable and construct the corresponding function
	 * @param variable
	 */
	private void initVariableWithRange(String variable, double min, double max) {
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
	
	/**
	 * Construct the corresponding function
	 * @param variable
	 */
	protected void initFunction(Variable variable) {
		List<String> variablesRemaining = new ArrayList<String>(this.variables.keySet());
		int nbVar = this.rand.nextInt(Math.min(NB_MAX_VAR, variablesRemaining.size())+1);
		
		Deque<String> parameters = new ArrayDeque<String>();
				
		for(int i = 0; i < nbVar && variablesRemaining.size()>0;i++) {
			String param = variablesRemaining.get(this.rand.nextInt(variablesRemaining.size()));
			parameters.push(param);
			Variable var = this.variables.get(param);

			variablesRemaining.remove(param);
			variablesRemaining.removeAll(var.getFun().getVariables()); //what?
		}
		variable.setFun(FunctionGen.generateFunction(parameters.size(), parameters));

		this.variables.put(variable.getName(),variable);
		System.out.println(variable.getName()+ " " + variable.getFun().getVariables() + variable.getFun().getOperators());
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
	
	public void printAllValuesOfH() {
		for(String s : this.variables.keySet()) {
			System.out.println(this.variables.get(s).getFun().getLastValue());
		}
	}
}
