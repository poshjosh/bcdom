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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 22, 2016 3:00:21 PM
 */
public class MetadataChain implements Metadata {
    
    private transient static final Logger logger = Logger.getLogger(MetadataChain.class.getName());
    
    private final Collection<Metadata> delegates;

    public MetadataChain(Collection<Metadata> delegates) {
        this.delegates = Collections.unmodifiableCollection(delegates);
    }
    
    public MetadataChain(Metadata... delegates) {
        this.delegates = Arrays.asList(delegates);
    }
    
    @Override
    public String getTitle() {
        return (String)this.getFirstMethodValue("getTitle");
    }

    @Override
    public String getAuthor() {
        return (String)this.getFirstMethodValue("getAuthor");
    }

    @Override
    public String getPublisher() {
        return (String)this.getFirstMethodValue("getPublisher");
    }

    @Override
    public String getType() {
        return (String)this.getFirstMethodValue("getType");
    }

    @Override
    public String getTags() {
        return (String)this.getFirstMethodValue("getTags");
    }

    @Override
    public Set<String> getTagSet() {
        return (Set<String>)this.getAllMethodValues("getTagSet");
    }

    @Override
    public String getCategories() {
        return (String)this.getFirstMethodValue("getCategories");
    }

    @Override
    public Set<String> getCategorySet() {
        return (Set<String>)this.getAllMethodValues("getCategorySet");
    }

    @Override
    public String getKeywords() {
        return (String)this.getFirstMethodValue("getKeywords");
    }

    @Override
    public String getDescription() {
        return (String)this.getFirstMethodValue("getDescription");
    }

    @Override
    public String getContent() {
        return (String)this.getFirstMethodValue("getContent");
    }
    
    @Override
    public String getDate() {
        return (String)this.getFirstMethodValue("getDate");
    }

    @Override
    public String getDateCreated() {
        return (String)this.getFirstMethodValue("getDateCreated");
    }

    @Override
    public String getDatePublished() {
        return (String)this.getFirstMethodValue("getDatePublished");
    }

    @Override
    public String getDateModified() {
        return (String)this.getFirstMethodValue("getDateModified");
    }

    @Override
    public Set<String> getImageUrls() {
        return (Set<String>)this.getAllMethodValues("getImageUrls");
    }

    @Override
    public String getLocale() {
        return (String)this.getFirstMethodValue("getLocale");
    }

    private Object getFirstMethodValue(String methodName) {
        Object output = null;
        Iterator<Metadata> iter = delegates.iterator();
        while(iter.hasNext()) {
            Metadata delegate = iter.next();
            output = this.getFirstMethodValue(delegate, methodName);
            if(output instanceof Collection) {
                Collection collection = (Collection)output;
                if(!collection.isEmpty()) {
                    break;
                }
            }else{
                if(output != null) {
                    break;
                }
            }
        }
        return output;
    }    

    private Set<String> getAllMethodValues(String methodName) {
        Set<String> output = null;
        Iterator<Metadata> iter = delegates.iterator();
        while(iter.hasNext()) {
            Metadata delegate = iter.next();
            Collection<String> c = (Collection<String>)this.getFirstMethodValue(delegate, methodName);
            if(!c.isEmpty()) {
                if(output == null) {
                    output = new HashSet<>();
                }
                output.addAll(c);
            }
        }
        return output == null || output.isEmpty() ? Collections.EMPTY_SET : output;
    }    
    
    private Object getFirstMethodValue(Metadata delegate, String methodName) {
        Object output = null;
        Class type = delegate.getClass();
        try{
            output = type.getMethod(methodName).invoke(delegate);
        }catch(NoSuchMethodException | SecurityException | IllegalAccessException | 
                IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        
        if(logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "{0}#{1} = {2}", 
                    new Object[]{type.getSimpleName(), methodName, output});
        }
        
        return output;
    }    
}
