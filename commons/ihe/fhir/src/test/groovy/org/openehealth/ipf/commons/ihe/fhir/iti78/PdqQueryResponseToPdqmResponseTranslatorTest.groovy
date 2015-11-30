/*
 * Copyright 2015 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti78

import ca.uhn.hl7v2.HapiContext
import org.apache.commons.io.IOUtils
import org.easymock.EasyMock
import org.hl7.fhir.instance.model.Identifier
import org.hl7.fhir.instance.model.Parameters
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.ihe.fhir.iti83.PixQueryResponseToPixmResponseTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.RSP_K21
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.RSP_K23
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions

/**
 *
 */
class PdqQueryResponseToPdqmResponseTranslatorTest extends Assert {

    private static final HapiContext PDQ_QUERY_CONTEXT = HapiContextFactory.createHapiContext(
            CustomModelClassUtils.createFactory("pdq", "2.5"),
            PixPdqTransactions.ITI21)

    private PdqResponseToPdqmResponseTranslator translator
    MappingService mappingService

    @Before
    public void setup() {
        mappingService = new BidiMappingService()
        mappingService.addMappingScript(getClass().getClassLoader().getResource('mapping.map'))
        mappingService.addMappingScript(getClass().getResource('/META-INF/map/fhir-hl7v2-translation.map'))
        UriMapper mapper = new DefaultUriMapper(mappingService, 'uriToOid', 'uriToNamespace')
        translator = new PdqResponseToPdqmResponseTranslator(mapper)

        Registry registry = EasyMock.createMock(Registry)
        ContextFacade.setRegistry(registry)
        EasyMock.expect(registry.bean(MappingService)).andReturn(mappingService).anyTimes()
        EasyMock.replay(registry)
    }

    @Test
    public void testTranslateRegularResponse() {
        RSP_K21 message = loadMessage('ok-1_Response')
        List<PdqPatient> patients = translator.translateHL7v2ToFhir(message, new HashMap<String, Object>())
        assertEquals(9, patients.size())
    }

    RSP_K21 loadMessage(String name) {
        String resourceName = "pdqquery/v2/${name}.hl7"
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)
        String content = IOUtils.toString(inputStream)
        return (RSP_K21)PDQ_QUERY_CONTEXT.getPipeParser().parse(content)
    }
}
