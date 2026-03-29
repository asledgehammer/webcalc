package com.asledgehammer.webcalc.io;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;

public class DocumentReader {

  private DocumentReader() {
    throw new RuntimeException("Utility class");
  }

  public static void parseHTMLDocument(File file)
      throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(true);
    factory.setIgnoringElementContentWhitespace(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setErrorHandler(new DefaultHandler());
    Document doc = builder.parse(file);
    //    System.out.println(doc.getDocumentElement().getNodeName());
    print(doc);
  }

  private static void print(Document doc) {
    recursivePrint(doc.getChildNodes(), 0);
  }

  private static void recursivePrint(NodeList nodeList, int indent) {
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      System.out.println("  ".repeat(indent) + node.getNodeName() + " (" + node.getClass().getName() + ")");
      recursivePrint(node.getChildNodes(), indent + 1);
    }
  }

  public static void main(String[] args) throws Exception {
    parseHTMLDocument(
        new File(
            "src"
                + File.separator
                + "main"
                + File.separator
                + "resources"
                + File.separator
                + "test"
                + File.separator
                + "blank.html"));
  }
}
