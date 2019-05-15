package fr.irit.smac.shield.recovac;

public class TestRecovac {
	
	public static void main(String[] args) {
		int nbDrivingContext = 3;
		int nbVariables = 6;
		int nbAbnormalDC = 2;

		RecovacGenerator genE = new RecovacGenerator(nbDrivingContext, nbVariables, nbAbnormalDC);
		genE.initAllVariables();
		genE.executeGenerator();
		//genE.printAllVariablesFunction();
		
		//genE.printAllHprimeVariables();
	}
}
