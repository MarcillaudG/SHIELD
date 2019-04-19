package fr.irit.smac.shield.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import fr.irit.smac.shield.exceptions.NotEnoughParametersException;
import fr.irit.smac.shield.exceptions.TooMuchVariableToRemoveException;

public class FunctionGen {


	public enum Operator{ADD,SUB,MULT};

	private int nbVar;

	private Deque<Operator> operators;

	private Deque<String> variables;

	private double lastValue;
	
	private double max;
	
	private double min;

	/**
	 * Return the value of the function
	 * @param xi
	 * 			the queue of the values of the variables
	 * @return the value of the function
	 * 
	 * @throws NotEnoughParametersException
	 * 			when the number of parameter in xi is not enough
	 */
	public double compute(Deque<Double> xi) throws NotEnoughParametersException {
		double res = 0.0;
		if(xi.size()>0) {
			if(xi.size() != this.operators.size()+1) {
				throw new NotEnoughParametersException("ERROR COMPUTATION : XI : "+xi.size()+" OPERATORS : "+operators.size());
			}
			res = xi.poll();
			Deque<Operator> tmp = new ArrayDeque<Operator>(operators);
			while(!xi.isEmpty()) {
				res = operate(res,xi.poll(),tmp.poll());
			}
		}
		this.lastValue = res;
		if(Math.abs(this.maxOfFunction()) > 1.0) {
			res = res / this.maxOfFunction();
		}
		return res;
	}

	private double operate(double res, Double poll, Operator poll2) {
		switch(poll2) {
		case ADD:
			return res + poll;
			/*case DIV:
			return res / poll;*/
		case MULT:
			return res * poll;
		case SUB:
			return res - poll;
		default:
			return res;

		}
	}

	/**
	 * The constructor
	 * 
	 * @param nbVar
	 * @param variables
	 */
	private FunctionGen(int nbVar, Deque<String> variables) {
		this.nbVar = nbVar;
		this.operators = new ArrayDeque<Operator>();
		this.variables = variables;
		this.lastValue = 0.0;
	}


	/**
	 * The constructor where we can specify the operators
	 * 
	 * @param nbVar
	 * @param variables
	 * @param queOperatorsTmp
	 */
	public FunctionGen(int nbVar, Deque<String> variables, Deque<Operator> operators) {
		this.nbVar = nbVar;
		this.operators = operators;
		this.variables = variables;
		this.lastValue = 0.0;
	}

	/**
	 * Static method to generate a function
	 * @param nbVar
	 * @param variables
	 * @return the new Function
	 */
	public static FunctionGen generateFunction(int nbVar, Deque<String> variables) {
		Random rand = new Random();
		FunctionGen res = new FunctionGen(nbVar,variables);

		for(int i = 0 ; i < nbVar-1; i++) {
			switch(rand.nextInt(3)) {
			case 0:
				res.addOperator(Operator.ADD);
				break;
			case 1:
				res.addOperator(Operator.SUB);
				break;
			case 2:
				res.addOperator(Operator.MULT);
				break;
				/*case 3:
				res.addOperator(Operator.DIV);
				break;*/
			default:
				break;
			}
		}

		return res;
	}


	/**
	 * Function used to degrade a function
	 * 
	 * Construct a new function with some of the variables
	 * and parameters remove
	 * 
	 * @param function
	 * 			the function to degrade
	 * @param nbToRemove
	 * 			the number of variables to remove
	 * @return the degraded function
	 * 
	 * @throws TooMuchVariableToRemoveException
	 * 			when nbToRemove is higher than the number of variables
	 * 			of the function
	 */
	public static FunctionGen degradeFunction(FunctionGen function, int nbToRemove) throws TooMuchVariableToRemoveException {
		Random rand = new Random();
		if(nbToRemove > function.getVariables().size()) {
			throw new TooMuchVariableToRemoveException("Error in degradeFunction : "+nbToRemove+ " asked to remove but only "+function.getVariables().size() + " are availables");
		}
		// Collections used to remove randomly 
		List<String> queVariableTmp = new ArrayList<String>(function.variables);
		List<Operator> queOperatorsTmp = new ArrayList<Operator>(function.operators);
		
		for(int i = 0 ; i < nbToRemove; i++) {
			int toRemove = rand.nextInt(queVariableTmp.size());
			queVariableTmp.remove(toRemove);
		}
		FunctionGen degraded = new FunctionGen(function.getVariables().size()-nbToRemove, new ArrayDeque<String>(queVariableTmp), new ArrayDeque<Operator>(queOperatorsTmp));
		return degraded;
	}
	
	/**
	 * Function used when we don't want to specify the number of variables to remove
	 * 
	 * @param function
	 * 
	 * @return the degraded function
	 * 
	 * @throws TooMuchVariableToRemoveException
	 * 		when the random gives a wrong answer (unlikely to happen)
	 */
	public static FunctionGen degradeFunction(FunctionGen function) throws TooMuchVariableToRemoveException {
		Random rand = new Random();
		return FunctionGen.degradeFunction(function, rand.nextInt(function.nbVar));
	}

	/**
	 * Add an operator for the function
	 * @param ope
	 */
	private void addOperator(Operator ope) {
		this.operators.push(ope);
	}

	public Deque<String> getVariables(){
		return this.variables;
	}

	public double getLastValue() {
		return this.lastValue;
	}

	/**
	 * Return the maximum a function can have
	 * when all variables are between 0 and 1.0
	 * 
	 * @return the maximum
	 */
	public double maxOfFunction() {
		double res = 1.0;
		Deque<Operator> tmp = new ArrayDeque<Operator>(operators);
		while(!tmp.isEmpty()) {
			Operator ope = tmp.poll();
			if(ope == Operator.SUB) {
				res = operate(res,0.0,ope);
			}
			else {
				res = operate(res,1.0,ope);
			}
		}
		return res;
	}
	
	/**
	 * Set the maximum that the function can have
	 * 
	 */
	private void maxOfFunctionWithPlage(Deque<Variable> values) {
		Deque<Variable> valuesTmp = new ArrayDeque<Variable>(values);
		double res = valuesTmp.poll().getMax();
		Deque<Operator> tmp = new ArrayDeque<Operator>(operators);
		while(!tmp.isEmpty()) {
			Operator ope = tmp.poll();
			Variable var = valuesTmp.poll();
			if(ope == Operator.SUB) {
				res = operate(res,var.getMin(),ope);
			}
			else {
				res = operate(res,var.getMax(),ope);
			}
		}
		this.max = res;
	}
	
	/**
	 * Set the maximum that the function can have
	 * 
	 */
	private void minOfFunctionWithPlage(Deque<Variable> values) {
		Deque<Variable> valuesTmp = new ArrayDeque<Variable>(values);
		double res = valuesTmp.poll().getMin();
		Deque<Operator> tmp = new ArrayDeque<Operator>(operators);
		while(!tmp.isEmpty()) {
			Operator ope = tmp.poll();
			Variable var = valuesTmp.poll();
			if(ope == Operator.SUB) {
				res = operate(res,var.getMax(),ope);
			}
			else {
				res = operate(res,var.getMin(),ope);
			}
		}
		this.max = res;
	}

}
