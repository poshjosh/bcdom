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
import org.htmlparser.util.NodeList;
import com.bc.dom.HtmlDocument;

/**
 * Extract some <code>http://schema.org/CreativeWork</code> meta data
 * @see http://schema.org/CreativeWork
 * @see http://schema.org/Thing
 * @see http://schema.org/docs/schemas.html
 * @see http://schema.org/docs/full.html
 * @author Chinomso Bassey Ikwuagwu on Oct 22, 2016 4:53:15 PM
 */
public class SchemaCreativeWork extends SchemaThing {

    public SchemaCreativeWork(String url, NodeList nodeList) {
        super(url, nodeList);
    }

    public SchemaCreativeWork(HtmlDocument dom) {
        super(dom);
    }

    private String title;
    @Override
    public String getTitle() {
        if(title == null) {
            title = this.getFirstMetaTagContent(null, false, "headline", "name");
        }
        return title;
    }
    
    private String keywords;
    @Override
    public String getKeywords() {
        if(keywords == null) {
            keywords = this.getFirstMetaTagContent("keywords", null, false);
        }
        return keywords;
    }

    private String author;
    @Override
    public String getAuthor() {
        if(author == null) {
            author = this.getFirstMetaTagContent(null, false, "author", "creator");
        }
        return author;
    }
    
    private String publisher;
    @Override
    public String getPublisher() {
        if(publisher == null) {
            publisher = this.getFirstMetaTagContent(null, false, "publisher", "sourceOrganization", "producer");
        }
        return publisher;
    }
    
    private String dateCreated;
    @Override
    public String getDateCreated() {
        if(dateCreated == null) {
            dateCreated = this.getFirstMetaTagContent(null, false, "dateCreated");
        }
        return dateCreated;
    }
    
    private String datePublished;
    @Override
    public String getDatePublished() {
        if(datePublished == null) {
            datePublished = this.getFirstMetaTagContent(null, false, "datePublished");
        }
        return datePublished;
    }
    
    private String dateModified;
    @Override
    public String getDateModified() {
        if(dateModified == null) {
            dateModified = this.getFirstMetaTagContent(null, false, "dateModified");
        }
        return dateModified;
    }

    private Set<String> imageUrls;
    @Override
    public Set<String> getImageUrls() {
        if(imageUrls == null) {
            imageUrls = this.getStringSet(false, "image", "thumbnailUrl");
        }
        return imageUrls;
    }
}
