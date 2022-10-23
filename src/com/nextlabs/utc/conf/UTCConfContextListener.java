/*
 * Created on Mar 13, 2013
 * 
 * All sources, binaries and HTML pages (C) copyright 2013 by NextLabs Inc.,
 * San Mateo CA, Ownership remains with NextLabs Inc, All rights reserved
 * worldwide.
 * 
 */
package com.nextlabs.utc.conf;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.bluejungle.destiny.container.dcc.ApplicationInformationImpl;
import com.bluejungle.destiny.container.dcc.DCCContextListener;
import com.bluejungle.destiny.container.dcc.FileSystemResourceLocatorImpl;
import com.bluejungle.destiny.container.dcc.IApplicationInformation;
import com.bluejungle.destiny.container.dcc.IDCCContainer;
import com.bluejungle.destiny.container.dcc.INamedResourceLocator;
import com.bluejungle.destiny.server.shared.registration.ServerComponentType;
import com.bluejungle.framework.comp.ComponentInfo;
import com.bluejungle.framework.comp.ComponentManagerFactory;
import com.bluejungle.framework.comp.HashMapConfiguration;
import com.bluejungle.framework.comp.IComponentManager;
import com.bluejungle.framework.comp.IConfiguration;
import com.bluejungle.framework.comp.LifestyleType;
import com.bluejungle.framework.environment.IResourceLocator;
import com.bluejungle.framework.environment.webapp.WebAppResourceLocatorImpl;
import com.bluejungle.framework.sharedcontext.DestinySharedContextLocatorImpl;
import com.bluejungle.framework.sharedcontext.IDestinySharedContextLocator;
import com.nextlabs.utc.conf.environment.UTCConfResourceLocators;

/** This is the UTC Config console context listener class.
 * 
 * @author dwashburn
 * @version $Id: $ */
public class UTCConfContextListener extends DCCContextListener {
	/** @see com.bluejungle.destiny.container.dcc.DCCContextListener#getComponentType() */
	public ServerComponentType getComponentType() {
		return UTCConfConstants.COMPONENT_TYPE;
	}

	@Override
	public String getTypeDisplayName() {
		return "UTCConfigManagerApp";
	}

	/** @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent) */
	@SuppressWarnings("unchecked")
	public void contextInitialized(ServletContextEvent initEvent) {
		// EnrollmentConfComponentImpl.log.info("Starting contextInitialized");
		IComponentManager manager = ComponentManagerFactory
				.getComponentManager();
		try {
			initEvent.getServletContext().setAttribute("componentManager",
					manager);
			this.initDefaultResourceLocators(initEvent.getServletContext());
			HashMapConfiguration containerConfig = new HashMapConfiguration();
			manager = (IComponentManager) initEvent.getServletContext()
					.getAttribute("componentManager");
			// EnrollmentConfComponentImpl.log.info("EV2.0 ::: Component Manager created ");
			IConfiguration componentConfig = prepareComponentConfiguration(initEvent
					.getServletContext());
			containerConfig.setProperty(
					IDCCContainer.COMP_CONFIG_CONFIG_PARAM_NAME,
					componentConfig);
			containerConfig.setProperty(
					IDCCContainer.COMPONENT_TYPE_CONFIG_PARAM,
					getComponentType());
			containerConfig.setProperty(
					IDCCContainer.COMPONENT_TYPE_DISPLAY_NAME_CONFIG_PARAM,
					getTypeDisplayName());
			containerConfig.setProperty(
					IDCCContainer.COMPONENT_NAME_CONFIG_PARAM, componentConfig
							.get(IDCCContainer.COMPONENT_NAME_CONFIG_PARAM));
			String componentName = (String) componentConfig
					.get(IDCCContainer.COMPONENT_CLASS_CONFIG_PARAM.toString());
			// EnrollmentConfComponentImpl.log.info("EV2.0 ::: Component Name  "+componentName);
			Class clazz = Class.forName(componentName);
			containerConfig.setProperty(
					IDCCContainer.COMPONENT_CLASS_CONFIG_PARAM.toString(),
					clazz);
			containerConfig
					.setProperty(
							IDCCContainer.COMPONENT_LOCATION_CONFIG_PARAM,
							componentConfig
									.get(IDCCContainer.COMPONENT_LOCATION_CONFIG_PARAM));
			containerConfig.setProperty(
					IDCCContainer.COMPONENT_RESOUCES_CONFIG_PARAM,
					getApplicationResources());
			ComponentInfo containerInfo = new ComponentInfo(
					"DCCContainerComponent", getContainerClassName(),
					IDCCContainer.class, LifestyleType.SINGLETON_TYPE,
					containerConfig);
			this.container = ((IDCCContainer) manager
					.getComponent(containerInfo));
		} catch (Exception e) {
			getLog().warn(
					"Startup of DCC Container '" + getContainerClassName()
							+ "' failed!! Will Load this component later");
			// manager.shutdown();
			// throw new RuntimeException(e);
		}
		initEvent.getServletContext().setAttribute("componentManager", manager);
		setupResourceLocator(initEvent.getServletContext());
		getLog().info("Setup Resource Locator done");
		getLog().info("Context Initialized done");
	}

