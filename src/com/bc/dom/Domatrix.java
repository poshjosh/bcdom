package com.bc.dom;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.util.NodeList;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 15, 2016 12:26:24 PM
 */
public interface Domatrix {

    NodeList getElements();
    
    Node getElementById(String id);
    
    NodeList getElementsByClassName(String className);
    
    NodeList getElementsByTagName(String nodeName);
    
    NodeList getElementsByTagName(String nodeName, String attributeName, String attributeValue);
    
    NodeList getElementsByAttribute(String attributeName, String attributeValue);
    
    NodeList getElements(NodeFilter filter);
}
