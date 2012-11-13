package com.vaadin.addon.charts.model;

import com.vaadin.addon.charts.model.style.Style;

/**
 * Abstract base for Title classes (namely Title and Subtitle)
 */
public abstract class AbstractTitle extends AbstractConfigurationObject {
    private Boolean floating;
    private VerticalAlign verticalAlign;
    private HorizontalAlign align;
    private String text;
    private Style style;
    private Number x;
    private Number y;

    /**
     * Default constructor
     */
    public AbstractTitle() {

    }

    /**
     * Construct title with given text
     * 
     * @param text
     */
    public AbstractTitle(String text) {
        this.text = text;
    }

    /**
     * When the title is floating, the plot area will not move to make space for
     * it. Defaults to false.
     * 
     * @return the floating
     */
    public boolean isFloating() {
    	return floating == null ? false : floating;
    }

    /**
     * @see #getFloating()
     * 
     * @param floating
     *            the floating to set
     */
    public void setFloating(Boolean floating) {
        this.floating = floating;
    }

    /**
     * The vertical alignment of the title. Can be one of TOP, MIDDLE and
     * BOTTOM. Defaults to TOP.
     * 
     * @return the verticalAlign
     */
    public VerticalAlign getVerticalAlign() {
        return verticalAlign;
    }

    /**
     * @see #getVerticalAlign()
     * @param verticalAlign
     *            the align to set
     */
    public void setVerticalAlign(VerticalAlign verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    /**
     * The horizontal alignment of the title. Can be one of LEFT, CENTER and
     * RIGHT. Defaults to CENTER.
     * 
     * @return the horizontalAlign
     */
    public HorizontalAlign getHorizontalAlign() {
        return align;
    }

    /**
     * @see #getHorizontalAlign()
     * @param horizontalAlign
     *            the align to set
     */
    public void setHorizontalAlign(HorizontalAlign horizontalAlign) {
        align = horizontalAlign;
    }

    /**
     * The title of the chart. To disable the title, set the text to null.
     * Defaults to "Chart title".
     * 
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * The title of the chart. To disable the title, set the text to null.
     * Defaults to "Chart title".
     * 
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * CSS styles for the title. Use this for font styling, but use align, x and
     * yfor text alignment.
     * 
     * @return the style
     */
    public Style getStyle() {
        return style;
    }

    /**
     * @see #getStyle()
     * @param style
     *            the style to set
     */
    public void setStyle(Style style) {
        this.style = style;
    }

    /**
     * The x position of the title relative to the alignment within
     * chart.spacingLeft and chart.spacingRight. Defaults to 0.
     * 
     * @return the x
     */
    public Number getX() {
        return x;
    }

    /**
     * @see #getX()
     * @param x
     *            the x to set
     */
    public void setX(Number x) {
        this.x = x;
    }

    /**
     * The y position of the title relative to the alignment within
     * chart.spacingTop and chart.spacingBottom. Defaults to 15.
     * 
     * @return the y
     */
    public Number getY() {
        return y;
    }

    /**
     * @see #getY()
     * @param y
     *            the y to set
     */
    public void setY(Number y) {
        this.y = y;
    }

}
