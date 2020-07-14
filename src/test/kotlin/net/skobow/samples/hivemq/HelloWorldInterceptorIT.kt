/*
 * Copyright 2020-present skobow.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.skobow.samples.hivemq

import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.testcontainer.core.MavenHiveMQExtensionSupplier
import com.hivemq.testcontainer.junit5.HiveMQTestContainerExtension
import junit.framework.Assert.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.extension.RegisterExtension
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

/**
 * This tests the functionality of the {@link HelloWorldInterceptor}.
 * It uses the HiveMQ Testcontainer to automatically package and deploy this extension inside a HiveMQ docker container.
 *
 * @author skobow
 * @since 4.3.1
 */
class HelloWorldInterceptorIT {

    @RegisterExtension
    @JvmField
    val extension: HiveMQTestContainerExtension = HiveMQTestContainerExtension()
            .withExtension(MavenHiveMQExtensionSupplier.direct().get())

    @Test
    @Timeout(value = 5, unit = TimeUnit.MINUTES)
    fun `test payload modifier`() {
        val client = Mqtt5Client.builder()
                .identifier("hello-world-client")
                .serverPort(extension.mqttPort)
                .buildBlocking()
        client.connect()

        val publishes = client.publishes(MqttGlobalPublishFilter.ALL)
        client.subscribeWith().topicFilter("hello/world").send()

        client.publishWith().topic("hello/world").payload(fromString("Good Bye World!")).send()

        val receive = publishes.receive()
        assertTrue(receive.payload.isPresent)
        assertEquals("Hello World!", String(receive.payloadAsBytes, StandardCharsets.UTF_8))
    }
}