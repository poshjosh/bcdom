package com.bc.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;

public class HtmlPageDomImpl extends DomatrixImpl implements HtmlPageDom, Serializable {

    private final String url;
    private final List<MetaTag> metaTags;
    private MetaTag robots;
    private MetaTag keywords;
    private MetaTag description;
    private Tag ico;
    private Tag icon;
    private final TitleTag title;
    private final BodyTag body;

    public HtmlPageDomImpl(String url, NodeList nodes) {
        super(nodes);
        this.url = url;
        
        NodeList metaNodes = nodes.extractAllNodesThatMatch(new TagNameFilter("META"), true);
        if(metaNodes == null || metaNodes.isEmpty()) {
            this.metaTags = null;
        }else{
            List<MetaTag> temp = new ArrayList();
            for(Node metaNode:metaNodes) {
                temp.add((MetaTag)metaNode);
            }
            this.metaTags = temp.isEmpty() ? Collections.EMPTY_LIST : Collections.unmodifiableList(temp);
        }
        
        for (MetaTag metaTag : metaTags) {

            String name = metaTag.getAttributeValue("name");
            if ((this.robots == null) && ("robots".equals(name))) {
                this.robots = metaTag;
            } else if ((this.keywords == null) && ("keywords".equals(name))) {
                this.keywords = metaTag;
            } else if ((this.description == null) && ("description".equals(name))) {
                this.description = metaTag;
            }
        }

        NodeList titles = nodes.extractAllNodesThatMatch(new NodeClassFilter(TitleTag.class), true);

        this.title = titles == null || titles.isEmpty() ? null : (TitleTag)titles.get(0);

        NodeList links = nodes.extractAllNodesThatMatch(new TagNameFilter("LINK"), true);
        for (Node node : links) {

            Tag link = (Tag)node;
            String rel = link.getAttributeValue("rel");
            if (rel != null)  {

                String lower = rel.toLowerCase().trim();
                if ((this.ico == null) && ("shortcut icon".equals(lower))) {
                    this.ico = link;
                } else if ((this.icon == null) && ("icon".equals(lower))) {
                    this.icon = link;
                }

                if ((this.ico != null) && (this.icon != null)) {
                    break;
                }
            }
        }

        NodeList bodies = nodes.extractAllNodesThatMatch(new NodeClassFilter(BodyTag.class), true);

        this.body = bodies == null || bodies.isEmpty() ? null : (BodyTag)bodies.get(0);
    }
    
    @Override
    public List<MetaTag> getMetaTags(NodeFilter filter) {
        List<MetaTag> output = null;
        for(MetaTag metaTag : this.metaTags) {
            if(filter.accept(metaTag)) {
                if(output == null) {
                    output = new ArrayList<>();
                }
                output.add(metaTag);
            }
        }
        return output == null ? Collections.EMPTY_LIST : output;
    }
    
    @Override
    public String getURL() {
        return this.url;
    }

    @Override
    public MetaTag getRobots() {
        return this.robots;
    }

    @Override
    public MetaTag getKeywords() {
        return this.keywords;
    }

    @Override
    public MetaTag getDescription() {
        return this.description;
    }

    /**
     * @return Tag of signature &lt;link rel="shortcut icon" href="..."/>
     */    
    @Override
    public Tag getIco(){
        return this.ico;
    }

    /**
     * @return Tag of signature &lt;link rel="icon" type="image/..." href="..."/>
     */    
    @Override
    public Tag getIcon() {
        return this.icon;
    }

    @Override
    public TitleTag getTitle() {
        return this.title;
    }

    @Override
    public BodyTag getBody() {
        return this.body;
    }

    @Override
    public List<MetaTag> getMetaTags() {
        return this.metaTags;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName());
        builder.append("{URL=").append(this.url);
        builder.append(", Elements=").append(this.getElements() == null ? null : this.getElements().size());
        builder.append('}');
        return builder.toString();
    }
}
