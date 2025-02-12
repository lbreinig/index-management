/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.indexmanagement.transform.resthandler

import org.opensearch.client.node.NodeClient
import org.opensearch.indexmanagement.IndexManagementPlugin.Companion.TRANSFORM_BASE_URI
import org.opensearch.indexmanagement.transform.action.stop.StopTransformAction
import org.opensearch.indexmanagement.transform.action.stop.StopTransformRequest
import org.opensearch.indexmanagement.transform.model.Transform
import org.opensearch.rest.BaseRestHandler
import org.opensearch.rest.BaseRestHandler.RestChannelConsumer
import org.opensearch.rest.RestHandler.Route
import org.opensearch.rest.RestRequest
import org.opensearch.rest.RestRequest.Method.POST
import org.opensearch.rest.action.RestToXContentListener
import java.io.IOException

class RestStopTransformAction : BaseRestHandler() {

    override fun routes(): List<Route> {
        return listOf(
            Route(POST, "$TRANSFORM_BASE_URI/{transformID}/_stop")
        )
    }

    override fun getName(): String {
        return "opendistro_stop_transform_action"
    }

    @Throws(IOException::class)
    override fun prepareRequest(request: RestRequest, client: NodeClient): RestChannelConsumer {
        val id = request.param("transformID", Transform.NO_ID)
        if (Transform.NO_ID == id) {
            throw IllegalArgumentException("Missing transform ID")
        }

        val stopRequest = StopTransformRequest(id)
        return RestChannelConsumer { channel ->
            client.execute(StopTransformAction.INSTANCE, stopRequest, RestToXContentListener(channel))
        }
    }
}
