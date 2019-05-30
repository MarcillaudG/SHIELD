package fr.irit.smac.shield.recovac;

public class TestRecovac {
	
	public static void main(String[] args) {
		int nbDrivingContext =2;
		int nbContextVariables = 22; //22 -> el verdadero valor
		int nbActionStateVariables = 14; //14 -> el verdadero valor

		RecovacGenerator genE = new RecovacGenerator(nbDrivingContext, nbContextVariables, nbActionStateVariables);
		genE.initAllVariables();
		genE.executeGenerator();
		
		//genE.printAllAVariablesFunctions();
		//genE.printAllCVariablesFunctions();
	}
}
