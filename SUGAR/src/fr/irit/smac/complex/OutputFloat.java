package fr.irit.smac.complex;

public class OutputFloat extends Output<Float> {

	public OutputFloat(Float value, int transform, String name) {
		super(value, transform, name);
	}

	@Override
	public Float compute() {
		return this.getValue();
	}

}
