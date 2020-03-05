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
		switch(this.getFormula()) {
		case INV:
			return 1/res;
		case LOG:
			return (float) Math.log(res);
		case POW:
			return res*res;
		case SQRT:
			return (float) Math.sqrt(res);
		default:
			break;

		}
		return res;
	}

	@Override
	public SubFunction<Float> createSubFunction(String name, int nbInput) {
		return new SubFunctionFloat(name,this.getMasterFunction(), nbInput, this.getTransform()-1);
	}

}