	protected void initDefaultResourceLocators(ServletContext ctx) {
		IComponentManager manager = (IComponentManager) ctx
				.getAttribute("componentManager");
		ComponentInfo locatorInfo = new ComponentInfo(
				"DestinySharedContextLocator",
				DestinySharedContextLocatorImpl.class,
				IDestinySharedContextLocator.class,
				LifestyleType.TRANSIENT_TYPE);
		IDestinySharedContextLocator locator = (IDestinySharedContextLocator) manager
				.getComponent(locatorInfo);
		String installRootDir = ctx.getInitParameter("InstallHome");
		HashMapConfiguration serverLocatorConfig = new HashMapConfiguration();
		serverLocatorConfig.setProperty("RootPath", installRootDir);
		ComponentInfo serverLocatorInfo = new ComponentInfo(
				"ServerHomeResourceLocator",
				FileSystemResourceLocatorImpl.class,
				INamedResourceLocator.class, LifestyleType.SINGLETON_TYPE,
				serverLocatorConfig);
		INamedResourceLocator serverResourceLocator = (INamedResourceLocator) manager
				.getComponent(serverLocatorInfo);
		HashMapConfiguration webAppFileLocatorConfig = new HashMapConfiguration();
		webAppFileLocatorConfig.setProperty("ContainerContext", ctx);
		ComponentInfo webAppFileLocatorInfo = new ComponentInfo(
				"WebAppResourceLocator", WebAppResourceLocatorImpl.class,
				IResourceLocator.class, LifestyleType.SINGLETON_TYPE,
				webAppFileLocatorConfig);
		manager.getComponent(webAppFileLocatorInfo);
		String contextName = ctx.getServletContextName();
		String tempDir = System.getProperty("java.io.tmpdir");
		String webAppTempDir = new File(tempDir, contextName).getAbsolutePath();
		HashMapConfiguration webAppTempDirResourceLocatorConfig = new HashMapConfiguration();
		webAppTempDirResourceLocatorConfig.setProperty("RootPath",
				webAppTempDir);
		webAppTempDirResourceLocatorConfig.setProperty(
				"CreateFolderIfNonexistent", new Boolean(true));
		ComponentInfo webAppTempDirResourceLocatorInfo = new ComponentInfo(
				"WebAppTemporaryDirectoryLocator",
				FileSystemResourceLocatorImpl.class,
				INamedResourceLocator.class, LifestyleType.SINGLETON_TYPE,
				webAppTempDirResourceLocatorConfig);
		INamedResourceLocator webAppTempDirResourceLocator = (INamedResourceLocator) manager
				.getComponent(webAppTempDirResourceLocatorInfo);
		HashMapConfiguration appInfoConfig = new HashMapConfiguration();
		appInfoConfig.setProperty("ServletContext", ctx);
		ComponentInfo appInfoCompInfo = new ComponentInfo(
				"ApplicationInformation", ApplicationInformationImpl.class,
				IApplicationInformation.class, LifestyleType.SINGLETON_TYPE,
				appInfoConfig);
		IApplicationInformation appInfo = (IApplicationInformation) manager
				.getComponent(appInfoCompInfo);
		ctx.setAttribute("componentManager", manager);
	}

	/** This function is called by the servlet container and destroys the servlet
	 * instance.
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent) */
	public void contextDestroyed(ServletContextEvent destroyEvent) {
		super.contextDestroyed(destroyEvent);
	}

	/** This function sets up the resource locator
	 * 
	 * @param servletContext servlet context to use */
	private void setupResourceLocator(ServletContext servletContext) {
		getLog().info("UTC setupResourceLocator");
		getLog().info("NLE setupResourceLocator");
		HashMapConfiguration webAppFileLocatorConfig = new HashMapConfiguration();
		webAppFileLocatorConfig.setProperty(
				WebAppResourceLocatorImpl.CONTAINER_CTX_CONFIG_PARAM,
				servletContext);
		ComponentInfo webAppFileLocatorInfo = new ComponentInfo(
				UTCConfResourceLocators.WEB_APP_RESOURCE_LOCATOR_COMP_NAME,
				WebAppResourceLocatorImpl.class.getName(),
				IResourceLocator.class.getName(), LifestyleType.SINGLETON_TYPE,
				webAppFileLocatorConfig);
		IComponentManager componentManager = (IComponentManager) servletContext
				.getAttribute("componentManager");
		componentManager.registerComponent(webAppFileLocatorInfo, true);
		servletContext.setAttribute("componentManager", componentManager);
	}
}