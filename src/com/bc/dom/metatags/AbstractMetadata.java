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

import com.bc.dom.HtmlDocumentImpl;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import org.htmlparser.Attribute;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasAttributeRegexFilter;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;
import com.bc.dom.HtmlDocument;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 21, 2016 11:46:33 AM
 */
public class AbstractMetadata implements Metadata {
//@todo add getGeo or getPlacename,getPosition,getRegion,getICBM
//<meta name="geo.placename" content="Lagos island, Lagos, Nigeria"/>
//<meta name="geo.position" content="6.4548790;3.4245980"/>
//<meta name="geo.region" content="NG-Lagos"/>

    private final HtmlDocument dom;
    
    private final List<String> attributeNames;

    public AbstractMetadata(String url, NodeList nodeList) {
        this(url, nodeList, defaultAttributeNames());
    }
    
    public AbstractMetadata(String url, NodeList nodeList, String [] defaultAttributeNames) {
        this(new HtmlDocumentImpl(url, nodeList), defaultAttributeNames);
    }
    
    public AbstractMetadata(HtmlDocument dom) {
        this(dom, defaultAttributeNames());
    }
    
    public AbstractMetadata(HtmlDocument dom, String [] defaultAttributeNames) {
        this.dom = Objects.requireNonNull(dom);
        this.attributeNames = Arrays.asList(requireNonNullOrEmpty(defaultAttributeNames));
    }

    private static String [] defaultAttributeNames() {
        return new String[]{"property", "itemprop", "name"};
    }
    
    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public String getPublisher() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getTags() {
        return this.toPlainString(this.getTagSet(), null);
    }

    @Override
    public Set<String> getTagSet() {
        return Collections.EMPTY_SET;
    }

    @Override
    public String getCategories() {
        return this.toPlainString(this.getCategorySet(), null);
    }

    @Override
    public Set<String> getCategorySet() {
        return Collections.EMPTY_SET;
    }

    @Override
    public String getKeywords() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getContent() {
        return null;
    }
    
    @Override
    public String getDate() {
        String date = this.getDateCreated();
        if(date == null) {
            date = this.getDatePublished();
            if(date == null) {
                date = this.getDateModified();
            }
        }
        return date;
    }

    @Override
    public String getDateCreated() {
        return null;
    }

    @Override
    public String getDatePublished() {
        return null;
    }

    @Override
    public String getDateModified() {
        return null;
    }

    @Override
    public Set<String> getImageUrls() {
        return Collections.EMPTY_SET;
    }
    
    @Override
    public String getLocale() {
        return null;
    }

    public Set<String> getStringSet(boolean regex, String... attributeValues) {
        List<String> contents = this.getMetaTagContents(regex, attributeValues);
        Set<String> output = contents == null || contents.isEmpty() ? Collections.EMPTY_SET : 
                Collections.unmodifiableSet(new HashSet(contents));
        return output;
    }

    public String getFirstMetaTagContent(String outputIfNone, boolean regex, String... attributeValues) {
        String output = null;
        if(attributeValues != null && attributeValues.length != 0) {
            for(String attributeName : this.attributeNames) {
                Attribute [] attributes = new Attribute[attributeValues.length];
                for(int i=0; i<attributes.length; i++) {
                    attributes[i] = new Attribute(attributeName, attributeValues[i]);
                }
                output = this.getFirstMetaTagContent(null, regex, attributes);
                if(output != null) {
                    break;
                }
            }
        }
        return output == null ? outputIfNone : output;
    }
    
    public String getFirstMetaTagContent(String outputIfNone, boolean regex, Attribute... attributes) {
        List<String> contents = this.getMetaTagContents(regex, attributes);
        return this.getFirst(contents, outputIfNone);
    }

    public List<String> getMetaTagContents(boolean regex, String... attributeValues) {
        List<String> output = null;
        if(attributeValues != null && attributeValues.length != 0) {
            for(String attributeName : this.attributeNames) {
                Attribute [] attributes = new Attribute[attributeValues.length];
                for(int i=0; i<attributes.length; i++) {
                    attributes[i] = new Attribute(attributeName, attributeValues[i]);
                }
                output = this.getMetaTagContents(regex, attributes);
                if(!output.isEmpty()) {
                    break;
                }
            }
        }
        return output == null || output.isEmpty() ? Collections.EMPTY_LIST : output;
    }
    
