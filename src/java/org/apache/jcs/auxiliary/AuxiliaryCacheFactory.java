package org.apache.jcs.auxiliary;


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


import org.apache.jcs.engine.control.CompositeCache;

/**
 * Description of the Interface
 *
 */
public interface AuxiliaryCacheFactory
{

    /**
     * @param attr Attributes the factory will use to create the AuxiliaryCache
     *             instance.
     * @param cache The CompositeCache which will contain the AuxiliaryCache
     *              instance being created. Allows auxiliaries to retain a
     *              reference to their associated CompositeCache.
     */
    public AuxiliaryCache createCache( AuxiliaryCacheAttributes attr,
                                       CompositeCache cache );

    /**
     * Sets the name attribute of the IAuxiliaryCacheFactory object
     *
     * @param s The new name value
     */
    public void setName( String s );

    /**
     * Gets the name attribute of the IAuxiliaryCacheFactory object
     *
     * @return The name value
     */
    public String getName();

}
