package fr.irit.smac.shield.recovac;

import java.util.ArrayList;
import java.util.List;

import fr.irit.smac.shield.model.Variable;

public class WithRecovacAbnormalDC extends RecovacGenerator{
	private int numberAbS;
	double totalNoise;
	
	public WithRecovacAbnormalDC(int _nbSituations, int _nbVariables) {
		super(_nbSituations, _nbVariables, _nbVariables);
		// TODO Auto-generated constructor stub
	}
	/*public RecovacGenerator(int _nbSituations, int _nbVariables) {
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
	}*/

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
		
		this.printSituationTuple();
	}
	
	public void executeMethods (boolean type, int index) {
		this.variablesEVTuple = new ArrayList<VariableEVTuple>();
		this.variablesEVRealObserved = new ArrayList<Double>();
		this.h = new ArrayList<Double>();
		List<Variable> auxVariable = new ArrayList<>();
		
		for (String s : this.variables.keySet()) {
			auxVariable.add(this.variables.get(s));
		}
		
		FuncEpsilonGen epsilonFunc = new FuncEpsilonGen(numberS, auxVariable, h);
		
		//Initial Value
		this.newValueForVariables();
		
		//Predicted Value
		this.generateAllValues();
		
		//Observed Value
		epsilonFunc.generateAllObservedValuesWithFunc();
		this.variablesEVRealObserved = epsilonFunc.getVariablesObserved();
		
		this.situationTuple.add(new SituationTuple (index, this.variablesEVTuple,this.calculateCapacityOfSituation()));
		
		//this.printHValues();
		this.generateTuple();
	}
	
	/**
	 * Determine the capacity to manage of the situation
	 * @return the capacity or not to manage the situation
	 */
	public boolean calculateCapacityOfSituation(){
		NoiseGenerator n = new NoiseGenerator(numberS, numberCV);
		double thresholde = numberCV * n.noise; 

		if (totalNoise <= thresholde) {
			return true; //with the ability to manage the situation
		}
		else {
			return false; //no ability to manage the situation
		}

	}
}
