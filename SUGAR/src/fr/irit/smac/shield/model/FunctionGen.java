package fr.irit.smac.shield.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

import fr.irit.smac.shield.exceptions.NotEnoughParametersException;

public class FunctionGen {


	public enum Operator{ADD,SUB,MULT};

	private int nbVar;

	private Deque<Operator> operators;

	private Deque<String> variables;
	
	private double lastValue;

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

	private FunctionGen(int nbVar, Deque<String> variables) {
		this.nbVar = nbVar;
		this.operators = new ArrayDeque<Operator>();
		this.variables = variables;
		this.lastValue = 0.0;
	}



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

	private void addOperator(Operator ope) {
		this.operators.push(ope);
	}

	public Deque<String> getVariables(){
		return this.variables;
	}
	
	public Deque<Operator> getOperators(){
		return this.operators;
	}
	
	public double getLastValue() {
		return this.lastValue;
	}
	
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
	
	
}
