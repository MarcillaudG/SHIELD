package fr.irit.smac.shield.recovac;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import fr.irit.smac.shield.model.FunctionGen;
import fr.irit.smac.shield.model.Generator;
import fr.irit.smac.shield.model.Variable;

public class RecovacGenerator extends Generator {
	protected int numberS;
	protected int numberV;
	protected int numberAbS;
	protected double totalNoise;
	protected List<Variable> variablesInitialized;
	protected List<Double> variablesObserved1;
	protected List<Function> variablesFunction;
	protected List<SituationTuple> situationTuple;
	protected List<VariableTuple> variablesTuple;

	public RecovacGenerator(int _nbSituations, int _nbVariables) {
		this.numberS = _nbSituations;
		this.numberV = _nbVariables;
		this.numberAbS = (int) Math.ceil(_nbSituations * 0.10);
		this.init();
	}
	
	public RecovacGenerator(int _nbSituations, int _nbVariables, int _nbAbnormalS) {
		this.numberS = _nbSituations;
		this.numberV = _nbVariables;
		this.numberAbS = _nbAbnormalS;
		this.init();
	}

	protected void init() {
		this.variables = new TreeMap<String, Variable>(); 
		this.variablesInitialized = new ArrayList<Variable>();
		this.variablesFunction = new ArrayList<Function>();
		this.situationTuple = new ArrayList<SituationTuple>();
		this.h = new ArrayList<Double>();
		this.rand = new Random();
		this.nbVar = 1;

		System.out.println("Success of the initialization Recovac");
	}

	/**
	 * Initialize all the variables
	 */
	public void initAllVariables() {
		for (int i = 0; i < numberV; i++) {
			this.initVariable("Variable" + this.nbVar);
			this.nbVar++;
		}
		this.initAllFunctions();
		
	}

	/**
	 * Generate a new value for the variables
	 */
	public void newValueForVariables() {
		double newValue = 0.0;
		for (String s : this.variables.keySet()) {
			newValue = this.variables.get(s).generateValue();
			this.variables.get(s).setValue(newValue);
			this.variablesTuple.add(new VariableTuple (this.variables.get(s).getName(), this.variables.get(s).getFun(), this.valueWithThreeDecimal(this.variables.get(s).getValue())));
		}
	}

	/**
	 * Create a new variable
	 * @param variable
	 * 		the variable name
	 */
	protected void initVariable(String variable) {
		Variable v = new Variable(variable, MIN_VAR, MAX_VAR);
		this.variablesInitialized.add(v);
	}

	/**
	 * Initialize all the functions
	 */
	public void initAllFunctions() {
		for (int i = 0; i < numberV; i++) {
			this.initFunction(this.variablesInitialized.get(i));
		}
	}

	/**
	 * Construct the corresponding function for the first situation
	 * @param variable
	 * 		the structure of the variable
	 */
	protected void initFunction(Variable variable) {
		List<Variable> variablesPossiblyInfluencing = new ArrayList<Variable>(this.variablesInitialized);

		int nbVarInf = this.rand.nextInt(numberV);
		Deque<String> parameters = new ArrayDeque<String>();

		for (int i = 0; i < nbVarInf && variablesPossiblyInfluencing.size() > 0; i++) {
			int nbR = this.rand.nextInt(variablesPossiblyInfluencing.size());
			String param = variablesPossiblyInfluencing.get(nbR).getName();

			// Variable not influenced by itself
			while (param == variable.getName()) {
				nbR = this.rand.nextInt(variablesPossiblyInfluencing.size());
				param = variablesPossiblyInfluencing.get(nbR).getName();
			}

			parameters.push(param);
			Variable var = variablesPossiblyInfluencing.get(nbR);

			variablesPossiblyInfluencing.remove(var);
		}
		variable.setFun(FunctionGen.generateFunction(parameters.size(), parameters));
		this.variables.put(variable.getName(), variable);
		this.variablesFunction.add(new Function(variable.getName(), variable.getFun()));
	}

