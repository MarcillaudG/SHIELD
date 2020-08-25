package fr.irit.smac.shield.recovac;

public class TestRecovac {
	
	public static void main(String[] args) {
		int nbDrivingContext = 5;
		int nbContextVariables = 4; //22 -> el verdadero valor
		int nbActionStateVariables = 3; //14 -> el verdadero valor

		RecovacGenerator genE = new RecovacGenerator(nbDrivingContext, nbContextVariables, nbActionStateVariables);
		genE.initAllVariables();
		genE.executeGenerator();
		//genE.printActionStateVariables();
		genE.printSituationTuple();

	}
}
