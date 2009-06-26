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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.LocalizedString;

/**
 * Encapsulation of the ebXML classes for {@code RegistryEntryType} and 
 * {@code RegistryObjectType}.
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface RegistryEntry {
    List<Slot> getSlots();
    void addSlot(String slotName, String... slotValues);
    List<String> getSlotValues(String slotName);
    String getSingleSlotValue(String slotName);

    List<Classification> getClassifications();
    List<Classification> getClassifications(String scheme);
    Classification getSingleClassification(String scheme);
    void addClassification(Classification classification, String scheme);    

    List<ExternalIdentifier> getExternalIdentifiers();
    String getExternalIdentifierValue(String scheme);
    void addExternalIdentifier(String value, String scheme, String name);
    
    String getObjectType();
    void setObjectType(String objectType);
    
    String getStatus();
    void setStatus(String status);
    
    LocalizedString getDescription();
    void setDescription(LocalizedString description);
    
    LocalizedString getName();
    void setName(LocalizedString name);
    
    String getId();
}
