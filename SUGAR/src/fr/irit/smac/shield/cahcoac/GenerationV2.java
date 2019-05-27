package fr.irit.smac.shield.cahcoac;

import fr.irit.smac.shield.model.Variable;

import java.util.Map;
import java.util.TreeMap;

public class GenerationV2 {
    private Variable output;
    private TreeMap<String, Variable> input;

    public GenerationV2(Variable out, TreeMap<String,Variable> in){
        this.output=out;
        this.input=in;
    }

    public Variable getOutput(){
        return output; }

    public TreeMap<String, Variable> getInput(){
        return input;
    }

    @Override
    public String toString() {
        String output_s = output.toString();
        String inputs_s = "";
        for (Variable v:input.values()) {
            inputs_s += "\t"+v.toString()+"\n";
        }
        return output_s+"\n"+inputs_s;
    }
}
