package fr.irit.smac.shield.c2av;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import fr.irit.smac.shield.exceptions.NotEnoughParametersException;
import fr.irit.smac.shield.exceptions.TooMuchVariableToRemoveException;

public class SyntheticFunction {

	private String name;

	private GeneratorOfFunction generator;

	private Deque<Operator> operators;

	private Deque<String> operands;
	
	private Set<String> operandsRemoved;

	//private Deque<Input> inputs;
	
	private Map<Integer,Input> inputs;
	
	private double lastValue;
	
	private int nbInput;

	public enum Operator{ADD,SUB,MULT};

	public SyntheticFunction(String name,GeneratorOfFunction gen) {
		this.name = name;
		this.generator = gen;
		this.operandsRemoved = new TreeSet<String>();
		//this.inputs = new ArrayDeque<Input>();

		init();
	}

	public SyntheticFunction(String name2,GeneratorOfFunction gen, Deque<String> operands) {
		this.name = name2;
		this.operands = operands;
		this.generator = gen;
		this.operandsRemoved = new TreeSet<String>();
		initOperators();
		initInputs();
	}


	public SyntheticFunction(String name2, GeneratorOfFunction gen, Deque<String> operands, Deque<Operator> operators) {
		this.name = name2;
		this.operands = operands;
		this.operators = operators;
		this.generator = gen;
		this.operandsRemoved = new TreeSet<String>();
		initInputs();
	}


	private void init() {
		this.operands = new ArrayDeque<String>();
		this.operators = new ArrayDeque<Operator>();

		this.lastValue = 0.0;
		initInputs();
	}

	/**
	 * Init les input
	 */
	private void initInputs() {
		this.nbInput = 0;
		this.inputs = new TreeMap<Integer,Input>();
		Deque<String> operandsTmp = new ArrayDeque<String>(this.operands);
		Deque<Operator> operatorsTmp = new ArrayDeque<Operator>(this.operators);
		Input first = new Input(Operator.ADD,operandsTmp.poll());
		this.inputs.put(this.nbInput,first);
		this.nbInput++;
		while(!operandsTmp.isEmpty()) {
			Input input = new Input(operatorsTmp.poll(),operandsTmp.poll());
			this.inputs.put(this.nbInput,input);
			this.nbInput++;
		}
		
	}

	private void initOperators() {
		this.operators = new ArrayDeque<Operator>();
		int nbVar = this.operands.size();
		Random rand = new Random();
		for(int i = 0 ; i < nbVar-1; i++) {
			switch(rand.nextInt(2)) {
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
	 * @return the value of the function
	 * 
	 */
	public double compute() {
		double res = 0.0;
		Deque<String> xi = new ArrayDeque<String>(this.operands);
		res = this.generator.getValueOfVariable(xi.poll());
		Deque<Operator> tmp = new ArrayDeque<Operator>(operators);
		while(!xi.isEmpty()) {
			String var = xi.poll();
			double value =this.generator.getValueOfVariable(var); 
			if(this.operandsRemoved.contains(var)) {
				value = this.generator.getWorstCaseValue(var);
			}
			res = operate(res,value,tmp.poll());
		}
		this.lastValue = res;
		return res;
	}
	
	public double compute(Deque<Double> queue) {
		double res =0.0;
		Deque<Double> xi = new ArrayDeque<Double>(queue);
		Deque<Operator> tmp = new ArrayDeque<Operator>(operators);
		while(!xi.isEmpty()) {
			double value =xi.poll();
			res = operate(res,value,tmp.poll());
		}
		this.lastValue = res;
		return res;
	}
	
	public double computeInput(Deque<Double> queue) {
		double res =0.0;
		for(int i = 0; i < this.nbInput; i++) {
			String operand = this.inputs.get(i).getOperand();
			double value = 0.0;
			if(operand.equals("UNKNOWN")) {
				value = this.generator.getWorstCaseValue(operand);
			}
			else {
				value = this.generator.getValueOfVariable(operand);
			}
			res = operate(res,value,this.inputs.get(i).getOperator());
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
		Set<String> opToRemove = new TreeSet<String>();

		for(int i = 0 ; i < nbToRemove; i++) {
			int toRemove = rand.nextInt(queVariableTmp.size());
			opToRemove.add(queVariableTmp.remove(toRemove));
		}
		SyntheticFunction degraded = new SyntheticFunction(this.name,this.generator, new ArrayDeque<String>(this.operands), new ArrayDeque<Operator>(queOperatorsTmp));
		degraded.setOpRemoved(opToRemove);
		return degraded;
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
	public SyntheticFunction degradeFunctionInput(int nbToRemove) throws TooMuchVariableToRemoveException {
		Random rand = new Random();
		if(nbToRemove > this.operands.size()) {
			throw new TooMuchVariableToRemoveException("Error in degradeFunction : "+nbToRemove+ " asked to remove but only "+this.operands.size() + " are availables");
		}
		// Collections used to remove randomly 
		List<Integer> inputToRemove = new ArrayList<Integer>(inputs.keySet());
		Set<Integer> opToRemove = new TreeSet<Integer>();

		for(int i = 0 ; i < nbToRemove; i++) {
			int toRemove = rand.nextInt(inputToRemove.size());
			int removed = inputToRemove.remove(toRemove);
			String operand = new String(this.inputs.get(removed).getOperand());
			opToRemove.add(removed);
		}
		SyntheticFunction degraded = new SyntheticFunction(this.name,this.generator, new ArrayDeque<String>(this.operands), new ArrayDeque<Operator>(this.operators));
		degraded.setOpRemovedInput(opToRemove);
		return degraded;
	}

	private void setOpRemoved(Set<String> opToRemove) {
		this.operandsRemoved = opToRemove;
	}
	
	private void setOpRemovedInput(Set<Integer> opToRemove) {
		for(Integer opRemoved : opToRemove) {
			this.operandsRemoved.add(this.inputs.get(opRemoved).getOperand());
			this.inputs.get(opRemoved).setOperand("UNKNOWN");
		}
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
		return "SyntheticFunction [name=" + name + ", inputs=" + inputs + "]";
	}

	public int getNbRemoved() {
		return this.operandsRemoved.size();
	}

	public String toStringRemoved() {

		return "SyntheticFunction [name=" + name + ", removed=" + this.operandsRemoved + "]";
	}

}
