package fr.irit.smac.shield.recovac;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import fr.irit.smac.lxplot.LxPlot;
import fr.irit.smac.lxplot.commons.ChartType;
import fr.irit.smac.lxplot.interfaces.ILxPlotChart;
import fr.irit.smac.shield.exceptions.NotEnoughParametersException;
import fr.irit.smac.shield.model.FunctionGen;
import fr.irit.smac.shield.model.Generator;
import fr.irit.smac.shield.model.Variable;

public class RecovacGenerator extends Generator {
	protected int numberS;
	protected int numberCV;
	protected int numberAV;
	protected int nbVarC;
	protected int nbVarA;
	protected List<Float> totalNoiseS1;
	protected List<Float> totalNoiseS2;
	protected List<Variable> variablesAInitialized;
	protected List<Variable> variablesCInitialized;
	protected List<Double> variablesObserved1;
	protected List<Double> variablesObserved2;
	protected List<Function> variablesCFunction;
	protected List<Function> variablesAFunction;
	protected List<SituationTuple> situationTuple;
	protected List<VariableTuple> variablesTuple;

	public RecovacGenerator(int _nbSituations, int _nbContextV, int _nbActionSV) {
		this.numberS = _nbSituations;
		this.numberCV = _nbContextV;
		this.numberAV = _nbActionSV;
		this.init();
	}
	
	protected void init() {
		this.variables = new TreeMap<String, Variable>(); 
		this.variablesActionState = new TreeMap<String,Variable>();
		this.variablesCInitialized = new ArrayList<Variable>();
		this.variablesAInitialized = new ArrayList<Variable>();
		this.variablesCFunction = new ArrayList<Function>();
		this.variablesAFunction = new ArrayList<Function>();
		this.situationTuple = new ArrayList<SituationTuple>();
		this.rand = new Random();
		this.nbVarC = 1;
		this.nbVarA = 1;

		System.out.println("Success of the initialization Recovac");
	}

