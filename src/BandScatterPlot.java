import java.util.List;
import java.util.Map;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.ui.ApplicationFrame;

public class BandScatterPlot extends ApplicationFrame{

    private static final int WINDOW_X = 560;
    private static final int WINDOW_Y = 367;

    public BandScatterPlot(String windowTitle, String chartTitle,
			   Map<Integer, List<Double>> data){
	super(windowTitle);
	JFreeChart scatterplot =
	    ChartFactory.createXYLineChart(chartTitle,
					   "k-point",
					   "E (eV)",
					   generateDataset(data),
					   PlotOrientation.VERTICAL,
					   false, true, false);
	ChartPanel chartPanel = new ChartPanel(scatterplot);
	chartPanel.setPreferredSize(new Dimension(WINDOW_X, WINDOW_Y));
	XYPlot plotData = scatterplot.getXYPlot();
	setContentPane(chartPanel);
    }

    private XYDataset generateDataset(Map<Integer, List<Double>> data){
	int nBand = data.get(1).size();
	XYSeries[] seriesArr = new XYSeries[data.size()];
	for(int k = 0; k < seriesArr.length; k++)
	    seriesArr[k] = new XYSeries("k-point: #" + (k+1));

	
	for(int i = 0; i < nBand; i++){
	    for(Integer kpt : data.getKeySet()){
		XYSeries series = seriesArr[kpt];
		series.add(kpt, data.get(kpt).get(i));
	    }
	}
	XYSeriesCollection organizedData = new XYSeriesCollection();
	for(int k = 0; k < seriesArr.length; k++)
	    organizedData.addSeries(seriesArr[k]);
	
	return organizedData;
    }
    
}
