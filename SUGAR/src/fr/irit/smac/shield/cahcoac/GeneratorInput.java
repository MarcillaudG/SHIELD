package fr.irit.smac.shield.cahcoac;

import fr.irit.smac.shield.model.Generator;
import fr.irit.smac.shield.model.Variable;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class GeneratorInput extends Generator {
    public double getValueOfVariableAfterCalculWithNoise(String variable) {
        Double res =0.0;
        Variable var = this.variables.get(variable);
        Random rand = new Random();
        Double variance = var.getMax()/20.0+rand.nextDouble()*var.getMax()/5.0;
        Double mid = (var.getMax()-var.getMin())/2;
        if(rand.nextDouble() < 0.5){
            res = var.getValue()+variance;
        } else {
            res = var.getValue() - variance;
        }
        if(res>var.getMax()) res = var.getMax()-rand.nextDouble()*mid; else if(res < var.getMin()) res = var.getMin()+rand.nextDouble()*mid;
        System.out.println("var:"+variance+" oldVal:"+var.getValue()+" newVal:"+res);
        this.variables.get(variable).setValue(res);
        return res;
    }

    public void generateAllRandomValues() {
        Random rand = new Random();
        for(Variable v : this.variables.values()) {
            v.setValue(v.getMin()+rand.nextDouble()*v.getMax());
        }
    }

    public void generateAllValuesWithNoise() {
        for(String s : this.variables.keySet()) {
            this.getValueOfVariableAfterCalculWithNoise(s);
        }
    }

    public TreeMap<String,Variable> getAllVariablesWithValue(){
        TreeMap<String,Variable> clone = new TreeMap<>();
        for(Map.Entry<String,Variable> var : variables.entrySet()){
            clone.put(var.getKey(),new Variable(var.getValue().getName(),var.getValue().getMin(),var.getValue().getMax(),var.getValue().getValue()));
        }
        return clone;
    }

}
