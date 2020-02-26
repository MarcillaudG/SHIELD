package fr.irit.smac.complex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SubFunction {

	public enum MathForm{SQRT,POW,LOG,INV};
	
	private MathForm myForm;
	
	private String name;
	
	private ComposedFunction masterFunction;
	
	private List<Input<?>> inputs;
	
	private Input<?> myOutput;
	
	private boolean satisfied;
	
	private int nbInput;
	
	private int transform;
	
	private int crit;
	
	public SubFunction(String name, ComposedFunction cf, int nbInput, int transform) {
		this.name = name;
		this.masterFunction = cf;
		this.satisfied = false;
		this.transform = transform;
		this.inputs = new ArrayList<>();
		this.crit = 0;
		this.nbInput = nbInput;
		
		Random rand = new Random();
		
		if(rand.nextInt(2)%2==0) {
			this.myOutput = new InputFloat(0.0f,this.masterFunction,this.name+ "Output",this.transform);
		}
		else {
			this.myOutput = new InputInt(0,this.masterFunction,this.name+ "Output",this.transform);
		}
		
		this.myForm = MathForm.values()[rand.nextInt(MathForm.values().length)];
		
		this.masterFunction.newInput(this.myOutput);
	}
	
	public boolean isSatisfied() {
		return this.nbInput == this.inputs.size();
	}
	
	public boolean bindInput(Input<?> in) {
		if(this.isSatisfied()) {
			return false;
		}
		this.inputs.add(in);
		//this.nbInput++;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((masterFunction == null) ? 0 : masterFunction.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SubFunction other = (SubFunction) obj;
		if (masterFunction == null) {
			if (other.masterFunction != null)
				return false;
		} else if (!masterFunction.equals(other.masterFunction))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNbInput() {
		return nbInput;
	}

	public void setNbInput(int nbInput) {
		this.nbInput = nbInput;
	}

	public int getTransform() {
		return transform;
	}

	public void setTransform(int transform) {
		this.transform = transform;
	}

	public void setSatisfied(boolean satisfied) {
		this.satisfied = satisfied;
	}

	public int getCrit() {
		return this.crit;
	}

	public void addInput() {
		this.nbInput++;
		
	}
	
	public String toString() {
		String res = this.name +": "+this.myForm.toString()+"(";
		for(Input<?> in:this.inputs) {
			res+= in.getName()+"+";
		}
		res+=")";
		return res;
	}
	
	
	
}
