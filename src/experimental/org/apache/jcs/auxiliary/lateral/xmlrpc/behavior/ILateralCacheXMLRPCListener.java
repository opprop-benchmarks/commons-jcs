package org.apache.jcs.auxiliary.lateral.xmlrpc.behavior;


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
import java.io.IOException;

import org.apache.jcs.auxiliary.lateral.behavior.ILateralCacheListener;

/**
 * Listens for lateral cache event notification.
 *
 * @version $Id:   asmuts
 *      Exp $
 */
public interface ILateralCacheXMLRPCListener extends ILateralCacheListener
{

    /** Description of the Method */
    public void init();

    /** Tries to get a requested item from the cache. */
    public Serializable handleGet( String cacheName, Serializable key )
        throws IOException;

}
