package fr.irit.smac.complex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class SubFunction<type extends Number> {

	private String name;

	protected ComposedFunction masterFunction;

	private List<Input<?>> inputs;

	protected Input<type> myOutput;

	private boolean satisfied;

	private int nbInput;

	private int transform;

	private int crit;
	
	protected float factor;

	public SubFunction(String name, ComposedFunction cf, int nbInput, int transform) {
		this.name = name;
		this.masterFunction = cf;
		this.satisfied = false;
		this.transform = transform;
		this.inputs = new ArrayList<>();
		this.crit = 0;
		this.nbInput = nbInput;

		Random rand = new Random();

		this.factor = rand.nextFloat() *4.0f-2.0f;



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
		String res = this.name +": "+this.factor+"(";
		for(Input<?> in:this.inputs) {
			res+= in.getName()+"+";
		}
		res+=")";
		return res;
	}

	public abstract void compute() ;
	
	/*public type applyFormula(type res) {
		switch(this.myForm) {
		case INV:
			return 1/res;
		case LOG:
			return (float) Math.log(res);
		case POW:
			return res*res;
		case SQRT:
			return (float) Math.sqrt(res);
		default:
			break;

		}
		return res;
	}*/
	
	public abstract type applyFormule(type value);

	public abstract SubFunction<type> createSubFunction(String name, int nbInput);
	
	/*public SubFunction<type> createSubFunction(String name, int nbInput){
		return new SubFunction<type>(name,this.masterFunction, nbInput, this.transform-1);
	}
*/
	
	public void SetValueOfMyOutput(type value) {
		this.myOutput.setValue(value);
	}

	public List<Input<?>> allInput(){
		return this.inputs;
	}


	public ComposedFunction getMasterFunction() {
		return masterFunction;
	}
	
}
