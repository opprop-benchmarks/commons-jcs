package org.apache.jcs.auxiliary.lateral.javagroups;

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 * any, must include the following acknowlegement:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowlegement may appear in the software itself,
 * if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Velocity", and "Apache Software
 * Foundation" must not be used to endorse or promote products derived
 * from this software without prior written permission. For written
 * permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 * nor may "Apache" appear in their names without prior written
 * permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
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
import org.apache.jcs.engine.control.CacheHub;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description of the Class
 *
 * @author <a href="mailto:asmuts@yahoo.com">Aaron Smuts</a>
 * @created January 15, 2002
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
    protected static transient CacheHub cacheMgr;
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
        cache.update( cb, ICache.REMOTE_INVOKATION );

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
        cache.remove( key, ICache.REMOTE_INVOKATION );
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
        return cache.get( key, true, ICache.REMOTE_INVOKATION );
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
        CacheHub cm = ( CacheHub ) cacheMgr;
        cm.freeCache( cacheName, ICache.REMOTE_INVOKATION );
    }


    // override for new funcitonality
    /**
     * Gets the cacheManager attribute of the LateralCacheJGListener object
     */
    protected void getCacheManager()
    {
        if ( cacheMgr == null )
        {
            cacheMgr = CacheHub.getInstance();

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
