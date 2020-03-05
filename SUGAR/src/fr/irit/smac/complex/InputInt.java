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

}
