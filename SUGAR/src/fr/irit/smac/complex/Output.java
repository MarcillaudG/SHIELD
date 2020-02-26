package fr.irit.smac.complex;

public abstract class Output<type> {

	private type value;
	
	private int transform;
	
	private String name;
	
	private Input<type> binded;
	
	public Output(type value, int transform, String name) {
		this.value = value;
		this.transform = transform;
		this.name = name;
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
	
	
}
