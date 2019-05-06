package fr.irit.smac.shield.cahcoac;

import fr.irit.smac.shield.model.Variable;

import java.util.TreeMap;

public abstract class OutputFunction {
    abstract double computeOutput(TreeMap<String, Variable> variables, double maxOutputBound, TreeMap<String,Double> weights);
}
