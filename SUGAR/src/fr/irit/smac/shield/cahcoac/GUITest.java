package fr.irit.smac.shield.cahcoac;

import fr.irit.smac.lxplot.LxPlot;
import fr.irit.smac.lxplot.commons.ChartType;
import fr.irit.smac.lxplot.server.LxPlotChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.WaferMapDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUITest {
    public static void main(String[] args) {
        // First, we inform the server that the chart "Layer" must be of type
		LxPlot.getChart("Layer", ChartType.PLOT, true, 10);
		for(int i = 0; i < 20; i++){
			LxPlot.getChart("Layer").add("My serie", i, i);try {
				Thread.sleep(10);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Add plots to the chart "Layer" for serie #0
		LxPlot.getChart("Layer").add(1, 2);
		LxPlot.getChart("Layer").add(2, 2.5);
		LxPlot.getChart("Layer").add(2, 3);

		// Add plots to the chart "Layer" for serie "My serie"
		LxPlot.getChart("Layer").add("My serie", 21, 2);
		LxPlot.getChart("Layer").add("My serie", 12, 2.5);

		try {
			Thread.sleep(1000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		// Add plots to the chart "My Chart" which will be created during this
		// call
		LxPlot.getChart("My chart").add(2, 3);
		LxPlot.getChart("My chart").add(3, 3);

		//Override the value at x=3
		LxPlot.getChart("My chart").add(3, 4);

		// Access the JFreeChart object (only from the server side)
        final JFreeChart chart = ((LxPlotChart) (LxPlot.getChart("Layer")))
                .getJFreeChart();

        final XYItemRenderer r = ((XYPlot) (chart.getPlot())).getRenderer();

        r.setSeriesStroke(0, new BasicStroke(0.1f));
        r.setSeriesShape(0, new Rectangle(3, 3));
        r.setSeriesShape(0, new Ellipse2D.Float(3, 3, 3, 3));
        r.setSeriesPaint(0, Color.green);



        // First, we inform the server that the chart "Layer" must be of type
		LxPlot.getChart("Multiple", ChartType.MULTIPLE);

		// Add plots to the chart "Layer" for serie #0
		LxPlot.getChart("Multiple", ChartType.MULTIPLE).add(1, 2);
		LxPlot.getChart("Multiple", ChartType.MULTIPLE).add(2, 2.5);
		LxPlot.getChart("Multiple", ChartType.MULTIPLE).add(3, 3);

		LxPlot.getChart("Multiple", ChartType.MULTIPLE).add(4, 4);
		LxPlot.getChart("Multiple", ChartType.MULTIPLE).add(5, 8);
		LxPlot.getChart("Multiple", ChartType.MULTIPLE).add(6, 12);


		List<ChartType> list = new ArrayList<ChartType>();
		list.add(ChartType.PLOT);
		list.add(ChartType.LINE);
		list.add(ChartType.BAR);
		LxPlot.getChart("TestMultiple", list);


		LxPlot.getChart("TestMultiple", list).add(1, 2);
		LxPlot.getChart("TestMultiple", list).add(2, 2.5);
		LxPlot.getChart("TestMultiple", list).add(3, 3);
		LxPlot.getChart("TestMultiple", list).add(4, 4);
		LxPlot.getChart("TestMultiple", list).add(5, 8);
		LxPlot.getChart("TestMultiple", list).add(6, 12);

        /*LxPlot.getChart("BOX", ChartType.BAR.BOX);

        for(int i = 1; i < 1000; i++){
        List<Double> listt = new ArrayList<Double>();
        listt.add(5.);
        listt.add(5.);
        listt.add(3.);
        listt.add(6.);
        listt.add(1.);
        listt.add(9.);
        LxPlot.getChart("BOX",ChartType.BOX).addBox(listt);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        }*/

        // First, we inform the server that the chart "Layer" must be of type
		LxPlot.getChart("Box", ChartType.SPIDER);

		// Add plots to the chart "Layer" for serie #0
		LxPlot.getChart("Box", ChartType.SPIDER).add(1, 2);
		LxPlot.getChart("Box", ChartType.SPIDER).add(2, 2.5);
		LxPlot.getChart("Box", ChartType.SPIDER).add(3, 3);

		LxPlot.getChart("Box", ChartType.SPIDER).add(4, 4);
		LxPlot.getChart("Box", ChartType.SPIDER).add(5, 8);
		LxPlot.getChart("Box", ChartType.SPIDER).add(6, 12);

        //Dispose test

		LxPlot.getChart("wafer chart", ChartType.WAFER);
        Random random = new Random();
	          for (int x = 1; x <= 30; x++) {
	              for (int y = 0; y < 20; y++) {
	          		LxPlot.getChart("wafer chart", ChartType.WAFER).add(x,random.nextInt(30));
            }
        }
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(1,1.0);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(2,14.0);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(3,3.0);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(4,4.0);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(5,5.0);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(7,6.0);

        WaferMapDataset data = new WaferMapDataset(30, 20);
        // data.addValue(1, 5, 14); // (value, chipx, chipy)
		data.addValue(1, 5, 13);
        data.addValue(1, 5, 12);
        data.addValue(1, 5, 11);
        data.addValue(1, 5, 10);
        data.addValue(1, 5, 9);
        data.addValue(7, 5, 8);
        data.addValue(8, 5, 7);
        data.addValue(9, 5, 6);
        data.addValue(1, 6, 10);
        data.addValue(1, 7, 10);
        data.addValue(1, 8, 10);
        data.addValue(1, 9, 10);
        data.addValue(1, 10, 10);
        data.addValue(1, 11, 10);
        data.addValue(1, 11, 11);
        data.addValue(1, 11, 12);
        data.addValue(2, 11, 13);
        data.addValue(1, 11, 14);
        data.addValue(2, 11, 9);
        data.addValue(2, 11, 8);
        data.addValue(2, 11, 7);
        data.addValue(2, 11, 6);

        data.addValue(6, 16, 6);
        data.addValue(6, 17, 6);
        data.addValue(6, 18, 6);
        data.addValue(6, 19, 6);
        data.addValue(6, 20, 6);
        data.addValue(6, 21, 6);
        data.addValue(6, 22, 6);
        data.addValue(3, 19, 7);
        data.addValue(3, 19, 8);
        data.addValue(3, 19, 9);
        data.addValue(3, 19, 10);
        data.addValue(3, 19, 11);
        data.addValue(3, 19, 12);
        data.addValue(3, 19, 13);
        data.addValue(4, 19, 14);
        data.addValue(4, 18, 14);
        data.addValue(4, 17, 14);
        data.addValue(4, 16, 14);
        data.addValue(4, 20, 14);
        data.addValue(4, 21, 14);
        data.addValue(4, 22, 14);

        Random rand = new Random();
	          for (int x = 1; x <= 30; x++) {
	              for (int y = 0; y < 20; y++) {
                data.addValue(rand.nextInt(30), x, y);
            }
        }
	   JFreeChart wafer = ChartFactory.createWaferMapChart("", data, PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(wafer);

		JInternalFrame internalChartFrame = new JInternalFrame();
		internalChartFrame.add(chartPanel);
		internalChartFrame.setVisible(true);
		LxPlotChart.getDesktopPane().add(internalChartFrame);

        //LxPlot.getChart("scatter chart", ChartType.SCATTER).add(7, 10);
        for (int i=3;i<100;i++){
		LxPlot.getChart("Bar chart", ChartType.BAR).add(i, 3);
			if(i%4==0)
				LxPlot.getChart("Linear chart", ChartType.LINE).add(i,i);
			else
				LxPlot.getChart("Linear chart", ChartType.LINE).add(i,i+i);
			LxPlot.getChart("pie chart", ChartType.PIE).add(i%6, (i*i)%100);;

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


        // Close chart
        // LxPlot.getChart("My chart").close();
        // LxPlot.getChart("Layer").close();

		/*OverlaidXYPlotDemo1 demo = new OverlaidXYPlotDemo1("Overlaid XYPlot Demo");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);   */

    }
}
