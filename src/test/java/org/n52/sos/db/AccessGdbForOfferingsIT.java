/**
 * Copyright (C) 2012 52°North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.db;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.n52.sos.dataTypes.ContactDescription;
import org.n52.sos.dataTypes.ObservationOffering;
import org.n52.sos.dataTypes.ServiceDescription;
import org.n52.sos.encoder.JSONEncoder;
import org.n52.sos.encoder.OGCCapabilitiesEncoder;
import org.n52.sos.handler.GetCapabilitiesOperationHandler;
import org.n52.sos.handler.capabilities.OperationsMetadataProvider;
import org.n52.sos.it.EsriTestBase;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class AccessGdbForOfferingsIT extends EsriTestBase {

    static Logger LOGGER = Logger.getLogger(AccessGdbForOfferingsIT.class.getName());
    
    @Test
    public void testGetObservationOfferings()
    {
        long millis = System.currentTimeMillis();
        
        System.out.println("start.");
        try {
            Collection<ObservationOffering> offerings = gdb.getOfferingAccess().getNetworksAsObservationOfferings();
            
            LOGGER.info("Offerings in JSON: " + JSONEncoder.encodeObservationOfferings(offerings).toString());
            
            // create ServiceDescription:
            List<String> procedureIDs = new ArrayList<String>();
            procedureIDs.add("procedureID");
            ContactDescription serviceContact = new ContactDescription("", "", "", "", "", "", "", "", "", "");
            ServiceDescription serviceDescription = new ServiceDescription("title", "description", new String[]{"keyword"}, "providerName", "providerSite", new ContactDescription[]{serviceContact}, procedureIDs); 
            
            // create operations:
            List<OperationsMetadataProvider> operationsMetadataProvider = new GetCapabilitiesOperationHandler().loadOperationsMetadataProviders("http://testSosUrl");
            
            // now encode the Caps:
            String caps = new OGCCapabilitiesEncoder().encodeCapabilities(serviceDescription, offerings, operationsMetadataProvider);
            
            OutputStream out = new FileOutputStream("c:/temp/capabilities.xml");
            out.write(caps.getBytes());
            out.flush();
            out.close();
            
        } catch (Exception e) {
        	LOGGER.severe(e.getLocalizedMessage());
            e.printStackTrace();
            fail();
        }
        
        LOGGER.info("Duration: " + (System.currentTimeMillis() - millis) / 1000 + " seconds");
    }

    public static void main(String[] args) throws Exception
    {
        AccessGdbForOfferingsIT a = new AccessGdbForOfferingsIT();
        a.setUp();
        a.testGetObservationOfferings();
    }
}
