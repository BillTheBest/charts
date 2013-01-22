package com.vaadin.addon.charts.client.ui;

/*
 * #%L
 * Vaadin Charts
 * %%
 * Copyright (C) 2012 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Add-On License 2.0
 * (CVALv2).
 * 
 * See the file licensing.txt distributed with this software for more
 * information about licensing.
 * 
 * You should have received a copy of the CVALv2 along with this program.
 * If not, see <http://vaadin.com/license/cval-2.0>.
 * #L%
 */

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.vaadin.addon.charts.Chart;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.VWindow;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.client.ui.window.WindowConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(Chart.class)
public class ChartConnector extends AbstractComponentConnector {

    ChartServerRpc rpc = RpcProxy.create(ChartServerRpc.class, this);
    protected ElementResizeListener resizeListener;
    public static final String POINT_CLICK_EVENT_ID = "pcl";
    public static final String LEGENDITEM_CLICK_EVENT_ID = "lic";
    public static final String CHART_SELECTION_EVENT_ID = "cs";
    public static final String CHART_CLICK_EVENT_ID = "cl";

    public ChartConnector() {
        registerRpc(ChartClientRpc.class, new ChartClientRpc() {
            @Override
            public void addPoint(double x, double y, int seriesIndex,
                    boolean shift) {
                getWidget().addPoint(x, y, seriesIndex, shift);
            }

            @Override
            public void removePoint(double x, double y) {
                getWidget().removePoint(x, y);
            }

            @Override
            public void updatePointValue(int seriesIndex, int pointIndex,
                    double newValue) {
                getWidget().updatePointValue(seriesIndex, pointIndex, newValue);
            }

            @Override
            public void setSeriesEnabled(int seriesIndex, boolean enabled) {
                getWidget().setSeriesEnabled(seriesIndex, enabled);
            }

            @Override
            public void setAnimationEnabled(boolean animation) {
                getWidget().setAnimation(animation);
            }
        });
    }

    @Override
    public HighchartWidget getWidget() {
        return (HighchartWidget) super.getWidget();
    }

    @Override
    public ChartState getState() {
        return (ChartState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        final HighchartConfig cfg = HighchartConfig
                .createFromServerSideString(getState().jsonState);
        if (getState().registeredEventListeners != null
                && getState().registeredEventListeners
                        .contains(CHART_CLICK_EVENT_ID)) {
            cfg.setClickHandler(new ChartClickHandler() {
                @Override
                public void onClick(ChartClickEvent event) {
                    rpc.onChartClick(event.getX(), event.getY());
                }
            });
        }
        if (getState().registeredEventListeners != null
                && getState().registeredEventListeners
                        .contains(POINT_CLICK_EVENT_ID)) {
            cfg.setSeriesPointClickHandler(new PointClickHandler() {

                @Override
                public void onClick(PointClickEvent event) {
                    HighchartPoint point = event.getPoint();
                    HighchartSeries series = point.getSeries();
                    int seriesIndex = getWidget().getSeriesIndex(series);
                    int pointIndex = series.indexOf(point);
                    rpc.onPointClick(event.getX(), event.getY(), seriesIndex,
                            event.getCategory(), pointIndex);
                }
            });
        }

        if (getState().registeredEventListeners != null
                && getState().registeredEventListeners
                        .contains(CHART_SELECTION_EVENT_ID)) {
            cfg.setChartSelectionHandler(new ChartSelectionHandler() {

                @Override
                public void onSelection(ChartSelectionEvent event) {
                    rpc.onSelection(event.getSelectionStart(),
                            event.getSelectionEnd());
                    event.preventDefault();
                }
            });
        }

        if (getState().registeredEventListeners != null
                && getState().registeredEventListeners
                        .contains(LEGENDITEM_CLICK_EVENT_ID)) {
            cfg.setLegendItemClickHandler(new LegendItemClickHandler() {

                @Override
                public void onClick(LegendItemClickEvent event) {
                    int seriesIndex = getWidget().getSeriesIndex(
                            event.getSeries());
                    rpc.onLegendItemClick(seriesIndex);
                    event.preventDefault();
                }
            });
        }

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                getWidget().init(cfg);

                // Add resize listener lazily here. If done in init like in
                // examples it will be called
                // way too early, like before the wiget is not even rendered yet
                if (resizeListener == null) {
                    resizeListener = new ElementResizeListener() {

                        @Override
                        public void onElementResize(ElementResizeEvent e) {
                            getWidget().updateSize();
                        }
                    };
                    getLayoutManager().addElementResizeListener(
                            getWidget().getElement(), resizeListener);
                }

                if (BrowserInfo.get().isIE()) {
                    // Workaround for Vaadin bug in IE (?), scrollbars...
                    ServerConnector parent2 = getParent();
                    if (parent2 instanceof WindowConnector) {
                        final WindowConnector w = (WindowConnector) parent2;
                        new Timer() {

                            @Override
                            public void run() {
                                VWindow widget2 = w.getWidget();
                                widget2.setWidth(widget2.getOffsetWidth() + "px");
                                widget2.setHeight(widget2.getOffsetHeight() + "px");
                            }
                        }.schedule(10);
                    }
                }
            }
        });
    }
}
