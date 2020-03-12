package fr.irit.smac.complex;

public class OutputInt extends Output<Integer> {

	public OutputInt(Integer value, int transform, String name,ComposedFunction cf) {
		super(value, transform, name,cf);
		// TODO Auto-generated constructor stub
	}
	
	public OutputInt(int value, int transform, String name,ComposedFunction cf, int min, int max) {
		super(value, transform, name, cf,min,max);
	}
	
	int mod ( int x , int y )
	{
		return x >= 0 ? x % y : y - 1 - ((-x-1) % y) ;
	}
	
	@Override
	public Integer compute() {
		this.setValue(mod(this.binded.getValue(), this.getMax())+ this.getMin());
		return this.getValue();
	}


	@Override
	public SubFunction<Integer> createSubFunction(String name, int nbInput) {
		return new SubFunctionInt(name, this.getCf(), nbInput, this.getTransform());

	}


	@Override
	public void perceiveValue() {
		this.setValue(mod(this.binded.getValue(), this.getMax())+ this.getMin());
	}
}
