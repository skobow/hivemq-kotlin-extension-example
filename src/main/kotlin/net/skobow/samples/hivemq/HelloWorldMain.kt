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

import com.hivemq.extension.sdk.api.ExtensionMain
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput
import com.hivemq.extension.sdk.api.parameter.ExtensionStartOutput
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput
import com.hivemq.extension.sdk.api.parameter.ExtensionStopOutput
import com.hivemq.extension.sdk.api.services.Services
import org.slf4j.LoggerFactory
import java.lang.Exception

/**
* This is the main class of the extension,
* which is instantiated either during the HiveMQ start up process (if extension is enabled)
* or when HiveMQ is already started by enabling the extension.
*
* @author skobow
* @since 4.0.0
*/
class HelloWorldMain : ExtensionMain {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val log = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    override fun extensionStart(extensionStartInput: ExtensionStartInput, extensionStartOutput: ExtensionStartOutput) {
        try {
            addClientLifecycleEventListener()
            addPublishModifier()

            val extensionInformation = extensionStartInput.extensionInformation
            log.info("Started {}:{}", extensionInformation.name, extensionInformation.version)
        } catch (e: Exception) {
            log.error("Exception thrown at extension start: ", e)
        }
    }

    override fun extensionStop(extensionStopInput: ExtensionStopInput, extensionStopOutput: ExtensionStopOutput) {

        val extensionInformation = extensionStopInput.extensionInformation
        log.info("Stopped {}:{}", extensionInformation.name, extensionInformation.version)
    }

    private fun addClientLifecycleEventListener() {
        val eventRegistry = Services.eventRegistry()
        val helloWorldListener = HelloWorldListener()

        eventRegistry.setClientLifecycleEventListener {
            helloWorldListener
        }
    }

    private fun addPublishModifier() {
        val initializerRegistry = Services.initializerRegistry()
        val helloWorldInterceptor = HelloWorldInterceptor()

        initializerRegistry.setClientInitializer { _, clientContext ->
            clientContext.addPublishInboundInterceptor(helloWorldInterceptor)
        }
    }
}