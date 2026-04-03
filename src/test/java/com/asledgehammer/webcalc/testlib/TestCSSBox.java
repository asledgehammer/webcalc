package com.asledgehammer.webcalc.testlib;

import cz.vutbr.web.css.MediaSpec;
import org.fit.cssbox.awt.BrowserCanvas;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DefaultDocumentSource;
import org.fit.cssbox.io.DocumentSource;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.URL;

public class TestCSSBox {
    public static void main(String[] args) throws Exception {

        File xmlFile = new File("src/test/resources/test/blank.html");
        String homeDirS = "file:///" + new File("").getAbsolutePath().replaceAll("\\\\", "/") + "/src/test/resources/test/";
        System.out.println(homeDirS);
        URL homeDir = new URL(homeDirS);

        // Open the network connection
        DocumentSource docSource = new DefaultDocumentSource(xmlFile.toURI().toURL());

        // Parse the input document
        DOMSource parser = new DefaultDOMSource(docSource);
        Document doc = parser.parse(); //doc represents the obtained DOM

        //we will use the "screen" media type for rendering
        MediaSpec media = new MediaSpec("screen");

        //specify some media feature values
        media.setDimensions(1000, 800); //set the visible area size in pixels
        media.setDeviceDimensions(1600, 1200); //set the display size in pixels

        //use the media specification in the analyzer
        DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
//        da.setMediaSpec(media);
        da.attributesToStyles();
        da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
        da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
        da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); //(optional) use the forms style sheet
        da.getStyleSheets();

        SwingUtilities.invokeLater(() -> {

            Dimension size = new java.awt.Dimension(800, 600);

            JFrame frame = new JFrame("CSSBox: Test");
            frame.setSize(size);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            BrowserCanvas browser = new BrowserCanvas(da.getRoot(), da, homeDir);
            browser.createLayout(size, frame.getBounds());
            System.out.println("browser: " + browser);

            frame.add(browser);
            frame.pack();
            frame.setVisible(true);
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    Dimension size = frame.getSize();
                    browser.createLayout(new org.fit.cssbox.layout.Dimension(size.width - 16, size.height - 16));
                }
            });

        });

    }

    class CSSBoxFrame extends JFrame {

    }
}
