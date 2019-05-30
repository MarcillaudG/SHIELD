package fr.irit.smac.shield.recovac;

import fr.irit.smac.shield.model.FunctionGen;

public class VariableTuple {
	private String variableName;
	private FunctionGen fun;
	private float value;
	private float valuePredicted;
	private float valueObserved1;
	private float valueObserved2;
	private float deviation1;
	private float deviation2;
	private String type;
	
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
	
	public float getValueObserved1() {
		return valueObserved1;
	}
	
	public void setValueObserved1(float _valueObserved) {
		this.valueObserved1 = _valueObserved;
	}
	
	public float getValueObserved2() {
		return valueObserved2;
	}
	
	public void setValueObserved2(float _valueObserved) {
		this.valueObserved2 = _valueObserved;
	}
	
	public float getDeviation1() {
		return deviation1;
	}
	
	public void setDeviation1(float _deviation) {
		this.deviation1 = _deviation;
	}
	
	public float getDeviation2() {
		return deviation2;
	}
	
	public void setDeviation2(float _deviation) {
		this.deviation2 = _deviation;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	} 
	
	@Override
	public String toString() {
		return variableName + "[value=" + value + ", predictedValue=" + valuePredicted + ", observedValueEp1=" + valueObserved1 + ", deviation1=" + deviation1 + ", observedValueEp2=" + valueObserved2 + ", deviation2=" + deviation2 +"] \n";
		//return variableName + " [predictedValue= " + valuePredicted + ", observedValueEp1= " + valueObserved1 + ", deviation1= " + deviation1 + ", observedValueEp2= " + valueObserved2 + ", deviation2= " + deviation2 +"] \n";

	}
	
	public String printComplet() {
		return variableName + "[value=" + value + ", predictedValue=" + valuePredicted + ", observedValue=" + valueObserved1 + ", function: h=" + fun.getOperators()+ ", nbVarInfluen=" + fun.getVariables().size() +"]";
	}
}
