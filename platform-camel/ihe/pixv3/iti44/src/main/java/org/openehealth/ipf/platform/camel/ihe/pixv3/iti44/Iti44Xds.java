package org.openehealth.ipf.platform.camel.ihe.pixv3.iti44;

import javax.xml.namespace.QName;

import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.hl7v3ws.Hl7v3ClientFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v3ws.Hl7v3ServiceFactory;

/**
 * Provides access to the service and client factories for ITI-44 (XDS-b).
 * @author Dmytro Rud
 */
public class Iti44Xds {
    private static final String NS_URI = "urn:ihe:iti:xds-b:2007";
    private final static ItiServiceInfo ITI_44 = new ItiServiceInfo(
            new QName(NS_URI, "DocumentRegistry_Service", "ihe"),
            Iti44XdsPortType.class,
            new QName(NS_URI, "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti44-xds-raw.wsdl",
            false,
            false);
           
    /**
     * Returns a factory for client stubs of the ITI-44 service.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceUrl
     *          the URL of the service to use.
     * @return the client factory.
     */
    public static ItiClientFactory getClientFactory(boolean audit, boolean allowIncompleteAudit, String serviceUrl) {
        return new Hl7v3ClientFactory(ITI_44, false, serviceUrl);
    }

    /**
     * Returns a factory for ITI-44 services.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceAddress
     *          the address used to publish the service in the current context.  
     * @return the service factory.
     */
    public static ItiServiceFactory getServiceFactory(boolean audit, boolean allowIncompleteAudit, String serviceAddress) {
        return new Hl7v3ServiceFactory(ITI_44, serviceAddress);
    }
}
