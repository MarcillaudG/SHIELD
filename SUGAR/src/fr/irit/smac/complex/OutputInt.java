package fr.irit.smac.complex;

public class OutputInt extends Output<Integer> {

	public OutputInt(Integer value, int transform, String name,ComposedFunction cf) {
		super(value, transform, name,cf);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Integer compute() {
		return this.getValue();
	}


	@Override
	public SubFunction<Integer> createSubFunction(String name, int nbInput) {
		return new SubFunctionInt(name, this.getCf(), nbInput, this.getTransform());
		
	}
	

	@Override
	public void perceiveValue() {
		this.setValue(this.binded.getValue()%this.getMax()+this.getMin());
	}
}
