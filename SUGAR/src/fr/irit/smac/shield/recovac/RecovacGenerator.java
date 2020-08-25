package fr.irit.smac.shield.recovac;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import fr.irit.smac.lxplot.LxPlot;
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
	protected List<Float> totalNoiseS;
	protected List<Variable> variablesAInitialized;
	protected List<Variable> variablesCInitialized;
	protected List<Double> variablesEVRealObserved;
	protected List<Function> variablesCFunction;
	protected List<Function> variablesAFunction;
	protected List<SituationTuple> situationTuple;
	protected List<VariableEVTuple> variablesEVTuple;
	protected List<Oracle> situationsSMA;

	public RecovacGenerator(int nbSituations, int nbContextV, int nbActionSV) {
		this.numberS = nbSituations;
		this.numberCV = nbContextV;
		this.numberAV = nbActionSV;
		this.init();
	}
	
	protected void init() {
		this.variables = new TreeMap<String, Variable>(); 
		this.variablesActionState = new TreeMap<String,Variable>();
		this.variablesCInitialized = new ArrayList<>();
		this.variablesAInitialized = new ArrayList<>();
		this.variablesCFunction = new ArrayList<>();
		this.variablesAFunction = new ArrayList<>();
		this.situationTuple = new ArrayList<>();
		this.situationsSMA = new ArrayList<>();
		this.rand = new Random();
		this.nbVarC = 1;
		this.nbVarA = 1;

		//System.out.println("Success initialization of RECOVAC");
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
			this.variablesEVTuple.add(new VariableEVTuple (this.variables.get(s).getName(), this.variables.get(s).getFun(), this.valueWithThreeDecimal(this.variables.get(s).getValue())));
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
		variable.setFun(FunctionGen.generateFunctionWithRange(parameters.size(), parameters));
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
		variable.setFun(FunctionGen.generateFunctionWithRange(parameters.size(), parameters));
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
		this.totalNoiseS = new ArrayList<Float>();
		int auxS = 0;
		float thresholde = 0;
		boolean auxcap;
		
		while (auxS < numberS) {
			this.executeMethods(auxS);
			auxS++;
		}
		
		//thresholde = this.valueWithThreeDecimal(this.calculateThresholdeForCapacity(this.totalNoiseS));
		thresholde = this.valueWithThreeDecimal(this.calculateThresholdeForAbnormalityDetection(totalNoiseS));
		
		for(int i = 0; i < this.situationTuple.size(); i++) {
			auxcap = this.calculateAbnormalityDetectionOfSituation(thresholde, this.valueWithThreeDecimal(totalNoiseS.get(i)));
			
			this.situationTuple.get(i).setCapacity(auxcap);
			this.situationsSMA.get(i).setCapacity(auxcap);
		}
		
		//System.out.println(" \nThresholde = " + thresholde + "\n");
		//this.printSituationTuple();
		
		//this.displayDeviationWithThresholde(1, thresholde, this.totalNoiseS);
			
	}
	
	/**
	 * Execute methods of RECOVAC generator
	 * @param type
	 * 		the type of the situation
	 * @param index
	 * 		the id of the situation
	 */
	public void executeMethods (int index) {
		this.variablesEVTuple = new ArrayList<VariableEVTuple>();
		this.variablesEVRealObserved = new ArrayList<Double>();
		this.h = new ArrayList<Double>();
		double minOb;
		double maxOb;
		
		List<Variable> auxVariable = new ArrayList<Variable>();
		
		//Initial Value
		this.newValueForVariables();
		
		//Generate Action State Variables
		this.generateAllActionStateValues();
		this.normalizeAllVariables(this.variablesActionState);
		//System.out.println("\nActions state in S" + (index+1) + ":");
		//this.printActionStateVariables();
		//this.printAllAVariablesFunctions();
		
		//Predicted Value
		this.generateAllValues();
		this.normalizeAllVariables(this.variables);
		//this.printAllVariables();
		//this.printAllCVariablesFunctions();
		
		for (String s : this.variables.keySet()) {
			auxVariable.add(this.variables.get(s));
		}
		
		FuncEpsilonGen epsilonFunc = new FuncEpsilonGen(numberCV, auxVariable, h);
		
		//Observed Value
		epsilonFunc.generateAllObservedValuesWithFunc();
		this.variablesEVRealObserved = epsilonFunc.getVariablesObserved();
		maxOb = epsilonFunc.getMaxValueList(variablesEVRealObserved);
		minOb = epsilonFunc.getMinValueList(variablesEVRealObserved); 
		
		for(int i = 0; i < this.variablesEVRealObserved.size(); i++) {
			//double aux = this.variablesObserved.get(i);
			double aux = this.normalize(this.variablesEVRealObserved.get(i), maxOb, minOb, MIN_VAR, MAX_VAR);

			this.variablesEVRealObserved.set(i, aux);
		}
	
		//this.printHValues();
		this.generateTuple();
			
		this.situationTuple.add(new SituationTuple ((index+1), this.variablesEVTuple, this.totalNoiseS.get(index)));
		
		//Oracle for SMA LDiR
		List<Variable> expectedValueEV = new ArrayList<Variable>();
		for (int j = 0; j < variablesEVTuple.size(); j++) {
			expectedValueEV.add(new Variable( this.variablesEVTuple.get(j).getName(),(double) this.variablesEVTuple.get(j).getRealValue()));
		}
		
		List<Variable> currentValueEV = new ArrayList<Variable>();
		for (int j = 0; j < variablesEVTuple.size(); j++) {
			currentValueEV.add(new Variable( this.variablesEVTuple.get(j).getName(),(double) this.variablesEVTuple.get(j).getCurrentValue()));
		} 
		
		List<Variable> intentionalValueEV = new ArrayList<Variable>();
		for (int j = 0; j < variablesEVTuple.size(); j++) {
			intentionalValueEV.add(new Variable( this.variablesEVTuple.get(j).getName(),(double) this.variablesEVTuple.get(j).getIntentionalValue()));
		}
		
		this.situationsSMA.add(new Oracle (this.variablesActionState, currentValueEV, expectedValueEV, intentionalValueEV));
		//printSituationsSMA();
		
		//this.displayAllVariablesEvolution(index);
		//this.displayDeviationEvolution(index);
	}
	
	/**
	 * Add the predicted and observed values of the variables in the situation
	 */
	public void generateTuple() {
		double auxdeviation = 0.0;
		double totalDeviation = 0.0;
		
		for(int s = 0; s < this.variablesEVTuple.size(); s++) {
			this.variablesEVTuple.get(s).setIntentionalValue(this.valueWithThreeDecimal(this.variables.get(this.variablesEVTuple.get(s).getName()).getValue()));
			this.variablesEVTuple.get(s).setRealValue(this.valueWithThreeDecimal(this.variablesEVRealObserved.get(s)));
			
			auxdeviation = this.deviationValue(this.variablesEVTuple.get(s).getIntentionalValue(), this.variablesEVTuple.get(s).getRealValue());
			totalDeviation += auxdeviation;
			
			this.variablesEVTuple.get(s).setDeviation(this.valueWithThreeDecimal(auxdeviation));
		}
		this.totalNoiseS.add(this.valueWithThreeDecimal(totalDeviation));
	}
	
	public double deviationValue(double valuePredicted, double valueObserved) {
		return Math.abs(valuePredicted - valueObserved);
	}
	
	/**
	 * Define the threshold at which the abnormality of the situations will be calculated
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
	
	public double calculateThresholdeForAbnormalityDetection(List<Float> totalNoiseS) {
		double threshold = 0;
		double auxThre1 = 0;
		double auxThre2 = 0;
		double q3N = 0;
		int iPart = 0;
		double fPart = 0;
		List<Float> auxNoise = new ArrayList<Float>(totalNoiseS);

		Collections.sort(auxNoise);
		
		//Calculating the third quartile
		q3N = 0.75*(numberS+1);
		iPart = (int) q3N; 
		fPart = q3N - iPart;

		if(totalNoiseS.size() == iPart) {
			threshold = auxNoise.get(iPart-1);
		}
		else {
			auxThre1 = auxNoise.get(iPart) - auxNoise.get(iPart-1);
			auxThre2 = fPart*auxThre1;
			threshold = auxNoise.get(iPart-1) + auxThre2;
		}
		
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
	public boolean calculateAbnormalityDetectionOfSituation(float thresholde, float valueNoise) {
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
			//variablesN.get(s).setValue(this.normalize(value, a, b, max, min));
		}
	}
	
	public List<Oracle> getSituationsSMA() {
		return situationsSMA;
	}

	public void printSituationsSMA() {
		for (int s = 0; s < situationsSMA.size(); s++) {
			System.out.println("\nSituation " +  (s+1) + ":\n"+this.situationsSMA.get(s));
		}	}
	
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
	
	public  Map<String, Variable> getActionStateVariables() {
		return this.variablesActionState;
	}
	
	public void displayAllVariablesEvolution(int _index) {
		ILxPlotChart auxChart;
		for(int i = 0; i < this.variablesEVTuple.size(); i++) {
			auxChart = LxPlot.getChart(this.variablesEVTuple.get(i).getName());
			auxChart.add("Intention value", _index, this.variablesEVTuple.get(i).getIntentionalValue());
			auxChart.add("Observed value ", _index, this.variablesEVTuple.get(i).getRealValue());
		}
	}
	
	public void displayDeviationEvolution (int _index) {
		ILxPlotChart auxChart;
		for(int i = 0; i < this.variablesEVTuple.size(); i++) {
			auxChart = LxPlot.getChart( this.variablesEVTuple.get(i).getName() + "deviation");
			auxChart.add("Deviation ", _index, this.variablesEVTuple.get(i).getDeviation());
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
