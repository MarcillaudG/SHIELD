package fr.irit.smac.shield.cahcoac;

import fr.irit.smac.lxplot.LxPlot;
import fr.irit.smac.lxplot.commons.ChartType;
import fr.irit.smac.shield.model.FunctionGen;
import fr.irit.smac.shield.model.Generator;
import fr.irit.smac.shield.model.Variable;

import java.util.*;

@SuppressWarnings("ALL")
public class GeneratorV2 extends Generator {

    private TreeMap<String,GenerationV2> data = new TreeMap<>();
    private Variable attention;
    private int nbGen = 0;
    int nbVarConst;
    int nbVarStat;
    int nbVarDyna;
    double rangeStat;
    double minBound;
    double maxBound;

    public GeneratorV2(int nbVarConst,int nbVarStat, int nbVarDyna, double rangeStat, double minBound, double maxBound){
        super(1000);
        this.nbVarConst = nbVarConst;
        this.nbVarStat = nbVarStat;
        this.nbVarDyna = nbVarDyna;
        this.rangeStat = rangeStat;
        this.minBound = minBound;
        this.maxBound = maxBound;
    }

    public GeneratorV2(int nbVarConst,int nbVarStat, int nbVarDyna, double rangeStat, double minBound, double maxBound, int nbVarMax){
        super(nbVarMax);
        this.nbVarConst = nbVarConst;
        this.nbVarStat = nbVarStat;
        this.nbVarDyna = nbVarDyna;
        this.rangeStat = rangeStat;
        this.minBound = minBound;
        this.maxBound = maxBound;
    }

    public void generateAllValuesWithNoise() {
        for(String s : this.variables.keySet()) {
            this.getValueOfVariableAfterCalculWithNoise(s);
        }
    }

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

    public void initAttentionWithRange(double min, double max) {
        Random rand = new Random();
        String variable = "attentionIn";
        List<String> allInputNames = new ArrayList<String>(this.variables.keySet());
        allInputNames.remove("attentionIn");

        Variable v = new Variable(variable,min,max);
        List<String> paramNames = new ArrayList<>();
        Deque<Variable> parameters = new ArrayDeque<Variable>();
        System.out.println("allParamNames:"+allInputNames);
        for(; allInputNames.size()>0;) {
            String param = allInputNames.get(rand.nextInt(allInputNames.size()));
            parameters.push(this.variables.get(param));
            Variable var = this.variables.get(param);
            paramNames.add(param);
            allInputNames.remove(param);
        }
        System.out.println("##Parametres affectant attentionOut:"+paramNames);
        v.setFun(FunctionGen.generateFunctionWithRange(parameters.size(), parameters,min,max));

        this.variables.put(variable,v);


    }

    private void autoInitVariables(){
        Random rand = new Random();
        double tmpVal;
        //initialisation des variables constantes
        for (int i=1; i< nbVarConst+1;i++) {
            //genere une valeur entre les deux bornes
            tmpVal = minBound+rand.nextDouble()*maxBound;
            //crée une variable avec cette valeur comme borne min et max
            this.initVariableWithRange("VarConst"+String.format("%03d", i),tmpVal,tmpVal);
        }
        //initialisation des variables statiques
        for (int i=1; i< nbVarStat+1;i++) {
            //assigne une borne min a la variable statique
            tmpVal = minBound+rand.nextDouble()*(maxBound-rangeStat);
            //crée une variable statique avec la borne min trouvée ci dessus et la range demandée
            this.initVariableWithRange("VarStat"+String.format("%03d", i),tmpVal,tmpVal+rangeStat);
        }
        //initialisation des variables dynamiques
        for (int i=1; i< nbVarDyna+1;i++) {
            //crée une variable avec comme borne les bornes passé en parametre
            this.initVariableWithRange("VarDyna"+String.format("%03d", i),minBound,maxBound);
        }
    }

    double map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    private void autoInitVariables(boolean doSpreadStatVar){
        Random rand = new Random();
        double tmpVal;
        //initialisation des variables constantes
        for (int i=1; i< nbVarConst+1;i++) {
            //genere une valeur entre les deux bornes
            tmpVal = minBound+rand.nextDouble()*maxBound;
            //crée une variable avec cette valeur comme borne min et max
            this.initVariableWithRange("VarConst"+String.format("%03d", i),tmpVal,tmpVal);
        }
        //initialisation des variables statiques
        for (int i=1; i< nbVarStat+1;i++) {
            if(doSpreadStatVar) {
                //assigne une borne min a la variable statique en les étalant entre borne min et max
                tmpVal = map(i,1,nbVarStat+1,minBound,maxBound-rangeStat);
                //crée une variable statique avec la borne min trouvée ci dessus et la range demandée
                this.initVariableWithRange("VarStat" + String.format("%03d", i), tmpVal, tmpVal + rangeStat);
            } else {
                //assigne une borne min a la variable statique
                tmpVal = minBound+rand.nextDouble()*(maxBound-rangeStat);
                //crée une variable statique avec la borne min trouvée ci dessus et la range demandée
                this.initVariableWithRange("VarStat"+String.format("%03d", i),tmpVal,tmpVal+rangeStat);
            }
        }
        //initialisation des variables dynamiques
        for (int i=1; i< nbVarDyna+1;i++) {
            //crée une variable avec comme borne les bornes passé en parametre
            this.initVariableWithRange("VarDyna"+String.format("%03d", i),minBound,maxBound);
        }
    }

