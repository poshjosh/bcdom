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

import com.bc.dom.Dom;
import org.htmlparser.util.NodeList;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 22, 2016 4:38:51 PM
 */
public class BasicMetadata extends AbstractMetadata {

    public BasicMetadata(String url, NodeList nodeList) {
        super(url, nodeList, defaultAttributeNames());
    }

    public BasicMetadata(Dom dom) {
        super(dom, defaultAttributeNames());
    }

    private static String [] defaultAttributeNames() {
        return new String[]{"name"};
    }
    
    private String author;
    @Override
    public String getAuthor() {
        if(author == null) {
            author = this.getFirstMetaTagContent("author", null, false);
        }
        return author;
    }

    private String keywords;
    @Override
    public String getKeywords() {
        if(this.keywords == null) {
            final String foundOnSiteButNotInSpecs = "news_keywords";
            this.keywords = this.getFirstMetaTagContent(null, false, "keywords", foundOnSiteButNotInSpecs);
        }
        return this.keywords;
    }
    
    private String description;
    @Override
    public String getDescription() {
        if(description == null) {
            description = this.getFirstMetaTagContent("description", null, false);
        }
        return description;
    }
    
}
