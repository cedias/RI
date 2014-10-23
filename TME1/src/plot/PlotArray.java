package plot;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;

public class PlotArray {

	double xData[];
	double yData[];
	String graphTitle;
	String xName;
	String yName;



	public PlotArray(Double[] xvals, Double[] yvals, String xName, String yName, String graphTitle) {
		super();
		this.xData = new double[xvals.length];
		this.yData = new double[xvals.length];
		this.graphTitle = graphTitle;
		this.xName = xName;
		this.yName = yName;

		for(int i=0;i< xvals.length;i++){
			xData[i] =  xvals[i];
			yData[i] =  yvals[i];
		}
	}

	public void plot(){
	    Chart chart = QuickChart.getChart(graphTitle, xName, yName, graphTitle, xData, yData);
	    new SwingWrapper(chart).displayChart();
	}



}