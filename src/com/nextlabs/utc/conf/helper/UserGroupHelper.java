package com.nextlabs.utc.conf.helper;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.xml.rpc.ServiceException;

import com.bluejungle.destiny.appframework.appsecurity.loginmgr.ILoggedInUser;
import com.bluejungle.destiny.container.dcc.BaseDCCComponentImpl;
import com.bluejungle.destiny.container.dcc.IDCCContainer;
import com.bluejungle.destiny.services.management.UserGroupServiceLocator;
import com.bluejungle.destiny.services.management.UserRoleServiceIF;
import com.bluejungle.destiny.services.management.UserRoleServiceLocator;
import com.bluejungle.destiny.services.management.types.UserDTO;
import com.bluejungle.destiny.services.management.types.UserGroupReduced;
import com.bluejungle.destiny.services.management.types.UserGroupReducedList;
import com.bluejungle.destiny.services.management.types.UserGroupServiceIF;
import com.bluejungle.framework.comp.ComponentManagerFactory;
import com.bluejungle.framework.comp.IComponentManager;
import com.bluejungle.framework.comp.IConfiguration;
import com.nextlabs.utc.conf.UTCConfComponentImpl;

public class UserGroupHelper extends BaseDCCComponentImpl {

	private static final String USER_SERVICE_LOCATION_SERVLET_PATH = "/services/UserRoleServiceIFPort";

	private UserRoleServiceIF userService;
	private static final String USER_GROUP_SERVICE_LOCATION_SERVLET_PATH = "/services/UserGroupService";

	private UserGroupServiceIF userGroupService;

	public ArrayList<String> getGroupForUser(ILoggedInUser user) {
		ArrayList<String> grouplist = new ArrayList<String>();
		try {
			UTCConfComponentImpl.log.info("login user id"
					+ user.getPrincipalId());
			if (user != null && user.getPrincipalId() != null) {
				UserDTO userdto = getUserService().getUser(
						BigInteger.valueOf(user.getPrincipalId().longValue()));
				UTCConfComponentImpl.log.info("userdto:"
						+ userdto.getFirstName());
				if (userdto != null) {
					UserGroupReducedList groupReducedList = getUserGroupServiceIF()
							.getUserGroupsForUser(userdto);
					if (groupReducedList != null) {
						UserGroupReduced[] ut = groupReducedList
								.getUserGroupReduced();
						for (UserGroupReduced ugr : ut) {
							UTCConfComponentImpl.log.info("Title:"
									+ ugr.getTitle());
							UTCConfComponentImpl.log.info("Domain:"
									+ ugr.getDomain());
							UTCConfComponentImpl.log.info("ID:" + ugr.getId());
							grouplist.add(ugr.getTitle());
						}
					}
				}
			}

		} catch (Exception e) {
			log.warn("Error in getgroup for user", e);
		}

		return grouplist;
	}

	private UserRoleServiceIF getUserService() throws ServiceException {
		if (this.userService == null) {
			IComponentManager compMgr = ComponentManagerFactory
					.getComponentManager();
			IConfiguration mainCompConfig = (IConfiguration) compMgr
					.getComponent(IDCCContainer.MAIN_COMPONENT_CONFIG_COMP_NAME);
			String location = (String) mainCompConfig
					.get(IDCCContainer.DMS_LOCATION_CONFIG_PARAM);
			location += USER_SERVICE_LOCATION_SERVLET_PATH;
			UserRoleServiceLocator locator = new UserRoleServiceLocator();
			locator.setUserRoleServiceIFPortEndpointAddress(location);

			this.userService = locator.getUserRoleServiceIFPort();
		}

		return this.userService;

	}

	/**
	 * Retrieve the User Group Service. (protected to allow unit testing of this
	 * class)
	 */
	protected UserGroupServiceIF getUserGroupServiceIF()
			throws ServiceException {
		if (this.userGroupService == null) {
			IComponentManager componentManager = ComponentManagerFactory
					.getComponentManager();
			IConfiguration mainCompConfig = (IConfiguration) componentManager
					.getComponent(IDCCContainer.MAIN_COMPONENT_CONFIG_COMP_NAME);
			String location = (String) mainCompConfig
					.get(IDCCContainer.DMS_LOCATION_CONFIG_PARAM);
			location += USER_GROUP_SERVICE_LOCATION_SERVLET_PATH;
			UserGroupServiceLocator locator = new UserGroupServiceLocator();
			locator.setUserGroupServiceEndpointAddress(location);
			this.userGroupService = locator.getUserGroupService();
		}

		return this.userGroupService;
	}
}