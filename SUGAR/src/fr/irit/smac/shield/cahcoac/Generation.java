package fr.irit.smac.shield.cahcoac;

import fr.irit.smac.shield.model.Variable;

import java.util.TreeMap;

public class Generation {
    private double output;
    private TreeMap<String, Variable> input;

    public Generation (double out, TreeMap<String,Variable> in){
        this.output=out;
        this.input=in;
    }

    public double getOutput(){
        return output; }

    public TreeMap<String, Variable> getInput(){
        return input;
    }
}
