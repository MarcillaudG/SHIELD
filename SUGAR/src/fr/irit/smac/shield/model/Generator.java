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


	private Random rand;

	public static int NB_MAX_VAR;

	public static double MIN_VAR = 0.0;

	public static double MAX_VAR = 1.0;

	private Map<String,Variable> variables;

	private int nbVar;

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
	 * Return the value calculated by the function of the variable
	 * 
	 * @param variable
	 * 		The value of the variable
	 * @param h
	 * 		the function
	 * @param xi
	 * 		the parameters of the function
	 * @param min
	 * 		the minimal value
	 * @param max
	 * 		the maximale value
	 * @return the new value of the variable
	 */
	private double calculValueOfVariable(double variable, FunctionGen h,Deque<Double> xi, double min, double max) {
		double res = variable;
		// resultat de la fonction
		double resFun;
		try {
			resFun = h.compute(xi);
			//System.out.println("RESFUN : "+resFun);

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

		} catch (NotEnoughParametersException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Return the new value 
	 * @param variable
	 * @return
	 */
	public double getValueOfVariableAfterCalcul(String variable) {
		if(!this.variables.keySet().contains(variable)) {
			initVariable(variable);
		}
		Variable var = this.variables.get(variable);

		Deque<Double> values = new ArrayDeque<Double>();
		Deque<String> paramTmp = new ArrayDeque<String>(var.getFun().getVariables());
		while(!paramTmp.isEmpty()) {
			values.offer(this.variables.get(paramTmp.poll()).getValue());
		}
		double res = calculValueOfVariable(var.getValue(), var.getFun(), values, var.getMin(), var.getMax());
		//System.out.println("DIFF : "+(this.variables.get(variable).getValue()-res));
		this.variables.get(variable).setValue(res);
		return res;
	}

	/**
	 * Create a new variable and construct the corresponding function
	 * @param variable
	 */
	private void initVariable(String variable) {
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

		//System.out.println(v.getName()+ " " +v.getValue()+" "+ v.getFun().getVariables());

	}

	public void initVariable() {
		this.initVariable("Variable"+this.nbVar);
		this.nbVar++;
	}

	/**
	 * For all variables, generate the new values
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

	public Set<String> getAllVariables() {
		return this.variables.keySet();
	}

	public double getValueOfH(String var) {
		return this.variables.get(var).getFun().getLastValue();
	}
}
