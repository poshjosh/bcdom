package com.bc.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;

public class HtmlDocumentImpl extends DocumentImpl implements HtmlDocument, Serializable {

    private final String url;
    private final List<MetaTag> metaTags;
    private final Optional<MetaTag> robots;
    private final Optional<MetaTag> keywords;
    private final Optional<MetaTag> description;
    private final Optional<Tag> ico;
    private final Optional<Tag> icon;
    private final TitleTag title;
    private final BodyTag body;

    public HtmlDocumentImpl(String url, NodeList nodes) {
        super(nodes);
        this.url = url;
        
        NodeList metaNodes = nodes.extractAllNodesThatMatch(new TagNameFilter("META"), true);
        if(metaNodes == null || metaNodes.isEmpty()) {
            this.metaTags = Collections.EMPTY_LIST;
        }else{
            List<MetaTag> temp = new ArrayList();
            for(Node metaNode:metaNodes) {
                temp.add((MetaTag)metaNode);
            }
            this.metaTags = temp.isEmpty() ? Collections.EMPTY_LIST : Collections.unmodifiableList(temp);
        }
        
        MetaTag mrobots = null;
        MetaTag mkeywords = null;
        MetaTag mdescription = null;
        for (MetaTag metaTag : metaTags) {

            String name = metaTag.getAttributeValue("name");
            if ((mrobots == null) && ("robots".equals(name))) {
                mrobots = metaTag;
            } else if ((mkeywords == null) && ("keywords".equals(name))) {
                mkeywords = metaTag;
            } else if ((mdescription == null) && ("description".equals(name))) {
                mdescription = metaTag;
            }
        }
        
        this.robots = Optional.ofNullable(mrobots);
        this.keywords = Optional.ofNullable(mkeywords);
        this.description = Optional.ofNullable(mdescription);

        NodeList titles = nodes.extractAllNodesThatMatch(new NodeClassFilter(TitleTag.class), true);

        this.title = titles == null || titles.isEmpty() ? null : (TitleTag)titles.get(0);

        Tag mico = null;
        Tag micon = null;

        NodeList links = nodes.extractAllNodesThatMatch(new TagNameFilter("LINK"), true);
        for (Node node : links) {

            Tag link = (Tag)node;
            String rel = link.getAttributeValue("rel");
            if (rel != null)  {

                String lower = rel.toLowerCase().trim();
                
                if ((mico == null) && ("shortcut icon".equals(lower))) {
                    mico = link;
                } else if ((micon == null) && ("icon".equals(lower))) {
                    micon = link;
                }

                if ((mico != null) && (micon != null)) {
                    break;
                }
            }
        }
        
        this.ico = Optional.ofNullable(mico);
        this.icon = Optional.ofNullable(micon);

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
    public Optional<MetaTag> getRobots() {
        return this.robots;
    }

    @Override
    public Optional<MetaTag> getKeywords() {
        return this.keywords;
    }

    @Override
    public Optional<MetaTag> getDescription() {
        return this.description;
    }

    /**
     * @return Tag of signature &lt;link rel="shortcut icon" href="..."/>
     */    
    @Override
    public Optional<Tag> getIco(){
        return this.ico;
    }

    /**
     * @return Tag of signature &lt;link rel="icon" type="image/..." href="..."/>
     */    
    @Override
    public Optional<Tag> getIcon() {
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
