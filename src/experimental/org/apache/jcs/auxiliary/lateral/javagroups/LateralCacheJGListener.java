package org.apache.jcs.auxiliary.lateral.javagroups;


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

import java.util.HashMap;

import org.apache.jcs.auxiliary.lateral.LateralCacheInfo;

import org.apache.jcs.auxiliary.lateral.behavior.ILateralCacheAttributes;
import org.apache.jcs.auxiliary.lateral.behavior.ILateralCacheListener;
import org.apache.jcs.auxiliary.lateral.javagroups.behavior.ILateralCacheJGListener;

import org.apache.jcs.engine.behavior.ICache;
import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.ICompositeCache;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.jcs.engine.CacheConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description of the Class
 *
 * @version $Id: LateralCacheJGListener.java,v 1.8 2002/02/15 04:33:37 jtaylor
 *      Exp $
 */
public class LateralCacheJGListener implements ILateralCacheJGListener, Serializable
{
    private final static Log log =
        LogFactory.getLog( LateralCacheJGListener.class );

    /**
     * Description of the Field
     */
    protected static transient CompositeCacheManager cacheMgr;
    /**
     * Description of the Field
     */
    protected final static HashMap instances = new HashMap();

    // instance vars
    private LateralJGReceiver receiver;
    private ILateralCacheAttributes ilca;
    private boolean inited = false;
    private int puts = 0;

    /**
     * Only need one since it does work for all regions, just reference by
     * multiple region names.
     *
     * @param ilca
     */
    protected LateralCacheJGListener( ILateralCacheAttributes ilca )
    {
        this.ilca = ilca;
    }


    /**
     * Description of the Method
     */
    public void init()
    {
        try
        {
            // need to connect based on type
            //ILateralCacheListener ilcl = this;
            //p( "in init, ilcl = " + ilcl );
            receiver = new LateralJGReceiver( ilca, this );
            Thread t = new Thread( receiver );
            t.start();
        }
        catch ( Exception ex )
        {
            log.error( ex );
            throw new IllegalStateException( ex.getMessage() );
        }
        inited = true;
    }


    /**
     * let the lateral cache set a listener_id. Since there is only one
     * listerenr for all the regions and every region gets registered? the id
     * shouldn't be set if it isn't zero. If it is we assume that it is a
     * reconnect.
     *
     * @param id The new listenerId value
     * @exception IOException
     */
    public void setListenerId( byte id )
        throws IOException
    {
        LateralCacheInfo.listenerId = id;
        if ( log.isDebugEnabled() )
        {
            log.debug( "set listenerId = " + id );
        }
    }


    /**
     * Gets the listenerId attribute of the LateralCacheJGListener object
     *
     * @return The listenerId value
     * @exception IOException
     */
    public byte getListenerId()
        throws IOException
    {

        // set the manager since we are in use
        //getCacheManager();

        //p( "get listenerId" );
        if ( log.isDebugEnabled() )
        {
            log.debug( "get listenerId = " + LateralCacheInfo.listenerId );
        }
        return LateralCacheInfo.listenerId;
    }


    /**
     * Gets the instance attribute of the LateralCacheJGListener class
     *
     * @return The instance value
     * @param ilca
     */
    public static ILateralCacheListener getInstance( ILateralCacheAttributes ilca )
    {
        //throws IOException, NotBoundException
        ILateralCacheListener ins = ( ILateralCacheListener ) instances.get( String.valueOf( ilca.getUdpMulticastAddr() ) );
        if ( ins == null )
        {
            synchronized ( LateralCacheJGListener.class )
            {
                if ( ins == null )
                {
                    ins = new LateralCacheJGListener( ilca );
                    ins.init();
                }
                if ( log.isDebugEnabled() )
                {
                    log.debug( "created new listener " + ilca.getUdpMulticastAddr() );
                }
                instances.put( String.valueOf( ilca.getUdpMulticastAddr() ), ins );
            }
        }
        return ins;
    }


    //////////////////////////// implements the ILateralCacheListener interface. //////////////
    /**
     * @param cb
     * @exception IOException
     */
    public void handlePut( ICacheElement cb )
        throws IOException
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( "PUTTING ELEMENT FROM LATERAL" );
        }

        getCacheManager();

        ICompositeCache cache = ( ICompositeCache ) cacheMgr.getCache( cb.getCacheName() );

        cache.update( cb, true );

        puts++;
        if ( puts % 100 == 0 )
        {
            log.info( "puts = " + puts );
        }

        //handleRemove(cb.getCacheName(), cb.getKey());
    }


    /**
     * Description of the Method
     *
     * @param cacheName
     * @param key
     * @exception IOException
     */
    public void handleRemove( String cacheName, Serializable key )
        throws IOException
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( "handleRemove> cacheName=" + cacheName + ", key=" + key );
        }

        getCacheManager();
        // interface limitation here

        ICompositeCache cache = ( ICompositeCache ) cacheMgr.getCache( cacheName );

        cache.remove( key, true );
    }


    /**
     * Description of the Method
     *
     * @param cacheName
     * @exception IOException
     */
    public void handleRemoveAll( String cacheName )
        throws IOException
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( "handleRemoveAll> cacheName=" + cacheName );
        }
        getCacheManager();
        ICache cache = cacheMgr.getCache( cacheName );
        cache.removeAll();
    }

    /**
     * Test get implementation.
     *
     * @return
     * @param cacheName
     * @param key
     * @exception IOException
     */
    public Serializable handleGet( String cacheName, Serializable key )
        throws IOException
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( "handleGet> cacheName=" + cacheName + ", key = " + key );
        }
        getCacheManager();
        ICompositeCache cache = ( ICompositeCache ) cacheMgr.getCache( cacheName );
        // get container
        return cache.get( key, true );
    }

    /**
     * Description of the Method
     *
     * @param cacheName
     * @exception IOException
     */
    public void handleDispose( String cacheName )
        throws IOException
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( "handleDispose> cacheName=" + cacheName );
        }
        CompositeCacheManager cm = ( CompositeCacheManager ) cacheMgr;
        cm.freeCache( cacheName, true );
    }


    // override for new funcitonality
    /**
     * Gets the cacheManager attribute of the LateralCacheJGListener object
     */
    protected void getCacheManager()
    {
        if ( cacheMgr == null )
        {
            cacheMgr = CompositeCacheManager.getInstance();

            if ( log.isDebugEnabled() )
            {
                log.debug( "cacheMgr = " + cacheMgr );
            }
        }
        else
        {
            if ( log.isDebugEnabled() )
            {
                log.debug( "already got cacheMgr = " + cacheMgr );
            }
        }
    }
}
