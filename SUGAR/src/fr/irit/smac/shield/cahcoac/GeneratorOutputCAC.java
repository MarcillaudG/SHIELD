package fr.irit.smac.shield.cahcoac;

import fr.irit.smac.shield.model.Variable;
import sun.reflect.generics.tree.Tree;

import java.util.*;

public class GeneratorOutputCAC {
    private int nbGen;
    private TreeMap<String,Double> weights = new TreeMap<>();
    private TreeMap<String,Generation> data = new TreeMap<>();
    private double maxOutputBound;
    private OutputFunction outputFunction;
    private Random rand;


    public GeneratorOutputCAC(OutputFunction f, Set<String> inputName, double maxOut){
        rand = new Random();
        nbGen = 0;
        weights = new TreeMap<>();
        maxOutputBound = maxOut;
        outputFunction = f;
        for (String s:inputName) {
            weights.put(s,rand.nextDouble());
        }
    }

//    private double computeOutput(TreeMap<String, Variable> variables, double maxOutputBound){
//        double output = 0;
//        double maxOutput = 0;
//        for (String entryName : variables.keySet()) {
//            output += variables.get(entryName).getValue() * weights.get(entryName);
//            maxOutput += variables.get(entryName).getMax() * weights.get(entryName);
//        }
//        double nOutput = (output / maxOutput)*maxOutputBound;
//        return nOutput;
//    }

    public void addGeneration(String generationName, TreeMap<String, Variable> variables){
        double output = outputFunction.computeOutput(variables,maxOutputBound,weights);
        data.put(generationName,new Generation(output,variables));
        nbGen++;
    };

    public Set<String> getGenerationNames(){
        return data.keySet();
    }

    public int getNbGen(){
        return data.size();
    }

    public TreeMap<String,Double> getWeights(){
        return weights;
    }

    public Generation getGeneration(String genName){
        return data.get(genName);
    }

    public void clearData(){
        data.clear();
    }

    public void setOutputFunction(OutputFunction f){
        outputFunction = f;
    }
}
