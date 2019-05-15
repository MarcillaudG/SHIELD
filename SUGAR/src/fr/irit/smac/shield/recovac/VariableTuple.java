package fr.irit.smac.shield.recovac;

import fr.irit.smac.shield.model.FunctionGen;

public class VariableTuple {
	private String variableName;
	private FunctionGen fun;
	private float value;
	private float valuePredicted;
	private float valueObserved;
	
	public VariableTuple(String _name, FunctionGen _fun, float _value) {
		this.variableName = _name;
		this.fun = _fun;
		this.value = _value;
	}
	public String getName() {
		return variableName;
	}
	
	public void setFun(String _name) {
		this.variableName = _name;
	}
	
	public FunctionGen getFun() {
		return fun;
	}
	
	public void setFun(FunctionGen _fun) {
		this.fun = _fun;
	}
	
	public float getValue() {
		return value;
	}
	
	public void setValue(float _value) {
		this.value = _value;
	}
	
	public float getValuePredicted() {
		return valuePredicted;
	}
	
	public void setValuePredicted(float _valuePredicted) {
		this.valuePredicted = _valuePredicted;
	}
	
	public float getValueObserved() {
		return valueObserved;
	}
	
	public void setValueObserved(float _valueObserved) {
		this.valueObserved = _valueObserved;
	}

	@Override
	public String toString() {
		return variableName + "[value=" + value + ", predictedValue=" + valuePredicted + ", observedValue=" + valueObserved + "] \n";
	}
	
	public String printComplet() {
		return variableName + "[value=" + value + ", predictedValue=" + valuePredicted + ", observedValue=" + valueObserved + ", function: h=" + fun.getOperators()+ ", nbVarInfluen=" + fun.getVariables().size() +"]";
	}
}
