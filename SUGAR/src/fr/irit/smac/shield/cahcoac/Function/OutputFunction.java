package fr.irit.smac.shield.cahcoac.Function;

import fr.irit.smac.shield.model.Variable;

import java.util.TreeMap;

public abstract class OutputFunction {
    public abstract double computeOutput(TreeMap<String, Variable> variables, double maxOutputBound, TreeMap<String,Double> weights);
    public abstract String getName();
}
