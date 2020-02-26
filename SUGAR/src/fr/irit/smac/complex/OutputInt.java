package fr.irit.smac.complex;

public class OutputInt extends Output<Integer> {

	public OutputInt(Integer value, int transform, String name) {
		super(value, transform, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Integer compute() {
		return this.getValue();
	}

}
