package fr.irit.smac.shield.views;


import fr.irit.smac.lxplot.LxPlot;
import fr.irit.smac.shield.model.Generator;

public class MainWindow {

	private Generator generator;
	
	private static int NB_VAR = 100;
	
	private int cycle;
	
	public MainWindow() {
		this.generator = new Generator();
		cycle = 0;
		init();
	}
	
	private void init() {
		for(int i = 0; i < NB_VAR;i++) {
			this.generator.initVariableWithRange();
		}
	}
	
	public void run() {
		while(true) {
			this.generator.generateAllValues();
			for(int i = 0; i < NB_VAR; i = i+20) {
				String name = "Variable"+(1000+i);
				//System.out.println(this.generator.getValueOfVariable(name));
				
				LxPlot.getChart(name).add("Variable",cycle,this.generator.getValueOfVariable(name));
				LxPlot.getChart(name+"H").add("H",cycle,this.generator.getValueOfH(name));
			}
			cycle++;
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		MainWindow main = new MainWindow();
		main.run();
	}
}
