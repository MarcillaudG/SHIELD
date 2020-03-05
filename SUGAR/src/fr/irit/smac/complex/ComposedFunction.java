package fr.irit.smac.complex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import fr.irit.smac.shield.model.Generator;

public class ComposedFunction {


	private String name;

	private Input<?> inputs[];

	private Output<?> outputs[];

	private List<SubFunction> subFunctions;

	private List<Input<?>> allInputs;

	private List<Output<?>> allOutputs;

	private Generator gen;

	private int nbCycle;

	private int complexity;



	private int seuilFunction;

	private int missingFunction[];

	private int seuilCrit;

	private boolean satisfied;

	public ComposedFunction(String name,int nbInput, int nbOutput, int complexity, int seuilFunction, Generator gen) {
		this.name = name;
		this.inputs = new Input[nbInput];
		this.outputs = new Output[nbOutput];
		this.gen = gen;
		this.complexity = complexity;
		this.seuilFunction = seuilFunction;
		this.nbCycle = 0;
		this.missingFunction = new int[complexity];
		this.seuilCrit = 5;
		init();
	}


	public ComposedFunction(String name,int nbInput, int nbOutput, int complexity, int seuilFunction) {
		this.name = name;
		this.inputs = new Input[nbInput];
		this.outputs = new Output[nbOutput];
		this.seuilFunction = seuilFunction;
		this.complexity = complexity;
		this.nbCycle = 0;
		this.missingFunction = new int[complexity];
		this.seuilCrit = 5;
		//init();
		init2output();
	}


	private void init() {
		this.subFunctions = new ArrayList<SubFunction>();
		this.allInputs = new ArrayList<>();
		this.allOutputs = new ArrayList<>();
		this.satisfied = false;
		Random rand = new Random();
		for(int i = 0; i < this.complexity;i++) {
			//this.subFunctions.add(new SubFunction("SubFunction "+i, this, 1+rand.nextInt(this.inputs.length)));
		}

		for(int i =0; i < this.inputs.length;i++) {
			Input<?> in = new InputFloat(this, "InputInit"+i,0);
			this.inputs[i] = in;
			this.allInputs.add(in);
		}

		for(int i =0; i < this.outputs.length;i++) {
			Output<?> out = new OutputFloat(0.0f, this.complexity, "Output"+i,this);
			this.outputs[i] = out;
			this.allOutputs.add(out);
		}
		
		initSubFunction();
	}
	
	private void init2output() {
		this.subFunctions = new ArrayList<SubFunction>();
		this.allInputs = new ArrayList<>();
		this.allOutputs = new ArrayList<>();
		this.satisfied = false;
		Random rand = new Random();
		for(int i = 0; i < this.complexity;i++) {
			//this.subFunctions.add(new SubFunction("SubFunction "+i, this, 1+rand.nextInt(this.inputs.length)));
		}

		int j = 0;
		for(j =0; j < this.inputs.length/2;j++) {
			InputFloat in = new InputFloat(this, "InputInit"+j,0);
			this.inputs[j] = in;
			this.allInputs.add(in);
		}
		

		for(; j < this.inputs.length;j++) {
			InputInt in = new InputInt(this, "InputInit"+j,0);
			this.inputs[j] = in;
			this.allInputs.add(in);
		}


		Output<?> out = new OutputInt(0, this.complexity, "OutputInt",this);
		this.outputs[0] = out;
		this.allOutputs.add(out);
		

		Output<?> out2 = new OutputFloat(0.0f, this.complexity, "OutputFloat",this);
		this.outputs[1] = out2;
		this.allOutputs.add(out2);
		
		initSubFunction();
	}

	private void initSubFunction() {
		int i =0;
		for(Output<?> out: this.allOutputs) {
			
			this.subFunctions.add(out.createSubFunction("SubFunction OUPUT"+i,this.seuilFunction));
			i++;
		}
		for(int level = this.complexity-1; level > 0;level--) {
			int k=0;
			for(SubFunction subf: this.getSubFunctionWithTransform(level+1)) {
				for(int j =0; j < this.seuilFunction;j++) {
					this.subFunctions.add(subf.createSubFunction("SubFunction"+level+"nb"+j+"K:"+k, this.seuilFunction));
					//this.subFunctions.add(new SubFunction("SubFunction"+level+"nb"+j+"K:"+k, this, this.seuilFunction, level));
				}
				k++;
			}
		}
		System.out.println("INIT");
		System.out.println(this);
		System.out.println("ENDS");
	}


