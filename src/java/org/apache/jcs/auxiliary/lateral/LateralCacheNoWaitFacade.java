package org.apache.jcs.auxiliary.lateral;


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


import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jcs.auxiliary.AuxiliaryCache;
import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.ICacheType;

/**
 * Used to provide access to multiple services under nowait protection.
 * Composite factory should construct LateralCacheNoWaitFacade to give to the
 * composite cache out of caches it constructs from the varies manager to
 * lateral services. Perhaps the lateralcache factory should be able to do this.
 *
 */
public class LateralCacheNoWaitFacade implements AuxiliaryCache
{
    private final static Log log =
        LogFactory.getLog( LateralCacheNoWaitFacade.class );

    /** Description of the Field */
    public LateralCacheNoWait[] noWaits;

    private String cacheName;

    /**
     * Constructs with the given lateral cache, and fires events to any
     * listeners.
     *
     * @param noWaits
     * @param cacheName
     */
    public LateralCacheNoWaitFacade( LateralCacheNoWait[] noWaits, String cacheName )
    {
        this.noWaits = noWaits;
        this.cacheName = cacheName;
    }

    /** Description of the Method */
    public void update( ICacheElement ce )
        throws IOException
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( "updating through lateral cache facade, noWaits.length = " + noWaits.length );
        }
        try
        {
            for ( int i = 0; i < noWaits.length; i++ )
            {
                noWaits[ i ].update( ce );
            }
        }
        catch ( Exception ex )
        {
            log.error( ex );
        }
    }

    /** Synchronously reads from the lateral cache. */
    public ICacheElement get( Serializable key )
    {
        for ( int i = 0; i < noWaits.length; i++ )
        {
            try
            {
                Object obj = noWaits[ i ].get( key );

                if ( obj != null )
                {
                    // TODO: return after first success
                    // could do this simultaneously
                    // serious blocking risk here
                    return ( ICacheElement ) obj;
                }
            }
            catch ( Exception ex )
            {
                log.error( "Failed to get", ex );
            }
            return null;
        }
        return null;
    }

    /**
     * Gets the set of keys of objects currently in the group
     */
    public Set getGroupKeys(String group)
    {
        HashSet allKeys = new HashSet();
        for ( int i = 0; i < noWaits.length; i++ )
        {
            AuxiliaryCache aux = noWaits[i];
            if ( aux != null )
            {
                try {
                    allKeys.addAll(aux.getGroupKeys(group));
                }
                catch ( IOException e )
                {
                    // ignore
                }
            }
        }
        return allKeys;
    }

    /** Adds a remove request to the lateral cache. */
    public boolean remove( Serializable key )
    {
        try
        {
            for ( int i = 0; i < noWaits.length; i++ )
            {
                noWaits[ i ].remove( key );
            }
        }
        catch ( Exception ex )
        {
            log.error( ex );
        }
        return false;
    }

    /** Adds a removeAll request to the lateral cache. */
    public void removeAll()
    {
        try
        {
            for ( int i = 0; i < noWaits.length; i++ )
            {
                noWaits[ i ].removeAll();
            }
        }
        catch ( Exception ex )
        {
            log.error( ex );
        }
    }

    /** Adds a dispose request to the lateral cache. */
    public void dispose()
    {
        try
        {
            for ( int i = 0; i < noWaits.length; i++ )
            {
                noWaits[ i ].dispose();
            }
        }
        catch ( Exception ex )
        {
            log.error( ex );
        }
    }

    /**
     * No lateral invokation.
     *
     * @return The size value
     */
    public int getSize()
    {
        return 0;
        //cache.getSize();
    }

    /**
     * Gets the cacheType attribute of the LateralCacheNoWaitFacade object
     *
     * @return The cacheType value
     */
    public int getCacheType()
    {
        return ICacheType.LATERAL_CACHE;
    }

    /**
     * Gets the cacheName attribute of the LateralCacheNoWaitFacade object
     *
     * @return The cacheName value
     */
    public String getCacheName()
    {
        return "";
        //cache.getCacheName();
    }


    // need to do something with this
    /**
     * Gets the status attribute of the LateralCacheNoWaitFacade object
     *
     * @return The status value
     */
    public int getStatus()
    {
        return 0;
        //q.isAlive() ? cache.getStatus() : cache.STATUS_ERROR;
    }

    /** Description of the Method */
    public String toString()
    {
        return "LateralCacheNoWaitFacade: " + cacheName;
    }
}
