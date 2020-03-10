package fr.irit.smac.complex;

public class SubFunctionInt extends SubFunction<Integer>{

	public SubFunctionInt(String name, ComposedFunction cf, int nbInput, int transform) {
		super(name, cf, nbInput, transform);

		this.myOutput = new InputInt(this.masterFunction,name+ "Output",transform);
		this.masterFunction.newInput(this.myOutput);
	}


	@Override
	public void compute() {
		int res = 0;
		for(Input in: this.allInput()) {
			if(in instanceof InputFloat) {

				res += Math.round((float) in.getValue());
			}
			else {
				res += (int) in.getValue();
			}
		}
		res = this.applyFormule(res);
		this.SetValueOfMyOutput(res);
	}

	@Override
	public Integer applyFormule(Integer res) {
		return (int) (res * this.factor);
	}



	@Override
	public SubFunction<Integer> createSubFunction(String name, int nbInput) {
		return new SubFunctionInt(name,this.getMasterFunction(), nbInput, this.getTransform()-1);
	}

	@Override
	public String toString() {
		return super.toString()+"INT";
	}
}
