package fr.irit.smac.complex;

public class SubFunctionFloat extends SubFunction<Float> {

	public SubFunctionFloat(String name, ComposedFunction cf, int nbInput, int transform) {
		super(name, cf, nbInput, transform);

		this.myOutput = new InputFloat(this.masterFunction,name+ "Output",transform);
		this.masterFunction.newInput(this.myOutput);
	}

	@Override
	public void compute() {
		float res = 0.0f;
		for(Input in: this.allInput()) {
			res += (Float) in.getValue();
		}
		res = this.applyFormule(res);
		this.SetValueOfMyOutput(res);
	}

	@Override
	public Float applyFormule(Float res) {
		return res * this.factor;
	}

	@Override
	public SubFunction<Float> createSubFunction(String name, int nbInput) {
		return new SubFunctionFloat(name,this.getMasterFunction(), nbInput, this.getTransform()-1);
	}

	@Override
	public String toString() {
		return super.toString()+"FLOAT";
	}
}
