package org.apache.jcs.auxiliary.remote.server;


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


import java.util.Iterator;

import org.apache.jcs.auxiliary.remote.RemoteCacheNoWait;

import org.apache.jcs.engine.CacheConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Used to monitor and repair any failed connection for the remote cache
 * service. By default the monitor operates in a failure driven mode. That is,
 * it goes into a wait state until there is an error. Upon the notification of a
 * connection error, the monitor changes to operate in a time driven mode. That
 * is, it attempts to recover the connections on a periodic basis. When all
 * failed connections are restored, it changes back to the failure driven mode.
 *
 */
public class RemoteCacheClusterMonitor implements Runnable
{
    private final static Log log =
        LogFactory.getLog( RemoteCacheClusterMonitor.class );

    private static RemoteCacheClusterMonitor instance;
    private static long idlePeriod = 30 * 1000;
    // minimum 30 seconds.
    //private static long idlePeriod = 3*1000; // for debugging.

    // Must make sure RemoteCacheMonitor is started before any remote error can be detected!
    private boolean alright = true;


    /**
     * Configures the idle period between repairs.
     *
     * @param idlePeriod The new idlePeriod value
     */
    public static void setIdlePeriod( long idlePeriod )
    {
        if ( idlePeriod > RemoteCacheClusterMonitor.idlePeriod )
        {
            RemoteCacheClusterMonitor.idlePeriod = idlePeriod;
        }
    }


    /** Constructor for the RemoteCacheClusterMonitor object */
    private RemoteCacheClusterMonitor() { }


    /**
     * Returns the singleton instance;
     *
     * @return The instance value
     */
    static RemoteCacheClusterMonitor getInstance()
    {
        if ( instance == null )
        {
            synchronized ( RemoteCacheClusterMonitor.class )
            {
                if ( instance == null )
                {
                    return instance = new RemoteCacheClusterMonitor();
                }
            }
        }
        return instance;
    }


    /**
     * Notifies the cache monitor that an error occurred, and kicks off the
     * error recovery process.
     */
    public void notifyError()
    {
        bad();
        synchronized ( this )
        {
            notify();
        }
    }

    // Run forever.

    // Avoid the use of any synchronization in the process of monitoring for performance reason.
    // If exception is thrown owing to synchronization,
    // just skip the monitoring until the next round.
    /**
     * Main processing method for the RemoteCacheClusterMonitor object
     */
    public void run()
    {
        do
        {
            if ( alright )
            {
                synchronized ( this )
                {
                    if ( alright )
                    {
                        // Failure driven mode.
                        try
                        {
                            wait();
                            // wake up only if there is an error.
                        }
                        catch ( InterruptedException ignore )
                        {
                        }
                    }
                }
            }
            // Time driven mode: sleep between each round of recovery attempt.
            try
            {
//      p("cache monitor sleeping for " + idlePeriod);
                Thread.currentThread().sleep( idlePeriod );
            }
            catch ( InterruptedException ex )
            {
                // ignore;
            }
            // The "alright" flag must be false here.
            // Simply presume we can fix all the errors until proven otherwise.
            synchronized ( this )
            {
                alright = true;
            }
//      p("cache monitor running.");
            // Monitor each RemoteCacheManager instance one after the other.
            // Each RemoteCacheManager corresponds to one remote connection.
            for ( Iterator itr = RemoteCacheClusterManager.instances.values().iterator(); itr.hasNext();  )
            {
                RemoteCacheClusterManager mgr = ( RemoteCacheClusterManager ) itr.next();
                try
                {
                    // If any cache is in error, it strongly suggests all caches managed by the
                    // same RmicCacheManager instance are in error.  So we fix them once and for all.
                    for ( Iterator itr2 = mgr.caches.values().iterator(); itr2.hasNext();  )
                    {
                        if ( itr2.hasNext() )
                        {
                            RemoteCacheNoWait c = ( RemoteCacheNoWait ) itr2.next();
                            //RemoteCacheNoWait c = (RemoteCacheNoWait)mgr.cache;
                            if ( c.getStatus() == CacheConstants.STATUS_ERROR )
                            {
                                RemoteCacheClusterRestore repairer = new RemoteCacheClusterRestore( mgr );
                                // If we can't fix them, just skip and re-try in the next round.
                                if ( repairer.canFix() )
                                {
                                    repairer.fix();
                                }
                                else
                                {
                                    bad();
                                }
                                break;
                            }
                        }
                    }
                }
                catch ( Exception ex )
                {
                    bad();
                    // Problem encountered in fixing the caches managed by a RemoteCacheManager instance.
                    // Soldier on to the next RemoteCacheClusterManager instance.
                    log.error( ex );
                }
            }
        } while ( true );
    }


    /** Sets the "alright" flag to false in a critial section. */
    private void bad()
    {
        if ( alright )
        {
            synchronized ( this )
            {
                alright = false;
            }
        }
    }
}

