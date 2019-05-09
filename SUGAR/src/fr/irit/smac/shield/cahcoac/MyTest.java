package fr.irit.smac.shield.cahcoac;

import fr.irit.smac.shield.cahcoac.Function.NormalisedWeightedSum;
import fr.irit.smac.shield.cahcoac.Function.OutputFunction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MyTest {
    public static void main(String[] args) {
        //##############################################################################################################
        // preset d'indicateurs pour exemple (nom,borneMax)
        Map<String,Double> indicateursPreset = new HashMap<>();
        indicateursPreset.put("gazeMirrorPercent",100.0);
        indicateursPreset.put("gazeRoadPercent",100.0);
        indicateursPreset.put("speedVariance",1.0);
        indicateursPreset.put("meanSpeed",130.0);
        indicateursPreset.put("lateralControlMean",100.0);
        indicateursPreset.put("longitudinalControlMean",100.0);
        indicateursPreset.put("heartRateBPM",160.0);
        indicateursPreset.put("steeringDevianceDeg",30.0);
        indicateursPreset.put("isAsleep",1.0);
        indicateursPreset.put("danseLaSambaPerMille",100.0);

        //preset de test
        Map<String,Double> presetTest = new HashMap<>();
        presetTest.put("var1",100.0);
        presetTest.put("var2",100.0);
        presetTest.put("var3",100.0);
        presetTest.put("var4",100.0);
        presetTest.put("var5",100.0);

        //choix du preset
        Map<String,Double> entryPreset = presetTest;

        //##############################################################################################################
        //Config
        double outputBound = 1.0;
        int nbGenerations = 10;
        boolean doRandGeneration = false;
        String fileName = "data.json";
        boolean doLxPlotDisplay = true;

        //##############################################################################################################
        //initialisation du générateur d'input (entrées/indicateurs)
        GeneratorInputCAC genIn = new GeneratorInputCAC();
        //initialisation du generateur avec leses indicateurs du preset
        for (Map.Entry<String,Double> e:entryPreset.entrySet()) {
            genIn.initVariableWithRange(e.getKey(),0.0,e.getValue());
        }

        //##############################################################################################################
        //initialisation du generateur d'output (sortie/attention)
        OutputFunction outputFunction = new NormalisedWeightedSum();
        GeneratorOutputCAC genOut = new GeneratorOutputCAC(outputFunction,entryPreset.keySet(),outputBound,false);

        //##############################################################################################################
        //génération des valeurs des indicateurs
        for (int i = 0; i < nbGenerations; i++) {
            //génération d'une instance d'inputs
            if(doRandGeneration) genIn.generateAllRandomValues();
            else genIn.generateAllValues();
            //génération des output
            genOut.addGeneration(genIn.getAllVariablesWithValue());
        }

        //##############################################################################################################
        //affichage final
        genOut.printAllData();

        //##############################################################################################################
        //write JSON to file
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(genOut.dataToJSON().toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //##############################################################################################################
        //affichage LxPlot
        if(doLxPlotDisplay) genOut.displayDataLxPlotShape();

    }
}
