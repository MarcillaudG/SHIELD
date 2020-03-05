package fr.irit.smac.complex;

import java.util.Set;

public class InputFloat extends Input<Float> {

	public InputFloat(ComposedFunction cf,String name, int transform) {
		super( cf, name, transform);
	}

	@Override
	protected Set<SubFunction<Float>> perceiveSubNonSatisfied(int transf, Set<SubFunction<Float>> bind) {
		return this.getCf().getSubFunctionNonSatisfiedFloat(transf, bind);
	}

	@Override
	public void setValue(Float value) {
		this.value = value;
		
	}

}