    public void autoInitAll(){
        autoInitVariables();
        initAttentionWithRange(0,20);
    }

    public void autoInitAll(boolean doSpreadStatVar){
        autoInitVariables(doSpreadStatVar);
        initAttentionWithRange(0,20);
    }

    public void createNewGenSHIELD(){
        generateAllValues();
        TreeMap<String,Variable> genIn = getAllVariablesWithValue();
        Variable attentionOut = new Variable("attentionOut",0,20,getValueOfVariableAfterCalcul("attentionIn"));
        data.put("Gen"+String.format("%03d", nbGen),new GenerationV2(attentionOut,genIn));
        nbGen++;
    }

    public void createNewGenRandom(){
        generateAllRandomValues();
        TreeMap<String,Variable> genIn = getAllVariablesWithValue();
        Variable attentionOut = new Variable("attentionOut",0,20,getValueOfVariableAfterCalcul("attentionIn"));
        data.put("Gen"+String.format("%03d", nbGen),new GenerationV2(attentionOut,genIn));
        nbGen++;
    }

    public void createNewGenNoise(){
        generateAllValuesWithNoise();
        TreeMap<String,Variable> genIn = getAllVariablesWithValue();
        Variable attentionOut = new Variable("attentionOut",0,20,getValueOfVariableAfterCalcul("attentionIn"));
        data.put("Gen"+String.format("%03d", nbGen),new GenerationV2(attentionOut,genIn));
        nbGen++;
    }
    public TreeMap<String, Variable> getAllVariablesWithValue(){
        TreeMap<String,Variable> clone = new TreeMap<>();
        for(Map.Entry<String,Variable> var : variables.entrySet()){
            clone.put(var.getKey(),new Variable(var.getValue().getName(),var.getValue().getMin(),var.getValue().getMax(),var.getValue().getValue()));
        }
        return clone;
    }

    public void generateAllRandomValues() {
        Random rand = new Random();
        for(Variable v : this.variables.values()) {
            //System.out.println("Var:"+v.getName()+" min:"+v.getMin()+" max:"+v.getMax());
            v.setValue((rand.nextDouble() * (v.getMax()-v.getMin())) + v.getMin());
        }
    }

    public void printAllGenerationsRaw(){
        for (Map.Entry<String,GenerationV2> e:data.entrySet()) {
            System.out.println("GenerationName:"+e.getKey());
            System.out.println("\t"+e.getValue().toString());
            System.out.println("################################################################################################################");
        }
    }

    public void printAttentionFunction(){
        System.out.println(variables.get("attentionIn").getFun().toString());
    }

    public void displayDataLxPlotGeneration(){
        System.out.println("data size: "+data.size());
        int i;
        ChartType chartType = ChartType.SHAPE;
        //ChartType chartType = ChartType.BAR;
        LxPlot.getChart("data",ChartType.MULTIPLE);
        //for all generations
        for(Map.Entry<String,GenerationV2> g: data.entrySet()){
            //gen number
            i = 0;
            for(Variable v : g.getValue().getInput().values()){
                LxPlot.getChart("data",chartType).add(v.getName(),i,v.getValue());
            }
            i++;
        }
    }

    public void displayDataLxPlotVariableByGeneration(){
        int i=0;
        ChartType chartType = ChartType.SHAPE;

        for(Map.Entry<String,GenerationV2> g: data.entrySet()){
            for(Map.Entry<String,Variable> v : g.getValue().getInput().entrySet()){
                if(v.getKey() == "attentionIn"){
                    LxPlot.getChart(v.getKey(),chartType).add("MaxLine",i,20);
                    LxPlot.getChart(v.getKey(),chartType).add("MinLine",i,0);
                } else {
                    LxPlot.getChart(v.getKey(), chartType).add("MaxLine", i, maxBound);
                    LxPlot.getChart(v.getKey(), chartType).add("MinLine", i, minBound);
                }

                LxPlot.getChart(v.getKey(),chartType).add("Data",i,v.getValue().getValue());

            }
            LxPlot.getChart("attentionOut",chartType).add("MaxLine",i,20);
            LxPlot.getChart("attentionOut",chartType).add("MinLine",i,0);
            LxPlot.getChart("attentionOut",chartType).add("Data",i,g.getValue().getOutput().getValue());



            i++;
        }
    }

}
