package org.apache.jcs.engine;


/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.jcs.engine.behavior.IElementAttributes;

/**
 * Description of the Class
 *
 */
public class CacheGroup
{

    //private Atrributes attr;
    /** Description of the Field */
    public IElementAttributes attr;


    /** Constructor for the CacheGroup object */
    public CacheGroup() { }


    /**
     * Sets the attributes attribute of the CacheGroup object
     *
     * @param attr The new attributes value
     */
    public void setElementAttributes( IElementAttributes attr )
    {
        this.attr = attr;
    }


    /**
     * Gets the attrributes attribute of the CacheGroup object
     *
     * @return The attrributes value
     */
    public IElementAttributes getElementAttrributes()
    {
        return attr;
    }

}
