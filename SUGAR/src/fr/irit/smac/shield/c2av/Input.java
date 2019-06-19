package fr.irit.smac.shield.c2av;

import fr.irit.smac.shield.c2av.SyntheticFunction.Operator;

public class Input {

	private Operator operator;
	
	private String operand;
	
	public Input(Operator ope, String operand) {
		this.operator = ope;
		this.operand = operand;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getOperand() {
		return operand;
	}

	public void setOperand(String operand) {
		this.operand = operand;
	}

	@Override
	public String toString() {
		return "Input [operator=" + operator + ", operand=" + operand + "]";
	}
	
	
	
}
