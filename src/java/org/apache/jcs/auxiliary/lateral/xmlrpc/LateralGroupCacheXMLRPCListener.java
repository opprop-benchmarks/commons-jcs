package org.apache.jcs.auxiliary.lateral.xmlrpc;

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
import org.apache.jcs.auxiliary.lateral.behavior.ILateralCacheAttributes;
import org.apache.jcs.auxiliary.lateral.behavior.ILateralCacheListener;
import org.apache.jcs.auxiliary.lateral.xmlrpc.behavior.ILateralCacheXMLRPCListener;

import org.apache.jcs.engine.control.CacheHub;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:asmuts@yahoo.com">Aaron Smuts</a>
 * @created January 15, 2002
 * @version $Id: LateralGroupCacheXMLRPCListener.java,v 1.6 2002/02/15 04:33:37
 *      jtaylor Exp $
 */
public class LateralGroupCacheXMLRPCListener
     extends LateralCacheXMLRPCListener
     implements ILateralCacheXMLRPCListener
{
    private final static Log log =
        LogFactory.getLog( LateralGroupCacheXMLRPCListener.class );

    /**
     * Constructor for the LateralGroupCacheXMLRPCListener object
     *
     * @param ilca
     */
    protected LateralGroupCacheXMLRPCListener( ILateralCacheAttributes ilca )
    {
        super( ilca );
        log.debug( "creating LateralGroupCacheXMLRPCListener" );
    }


    /**
     * Gets the instance attribute of the LateralGroupCacheXMLRPCListener class
     *
     * @return The instance value
     */
    public static ILateralCacheListener getInstance( ILateralCacheAttributes ilca )
    {
        //throws IOException, NotBoundException
        ILateralCacheListener ins = ( ILateralCacheListener ) instances.get( String.valueOf( ilca.getTcpListenerPort() ) );
        if ( ins == null )
        {
            synchronized ( LateralGroupCacheXMLRPCListener.class )
            {
                if ( ins == null )
                {
                    ins = new LateralGroupCacheXMLRPCListener( ilca );
                    ins.init();
                }
                if ( log.isDebugEnabled() )
                {
                    log.debug( "created new listener " + ilca.getTcpListenerPort() );
                }
                instances.put( String.valueOf( ilca.getTcpListenerPort() ), ins );
            }
        }
        return ins;
    }


    // override for new funcitonality
    // lazy init is too slow, find a better way
    /**
     * Gets the cacheManager attribute of the LateralGroupCacheXMLRPCListener
     * object
     */
    protected void getCacheManager()
    {
        try
        {
            if ( cacheMgr == null )
            {
                cacheMgr = CacheHub.getInstance();
                if ( log.isDebugEnabled() )
                {
                    log.debug( " groupcache cacheMgr = " + cacheMgr );
                }
            }
            else
            {
                if ( log.isDebugEnabled() )
                {
                    log.debug( "already got groupcache cacheMgr = " + cacheMgr );
                }
            }
        }
        catch ( Exception e )
        {
            log.error( e );
        }
    }

}
