/*
 * Copyright 2016 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bc.dom.metatags;

import java.util.Set;
import java.util.regex.Pattern;
import org.htmlparser.util.NodeList;
import com.bc.dom.HtmlPageDom;

/**
 * Extract Open Graph meta data
 * @see http://ogp.me/
 * @author Chinomso Bassey Ikwuagwu on Oct 21, 2016 11:43:34 AM
 */
public class OpenGraph extends AbstractMetadata {

    public OpenGraph(String url, NodeList nodeList) {
        super(url, nodeList, defaultAttributeNames(true));
    }

    public OpenGraph(HtmlPageDom dom) {
        super(dom, defaultAttributeNames(true));
    }

    private static String [] defaultAttributeNames(boolean lenient) {
        return lenient ? new String[]{"property", "name", "itemprop"} : new String[]{"property"};
    }
    
    private String title;
    @Override
    public String getTitle() {
        if(title == null) {
            title = this.getFirstMetaTagContent("og:title", null, false);
        }
        return title;
    }
    
    private String author;
    @Override
    public String getAuthor() {
        if(author == null) {
            author = this.getFirstMetaTagContent(null, false, "article:author", "book:author");
        }
        return author;
    }

    private String publisher;
    @Override
    public String getPublisher() {
        if(publisher == null) {
            publisher = this.getFirstMetaTagContent("og:site_name", null, false);
        }
        return publisher;
    }

    private String type;
    @Override
    public String getType() {
        if(type == null) {
            type = this.getFirstMetaTagContent("og:type", null, false);
        }
        return type;
    }

    private Set<String> tagSet;
    @Override
    public Set<String> getTagSet() {
        if(tagSet == null) {
            final boolean regex = true;
            tagSet = this.getStringSet(regex, Pattern.quote(":tag"));
        }
        return tagSet;
    }

    private Set<String> categorySet;
    @Override
    public Set<String> getCategorySet() {
        if(categorySet == null) {
            final boolean regex = true;
            categorySet = this.getStringSet(regex, Pattern.quote(":section"));
        }
        return categorySet;
    }
    
    private String description;
    @Override
    public String getDescription() {
        if(description == null) {
            description = this.getFirstMetaTagContent("og:description", null, false);
        }
        return description;
    }
    
    private String datePublished;
    @Override
    public String getDatePublished() {
        if(datePublished == null) {
            final boolean regex = true;
            datePublished = this.getFirstMetaTagContent(null, regex, 
                    Pattern.quote(":release_date"), Pattern.quote(":published_time"));
        }
        return datePublished;
    }
    
    private String dateModified;
    @Override
    public String getDateModified() {
        if(dateModified == null) {
            final String foundOnASiteButNotInSpecs = "og:updated_time";
            dateModified = this.getFirstMetaTagContent(null, false, "article:modified_time", foundOnASiteButNotInSpecs);
        }
        return dateModified;
    }
    
    private Set<String> imageUrls;
    @Override
    public Set<String> getImageUrls() {
        if(imageUrls == null) {
            imageUrls = this.getStringSet(false, 
                    "og:image:secure_url", "og:image", "og:image_url");
        }
        return imageUrls;
    }

    private String locale;
    @Override
    public String getLocale() {
        if(locale == null) {
            locale = this.getFirstMetaTagContent(null, false, "og:locale");
        }
        return locale;
    }
}
