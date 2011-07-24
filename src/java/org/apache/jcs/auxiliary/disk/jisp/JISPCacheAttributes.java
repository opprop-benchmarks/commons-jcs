package org.apache.jcs.auxiliary.disk.jisp;


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


import org.apache.jcs.auxiliary.AuxiliaryCacheAttributes;

/**
 * Description of the Class
 *
 */
public class JISPCacheAttributes implements AuxiliaryCacheAttributes
{

    private String cacheName;
    private String name;

    private String diskPath;

    private boolean clearOnStart;

    /** Constructor for the JISPCacheAttributes object */
    public JISPCacheAttributes()
    {
        clearOnStart = false;
    }

    /**
     * Sets the diskPath attribute of the JISPCacheAttributes object
     *
     * @param path The new diskPath value
     */
    public void setDiskPath( String path )
    {
        this.diskPath = path;
    }

    /**
     * Gets the diskPath attribute of the JISPCacheAttributes object
     *
     * @return The diskPath value
     */
    public String getDiskPath()
    {
        return this.diskPath;
    }


    // whether the disk cache should clear the old files
    // so there are no lingering elements.
    /**
     * Sets the clearOnStart attribute of the JISPCacheAttributes object
     *
     * @param clear The new clearOnStart value
     */
    public void setClearOnStart( boolean clear )
    {
        clearOnStart = clear;
    }

    /**
     * Gets the clearOnStart attribute of the JISPCacheAttributes object
     *
     * @return The clearOnStart value
     */
    public boolean getClearOnStart()
    {
        return clearOnStart;
    }

    /**
     * Sets the cacheName attribute of the JISPCacheAttributes object
     *
     * @param s The new cacheName value
     */
    public void setCacheName( String s )
    {
        this.cacheName = s;
    }

    /**
     * Gets the cacheName attribute of the JISPCacheAttributes object
     *
     * @return The cacheName value
     */
    public String getCacheName()
    {
        return this.cacheName;
    }

    /**
     * Gets the name attribute of the JISPCacheAttributes object
     *
     * @return The name value
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name attribute of the JISPCacheAttributes object
     *
     * @param name The new name value
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /** Description of the Method */
    public AuxiliaryCacheAttributes copy()
    {
        try
        {
            return ( AuxiliaryCacheAttributes ) this.clone();
        }
        catch ( Exception e )
        {
        }
        return this;
    }

    /** Description of the Method */
    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append( "diskPath = " + diskPath );
        return str.toString();
    }

}