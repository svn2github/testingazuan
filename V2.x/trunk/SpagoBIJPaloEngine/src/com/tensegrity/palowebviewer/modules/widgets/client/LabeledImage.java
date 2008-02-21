package com.tensegrity.palowebviewer.modules.widgets.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SourcesClickEvents;


/**
 * Label that contains not only text but picture too. It also can reciev click events.
 *
 * CSS:
 * <ol> 
 * <li>tensegrity-gwt-widgets-labeledimage - label<li>
 * <li>tensegrity-gwt-widgets-labeledimage-icon - icon<li>
 * </ol>
 */
public class LabeledImage extends Composite implements SourcesClickEvents
{

    public static final int RIGHT = 1;
    public static final int LEFT = 2;

    private HorizontalPanel panel;
    private FocusPanel focusPanel;
    private Hyperlink image;
    private HTML label;

    public void addClickListener(ClickListener listener) {
        image.addClickListener(listener);
        label.addClickListener(listener);
        //focusPanel.addClickListener(listener);
    }
    
    public void setText(String value) {
    	label.setText(value);
    }

    public void setHTML(String value) {
    	label.setHTML(value);
    }

    public void removeClickListener(ClickListener listener) {
        image.removeClickListener(listener);
        label.removeClickListener(listener);
        //focusPanel.removeClickListener(listener);
    }


    public void setStyleName(String name){
        super.setStyleName(name);
        image.setStyleName(name+"-icon");
    }

    public void addStyleName(String name){
        super.addStyleName(name);
        image.addStyleName(name+"-icon");
    }
    
    public void removeStyleName(String name){
        super.removeStyleName(name);
        image.removeStyleName(name+"-icon");
    }

    public LabeledImage (String style, String text) {
        this(style, text, RIGHT);
    }

    public LabeledImage (String style, String text, int textPosition) {
        this.panel = new HorizontalPanel();
        this.image = new Hyperlink();
        this.label = new HTML();
        setText(text);
        panel.setCellVerticalAlignment(image, HorizontalPanel.ALIGN_MIDDLE);
        panel.setSpacing(0);
        focusPanel = new FocusPanel(panel);
        this.initWidget(focusPanel);
        setStyleName("tensegrity-gwt-widgets-labeledimage");
        if(style!= null)
            addStyleName(style);
        addImageAndLabel(textPosition);
    }

	private void addImageAndLabel(int textPosition) {
		switch(textPosition) {
        case RIGHT: {
                panel.add(image);
                panel.add(label);
                break;
            }
        case LEFT: {
                panel.add(label);
                panel.add(image);
                break;
            }
        }
	}

}
