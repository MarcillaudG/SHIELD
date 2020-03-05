package fr.irit.smac.complex;

import java.util.HashSet;
import java.util.Set;

public abstract class Input<T extends Number> {

	private String name;

	protected T value;

	private int crit;

	private ComposedFunction cf;
	private Set<SubFunction<T>> binded;
	private Set<SubFunction> sbns;
	private Set<Output> outputs;

	private Set<Input> others;

	private Output out;

	private int transform;

	public Input(ComposedFunction cf, String name, int transform) {
		crit = 0;
		this.cf = cf;
		this.binded = new HashSet<>();
		this.sbns = new HashSet<>();
		this.outputs = new HashSet<>();
		this.others = new HashSet<>();
		this.name = name;
		this.transform = transform;
	}

	public T getValue() {
		return this.value;
	}

	public abstract void setValue(T value);

	public void start() {

		perceive();
		decideAndAct();
	}

	private void perceive() {
		// perceive my value
	}

	private void decideAndAct() {
		// send my value to all binded function
	}

	public void perceiveFunction() {
		this.sbns.clear();
		//this.sbns.addAll(this.cf.getSubFunctionNonSatisfied(this.transform,this.binded));
		this.sbns.addAll(this.perceiveSubNonSatisfied(this.transform, this.binded));
		this.sbns.removeAll(this.binded);

		this.others.clear();
		this.others.addAll(this.cf.getOtherInput(this.name,this.transform,this.getClass()));

		this.outputs.clear();
		this.outputs.addAll(this.cf.getOutputs(this.transform));

	}
	protected abstract Set<SubFunction<T>> perceiveSubNonSatisfied(int transf, Set<SubFunction<T>> bind);

	public void decideAndActFunction() {
		this.crit = this.binded.size();
		if(this.out == null) {

			SubFunction target = null;
			if(this.crit == 0) {
				if(this.transform == this.cf.getComplexity()) {
					for(Output out: this.outputs) {
						if(!out.satisfied() && this.out == null) {
							out.bind(this);
							this.out = out;
						}
					}
				}
				else {
					for(SubFunction sub : this.sbns) {
						if((target == null || target.getCrit() < sub.getCrit()) && !this.binded.contains(sub)) {
							target = sub;
						}
					}
					//TODO
					//Creation de function quand pas trouve
					if(target == null) {
						this.cf.MissingFunction(this.transform);
					}
				}
			}
			else {
				boolean mostCrit = true;
				for(Input<?> in : this.others) {
					if(in.getCrit() <this.crit && !in.binded.containsAll(this.sbns)) {
						mostCrit = false;
					}
				}
				if(mostCrit) {
					for(SubFunction sub : this.sbns) {
						if((target == null || target.getCrit() < sub.getCrit()) && !this.binded.contains(sub)) {
							target = sub;
						}
					}
				}
			}

			// act
			if(target != null) {
				if(target.bindInput(this)) {
					this.binded.add(target);
					this.crit++;
				}
			}
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cf == null) ? 0 : cf.hashCode());
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
		Input other = (Input) obj;
		if (cf == null) {
			if (other.cf != null)
				return false;
		} else if (!cf.equals(other.cf))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return this.name;
	}


	public int getCrit() {
		return this.crit;
	}

	public int getTransform() {
		return transform;
	}

	public void setTransform(int transform) {
		this.transform = transform;
	}

	public ComposedFunction getCf() {
		return cf;
	}

	@Override
	public String toString() {
		return "Input [name=" + name + "]";
	}

	public boolean satisfied() {
		if(this.binded.size() >0 || this.out != null) {
			return true;
		}
		return false;
	}


	
}
