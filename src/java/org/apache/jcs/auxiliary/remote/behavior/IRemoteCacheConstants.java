package org.apache.jcs.auxiliary.remote.behavior;


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


/**
 * Description of the Interface
 *
 */
public interface IRemoteCacheConstants
{

    /** Mapping to props file value */
    public final static String REMOTE_CACHE_SERVICE_NAME = "remote.cache.service.name";
    /** Mapping to props file value */
    public final static String REMOTE_CACHE_SERVICE_VAL = IRemoteCacheService.class.getName();
    /** Mapping to props file value */
    public final static String TOMCAT_XML = "remote.tomcat.xml";
    /** Mapping to props file value */
    public final static String TOMCAT_ON = "remote.tomcat.on";
    /** Mapping to props file value */
    public final static String REMOTE_CACHE_SERVICE_PORT = "remote.cache.service.port";
    /** Mapping to props file value */
    public final static String REMOTE_LOCAL_CLUSTER_CONSISTENCY = "remote.cluster.LocalClusterConsistency";

    /** Mapping to props file value */
    public final static String REMOTE_ALLOW_CLUSTER_GET = "remote.cluster.AllowClusterGet";

}
