package org.apache.jcs.auxiliary.disk;


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


import java.io.Serializable;

import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.IElementAttributes;

/**
 * Wrapper for cache elements in purgatory. Elements are stored in purgatory
 * when they are spooled to the auxilliary cache, but have not yet been
 * written to disk.
 *
 * @version $Id$
 */
public class PurgatoryElement implements ICacheElement, Serializable
{
    /**
     * Is the element ready to be spooled?
     */
    protected boolean spoolable = false;

    /**
     * Wrapped cache Element
     */
    protected ICacheElement cacheElement;

    /**
     * Constructor for the PurgatoryElement object
     *
     * @param cacheElement CacheElement to wrap.
     */
    public PurgatoryElement( ICacheElement cacheElement )
    {
        this.cacheElement = cacheElement;
    }

    /**
     * Gets the spoolable property.
     *
     * @return The spoolable value
     */
    public boolean isSpoolable()
    {
        return spoolable;
    }

    /**
     * Sets the spoolable property.
     *
     * @param spoolable The new spoolable value
     */
    public void setSpoolable( boolean spoolable )
    {
        this.spoolable = spoolable;
    }

    /**
     * Get the wrapped cache element.
     */
    public ICacheElement getCacheElement()
    {
        return cacheElement;
    }

    // ------------------------------------------------ interface ICacheElement

    /**
     * @see ICacheElement#getCacheName
     */
    public String getCacheName()
    {
        return cacheElement.getCacheName();
    }

    /**
     * @see ICacheElement#getKey
     */
    public Serializable getKey()
    {
        return cacheElement.getKey();
    }

    /**
     * @see ICacheElement#getVal
     */
    public Serializable getVal()
    {
        return cacheElement.getVal();
    }

    /**
     * @see ICacheElement#getElementAttributes
     */
    public IElementAttributes getElementAttributes()
    {
        return cacheElement.getElementAttributes();
    }

    /**
     * @see ICacheElement#setElementAttributes
     */
    public void setElementAttributes( IElementAttributes attr )
    {
        cacheElement.setElementAttributes( attr );
    }
}
