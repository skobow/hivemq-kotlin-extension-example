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

import com.hivemq.extension.sdk.api.events.client.ClientLifecycleEventListener
import com.hivemq.extension.sdk.api.events.client.parameters.AuthenticationSuccessfulInput
import com.hivemq.extension.sdk.api.events.client.parameters.ConnectionStartInput
import com.hivemq.extension.sdk.api.events.client.parameters.DisconnectEventInput
import com.hivemq.extension.sdk.api.packets.general.MqttVersion
import org.slf4j.LoggerFactory

/**
 * This is a very simple {@link ClientLifecycleEventListener}
 * which logs the MQTT version and identifier of every connecting client.
 *
 * @author skobow
 * @since 4.0.0
 */
class HelloWorldListener : ClientLifecycleEventListener {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    override fun onMqttConnectionStart(connectionStartInput: ConnectionStartInput) {
        val mqttVersion = connectionStartInput.connectPacket.mqttVersion
        mqttVersion?.apply {
            when(this) {
                MqttVersion.V_3_1 -> log.info("MQTT 5 client connected with id: {} ", connectionStartInput.clientInformation.clientId)
                MqttVersion.V_3_1_1 -> log.info("MQTT 3.1.1 client connected with id: {} ", connectionStartInput.clientInformation.clientId)
                MqttVersion.V_5 -> log.info("MQTT 3.1 client connected with id: {} ", connectionStartInput.clientInformation.clientId)
            }
        }
    }

    override fun onDisconnect(disconnectEventInput: DisconnectEventInput) {
        log.info("Client disconnected with id: {} ", disconnectEventInput.clientInformation.clientId)
    }

    override fun onAuthenticationSuccessful(authenticationSuccessfulInput: AuthenticationSuccessfulInput) {
        log.info("Client authentication successful with id: {}", authenticationSuccessfulInput.clientInformation.clientId)
    }

}