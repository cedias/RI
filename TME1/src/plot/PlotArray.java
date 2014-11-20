package plot;

import java.io.IOException;
import java.util.List;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.VectorGraphicsEncoder;
import com.xeiam.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;

public class PlotArray {

	double xData[];
	double yData[];
	String graphTitle;
	String xName;
	String yName;
	Chart c;


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
	    this.c = chart;
	    new SwingWrapper(chart).displayChart();
	}

	public void save(String filename) throws IOException{
		VectorGraphicsEncoder.saveVectorGraphic(this.c, filename, VectorGraphicsFormat.PDF);
	}


}
