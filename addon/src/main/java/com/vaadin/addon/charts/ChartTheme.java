package com.vaadin.addon.charts;

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

import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaadin.addon.charts.client.ui.ChartThemeState;
import com.vaadin.addon.charts.model.AbstractConfigurationObject;
import com.vaadin.addon.charts.model.style.GradientColor;
import com.vaadin.addon.charts.model.style.Theme;
import com.vaadin.addon.charts.model.style.ThemeGradientColorSerializer;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Extension;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * The ChartTheme extension configures a page local theme for charts. With this
 * extension it is possible to configure e.g. default colors used by all Chart
 * objects displayed in the UI.
 */
public class ChartTheme extends AbstractExtension {

    protected ChartTheme() {
    }

    private void notifyListeners() {
        UI ui = getUI();

        if (ui == null) {
            return;
        }

        searchAndNotifyListeners(ui);
    }

    private void searchAndNotifyListeners(Component component) {
        if (component instanceof HasComponents) {
            HasComponents container = (HasComponents) component;
            Iterator<Component> iter = container.iterator();
            while (iter.hasNext()) {
                searchAndNotifyListeners(iter.next());
            }
        } else if (component instanceof Chart) {
            Chart listener = (Chart) component;
            listener.drawChart();
        }
    }

    final static Gson gson;
    static {
        // .serializeNulls()
        GsonBuilder builder = AbstractConfigurationObject.createGsonBuilder();
        builder.registerTypeHierarchyAdapter(GradientColor.class,
                new ThemeGradientColorSerializer());
        gson = builder.create();
    }

    /**
     * Sets the theme to use.
     * <p>
     * Note that if the view is already drawn, all existing {@link Chart}s will
     * be redrawn.
     * 
     * @param theme
     */
    public void setTheme(Theme theme) {
        getState().json = gson.toJson(theme);
        notifyListeners();
    }

    @Override
    protected ChartThemeState getState() {
        return (ChartThemeState) super.getState();
    }

    void extendConnector(AbstractClientConnector connector) {
        super.extend(connector);
    }

    /**
     * Returns a ChartTheme extension for the given UI. If a ChartTheme
     * extension has not yet been added, a new one is created and added.
     * 
     * @param ui
     *            the UI for which the ChartTheme should be returned
     * @return the ChartTheme instance connected to the given UI
     */
    public static ChartTheme get(UI ui) {
        ChartTheme themer = null;

        // Search singleton themer
        for (Extension extension : ui.getExtensions()) {
            if (extension instanceof ChartTheme) {
                themer = (ChartTheme) extension;
                break;
            }
        }

        // Create new themer if not found
        if (themer == null) {
            themer = new ChartTheme();
            themer.extendConnector(ui);
        }

        return themer;

    }

    /**
     * Returns a ChartTheme extension for the current UI. If a ChartTheme
     * extension has not yet been added, a new one is created and added.
     * 
     * @return a ChartTheme instance connected to the currently active UI
     */
    public static ChartTheme get() {
        UI ui = UI.getCurrent();

        if (ui == null) {
            throw new IllegalStateException(
                    "This method must be used from UI thread");
        }
        return get(ui);
    }

}
