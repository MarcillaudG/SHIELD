package fr.irit.smac.complex;

import java.util.Set;

public class InputInt extends Input<Integer> {

	public InputInt(ComposedFunction cf,String name, int transform) {
		super( cf, name, transform);
	}
	
	@Override
	protected Set<SubFunction<Integer>> perceiveSubNonSatisfied(int transf, Set<SubFunction<Integer>> bind) {
		return this.getCf().getSubFunctionNonSatisfiedInt(transf, bind);
	}

	@Override
	public void setValue(Integer value) {
		this.value = value;
	}
	

	@Override
	public String toString() {
		return "InputInt: "+this.getName()+" : "+this.value;
	}

	@Override
	public void decideAndActFunction() {
		this.crit = this.binded.size();
		if(this.out == null) {

			SubFunction target = null;
			if(this.crit == 0) {
				if(this.transform == this.cf.getComplexity()) {
					for(Output out: this.outputs) {
						if(out instanceof OutputInt && !out.satisfied() && this.out == null) {
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
}
