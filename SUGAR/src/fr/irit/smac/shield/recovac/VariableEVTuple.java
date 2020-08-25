package fr.irit.smac.shield.recovac;

import fr.irit.smac.shield.model.FunctionGen;

public class VariableEVTuple {
	private String variableName;
	private FunctionGen fun;
	private float currentValue;
	private float intentionalValue;
	private float realValue;
	private float deviation;
	private String type;
	
	public VariableEVTuple(String _name, FunctionGen _fun, float _value) {
		this.variableName = _name;
		this.fun = _fun;
		this.currentValue = _value;
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
	
	public float getCurrentValue() {
		return currentValue;
	}
	
	public void setCurrentValue(float _value) {
		this.currentValue = _value;
	}
	
	public float getIntentionalValue() {
		return intentionalValue;
	}
	
	public void setIntentionalValue(float _valuePredicted) {
		this.intentionalValue = _valuePredicted;
	}
	
	public float getRealValue() {
		return realValue;
	}
	
	public void setRealValue(float _valueObserved) {
		this.realValue = _valueObserved;
	}
	
	public float getDeviation() {
		return deviation;
	}
	
	public void setDeviation(float _deviation) {
		this.deviation = _deviation;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	} 
	
	@Override
	public String toString() {
		return variableName + "[value=" + currentValue + ", predictedValue=" + intentionalValue + ", observedValueEp=" + realValue + ", deviation=" + deviation +"] \n";
	}
	
	public String printComplet() {
		return variableName + "[value=" + currentValue + ", predictedValue=" + intentionalValue + ", observedValue=" + realValue + ", function: h=" + fun.getOperators()+ ", nbVarInfluen=" + fun.getVariables().size() +"]";
	}
}
