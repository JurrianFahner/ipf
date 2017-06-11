/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti38;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsComponent;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsEndpoint;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.xds.XCA.Interactions.ITI_38;

/**
 * The Camel component for the ITI-38 transaction.
 */
public class Iti38Component extends XdsComponent<XdsQueryAuditDataset> {


    public Iti38Component() {
        super(ITI_38);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new XdsEndpoint<XdsQueryAuditDataset>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                null) {
            @Override
            public AbstractWsProducer<XdsQueryAuditDataset, WsTransactionConfiguration, ?, ?> getProducer(AbstractWsEndpoint<XdsQueryAuditDataset, WsTransactionConfiguration> endpoint,
                                                                                                          JaxWsClientFactory<XdsQueryAuditDataset> clientFactory) {
                return new SimpleWsProducer<>(
                        endpoint, clientFactory, AdhocQueryRequest.class, AdhocQueryResponse.class);
            }

            @Override
            protected AbstractWebService getCustomServiceInstance(AbstractWsEndpoint<XdsQueryAuditDataset, WsTransactionConfiguration> endpoint) {
                return new Iti38Service(endpoint.getHomeCommunityId());
            }
        };
    }


}
