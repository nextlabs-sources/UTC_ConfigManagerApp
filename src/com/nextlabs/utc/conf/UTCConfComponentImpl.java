/*
 * Created on Mar 13, 2013
 * 
 * All sources, binaries and HTML pages (C) copyright 2013 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 * 
 */

package com.nextlabs.utc.conf;

import java.util.HashMap;

import org.apache.commons.logging.Log;

import com.bluejungle.destiny.appframework.appsecurity.loginmgr.ILoginMgr;
import com.bluejungle.destiny.container.dcc.BaseDCCComponentImpl;
import com.bluejungle.destiny.container.dcc.IDCCContainer;
import com.bluejungle.destiny.server.shared.registration.ServerComponentType;
import com.bluejungle.destiny.webui.framework.loginmgr.remote.WebAppRemoteLoginMgr;
import com.bluejungle.framework.comp.ComponentInfo;
import com.bluejungle.framework.comp.HashMapConfiguration;
import com.bluejungle.framework.comp.IConfiguration;
import com.bluejungle.framework.comp.LifestyleType;
import com.nextlabs.utc.conf.helper.HibernateDataAccessor;

/**
 * This is the implementation class for the UTC Config console component.
 * 
 * @author dwashburn
 * @version $Id: //depot/ProfessionalServices/UTC/2.0/ConfigManagerApp/src/com/nextlabs/utc/conf/UTCConfComponentImpl.java#2 $
 */

public class UTCConfComponentImpl extends BaseDCCComponentImpl {

	private static final String SECURE_SESSION_SERVICE_PATH_INFO = "services/SecureSessionService";

	public static Log log = null;

	/**
	 * @see com.bluejungle.destiny.server.shared.registration.IRegisteredDCCComponent#getComponentType()
	 */
	public ServerComponentType getComponentType() {
		return UTCConfConstants.COMPONENT_TYPE;
	}

	/**
	 * Initializes the Management console component. The initialization sets up
	 * the authentication manager.
	 */
	public void init() {
		log = getLog();

		getLog().info("1UTC setting login manager");

		super.init();

		getLog().info("2UTC setting login manager");

		// Initializes the login manager
		IConfiguration componentConfiguration = (IConfiguration) getManager()
				.getComponent(IDCCContainer.MAIN_COMPONENT_CONFIG_COMP_NAME);
		/*final String dacLocation = (String) componentConfiguration
				.get("DACLocation");*/
		final String dmsLocation = (String) componentConfiguration
				.get("DMSLocation");
		
		String dbUrl, userName, password, dialect, driver;
		dbUrl = (String) componentConfiguration.get("DBUrl");
		log.info("DBUrl " + dbUrl);
		userName = (String) componentConfiguration.get("UserName");
		log.info("UserName " + userName);
		password = (String) componentConfiguration.get("Password");
		log.info("Password " + password);
		dialect = (String) componentConfiguration.get("HibernateDialect");
		log.info("HibernateDialect " + dialect);
		driver = (String) componentConfiguration.get("DriverClass");
		log.info("DriverClass " + driver);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("dburl", dbUrl);
		map.put("un", userName);
		map.put("pw", password);
		map.put("dialect", dialect);
		map.put("driver", driver);
		log.info("map " + map);
		HibernateDataAccessor.getInstance(map);
		initLoginManager(dmsLocation);

	}

	/**
	 * This function sets up the remote login manager component
	 * 
	 * @param dmsLocation
	 *            location of the DMS component
	 */
	private void initLoginManager(final String location) {
		if (location == null) {
			final String msg = "Error : no DMS location specified. Unable to intialize login manager";
			getLog().fatal(msg);
			throw new RuntimeException(msg);
		}
		HashMapConfiguration componentConfig = new HashMapConfiguration();
		String loginServiceLocation = location;
		if (!loginServiceLocation.endsWith("/")) {
			loginServiceLocation = loginServiceLocation.concat("/");
		}
		loginServiceLocation = loginServiceLocation
				.concat(SECURE_SESSION_SERVICE_PATH_INFO);
		componentConfig.setProperty(
				WebAppRemoteLoginMgr.SECURE_SESSION_SERVICE_ENDPOINT_PROP_NAME,
				loginServiceLocation);
		ComponentInfo componentInfo = new ComponentInfo(ILoginMgr.COMP_NAME,
				WebAppRemoteLoginMgr.class.getName(),
				ILoginMgr.class.getName(), LifestyleType.SINGLETON_TYPE,
				componentConfig);
		// Prime the login manager (make sure it comes up)
		ILoginMgr loginMgr = (ILoginMgr) getManager().getComponent(
				componentInfo);

	}
}
