package fr.irit.smac.shield.c2av;

import fr.irit.smac.shield.c2av.SyntheticFunction.Operator;

public class Input {

	private Operator operator;
	
	private String operand;
	
	private int id;
	
	public Input(Operator ope, String operand, int id) {
		this.operator = ope;
		this.operand = operand;
		this.id = id;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Input [operator=" + operator + ", operand=" + operand + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operand == null) ? 0 : operand.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Input other = (Input) obj;
		if (operand == null) {
			if (other.operand != null)
				return false;
		} else if (!operand.equals(other.operand))
			return false;
		if (operator != other.operator)
			return false;
		return true;
	}
	
	
	
	
}
