package fr.irit.smac.complex;

public class OutputFloat extends Output<Float> {

	public OutputFloat(Float value, int transform, String name,ComposedFunction cf) {
		super(value, transform, name, cf);
	}

	float mod ( float x , int y )
	{
		return x >= 0 ? x % y : y - 1 - ((-x-1) % y) ;
	}
	
	@Override
	public Float compute() {
		this.setValue(mod(this.binded.getValue(), this.getMax())+ this.getMin());
		return this.getValue();
	}

	@Override
	public SubFunction<Float> createSubFunction(String name, int nbInput) {
		return new SubFunctionFloat(name, this.getCf(), nbInput, this.getTransform());
		
	}
	
	@Override
	public void perceiveValue() {
		this.setValue(mod(this.binded.getValue(), this.getMax())+ this.getMin());
	}

}
