package fr.irit.smac.shield.cahcoac;

import com.sun.media.sound.EmergencySoundbank;
import fr.irit.smac.shield.model.Variable;

import java.util.*;

public class MyTest {

    private static final int NB_INDICATEURS = 10;


    public static void main(String[] args) {
        //preset d'indicateurs pour tester (nom,borneMax)
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
        indicateursPreset.put("danseLaSambaPerMille",1000.0);



        //initialisation du générateur d'input (entrées/indicateurs)
        GeneratorInputCAC genIn = new GeneratorInputCAC();
        //initialisation des indicateurs du preset
        for (Map.Entry<String,Double> e:indicateursPreset.entrySet()) {
            genIn.initVariableWithRange(e.getKey(),0.0,e.getValue());
        }

        //initialisation du generateur d'output (sortie/attention)
        double outputBound = 1.0;
        OutputFunction outputFunction = new MeanWeightedSum();
        GeneratorOutputCAC genOut = new GeneratorOutputCAC(outputFunction,indicateursPreset.keySet(),outputBound);

        //génération des valeurs des indicateurs
        int nbGenerations = 10;
        boolean doAffichage = false;

        for (int i = 0; i < nbGenerations; i++) {
            genIn.generateAllValues();
            TreeMap<String,Variable> vars = genIn.getAllVariablesWithValue();

            //génération des output
            genOut.addGeneration("Gen"+i,vars);

            if(doAffichage)for (Map.Entry<String,Variable> e :vars.entrySet()) {
                System.out.println(e.getValue().toString());
            }
        }

        //affichage final
        //weights
        System.out.println("Weights:");
        for (Map.Entry<String,Double> e:genOut.getWeights().entrySet()) {
            System.out.format("\tvarName:%-25s, Value:%10.3f\n",e.getKey(),e.getValue());
        }
        System.out.print("\n");
        for (String gName: genOut.getGenerationNames()) {
            Generation generation = genOut.getGeneration(gName);
            System.out.format("Generation:[%s], Output:[%.3f], fullness:[%5.1f%%]\n",gName,generation.getOutput(), (generation.getOutput()/outputBound)*100);

            for (Variable g :generation.getInput().values()) {
                System.out.println("\t"+g.toString());
            }
            System.out.print("\n");
        }



    }
}
