package fr.irit.smac.shield.cahcoac;

import com.google.gson.JsonObject;
import com.sun.javafx.geom.Shape;
import fr.irit.smac.lxplot.LxPlot;
import fr.irit.smac.lxplot.commons.ChartType;
import fr.irit.smac.lxplot.server.LxPlotChart;
import fr.irit.smac.shield.cahcoac.Function.OutputFunction;
import fr.irit.smac.shield.model.Variable;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class GeneratorOutputCAC {
    private int nbGen;
    private TreeMap<String,Double> weights;
    private TreeMap<String,Generation> data;
    private double maxOutputBound;
    private OutputFunction outputFunction;
    private Random rand;

    public GeneratorOutputCAC(OutputFunction f, Set<String> inputNames, double maxOut, boolean doRandomWeights){
        rand = new Random();
        nbGen = 0;
        weights = new TreeMap<>();
        data = new TreeMap<>();
        maxOutputBound = maxOut;
        outputFunction = f;
        for (String s:inputNames) {
            if(doRandomWeights)weights.put(s,rand.nextDouble());
            else weights.put(s,1.0);
        }
    }

    public void addGeneration(TreeMap<String, Variable> variables){
        //calcule l'output avec la fonction associée puis stoque le set de variable associé a son output dans data avec son nom
        double output = outputFunction.computeOutput(variables,maxOutputBound,weights);
        data.put("Gen"+nbGen,new Generation(output,variables));
        nbGen++;
    };


    public Set<String> getGenerationNames(){
        return data.keySet();
    }

    public Generation getGeneration(String genName){
        return data.get(genName);
    }

    public JsonObject dataToJSON(){
        // output
        JsonObject jsonFinal = new JsonObject();

        //info
        JsonObject jsonInfo = new JsonObject();
        jsonInfo.addProperty("function",outputFunction.getName());
        jsonInfo.addProperty("outputMinBound",0.0);
        jsonInfo.addProperty("outputMaxBound",maxOutputBound);
        jsonInfo.addProperty("nbInputs",weights.size());
        jsonInfo.addProperty("nbGenerations",nbGen);
        jsonFinal.add("info",jsonInfo);

        //weights
        JsonObject jsonWeights = new JsonObject();
        for(Map.Entry<String,Double> e:weights.entrySet()){
            jsonWeights.addProperty(e.getKey(),e.getValue());
        }
        jsonFinal.add("weights",jsonWeights);

        //generations
        JsonObject jsonData = new JsonObject();
        for(Map.Entry<String,Generation> g:data.entrySet()){
            JsonObject jsonGeneration = new JsonObject();
            jsonGeneration.addProperty("output",g.getValue().getOutput());
            for(Map.Entry<String,Variable> v:g.getValue().getInput().entrySet()){
                JsonObject jsonVariable = new JsonObject();
                jsonVariable.addProperty("min",v.getValue().getMin());
                jsonVariable.addProperty("max",v.getValue().getMax());
                jsonVariable.addProperty("value",v.getValue().getValue());
                jsonGeneration.add(v.getKey(),jsonVariable);
            }
            jsonData.add(g.getKey(),jsonGeneration);
        }
        jsonFinal.add("data",jsonData);

        return jsonFinal;
    }

    public TreeMap<String,Double> getWeights(){
        return weights;
    }

    public void printAllData(){
        //affichage des poids
        System.out.println("Weights:");
        for (Map.Entry<String,Double> e:weights.entrySet()) {
            System.out.format("\tvarName:%-25s, Value:%10.3f\n",e.getKey(),e.getValue());
        }

        //affichage des generations
        for(Map.Entry<String,Generation> data: data.entrySet()){
            //entete général sur la generation
            System.out.format("Generation:[%s], Output:[%.3f], fullness:[%5.1f%%]\n",data.getKey(),data.getValue().getOutput(), (data.getValue().getOutput()/maxOutputBound)*100);
            //variables de la génération
            for (Variable v :data.getValue().getInput().values()) {
                System.out.println("\t"+v.toString());
            }
            System.out.print("\n");
        }
    }

    public void displayDataLxPlotShape(){
        int i;
        ChartType chartType = ChartType.SHAPE;

        for(Map.Entry<String,Generation> g: data.entrySet()){
            String genName = g.getKey()+"[Out:"+g.getValue().getOutput()+"]";
            i = 0;
            for(Map.Entry<String,Variable> v : g.getValue().getInput().entrySet()){
                LxPlot.getChart(genName,chartType).add("Data(%)",i++,v.getValue().getValue()/v.getValue().getMax());
            }
            i=0;
            for(Map.Entry<String,Double> w: weights.entrySet()){
                LxPlot.getChart(genName,chartType).add("Weights(%)",i++,w.getValue());
            }
        }
    }

    public TreeMap<String, Generation> getData(){
        return data;
    }

    public void clearData(){
        data.clear();
    }

    public void setOutputFunction(OutputFunction f){
        outputFunction = f;
    }
}
