package com.asledgehammer.webcalc.testlib;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import cz.vutbr.web.css.StyleSheet;
import cz.vutbr.web.css.CSSFactory;

public class CSSJsoupExample {
    public static void main(String[] args) throws Exception {
        // 1. Load HTML document using Jsoup
        String html = "<html><head><style>.red { color: red; }</style></head>"
                    + "<body><p class='red'>Hello World</p></body></html>";
        Document doc = Jsoup.parse(html);

        // 2. Parse CSS using jStyleParser
        // You can parse from a string, file, or URL
        StyleSheet stylesheet = CSSFactory.parseString(".red { color: red; }", null);

        // 3. Compute styles for elements
        // jStyleParser can analyze the DOM and determine the final "computed" style
        // Note: jStyleParser works best with W3C DOM; some conversion may be needed
        // for advanced jsoup-to-W3C mapping.
        
        // Example: Finding elements and checking attributes with Jsoup
        for (Element link : doc.select(".red")) {
            System.out.println("Element text: " + link.text());
            // You would use jStyleParser's Analyzer here to get specific CSS values
        }
    }
}