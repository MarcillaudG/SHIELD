package fr.irit.smac.shield.recovac;

public class TestRecovac {
	
	public static void main(String[] args) {
		int nbDrivingContext = 10;
		int nbContextVariables = 16; //22 -> el verdadero valor
		int nbActionStateVariables = 11; //14 -> el verdadero valor

		RecovacGenerator genE = new RecovacGenerator(nbDrivingContext, nbContextVariables, nbActionStateVariables);
		genE.initAllVariables();
		genE.executeGenerator();
		//genE.printActionStateVariables();
		genE.printSituationTuple();

	}
}
