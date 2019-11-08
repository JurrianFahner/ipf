/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.chppq;

import static org.openehealth.ipf.commons.ihe.xacml20.Xacml20MessageValidator.validateChPpq1Request;
import static org.openehealth.ipf.commons.ihe.xacml20.Xacml20MessageValidator.validateChPpq1Response;
import static org.openehealth.ipf.commons.ihe.xacml20.Xacml20MessageValidator.validateChPpq2Request;
import static org.openehealth.ipf.commons.ihe.xacml20.Xacml20MessageValidator.validateChPpq2Response;

import java.io.InputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 */
public class MessageValidationTest {

    @BeforeClass
    public static void beforeClass() {
        Xacml20Utils.initializeHerasaf();
    }

    private static <T> T loadFile(String fn) throws Exception {
        InputStream stream = MessageValidationTest.class.getClassLoader().getResourceAsStream("messages/" + fn);
        Unmarshaller unmarshaller = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller();
        Object object = unmarshaller.unmarshal(stream);
        if (object instanceof JAXBElement) {
            object = ((JAXBElement) object).getValue();
        }
        return (T) object;
    }

    @Test
    public void testValidation() throws Exception {
        validateChPpq1Request(loadFile("chppq1/add-request-ppq.xml"));
        validateChPpq1Request(loadFile("chppq1/update-request-ppq.xml"));
        validateChPpq1Request(loadFile("chppq1/delete-request.xml"));
        validateChPpq1Response(loadFile("chppq1/ack.xml"));

        validateChPpq2Request(loadFile("chppq2/query-per-patient-id.xml"));
        validateChPpq2Request(loadFile("chppq2/query-per-policy-id.xml"));
        validateChPpq2Response(loadFile("chppq2/query-response.xml"));
    }
}

