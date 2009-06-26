/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Classification;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExtrinsicObject;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.Slot;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.ExtrinsicObject30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ExtrinsicObjectType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.IdentifiableType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryObjectListType;

/**
 * Tests for marshaling objects created with our ebxml 2.1 classes.
 * @author Jens Riemschneider
 */
public class Ebrs30MarshalingTest {
    private SubmitObjectsRequest request;
    private ExtrinsicObject docEntry;
    private EbXMLFactory30 factory;

    private static final QName EXTRINSIC_OBJECT_QNAME = 
        new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "ExtrinsicObject", "rim"); 

    @Before
    public void setUp() {
        factory = new EbXMLFactory30();
        
        request = new SubmitObjectsRequest();
        RegistryObjectListType objListElement = new RegistryObjectListType();
        request.setRegistryObjectList(objListElement);
        List<JAXBElement<? extends IdentifiableType>> objList = objListElement.getIdentifiable();

        docEntry = factory.createExtrinsic("Document01");
        docEntry.setObjectType(Vocabulary.DOC_ENTRY_CLASS_NODE);
        objList.add(getJaxbElement(EXTRINSIC_OBJECT_QNAME, ((ExtrinsicObject30)docEntry).getInternal()));
    }
    
    @SuppressWarnings("unchecked")
    private static JAXBElement<IdentifiableType> getJaxbElement(QName qname, IdentifiableType object) {
        return new JAXBElement<IdentifiableType>(qname, (Class)object.getClass(), object);         
    }
    
    @Test
    public void testCreateClassification() throws Exception {        
        Classification classification = factory.createClassification();
        classification.setClassifiedObject(docEntry.getId());
        docEntry.addClassification(classification, Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        
        SubmitObjectsRequest received = send();
        
        ExtrinsicObject docEntryResult = getDocumentEntry(received);
        assertEquals(1, docEntryResult.getClassifications().size());
        Classification classificationResult = docEntryResult.getClassifications().get(0);
        assertNotNull(classificationResult);
        
        assertEquals(Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME, classificationResult.getClassificationScheme());
        assertNull(classificationResult.getNodeRepresentation());
        assertEquals(docEntryResult.getId(), classificationResult.getClassifiedObject());
    }
    
    private ExtrinsicObject getDocumentEntry(SubmitObjectsRequest received) {
        for(JAXBElement<? extends IdentifiableType> obj : received.getRegistryObjectList().getIdentifiable()) {
            if(obj.getDeclaredType() == ExtrinsicObjectType.class) {
                return ExtrinsicObject30.create((ExtrinsicObjectType)obj.getValue());
            }
        }
        fail("Document entry not found");
        return null;
    }

    @Test
    public void testAddSlot() throws Exception {
        Classification classification = factory.createClassification();
        docEntry.addClassification(classification, Vocabulary.DOC_ENTRY_AUTHOR_CLASS_SCHEME);
        
        classification.addSlot("something", "a", "b", "c");

        SubmitObjectsRequest received = send();        
        ExtrinsicObject docEntryResult = getDocumentEntry(received);
        List<Slot> slots = docEntryResult.getClassifications().get(0).getSlots();        
        assertEquals(1, slots.size());        
        
        Slot slot = slots.get(0);
        assertEquals("something", slot.getName());
        
        List<String> values = slot.getValueList();
        assertEquals(3, values.size());
        assertTrue(values.contains("a"));
        assertTrue(values.contains("b"));
        assertTrue(values.contains("c"));
    }

    private SubmitObjectsRequest send() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance("org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rs");
        Marshaller marshaller = context.createMarshaller();
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
        marshaller.marshal(request, outputStream);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        SubmitObjectsRequest received = (SubmitObjectsRequest) unmarshaller.unmarshal(inputStream);
        return received;
    }
}