    public List<String> getMetaTagContents(boolean regex, Attribute... attributes) {
        List<String> output = null;
        if(attributes != null && attributes.length != 0) {
            HasAttributeFilter filter = this.getAttributeFilter(null, null, regex);
            for(Attribute attribute : attributes) {
                filter.setAttributeName(attribute.getName());
                filter.setAttributeValue(attribute.getValue());
                List<String> contents = this.getMetaTagContents(filter);
                if(contents.isEmpty()) {
                    continue;
                }
                if(output == null) {
                    output = new ArrayList<>();
                }
                output.addAll(contents);
            }
        }
        return output == null || output.isEmpty() ? Collections.EMPTY_LIST : output;
    }

    public String getFirstMetaTagContent(String attributeValue, String outputIfNone, boolean regex) {
        String output = null;
        HasAttributeFilter filter = this.getAttributeFilter(null, attributeValue, regex);
        filter.setAttributeValue(attributeValue);
        for(String attributeName : this.attributeNames) {
            filter.setAttributeName(attributeName);
            output = this.getFirstMetaTagContent(filter, null);
            if(output != null) {
                break;
            }
        }
        return output == null ? outputIfNone : output;
    }
    
    public String getFirstMetaTagContent(String attributeName, String attributeValue, String outputIfNone, boolean regex) {
        NodeFilter filter = this.getAttributeFilter(attributeName, attributeValue, regex);
        return this.getFirstMetaTagContent(filter, outputIfNone);
    }

    public String getFirstMetaTagContent(NodeFilter filter, String outputIfNone) {
        List<String> contents = this.getMetaTagContents(filter);
        return this.getFirst(contents, outputIfNone);
    }
    
    public List<String> getMetaTagContents(String attributeValue, boolean regex) {
        List<String> output = null;
        HasAttributeFilter filter = this.getAttributeFilter(null, attributeValue, regex);
        filter.setAttributeValue(attributeValue);
        for(String attributeName : this.attributeNames) {
            filter.setAttributeName(attributeName);
            output = this.getMetaTagContents(filter);
            if(!output.isEmpty()) {
                break;
            }
        }
        return output == null || output.isEmpty() ? Collections.EMPTY_LIST : output;
    }

    public List<String> getMetaTagContents(String attributeName, String attributeValue, boolean regex) {
        NodeFilter filter = this.getAttributeFilter(attributeName, attributeValue, regex);
        return this.getMetaTagContents(filter);
    }
    
    public List<String> getMetaTagContents(NodeFilter filter) {
        List<String> output = null;
        List<MetaTag> metaTags = dom.getMetaTags();
        for(MetaTag metaTag : metaTags) {
            if(!filter.accept(metaTag)) {
                continue;
            }
            String content = this.getAttributeValue(metaTag, "content", null);
            if(content == null) {
                continue;
            }
            if(output ==  null) {
                output = new ArrayList<>();
            }
            output.add(content);
        }
        return output == null || output.isEmpty() ? Collections.EMPTY_LIST : output;
    }

    public MetaTag getFirstMetaTag(String attributeValue, MetaTag outputIfNone, boolean regex) {
        MetaTag output = null;
        HasAttributeFilter filter = this.getAttributeFilter(null, attributeValue, regex);
        filter.setAttributeValue(attributeValue);
        for(String attributeName : this.attributeNames) {
            filter.setAttributeName(attributeName);
            output = this.getFirstMetaTag(filter, null);
            if(output != null) {
                break;
            }
        }
        return output == null ? outputIfNone : output;
    }

    public MetaTag getFirstMetaTag(String attributeName, String attributeValue, 
            MetaTag outputIfNone, boolean regex) {
        NodeFilter filter = this.getAttributeFilter(attributeName, attributeValue, regex);
        return this.getFirstMetaTag(filter, outputIfNone);
    }
    
