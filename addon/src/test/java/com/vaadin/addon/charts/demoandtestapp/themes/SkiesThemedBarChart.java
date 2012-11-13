package com.vaadin.addon.charts.demoandtestapp.themes;

import com.vaadin.addon.charts.ChartTheme;
import com.vaadin.addon.charts.demoandtestapp.columnandbar.BarWithNegativeStack;
import com.vaadin.addon.charts.themes.SkiesTheme;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class SkiesThemedBarChart extends BarWithNegativeStack {

    @Override
    public String getDescription() {
        return "Skies themed: " + super.getDescription();
    }

    @Override
    protected Component getChart() {
        ChartTheme.get().setTheme(new SkiesTheme());
        return super.getChart();
    }

}
