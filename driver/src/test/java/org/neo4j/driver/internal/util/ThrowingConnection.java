/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package org.neo4j.driver.internal.util;

import java.util.Map;

import org.neo4j.driver.internal.net.BoltServerAddress;
import org.neo4j.driver.internal.spi.Collector;
import org.neo4j.driver.internal.spi.Connection;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.summary.ServerInfo;

public class ThrowingConnection implements Connection
{
    private final Connection realConnection;
    private RuntimeException nextRunFailure;

    public ThrowingConnection( Connection realConnection )
    {
        this.realConnection = realConnection;
    }

    @Override
    public void init( String clientName, Map<String,Value> authToken )
    {
        realConnection.init( clientName, authToken );
    }

    @Override
    public void run( String statement, Map<String,Value> parameters, Collector collector )
    {
        if ( nextRunFailure != null )
        {
            RuntimeException error = nextRunFailure;
            nextRunFailure = null;
            throw error;
        }
        realConnection.run( statement, parameters, collector );
    }

    @Override
    public void discardAll( Collector collector )
    {
        realConnection.discardAll( collector );
    }

    @Override
    public void pullAll( Collector collector )
    {
        realConnection.pullAll( collector );
    }

    @Override
    public void reset()
    {
        realConnection.reset();
    }

    @Override
    public void ackFailure()
    {
        realConnection.ackFailure();
    }

    @Override
    public void sync()
    {
        realConnection.sync();
    }

    @Override
    public void flush()
    {
        realConnection.flush();
    }

    @Override
    public void receiveOne()
    {
        realConnection.receiveOne();
    }

    @Override
    public void close()
    {
        realConnection.close();
    }

    @Override
    public boolean isOpen()
    {
        return realConnection.isOpen();
    }

    @Override
    public void resetAsync()
    {
        realConnection.resetAsync();
    }

    @Override
    public boolean isAckFailureMuted()
    {
        return realConnection.isAckFailureMuted();
    }

    @Override
    public ServerInfo server()
    {
        return realConnection.server();
    }

    @Override
    public BoltServerAddress boltServerAddress()
    {
        return realConnection.boltServerAddress();
    }

    public void setNextRunFailure( RuntimeException nextRunFailure )
    {
        this.nextRunFailure = nextRunFailure;
    }
}