    public MetaTag getFirstMetaTag(NodeFilter filter, MetaTag outputIfNone) {
        MetaTag output = outputIfNone;
        List<MetaTag> metaTags = this.dom.getMetaTags();
        for(MetaTag metaTag : metaTags) {
            if(filter.accept(metaTag)) {
                output = metaTag;
                break;
            }
        }
        return output;
    }
    
    public HasAttributeFilter getAttributeFilter(String attributeName, String attributeValue, boolean regex) {
        HasAttributeFilter filter = !regex ? new HasAttributeFilter() : new HasAttributeRegexFilter();
        filter.setAttributeName(attributeName);
        filter.setAttributeValue(attributeValue);
        return filter;
    }

    public <E> E getFirst(Collection<E> c, E outputIfNone) {
        E first = c == null || c.isEmpty() ? outputIfNone : c.iterator().next();
        return first == null ? outputIfNone : first;
    }
    
    public String getAttributeValue(Tag tag, String attributeName, String outputIfNone) {
        final String attributeValue = tag.getAttributeValue(attributeName); 
        return attributeValue == null ? outputIfNone : attributeValue;
    }

    public String toPlainString(Collection<String> c, String outputIfNone) {
        String output;
        if(c == null || c.isEmpty()) {
            output = outputIfNone;
        }else{
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for(String s:c) {
                builder.append(s);
                if(i < c.size() -1) {
                    builder.append(',').append(' ');
                }
                ++i;
            }
            output = builder.toString();
        }
        return output == null ? outputIfNone : output;
    }

    /**
     * <p>Source: http://www.java2s.com/Code/Java/Data-Type/ConvertsaStringtoaLocale.htm</p>
     * 
     * <p>Converts a String to a Locale.</p>
     *
     * <p>This method takes the string format of a locale and creates the
     * locale object from it.</p>
     *
     * <pre>
     *   LocaleUtils.toLocale("en")         = new Locale("en", "")
     *   LocaleUtils.toLocale("en_GB")      = new Locale("en", "GB")
     *   LocaleUtils.toLocale("en_GB_xxx")  = new Locale("en", "GB", "xxx")   (#)
     * </pre>
     *
     * <p>(#) The behaviour of the JDK variant constructor changed between JDK1.3 and JDK1.4.
     * In JDK1.3, the constructor upper cases the variant, in JDK1.4, it doesn't.
     * Thus, the result from getVariant() may vary depending on your JDK.</p>
     *
     * <p>This method validates the input strictly.
     * The language code must be lowercase.
     * The country code must be uppercase.
     * The separator must be an underscore.
     * The length must be correct.
     * </p>
     *
     * @param str  the locale String to convert, null returns null
     * @return a Locale, null if null input
     * @throws ParseException if the string is an invalid format
     */
    public Locale toLocale(String str) throws ParseException {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len != 2 && len != 5 && len < 7) {
            throw new ParseException("Invalid locale format: " + str, 0);
        }
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
            throw new ParseException("Invalid locale format: " + str, 0);
        }
        if (len == 2) {
            return new Locale(str, "");
        } else {
            if (str.charAt(2) != '_') {
                throw new ParseException("Invalid locale format: " + str, 2);
            }
            char ch3 = str.charAt(3);
            if (ch3 == '_') {
                return new Locale(str.substring(0, 2), "", str.substring(4));
            }
            char ch4 = str.charAt(4);
            if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
                throw new ParseException("Invalid locale format: " + str, 4);
            }
            if (len == 5) {
                return new Locale(str.substring(0, 2), str.substring(3, 5));
            } else {
                if (str.charAt(5) != '_') {
                    throw new ParseException("Invalid locale format: " + str, 5);
                }
                return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
            }
        }
    }
    
    private String [] requireNonNullOrEmpty(String... arr) {
        if(arr.length == 0) {
            throw new IllegalArgumentException();
        }
        return arr;
    }

    public final HtmlDocument getDom() {
        return dom;
    }

    public final List<String> getAttributeNames() {
        return attributeNames;
    }
}