	/**
	 * Initialize all the variables
	 */
	public void initAllVariables() {
		for (int i = 0; i < numberCV; i++) {
			this.initVariable("Context V" + this.nbVarC);
			this.nbVarC++;
		}
		for (int i = 0; i < numberAV; i++) {
			this.initVariable("Action State V" + this.nbVarA);
			this.nbVarA++;
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
		for (String s : this.variablesActionState.keySet()) {
			newValue = this.variablesActionState.get(s).generateValue();
			this.variablesActionState.get(s).setValue(newValue);
		}
	}

	/**
	 * Create a new variable
	 * @param variable
	 * 		the variable name
	 */
	protected void initVariable(String variable) {
		Variable v = new Variable(variable, MIN_VAR, MAX_VAR);
		if (variable.contains("Context V")) {
			this.variablesCInitialized.add(v);
		} 
		else {
			this.variablesAInitialized.add(v);
		}
	}

	/**
	 * Initialize all the functions
	 */
	public void initAllFunctions() {
		for (int i = 0; i < numberAV; i++) {
			this.defineActionStateFunction(this.variablesAInitialized.get(i));
		}
		
		for (int i = 0; i < numberCV; i++) {
			this.initFunction(this.variablesCInitialized.get(i));
		}
	}

	/**
	 * Construct the corresponding function for the first situation
	 * @param variable
	 * 		the structure of the variable
	 */
	protected void initFunction(Variable variable) {
		List<Variable> variablesPossiblyInfluencing = new ArrayList<Variable>(this.variablesCInitialized);
		int sizeAV = this.variablesAInitialized.size();
		int sizeCV = this.variablesCInitialized.size();
		
		for(int j = 0; j < sizeAV; j++) {
			variablesPossiblyInfluencing.add(this.variablesAInitialized.get(j));
		}

		int nbVarInf = this.rand.nextInt(sizeCV+sizeAV);
		Deque<Variable> parameters = new ArrayDeque<Variable>();

		for (int i = 0; i < nbVarInf && variablesPossiblyInfluencing.size() > 0; i++) {
			int nbR = this.rand.nextInt(variablesPossiblyInfluencing.size());
			Variable param = variablesPossiblyInfluencing.get(nbR);

			// Variable not influenced by itself
			while (param.getName() == variable.getName()) {
				nbR = this.rand.nextInt(variablesPossiblyInfluencing.size());
				param = variablesPossiblyInfluencing.get(nbR);
			}

			parameters.push(param);
			Variable var = variablesPossiblyInfluencing.get(nbR);

			variablesPossiblyInfluencing.remove(var);
		}
		variable.setFun(FunctionGen.generateFunctionWithRange(parameters.size(), parameters,variable.getMin(),variable.getMin()));
		this.variables.put(variable.getName(), variable);
		this.variablesCFunction.add(new Function(variable.getName(), variable.getFun()));
	}
	
	/**
	 * Define action state variable function
	 * @param variable
	 * 		the action state variable
	 */
	public void defineActionStateFunction(Variable variable) {
		List<Variable> variablesPossiblyInfluencing = new ArrayList<Variable>(this.variablesCInitialized);

		int nbVarInf = this.rand.nextInt(numberCV);
		Deque<Variable> parameters = new ArrayDeque<Variable>();

		for (int i = 0; i < nbVarInf && variablesPossiblyInfluencing.size() > 0; i++) {
			int nbR = this.rand.nextInt(variablesPossiblyInfluencing.size());
			Variable param = variablesPossiblyInfluencing.get(nbR);

			parameters.push(param);
			Variable var = variablesPossiblyInfluencing.get(nbR);

			variablesPossiblyInfluencing.remove(var);
		}
		variable.setFun(FunctionGen.generateFunctionWithRange(parameters.size(), parameters,variable.getMin(),variable.getMin()));
		this.variablesActionState.put(variable.getName(), variable);
		this.variablesAFunction.add(new Function(variable.getName(), variable.getFun()));
	}
	
	/**
	 * For all action state variable, generate the new value
	 */
	public void generateAllActionStateValues(){
		for(String s : this.variablesActionState.keySet()) {
			this.calculateActionStateValue(s);
		}
	}
	
	/**
	 * Calculate the new action state variable value
	 * @param varName
	 * 		the name of the action state variable
	 */
	public void calculateActionStateValue(String varName) {
		Variable var = this.variablesActionState.get(varName);
		FunctionGen func = var.getFun();
		Deque<Double> xi = new ArrayDeque<Double>();
		double res = 0.0;
		
		Deque<String> paramTmp = new ArrayDeque<String>(var.getFun().getVariables());

		while(!paramTmp.isEmpty()) {
			xi.offer(this.variables.get(paramTmp.poll()).getValue());
		}

		try {		
			res = func.compute(xi, var);
		} catch (NotEnoughParametersException e) {
			e.printStackTrace();
		}
		
		this.variablesActionState.get(var.getName()).setValue(res);
	}
	
	/**
	 * Execute RECOVAC generator
	 */
	public void executeGenerator() {
		this.totalNoiseS1 = new ArrayList<Float>();
		this.totalNoiseS2 = new ArrayList<Float>();
		int auxS = 0;
		float thresholde1;
		float thresholde2;
		boolean auxcap1;
		boolean auxcap2;
		
		while (auxS < numberS) {
			//System.out.println("Driving Context n°" + (auxS+1));
			this.executeMethods(auxS);
			auxS++;
		}
		
		thresholde1	 = this.valueWithThreeDecimal(this.calculateThresholdeForCapacity(this.totalNoiseS1));
		thresholde2	 = this.valueWithThreeDecimal(this.calculateThresholdeForCapacity(this.totalNoiseS2));
		
		for(int i = 0; i < this.situationTuple.size(); i++) {
			auxcap1 = this.calculateCapacityOfSituation(thresholde1, this.valueWithThreeDecimal(totalNoiseS1.get(i)));
			auxcap2 = this.calculateCapacityOfSituation(thresholde2, this.valueWithThreeDecimal(totalNoiseS2.get(i)));
			
			this.situationTuple.get(i).setCapacity1(auxcap1);
			this.situationTuple.get(i).setCapacity2(auxcap2);
		}
		
		System.out.println(" \nThresholde 1= " + thresholde1 + " Thresholde 2= " + thresholde2 + "\n");
		this.printSituationTuple();
		
		this.displayDeviationWithThresholde(1, thresholde1, this.totalNoiseS1);
		this.displayDeviationWithThresholde(2,thresholde2, this.totalNoiseS2);
			
	}
	
	/**
	 * Execute methods of RECOVAC generator
	 * @param type
	 * 		the type of the situation
	 * @param index
	 * 		the id of the situation
	 */
	public void executeMethods (int index) {
		this.variablesTuple = new ArrayList<VariableTuple>();
		this.variablesObserved1 = new ArrayList<Double>();
		this.variablesObserved2 = new ArrayList<Double>();
		this.h = new ArrayList<Double>();
		double minOb1;
		double minOb2;
		double maxOb1;
		double maxOb2;
		List<Variable> auxVariable = new ArrayList<Variable>();
		
		//Initial Value
		this.newValueForVariables();
		
		//Generate Action State Variables
		this.generateAllActionStateValues();
		this.normalizeAllVariables(this.variablesActionState);
		//this.printActionStateVariables();
		
		//Predicted Value
		this.generateAllValues();
		this.normalizeAllVariables(this.variables);
		//this.printAllVariables();
		
		for (String s : this.variables.keySet()) {
			auxVariable.add(this.variables.get(s));
		}
		
		FuncEpsilonGen epsilonFunc = new FuncEpsilonGen(numberCV, auxVariable, h);
		
		//Observed Value
		epsilonFunc.generateAllObservedValuesWithFunc();
		this.variablesObserved1 = epsilonFunc.getVariablesObserved1();
		this.variablesObserved2 = epsilonFunc.getVariablesObserved2();
		maxOb1 = epsilonFunc.getMaxValueList(variablesObserved1);
		maxOb2 = epsilonFunc.getMaxValueList(variablesObserved2);
		minOb1 = epsilonFunc.getMinValueList(variablesObserved1); 
		minOb2 = epsilonFunc.getMinValueList(variablesObserved2); 
		
		for(int i = 0; i < this.variablesObserved1.size(); i++) {
			double aux1 = this.normalize(this.variablesObserved1.get(i), maxOb1, minOb1, MIN_VAR, MAX_VAR);
			this.variablesObserved1.set(i, aux1);
		}
		
		for(int i = 0; i < this.variablesObserved2.size(); i++) {
			double aux2 = this.normalize(this.variablesObserved2.get(i), maxOb2, minOb2, MIN_VAR, MAX_VAR);
			this.variablesObserved2.set(i, aux2);
		}
		
		//this.printHValues();
		this.generateTuple();
			
		this.situationTuple.add(new SituationTuple ((index+1), this.variablesTuple, this.totalNoiseS1.get(index), this.totalNoiseS2.get(index)));
		
		//this.displayAllVariablesEvolution(index);
		//this.displayDeviationEvolution(index);
	}
	
	/**
	 * Add the predicted and observed values of the variables in the situation
	 */
	public void generateTuple() {
		double auxdeviation1 = 0.0;
		double auxdeviation2 = 0.0;
		double totalDeviation1 = 0.0;
		double totalDeviation2 = 0.0;
		
		for(int s = 0; s < this.variablesTuple.size(); s++) {
			this.variablesTuple.get(s).setValuePredicted(this.valueWithThreeDecimal(this.variables.get(this.variablesTuple.get(s).getName()).getValue()));
			this.variablesTuple.get(s).setValueObserved1(this.valueWithThreeDecimal(this.variablesObserved1.get(s)));
			this.variablesTuple.get(s).setValueObserved2(this.valueWithThreeDecimal(this.variablesObserved2.get(s)));
			
			auxdeviation1 = this.deviationValue(this.variablesTuple.get(s).getValuePredicted(), this.variablesTuple.get(s).getValueObserved1());
			auxdeviation2 = this.deviationValue(this.variablesTuple.get(s).getValuePredicted(), this.variablesTuple.get(s).getValueObserved2());
			totalDeviation1 += auxdeviation1;
			totalDeviation2 += auxdeviation2;
			
			this.variablesTuple.get(s).setDeviation1(this.valueWithThreeDecimal(auxdeviation1));
			this.variablesTuple.get(s).setDeviation2(this.valueWithThreeDecimal(auxdeviation2));
		}
		this.totalNoiseS1.add(this.valueWithThreeDecimal(totalDeviation1));
		this.totalNoiseS2.add(this.valueWithThreeDecimal(totalDeviation2));
	}
	
	public double deviationValue(double valuePredicted, double valueObserved) {
		return Math.abs(valuePredicted - valueObserved);
	}
	
	/**
	 * Define the threshold at which the capacity of the situations will be calculated
	 * @param totalNoise
	 * 		list of the total noise added to the variables that compose each of the situations
	 * @return the threshold for the scenario
	 */
	public float calculateThresholdeForCapacity(List<Float> totalNoiseS){
		float threshold = (float) 0.0;
		float auxThreshold = (float) 0.0;
		
		for (int i = 0; i < totalNoiseS.size(); i++) {
			auxThreshold += totalNoiseS.get(i); 
		}
		threshold = auxThreshold / totalNoiseS.size();
		
		return threshold;
	}
	
	/**
	 * Determine the capacity to manage of the situation
	 * @param thresolde
	 * 		the threshold calculated for the scenario
	 * @param valueNoise
	 * 		the total noise added to the variables that compose a specific situations
	 * @return the capacity or not to manage the situation
	 */
	public boolean calculateCapacityOfSituation(float thresholde, float valueNoise) {
		if (valueNoise <= thresholde) {
			return true; //with the ability to manage the situation
		}
		else {
			return false; //no ability to manage the situation
		}
	}
	
	/**
	 * Limit the value to 3 decimals
	 * @param valueD
	 * 		value of a variable of double type
	 * @return
	 */
	public float valueWithThreeDecimal(double valueD) {
		String auxFloat;
		float newValueD = 0;
		
		try {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("0.0000"); 
			df.setDecimalFormatSymbols(symbols);
			
			auxFloat = df.format(valueD);	
			newValueD = Float.parseFloat(auxFloat);
		}
		catch (NumberFormatException e) {
			 newValueD = 0;
		}
		
		return newValueD;
	}
	
	/**
	 * Function that allows normalizing the value within an expected range
	 * @param value
	 * @param max
	 * @param min
	 * @param a
	 * @param b
	 * @return
	 */
	public double normalize(double value, double max, double min, double a, double b) {
		double valueN;
		double auxN1;
		double auxN2;
		
		auxN1 = (value - min)/ (max - min);
		auxN2 = auxN1 * (b-a);
		valueN = auxN2 + a;

		if ( Double.isNaN(valueN))
			valueN = 0.0;
		
		return valueN;
	}
	
	
	public void normalizeAllVariables(Map<String,Variable> variablesN) {
		double max = 0;
		double min;
		double value;
		double a;
		double b;
		
		//Obtain max of the values
		for(String s : variablesN.keySet()) {
			if (variablesN.get(s).getValue() > max) {
				max = variablesN.get(s).getValue();
			}
		}
		
		//Obtain min of the values
		min = max;
		for(String s : variablesN.keySet()) {
			if (variablesN.get(s).getValue() < min) {
				min = variablesN.get(s).getValue();
			}
		}
		
		for(String s : variablesN.keySet()) {
			value = variablesN.get(s).getValue();
			a = variablesN.get(s).getMin();
			b = variablesN.get(s).getMax();
			
			variablesN.get(s).setValue(this.normalize(value, max, min, a, b));
		}
	}
	
	public void printAllCVariablesFunctions() {
		for (int s = 0; s < variablesCFunction.size(); s++) {
			System.out.println(this.variablesCFunction.get(s));
		}
	}
	
	public void printAllAVariablesFunctions() {
		for (int s = 0; s < variablesAFunction.size(); s++) {
			System.out.println(this.variablesAFunction.get(s));
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
	
	public void printActionStateVariables() {
		for(String s : this.variablesActionState.keySet()) {
			System.out.println(this.variablesActionState.get(s));
		}
	}
	
	public void displayAllVariablesEvolution(int _index) {
		ILxPlotChart auxChart;
		for(int i = 0; i < this.variablesTuple.size(); i++) {
			auxChart = LxPlot.getChart(this.variablesTuple.get(i).getName());
			auxChart.add("Predicted value", _index, this.variablesTuple.get(i).getValuePredicted());
			auxChart.add("Observed value 1", _index, this.variablesTuple.get(i).getValueObserved1());
			auxChart.add("Observed value 2", _index, this.variablesTuple.get(i).getValueObserved2());
		}
	}
	
	public void displayDeviationEvolution (int _index) {
		ILxPlotChart auxChart;
		for(int i = 0; i < this.variablesTuple.size(); i++) {
			auxChart = LxPlot.getChart( this.variablesTuple.get(i).getName() + "deviation");
			auxChart.add("Deviation 1", _index, this.variablesTuple.get(i).getDeviation1());
			auxChart.add("Deviation 2", _index, this.variablesTuple.get(i).getDeviation2());
		}
	}
	
	public void displayDeviationWithThresholde (int index, float _thresholde, List<Float> totalNoise) {
		ILxPlotChart auxChart;
		for(int i = 1; i <= numberS; i++) {
			auxChart = LxPlot.getChart( "Abnormality detection" + index);
			auxChart.add("Thresholde ", i,_thresholde);
			auxChart.add("Deviation by situation", i, totalNoise.get(i-1));
		}
	}
}