	/**
	 * Execute RECOVAC generator
	 */
	public void executeGenerator() {
		int auxS = 0;
		int index = 1;
		int auxIndN = numberS - numberAbS;
		int auxIndA = numberAbS;
		int auxNoS;
		int auxAbS;

		while (auxS < numberS && auxIndN >= 0 && auxIndA >= 0) {
			if (auxIndN <= 0) {
				auxNoS = 0;
			}
			else {
				auxNoS = this.rand.nextInt(auxIndN);
			}
			if (auxIndA <= 0) {
				auxAbS = 0;
			}
			else {
				auxAbS = this.rand.nextInt(auxIndA);
			}
			
			while (auxNoS >= 0 && auxS < numberS && auxIndN > 0) {
				//System.out.println("Driving Context N n°" + index);
				this.executeMethods(true, index);
				auxNoS--;
				auxS++;
				index++;
				auxIndN--;
			}
			
			while (auxAbS >= 0 && auxS < numberS && auxIndA > 0) {
				//System.out.println("Driving Context A n°" + index);
				this.executeMethods(false, index);
				auxAbS--;
				auxS++;
				index++;
				auxIndA--;
			}	
		}
		//this.printSituationTuple();
	}
	
	/**
	 * Execute methods of RECOVAC generator
	 * @param type
	 * 		the type of the situation
	 * @param index
	 * 		the id of the situation
	 */
	public void executeMethods (boolean type, int index) {
		this.variablesTuple = new ArrayList<VariableTuple>();
		this.variablesObserved1 = new ArrayList<Double>();
		FuncEpsilonGen epsilonFunc = new FuncEpsilonGen(numberS, numberV);
		
		//Initial Value
		this.newValueForVariables();
		
		//Predicted Value
		this.generateAllValues();
		
		//Observed Value
		System.out.println("Vari size " + this.variables.size());
		epsilonFunc.generateAllObservedValuesWithFunc1();
		
		this.situationTuple.add(new SituationTuple (index, this.variablesTuple,this.calculateCapacityOfSituation()));
			
		this.generateTuple();
		this.printSituationTuple();
	}
	
	/**
	 * Add the predicted and observed values of the variables in the situation
	 */
	public void generateTuple() {
		for(int s = 0; s < this.variablesTuple.size(); s++) {
			this.variablesTuple.get(s).setValuePredicted(this.valueWithThreeDecimal(this.variables.get(this.variablesTuple.get(s).getName()).getValue()));
			this.variablesTuple.get(s).setValueObserved(this.valueWithThreeDecimal(this.variablesObserved1.get(s)));
		}
	}
	
	/**
	 * Determine the capacity to manage of the situation
	 * @return the capacity or not to manage the situation
	 */
	public boolean calculateCapacityOfSituation(){
		NoiseGenerator n = new NoiseGenerator(numberS, numberV);
		double thresholde = numberV * n.getNoiseValue(); 

		if (totalNoise <= thresholde) {
			return true; //with the ability to manage the situation
		}
		else {
			return false; //no ability to manage the situation
		}

	}
	
	public void printAllVariablesFunction() {
		for (int s = 0; s < variablesFunction.size(); s++) {
			System.out.println(this.variablesFunction.get(s));
		}
	}
	
	public void printSituationTuple() {
		for (int s = 0; s < situationTuple.size(); s++) {
			System.out.println(this.situationTuple.get(s));
		}
	}
	
	public void printHValues() {
		for (int s = 0; s < h.size(); s++) {
			System.out.println(this.h.get(s));
		}
	}
	
	public float valueWithThreeDecimal(double valueD) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("0.000000"); 
		df.setDecimalFormatSymbols(symbols);
		String auxFloat;
		float newValueD;
		
		auxFloat = df.format(valueD);	
		newValueD = Float.parseFloat(auxFloat);
		
		return newValueD;
	}
}
