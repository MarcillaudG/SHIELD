package fr.irit.smac.shield.cahcoac;

import fr.irit.smac.shield.exceptions.NotEnoughParametersException;
import fr.irit.smac.shield.model.FunctionGen;
import fr.irit.smac.shield.model.Variable;

import java.util.*;

public class GeneratorInputCAC {

    /**
     * Singleton or intern class
     */
    private Random rand;
    //
    public static int NB_MAX_VAR;

    public static double MIN_VAR = 0.0;

    public static double MAX_VAR = 1.0;

    private TreeMap<String, Variable> variables;

    private int nbVar;

    public GeneratorInputCAC() {
        NB_MAX_VAR = 1000;
        init();
    }

    public GeneratorInputCAC(int nbVarMax) {
        NB_MAX_VAR = nbVarMax;
        init();
    }

    private void init() {
        this.variables = new TreeMap<String,Variable>();
        this.rand = new Random();
        this.nbVar = 1000;

        System.out.println("Success of the initialization");
    }

    /**
     * Return the value calculated by the function of the variable
     *
     * @param variable
     * 		The value of the variable
     * @param h
     * 		the function
     * @param xi
     * 		the parameters of the function
     * @param min
     * 		the minimal value (of the output??)
     * @param max
     * 		the maximal value (of the output??)
     * @return the new value of the variable
     */
    private double calculValueOfVariable(double variable, FunctionGen h, Deque<Double> xi, double min, double max) {
        double res = variable;
        // resultat de la fonction
        double resFun;
        try {
            resFun = h.compute(xi);
            //System.out.println("RESFUN : "+resFun);

            //log version
            double secop = Math.log(Math.abs(variable - resFun));
            //pow version
            //double secop = Math.pow(variable - resFun,2);


            double denom = secop+1;

            if(resFun == 0.0) {
                res = min + new Random().nextDouble()*(max-min);
            }
            else {
                resFun = Math.abs(resFun);
                if (resFun <= variable) {
                    res = variable + (min-variable)*secop/denom;
                }
                else {
                    res = variable + (max-variable)*secop/denom;
                }
            }

        } catch (NotEnoughParametersException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Return the new value
     *
     * @param variable
     *
     * @return the value
     */
    public double getValueOfVariableAfterCalcul(String variable) {
        if(!this.variables.keySet().contains(variable)) {
            initVariableWithRange(variable);
        }
        Variable var = this.variables.get(variable);

        Deque<Double> values = new ArrayDeque<Double>();
        Deque<String> paramTmp = new ArrayDeque<String>(var.getFun().getVariables());
        while(!paramTmp.isEmpty()) {
            values.offer(this.variables.get(paramTmp.poll()).getValue());
        }
        double res = calculValueOfVariable(var.getValue(), var.getFun(), values, var.getMin(), var.getMax());
        //System.out.println("DIFF : "+(this.variables.get(variable).getValue()-res));
        this.variables.get(variable).setValue(res);
        return res;
    }

    public double getValueOfVariableAfterCalculWithGaussianNoise(String variable) {
        //En chantier
        Variable var = this.variables.get(variable);
        Random rand = new Random();
        Double res = 0.0;
        Double mid = (var.getMin()+(var.getMax()- var.getMin())/2);
        Double distToMid = Math.abs(var.getValue() - mid);
        res = res + rand.nextGaussian()*distToMid*1.5+mid;
        if(res>var.getMax()) res = var.getMax();
        else if(res < var.getMin()) res = var.getMin();
        this.variables.get(variable).setValue(res);
        return res;
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

    /**
     * Create a new variable and construct the corresponding function
     * @param variable
     */
    @Deprecated
    private void initVariable(String variable) {
        List<String> variablesRemaining = new ArrayList<String>(this.variables.keySet());

        Variable v = new Variable(variable,MIN_VAR,MAX_VAR);

        int nbVar = this.rand.nextInt(Math.min(NB_MAX_VAR, variablesRemaining.size())+1);
        Deque<String> parameters = new ArrayDeque<String>();

        for(int i = 0; i < nbVar && variablesRemaining.size()>0;i++) {
            String param = variablesRemaining.get(this.rand.nextInt(variablesRemaining.size()));
            parameters.push(param);
            Variable var = this.variables.get(param);

            variablesRemaining.remove(param);
            variablesRemaining.removeAll(var.getFun().getVariables());
        }
        v.setFun(FunctionGen.generateFunction(parameters.size(), parameters));

        this.variables.put(variable,v);

        //System.out.println(v.getName()+ " " +v.getValue()+" "+ v.getFun().getVariables());

    }

    /**
     * Create a new variable and construct the corresponding function
     * @param variable
     */
    private void initVariableWithRange(String variable) {
        List<String> variablesRemaining = new ArrayList<String>(this.variables.keySet());

        Variable v = new Variable(variable,MIN_VAR,MAX_VAR);

        int nbVar = this.rand.nextInt(Math.min(NB_MAX_VAR, variablesRemaining.size())+1);
        Deque<Variable> parameters = new ArrayDeque<Variable>();

        for(int i = 0; i < nbVar && variablesRemaining.size()>0;i++) {
            String param = variablesRemaining.get(this.rand.nextInt(variablesRemaining.size()));
            parameters.push(this.variables.get(param));
            Variable var = this.variables.get(param);

            variablesRemaining.remove(param);
            variablesRemaining.removeAll(var.getFun().getVariables());
        }
        v.setFun(FunctionGen.generateFunctionWithRange(parameters.size(), parameters));

        this.variables.put(variable,v);

        //System.out.println(v.getName()+ " " +v.getValue()+" "+ v.getFun().getVariables());

    }

    /**
     * Create a new variable and construct the corresponding function
     * @param variable
     */
    public void initVariableWithRange(String variable,double minBound, double maxBound) {
        List<String> variablesRemaining = new ArrayList<String>(this.variables.keySet());

        Variable v = new Variable(variable,minBound,maxBound);

        int nbVar = this.rand.nextInt(Math.min(NB_MAX_VAR, variablesRemaining.size())+1);
        Deque<Variable> parameters = new ArrayDeque<Variable>();

        for(int i = 0; i < nbVar && variablesRemaining.size()>0;i++) {
            String param = variablesRemaining.get(this.rand.nextInt(variablesRemaining.size()));
            parameters.push(this.variables.get(param));
            Variable var = this.variables.get(param);

            variablesRemaining.remove(param);
            variablesRemaining.removeAll(var.getFun().getVariables());
        }
        v.setFun(FunctionGen.generateFunctionWithRange(parameters.size(), parameters));

        this.variables.put(variable,v);

        //System.out.println(v.getName()+ " " +v.getValue()+" "+ v.getFun().getVariables());

    }

    @Deprecated
    public void initVariable() {
        this.initVariable("Variable"+this.nbVar);
        this.nbVar++;
    }


    /**
     * Create a new variable with a name generated
     */
    public void initVariableWithRange() {
        this.initVariableWithRange("Variable"+this.nbVar);
        this.nbVar++;
    }

    /**
     * For all variables, generate the new values
     */
    public void generateAllValues() {
        for(String s : this.variables.keySet()) {
            this.getValueOfVariableAfterCalcul(s);
        }
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

    public void printAllVariables() {
        for(String s : this.variables.keySet()) {
            System.out.println(this.variables.get(s));
        }
    }

    public Double getValueOfVariable(String s) {
        return this.variables.get(s).getValue();

    }

    /**
     * Return the collection with the name of all variables
     *
     * @return the set with all the name
     *
     */
    public Set<String> getAllVariablesNames() {
        return this.variables.keySet();
    }

    public TreeMap<String,Variable> getAllVariablesWithValue(){
        TreeMap<String,Variable> clone = new TreeMap<>();
        for(Map.Entry<String,Variable> var : variables.entrySet()){
            clone.put(var.getKey(),new Variable(var.getValue().getName(),var.getValue().getMin(),var.getValue().getMax(),var.getValue().getValue()));
        }
        return clone;
    }

    /**
     * Return the value of a variable
     *
     * @param var
     *
     * @return the value
     */
    public double getValueOfH(String var) {
        return this.variables.get(var).getFun().getLastValue();
    }
}
