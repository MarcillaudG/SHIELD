package fr.irit.smac.shield.cahcoac;

import fr.irit.smac.shield.model.Variable;

import java.util.TreeMap;

public class MeanWeightedSum extends OutputFunction {
    @Override
    double computeOutput(TreeMap<String, Variable> variables, double maxOutputBound, TreeMap<String,Double> weights) {
        double output = 0;
        double maxOutput = 0;
        for (String entryName : variables.keySet()) {
            output += variables.get(entryName).getValue() * weights.get(entryName);
            maxOutput += variables.get(entryName).getMax() * weights.get(entryName);
        }

        double nOutput = (output / maxOutput)*maxOutputBound;
        //System.out.println("OUTPUT:"+output+"\nMaxOUTPUT:"+maxOutput+"\nnOUTPUT:"+nOutput+"\n");
        return nOutput;
    }
}
