package com.bc.dom;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 15, 2016 1:08:53 PM
 */
public class DomatrixImpl implements Domatrix {

    private final NodeList nodeList;

    public DomatrixImpl(NodeList nodes) {
        this.nodeList = nodes;
    }
    
    @Override
    public Node getElementById(String id) {
        
        NodeList nodes = this.getElementsByAttribute("id", id);
        
        return nodes == null || nodes.isEmpty() ? Node.BLANK_NODE : nodes.get(0);
    }
    
    @Override
    public NodeList getElementsByClassName(String className) {
        
        return this.getElementsByAttribute("class", className);
    }
    
    @Override
    public NodeList getElementsByTagName(String nodeName) {
        
        TagNameFilter filter = new TagNameFilter(nodeName);
        
        NodeList output = this.nodeList.extractAllNodesThatMatch(filter, true);
        
        return output;
    }
    
    @Override
    public NodeList getElementsByTagName(String nodeName, String attributeName, String attributeValue) {
        
        TagNameFilter tagNameFilter = new TagNameFilter(nodeName);
        HasAttributeFilter hasAttributeFilter = new HasAttributeFilter(attributeName, attributeValue);
        NodeFilter filter = new AndFilter(tagNameFilter, hasAttributeFilter);
        
        NodeList output = this.nodeList.extractAllNodesThatMatch(filter, true);
        
        return output;
    }
    
    @Override
    public NodeList getElementsByAttribute(String attributeName, String attributeValue) {
        
        return this.getElements(new HasAttributeFilter(attributeName, attributeValue));
    }
    
    @Override
    public NodeList getElements(NodeFilter filter) {
        
        NodeList output = this.nodeList.extractAllNodesThatMatch(filter, true);
        
        return output;
    }
    
    @Override
    public NodeList getElements(){
        return this.nodeList;
    }
}
