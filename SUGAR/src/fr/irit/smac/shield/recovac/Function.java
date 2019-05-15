package fr.irit.smac.shield.recovac;

import fr.irit.smac.shield.model.FunctionGen;

public class Function {
	private String name;
	
	private FunctionGen fun;

	public Function (String _name, FunctionGen _fun) {
		this.name = _name;
		this.fun = _fun;
	}
	
	public String getName() {
		return name;
	}
	
	public void setFun(String _name) {
		this.name = _name;
	}
	
	public FunctionGen getFun() {
		return fun;
	}
	
	public void setFun(FunctionGen _fun) {
		this.fun = _fun;
	}
	
	public int getVarInfluence() {
		return fun.getVariables().size();
	}
	
	@Override
	public String toString() {
		return "Variable [name=" + name + ", function: h=" + fun.getOperators()+ ", nbVarInfluen=" + fun.getVariables().size() +"]";
	}
	
}
