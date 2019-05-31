package fr.irit.smac.shield.recovac;

public class TestRecovac {
	
	public static void main(String[] args) {
		int nbDrivingContext = 50;
		int nbContextVariables = 12; //22 -> el verdadero valor
		int nbActionStateVariables = 6; //14 -> el verdadero valor

		RecovacGenerator genE = new RecovacGenerator(nbDrivingContext, nbContextVariables, nbActionStateVariables);
		genE.initAllVariables();
		genE.executeGenerator();
		
		//genE.printAllAVariablesFunctions();
		//genE.printAllCVariablesFunctions();
	}
}
