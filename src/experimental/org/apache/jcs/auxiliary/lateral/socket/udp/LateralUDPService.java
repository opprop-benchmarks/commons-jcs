package org.apache.jcs.auxiliary.lateral.socket.udp;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;

import org.apache.jcs.auxiliary.lateral.LateralCacheAttributes;
import org.apache.jcs.auxiliary.lateral.LateralCacheInfo;
import org.apache.jcs.auxiliary.lateral.LateralElementDescriptor;

import org.apache.jcs.auxiliary.lateral.behavior.ILateralCacheAttributes;
import org.apache.jcs.auxiliary.lateral.behavior.ILateralCacheObserver;
import org.apache.jcs.auxiliary.lateral.behavior.ILateralCacheService;

import org.apache.jcs.engine.CacheElement;

import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.ICacheListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description of the Class
 *
 * @author asmuts
 * @created January 15, 2002
 */
public class LateralUDPService implements ILateralCacheService, ILateralCacheObserver
{
    private final static Log log =
        LogFactory.getLog( LateralUDPService.class );

    private ILateralCacheAttributes ilca;

    private LateralUDPSender sender;

    /**
     * Constructor for the LateralUDPService object
     *
     * @param lca
     * @exception IOException
     */
    public LateralUDPService( ILateralCacheAttributes lca )
        throws IOException
    {

        this.ilca = lca;

        try
        {
            sender = new LateralUDPSender( lca );

        }
        catch ( IOException e )
        {
            log.error( "Could not create sender", e );

            throw e;
        }

    }

    /** Description of the Method */
    public void update( ICacheElement item )
        throws IOException
    {
        update( item, LateralCacheInfo.listenerId );
    }


    /** Description of the Method */
    public void update( ICacheElement item, byte requesterId )
        throws IOException
    {
        LateralElementDescriptor led = new LateralElementDescriptor( item );
        led.requesterId = requesterId;
        led.command = led.UPDATE;
        sender.send( led );
    }


    /** Description of the Method */
    public void remove( String cacheName, Serializable key )
        throws IOException
    {
        remove( cacheName, key, LateralCacheInfo.listenerId );
    }


    /** Description of the Method */
    public void remove( String cacheName, Serializable key, byte requesterId )
        throws IOException
    {
        CacheElement ce = new CacheElement( cacheName, key, null );
        LateralElementDescriptor led = new LateralElementDescriptor( ce );
        led.requesterId = requesterId;
        led.command = led.REMOVE;
        sender.send( led );
    }


    /** Description of the Method */
    public void release()
        throws IOException
    {
        // nothing needs to be done
    }


    /** Description of the Method */
    public void dispose( String cache )
        throws IOException
    {
        //sender = null;
        // nothing needs to be done
    }


    /** Description of the Method */
    public Serializable get( String cache )
        throws IOException
    {
        return null;
        // nothing needs to be done
    }


    /** Description of the Method */
    public ICacheElement get( String cache, Serializable att )
        throws IOException
    {
        return null;
        // nothing needs to be done
    }


    /** Description of the Method */
    public Serializable get( String cache, Serializable att, boolean container )
        throws IOException
    {
        return null;
        // nothing needs to be done
    }


    /** Description of the Method */
    public void removeAll( String cacheName )
        throws IOException
    {
        removeAll( cacheName, LateralCacheInfo.listenerId );
    }


    /** Description of the Method */
    public void removeAll( String cacheName, byte requesterId )
        throws IOException
    {
        CacheElement ce = new CacheElement( cacheName, "ALL", null );
        LateralElementDescriptor led = new LateralElementDescriptor( ce );
        led.requesterId = requesterId;
        led.command = led.REMOVEALL;
        sender.send( led );
    }


    /** Description of the Method */
    public static void main( String args[] )
    {
        try
        {
            LateralUDPSender sender = new LateralUDPSender( new LateralCacheAttributes() );

            // process user input till done
            boolean notDone = true;
            String message = null;
            // wait to dispose
            BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );

            while ( notDone )
            {
                System.out.println( "enter mesage:" );
                message = br.readLine();
                CacheElement ce = new CacheElement( "test", "test", message );
                LateralElementDescriptor led = new LateralElementDescriptor( ce );
                sender.send( led );
            }
        }
        catch ( Exception e )
        {
            System.out.println( e.toString() );
        }
    }


    // ILateralCacheObserver methods, do nothing here since
    // the connection is not registered, the udp service is
    // is not registered.
    /**
     * Adds a feature to the CacheListener attribute of the LateralUDPService
     * object
     *
     * @param cacheName The feature to be added to the CacheListener attribute
     * @param obj The feature to be added to the CacheListener attribute
     */
    public void addCacheListener( String cacheName, ICacheListener obj )
        throws IOException { }


    /**
     * Adds a feature to the CacheListener attribute of the LateralUDPService
     * object
     *
     * @param obj The feature to be added to the CacheListener attribute
     */
    public void addCacheListener( ICacheListener obj )
        throws IOException { }


    /** Description of the Method */
    public void removeCacheListener( String cacheName, ICacheListener obj )
        throws IOException { }


    /** Description of the Method */
    public void removeCacheListener( ICacheListener obj )
        throws IOException { }

}
// end class
