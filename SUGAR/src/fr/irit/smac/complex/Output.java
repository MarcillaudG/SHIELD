package fr.irit.smac.complex;

public abstract class Output<type extends Number> {

	private type value;
	
	private int transform;
	
	private String name;
	
	protected Input<type> binded;
	
	private ComposedFunction cf;
	
	private int min;
	
	private int max;
	
	public Output(type value, int transform, String name,ComposedFunction cf) {
		this.value = value;
		this.transform = transform;
		this.name = name;
		this.cf = cf;
		this.min = 0;
		this.max = 50;
	}
	
	public Output(type value, int transform, String name,ComposedFunction cf, int min, int max) {
		this.value = value;
		this.transform = transform;
		this.name = name;
		this.cf = cf;
		this.min = min;
		this.max = max;
	}

	public abstract type compute();
	
	public type getValue() {
		return this.value;
	}
	
	public void bind(Input<type> in) {
		this.binded = in;
	}
	
	public boolean satisfied() {
		return !(this.binded == null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + transform;
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
		Output other = (Output) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (transform != other.transform)
			return false;
		return true;
	}

	public int getTransform() {
		return this.transform;
	}

	@Override
	public String toString() {
		return "Output [name=" + name + ", binded=" + binded + "]";
	}


	public abstract void perceiveValue();
	
	public abstract SubFunction<type> createSubFunction(String name, int nbInput);

	public ComposedFunction getCf() {
		return cf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setValue(type value) {
		this.value = value;
	}
	
	/*public SubFunction<type> createSubFunction(String name, int nbInput){
		return new SubFunction<type>(name, this.cf, nbInput, this.transform);
	}*/
	
	
	
}
