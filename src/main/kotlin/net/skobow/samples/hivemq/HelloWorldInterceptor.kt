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

import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundInput
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundOutput
import com.hivemq.extension.sdk.api.packets.publish.ModifiablePublishPacket
import java.nio.ByteBuffer

/**
 * This is a very simple {@link PublishInboundInterceptor},
 * it changes the payload of every incoming PUBLISH with the topic 'hello/world' to 'Hello World!'.
 *
 * @author skobow
 * @since 4.3.1
 */
class HelloWorldInterceptor : PublishInboundInterceptor {
    override fun onInboundPublish(publishInboundInput: PublishInboundInput, publishInboundOutput: PublishInboundOutput) {
        publishInboundOutput.publishPacket.onTopic("hello/world") {
            setPayload(fromString("Hello World!"))
        }
    }
}

fun ModifiablePublishPacket.onTopic(topic: String, action: ModifiablePublishPacket.() -> Unit) {
    if (topic == this.topic) {
        action()
    }
}

fun fromString(payload: String) = ByteBuffer.wrap(payload.toByteArray())