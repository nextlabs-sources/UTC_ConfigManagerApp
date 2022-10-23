/*
 * Created on Mar 13, 2013
 * 
 * All sources, binaries and HTML pages (C) copyright 2013 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 * 
 */
package com.nextlabs.utc.conf;

import com.bluejungle.destiny.server.shared.registration.ServerComponentType;

/**
 * Constants which are shared amongst the classes within the On Demand Rights Management console.
 * Please add constants only if it truly makes sense for them to be shared
 * amongst the console
 * 
 * @author dwashburn
 * @version $Id: //depot/ProfessionalServices/UTC/2.0/ConfigManagerApp/src/com/nextlabs/utc/conf/UTCConfConstants.java#1 $
 */
public final class UTCConfConstants {
	
    public static final String UTC_CONF_BUNDLE_NAME = "UTCConfMessages";
    
    public static final String COMPONENT_TYPE_NAME = "REPORTER";
    
    public static final ServerComponentType COMPONENT_TYPE = 
            ServerComponentType.fromString(UTCConfConstants.COMPONENT_TYPE_NAME);
        

    
}