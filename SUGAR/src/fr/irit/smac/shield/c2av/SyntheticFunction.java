package fr.irit.smac.shield.c2av;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import fr.irit.smac.shield.exceptions.NotEnoughParametersException;
import fr.irit.smac.shield.exceptions.TooMuchVariableToRemoveException;
import fr.irit.smac.shield.model.FunctionGen;
import fr.irit.smac.shield.model.FunctionGen.Operator;

public class SyntheticFunction {

	private String name;
	
	private Deque<Operator> operators;
	
	private Deque<String> operands;
	
	private double lastValue;

	public enum Operator{ADD,SUB,MULT};
	
	public SyntheticFunction(String name) {
		this.name = name;
		
		init();
	}

	public SyntheticFunction(String name2, Deque<String> operands) {
		this.name = name2;
		this.operands = operands;
		initOperators();
	}
	
	public SyntheticFunction(String name2, Deque<String> operands, Deque<Operator> operators) {
		this.name = name2;
		this.operands = operands;
		this.operators = operators;
	}


	private void init() {
		this.operands = new ArrayDeque<String>();
		this.operators = new ArrayDeque<Operator>();
		
		this.lastValue = 0.0;
	}
	

	private void initOperators() {
		this.operators = new ArrayDeque<Operator>();
		int nbVar = this.operands.size();
		Random rand = new Random();
		for(int i = 0 ; i < nbVar-1; i++) {
			switch(rand.nextInt(3)) {
			case 0:
				this.operators.push(Operator.ADD);
				break;
			case 1:
				this.operators.push(Operator.SUB);
				break;
			case 2:
				this.operators.push(Operator.MULT);
				break;
				/*case 3:
				res.addOperator(Operator.DIV);
				break;*/
			default:
				break;
			}
		}
	}
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
	public SyntheticFunction degradeFunction(int nbToRemove) throws TooMuchVariableToRemoveException {
		Random rand = new Random();
		if(nbToRemove > this.operands.size()) {
			throw new TooMuchVariableToRemoveException("Error in degradeFunction : "+nbToRemove+ " asked to remove but only "+this.operands.size() + " are availables");
		}
		// Collections used to remove randomly 
		List<String> queVariableTmp = new ArrayList<String>(this.operands);
		List<Operator> queOperatorsTmp = new ArrayList<Operator>(this.operators);
		
		for(int i = 0 ; i < nbToRemove; i++) {
			int toRemove = rand.nextInt(queVariableTmp.size());
			queVariableTmp.remove(toRemove);
		}
		SyntheticFunction degraded = new SyntheticFunction(this.name, new ArrayDeque<String>(queVariableTmp), new ArrayDeque<Operator>(queOperatorsTmp));
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
	public SyntheticFunction degradeFunction() throws TooMuchVariableToRemoveException {
		Random rand = new Random();
		return this.degradeFunction(rand.nextInt(this.operands.size()));
	}
	
	public String getName() {
		return this.name;
	}
	
	public Deque<String> getOperands(){
		return this.operands;
	}

	@Override
	public String toString() {
		return "SyntheticFunction [name=" + name + ", operators=" + operators + ", operands=" + operands
				+ ", lastValue=" + lastValue + "]";
	}
	
	
}
