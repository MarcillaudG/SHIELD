package fr.irit.smac.complex;

public class OutputFloat extends Output<Float> {

	public OutputFloat(Float value, int transform, String name,ComposedFunction cf) {
		super(value, transform, name, cf);
	}

	@Override
	public Float compute() {
		return this.getValue();
	}

	@Override
	public SubFunction<Float> createSubFunction(String name, int nbInput) {
		return new SubFunctionFloat(name, this.getCf(), nbInput, this.getTransform());
		
	}
	
	@Override
	public void perceiveValue() {
		this.setValue(this.binded.getValue()%this.getMax()+this.getMin());
	}

}
