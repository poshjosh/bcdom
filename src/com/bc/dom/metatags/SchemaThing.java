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
 * Extract some <code>http://schema.org/Thing</code> meta data
 * @see http://schema.org/Thing
 * @see http://schema.org/docs/schemas.html
 * @see http://schema.org/docs/full.html
 * @author Chinomso Bassey Ikwuagwu on Oct 22, 2016 4:19:16 PM
 */
public class SchemaThing extends AbstractMetadata {

    public SchemaThing(String url, NodeList nodeList) {
        super(url, nodeList, defaultAttributeNames());
    }

    public SchemaThing(HtmlDocument dom) {
        super(dom, defaultAttributeNames());
    }

    private static String [] defaultAttributeNames() {
        return new String[]{"itemprop"};
    }
    
    private String title;
    @Override
    public String getTitle() {
        if(title == null) {
            title = this.getFirstMetaTagContent("name", null, false);
        }
        return title;
    }
    
    private Set<String> imageUrls;
    @Override
    public Set<String> getImageUrls() {
        if(imageUrls == null) {
            imageUrls = this.getStringSet(false, "image");
        }
        return imageUrls;
    }


    private String description;
    @Override
    public String getDescription() {
        if(description == null) {
            description = this.getFirstMetaTagContent("description", null, false);
        }
        return description;
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
}
