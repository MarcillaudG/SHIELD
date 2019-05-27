package fr.irit.smac.shield.cahcoac;

import java.util.Random;

public class V2_Test {
    public static void main(String[] args) {
        //config
        double minBound = -20;
        double maxBound = 100;
        double rangeStat = 20;
        int nbVarConst = 2;
        int nbVarStat = 6;
        int nbVarDyna = 5;
        int nbGeneration = 50;

        GeneratorV2 genV2 = new GeneratorV2(nbVarConst,nbVarStat,nbVarDyna,rangeStat,minBound,maxBound,1000);
        genV2.autoInitAll();
        for (int i = 0; i<nbGeneration;i++) {
            switch (new Random().nextInt(2)){
                case 0: genV2.createNewGenNoise(); break;
                case 1: genV2.createNewGenSHIELD(); break;
                case 2: genV2.createNewGenRandom(); break;
            }
        }
        genV2.printAllGenerationsRaw();
        genV2.printAttentionFunction();
        genV2.displayDataLxPlotVariableByGeneration();
    }

}
