package com.asledgehammer.webcalc.testlib;

import cz.vutbr.web.css.*;
import cz.vutbr.web.domassign.StyleMap;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTMLTest {

    public static void printDocument(Document doc, OutputStream out) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(doc),
                new StreamResult(new OutputStreamWriter(out, StandardCharsets.UTF_8)));
    }

    private static void setAllIds(Document doc) {
        NodeList nodeList = doc.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if(element.hasAttribute("id")) {
                    System.out.println(i + ": " + node + " id=" + element.getAttribute("id"));
                    element.setIdAttribute("id", true);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        // 3. Parse the XML file into a Document object
        File xmlFile = new File("src/test/resources/test/blank.html");
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(xmlFile, "UTF-8");

        System.out.println("pre: " + jsoupDoc.getElementById("search"));

        // Convert Jsoup Document to org.w3c.dom.Document
        Document document = W3CDom.convert(jsoupDoc);
        document.getDocumentElement().normalize();
        setAllIds(document);

        // get the style of a single element
        Element div = document.getElementById("search"); //choose a DOM element
        System.out.println("#search: " + div);

        String homeDirS = "file:///" + new File("").getAbsolutePath().replaceAll("\\\\", "/") + "/src/test/resources/test/";
        System.out.println(homeDirS);
        URL homeDir = URI.create(homeDirS).toURL();
        MediaSpec media = new MediaSpecAll();
        StyleMap map = CSSFactory.assignDOM(document, "UTF-8", homeDir, media, true);

        NodeData style = map.get(div);
        CSSProperty.Color c = style.getProperty("color");
        if (c.getValueType() == CSSProperty.ValueType.SIMPLE) {
            TermColor term = style.getValue(TermColor.class, "color", true);
            System.out.println("color=\"" + term + "\" " + term.getClass());
        }

    }
}
