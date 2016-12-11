package com.bc.dom;

import java.util.List;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.TitleTag;

public interface HtmlPageDom extends Domatrix {
    
    String getURL();

    List<MetaTag> getMetaTags();
    
    List<MetaTag> getMetaTags(NodeFilter filter);

    MetaTag getRobots();

    MetaTag getKeywords();

    MetaTag getDescription();

    /**
     * @return Tag of signature &lt;link rel="shortcut icon" href="..."/>
     */    
    Tag getIco();

    /**
     * @return Tag of signature &lt;link rel="icon" type="image/..." href="..."/>
     */    
    Tag getIcon();

    TitleTag getTitle();

    BodyTag getBody();
}
