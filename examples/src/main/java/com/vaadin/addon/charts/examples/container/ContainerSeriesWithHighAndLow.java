package com.vaadin.addon.charts.examples.container;

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.examples.AbstractVaadinChartExample;
import com.vaadin.addon.charts.examples.SkipFromDemo;
import com.vaadin.addon.charts.model.ChartDataSeries;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataLabelsRange;
import com.vaadin.addon.charts.model.PlotOptionsColumnrange;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.server.data.DataSource;
import com.vaadin.server.data.ListDataSource;
import com.vaadin.ui.Component;

@SkipFromDemo
public class ContainerSeriesWithHighAndLow extends AbstractVaadinChartExample {

    private DataSource<Temperature> temps = new ListDataSource<>(getMockData());
    @Override
    public String getDescription() {
        return "Ranged with ContainerSeries high and low set";
    }

    @Override
    protected Component getChart() {
        Chart chart = new Chart(ChartType.COLUMNRANGE);

        Configuration conf = chart.getConfiguration();
        conf.getChart().setInverted(true);
        conf.setTitle("Temperature variation by month");
        conf.setSubTitle("Observed in Vik i Sogn, Norway, 2009");

        XAxis xAxis = new XAxis();
        xAxis.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec");
        conf.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Temperature ( °C )");
        conf.addyAxis(yAxis);

        Tooltip tooltip = new Tooltip();
        tooltip.setValueSuffix("°C");
        conf.setTooltip(tooltip);

        PlotOptionsColumnrange columnRange = new PlotOptionsColumnrange();
        columnRange.setDataLabels(new DataLabelsRange(true));
        columnRange.getDataLabels().setFormatter(
                "function() {return this.y + '°C';}");
        conf.setPlotOptions(columnRange);

        conf.getLegend().setEnabled(false);
        conf.setSeries(createChartDS());

        return chart;
    }
    private ChartDataSeries createChartDS(){
        ChartDataSeries<Temperature> ds = new ChartDataSeries(temps);
        ds.setLowDataProvider(Temperature::getMin);
        ds.setHighDataProvider(Temperature::getMax);
        ds.setName("Temperatures");
        return ds;
    }
    private class Temperature {
        private double min;
        private double max;

        public Temperature(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }
    }
    private Collection<Temperature> getMockData() {

        Double[][] data=  { { -9.7, 9.4 }, { -8.7, 6.5 }, { -3.5, 9.4 },
                { -1.4, 19.9 }, { 0.0, 22.6 }, { 2.9, 29.5 }, { 9.2, 30.7 },
                { 7.3, 26.5 }, { 4.4, 18.0 }, { -3.1, 11.4 }, { -5.2, 10.4 },
                { -13.5, 9.8 } };

        Collection<Temperature> temps= new ArrayList<>();
        for(int i=0;i<data.length;i++) {
            temps.add(new Temperature(data[i][0],data[i][1]));
        }
        return temps;
    }
}