	private List<SubFunction> getSubFunctionWithTransform(int level) {
		List<SubFunction> res = new ArrayList<>();
		for(SubFunction subf: this.subFunctions) {
			if(subf.getTransform() == level) {
				res.add(subf);
			}
		}
		return res;
	}


	public void cycleBinding() {
		System.out.println("CYCLE: "+this.nbCycle);
		for(int i = 0; i < this.complexity; i++) {
			this.missingFunction[i] = 0;
		}


		Collections.shuffle(this.allInputs);
		for(Input in : this.allInputs) {
			in.perceiveFunction();
			in.decideAndActFunction();
		}

		for(int i = 0; i < this.complexity; i++) {
			System.out.println(this.missingFunction[i]);
		}
		for(int i = 0; i < this.complexity; i++) {
			if(this.missingFunction[i] > 0) {
				int reste = this.missingFunction[i]%this.seuilFunction;
				for(int j =0; j < this.missingFunction[i]/this.seuilFunction;j++) {
					//TODO
					//this.subFunctions.add(new SubFunction("SubFunction t:"+i+":"+j+"c:"+this.nbCycle, this, this.seuilFunction, i+1));
				}
				this.missingFunction[i] = reste;
			}
		}

		for(int i = 0; i < this.complexity; i ++) {
			if(this.missingFunction[i] > 0) {
				List<SubFunction> tmp = new ArrayList<>();
				for(SubFunction subf: this.subFunctions) {
					if(subf.getTransform() == i+1) {
						tmp.add(subf);
					}
				}
				while(this.missingFunction[i] > 0) {
					Collections.shuffle(tmp);
					SubFunction target = null;
					for(SubFunction subf : tmp) {
						if(target == null && subf.getCrit() < this.seuilCrit || target.getCrit() > subf.getCrit()) {
							target = subf;
						}
					}
					if(target != null) {
						target.addInput();
						this.missingFunction[i]--;
					}
					else {
						//TODO
						//this.subFunctions.add(new SubFunction("SubFunction t: EXTRA c:"+this.nbCycle, this, this.seuilFunction, i+1));
						this.missingFunction[i] = 0;
					}
				}
			}
		}

		System.out.println(this);

		int nbSatisfied = 0;
		for(Input in : this.allInputs) {
			if(in.satisfied()) {
				nbSatisfied++;
			}
			else {
				System.out.println("NONSAT: "+in);
			}
		}
		System.out.println("NBSUBF: "+this.subFunctions.size());
		
		
		System.out.println("NBIN: "+this.allInputs.size());
		System.out.println("NBSATIS: "+nbSatisfied);
		this.satisfied = true;
		for(Input in : this.allInputs) {
			if(!in.satisfied()) {
				this.satisfied=false;
				System.out.println("ICI");
			}
		}
		for(Output out: this.allOutputs) {
			if(!out.satisfied()) {
				this.satisfied = false;
				System.out.println("LA");
			}
		}
		for(SubFunction subf: this.subFunctions) {
			if(!subf.isSatisfied()) {
				this.satisfied = false;
				//System.out.println(subf);
			}
		}
		
		this.nbCycle++;
	}

	public void newInput(Input<?> in) {
		this.allInputs.add(in);
	}

	public void newOutput(Output<?> out) {
		this.allOutputs.add(out);
	}

