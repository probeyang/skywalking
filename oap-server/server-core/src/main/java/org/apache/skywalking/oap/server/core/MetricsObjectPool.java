/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.skywalking.oap.server.core;

import io.netty.util.internal.ObjectPool;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.oap.server.core.analysis.metrics.Metrics;

@Slf4j
public final class MetricsObjectPool {
    private static final Map<Class<? extends Metrics>, ObjectPool<Metrics>> POOLS =
        new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Metrics> T get(Class<T> type) {
        final ObjectPool<Metrics> pool = POOLS.computeIfAbsent(type, __ -> ObjectPool.newPool(
            handle -> {
                try {
                    final Metrics m = type.getDeclaredConstructor().newInstance();
                    m.handle(self -> {
                        log.info("recycling pooled objects");
                        handle.recycle(self);
                    });
                    return m;
                } catch (Exception e) {
                    log.error("Failed to create object for {}", type, e);
                    throw new RuntimeException(e);
                }
            })
        );
        log.info("getting pooled objects");
        return (T) pool.get();
    }
}
