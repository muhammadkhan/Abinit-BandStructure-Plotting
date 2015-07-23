import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class BandScatterPlot extends ApplicationFrame{

    private int nBand;
    protected static final int WINDOW_X = 560;
    protected static final int WINDOW_Y = 367;

    public BandScatterPlot(String windowTitle, String chartTitle,
			   Map<Integer, List<Double>> data) throws IOException{
	super(windowTitle);
	nBand = data.get(1).size();
	JFreeChart scatterplot =
	    ChartFactory.createXYLineChart(chartTitle,
					   "k-point",
					   "E (eV)",
					   generateDataset(data),
					   PlotOrientation.VERTICAL,
					   false, true, false);
	ChartPanel chartPanel = new ChartPanel(scatterplot);
	chartPanel.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));
	setContentPane(chartPanel);
    }

    private XYDataset generateDataset(Map<Integer, List<Double>> data){
	XYSeries[] seriesArr = new XYSeries[nBand];
	for(int i = 0; i < seriesArr.length; i++)
	    seriesArr[i] = new XYSeries("Band #" + (i+1));

	
	for(int i = 0; i < nBand; i++){
	    for(Integer kpt : data.keySet()){
		XYSeries series = seriesArr[i];
		series.add(kpt, data.get(kpt).get(i));
	    }
	}
	XYSeriesCollection organizedData = new XYSeriesCollection();
	for(int k = 0; k < seriesArr.length; k++)
	    organizedData.addSeries(seriesArr[k]);
	
	return organizedData;
    }
    
}