	public Set<SubFunction> getSubFunctionNonSatisfied(int transform, Set<SubFunction> binded) {
		Set<SubFunction> res = new HashSet<SubFunction>();
		for(int i =0; i < this.subFunctions.size();i++) {
			if(this.subFunctions.get(i).getTransform() == transform+1 && !this.subFunctions.get(i).isSatisfied() && !binded.contains(this.subFunctions.get(i))) {
				res.add(this.subFunctions.get(i));
			}
		}
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComposedFunction other = (ComposedFunction) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Collection<? extends Input> getOtherInput(String name2, int transform, Class<? extends Input> cls) {
		Set<Input> res = new HashSet<Input>();
		for(Input in : this.allInputs) {
			if(!in.getName().equals(name2) && in.getTransform() == transform && in.getClass().equals(cls)) {
				res.add(in);
			}
		}
		return res;
	}

	public void MissingFunction(int transform) {
		this.missingFunction[transform] += 1;

	}

	public String toString() {
		String res = this.name;
		List<SubFunction> tmplist = new ArrayList<SubFunction>(this.subFunctions);
		int level = 1;
		while(!tmplist.isEmpty()) {
			List<SubFunction> toRemove = new ArrayList<>();
			res += "LEVEL "+level +"\n";
			for(int i = 0; i < tmplist.size();i++) {
				if(tmplist.get(i).getTransform() == level) {
					res += tmplist.get(i).toString() +"\n";
					toRemove.add(tmplist.get(i));
				}
			}
			tmplist.removeAll(toRemove);
			level++;
		}
		res+="OUTPUT\n";
		for(Output out: this.allOutputs) {
			res+=out+"\n";
		}
		return res;		
	}

	public int getComplexity() {
		return this.complexity;
	}


	public Collection<? extends Output> getOutputs(int transform) {
		Set<Output> res = new HashSet<Output>();
		for(Output out : this.allOutputs) {
			if(out.getTransform() == transform) {
				res.add(out);
			}
		}
		return res;
	}



	private boolean isSatisfied() {
		return this.satisfied;
	}


	public void compute() {
		List<SubFunction> tmplist = new ArrayList<SubFunction>(this.subFunctions);
		int level = 1;
		while(!tmplist.isEmpty()) {
			List<SubFunction> toRemove = new ArrayList<>();
			for(int i = 0; i < tmplist.size();i++) {
				if(tmplist.get(i).getTransform() == level) {
					tmplist.get(i).compute();
					toRemove.add(tmplist.get(i));
				}
			}
			tmplist.removeAll(toRemove);
			level++;
		}
		for(Output out:this.allOutputs) {
			out.perceiveValue();
		}
	}


	private List<Input> getinitInput() {
		List<Input> res = new ArrayList<>();
		for(Input in: this.allInputs) {
			if(in.getTransform()==0) {
				res.add(in);
			}
		}
		return res;
	}
	
	public Output<?> getOutput(int number){
		if(number > this.allOutputs.size()) {
			return null;
		}
		return this.allOutputs.get(number);
	}


	/**
	 * TODO reunir en une
	 * @param transform
	 * @param binded
	 * @return
	 */
	public Set<SubFunction<Float>> getSubFunctionNonSatisfiedFloat(int transform, Set<SubFunction<Float>> binded) {
		Set<SubFunction<Float>> res = new HashSet<SubFunction<Float>>();
		for(int i =0; i < this.subFunctions.size();i++) {
			if(this.subFunctions.get(i) instanceof SubFunctionFloat && this.subFunctions.get(i).getTransform() == transform+1 && !this.subFunctions.get(i).isSatisfied() && !binded.contains(this.subFunctions.get(i))) {
				res.add(this.subFunctions.get(i));
			}
		}
		return res;
	}
	
	/**
	 * TODO reunir en une
	 * @param transform
	 * @param binded
	 * @return
	 */
	public Set<SubFunction<Integer>> getSubFunctionNonSatisfiedInt(int transform, Set<SubFunction<Integer>> binded) {
		Set<SubFunction<Integer>> res = new HashSet<SubFunction<Integer>>();
		for(int i =0; i < this.subFunctions.size();i++) {
			if(this.subFunctions.get(i) instanceof SubFunctionInt && this.subFunctions.get(i).getTransform() == transform+1 && !this.subFunctions.get(i).isSatisfied() && !binded.contains(this.subFunctions.get(i))) {
				res.add(this.subFunctions.get(i));
			}
		}
		return res;
	}
	
	


	public static void main(String args[]) {
		ComposedFunction cf = new ComposedFunction("CF1", 13, 2, 3, 3);
		while(!cf.isSatisfied()) {
			cf.cycleBinding();
		}
		
		for(Input in: cf.getinitInput()) {
			if(in instanceof InputFloat) {
				in.setValue(10.0f);
			}
			else {
				in.setValue(10);
			}
		}
		cf.compute();
		
		System.out.println(cf.getOutput(0).getValue());
		System.out.println(cf.getOutput(1).getValue());
	}


}
