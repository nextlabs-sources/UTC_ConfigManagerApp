package com.nextlabs.utc.conf.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.nextlabs.utc.conf.UTCConfComponentImpl;
import com.nextlabs.utc.conf.bean.KeyValueBean;
import com.nextlabs.utc.conf.hibernate.CCLCountry;
import com.nextlabs.utc.conf.hibernate.IpcountryMapping;

public class Utils {

	public static List<CCLCountry> groupCountryCode(List<CCLCountry> cclList) {
		List<CCLCountry> resultList = new ArrayList<CCLCountry>();
		HashMap<String, HashMap<String, HashMap<String, String>>> gcc = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		if (cclList != null) {
			UTCConfComponentImpl.log.info("CCL List " + cclList);
			for (CCLCountry ccl : cclList) {
				UTCConfComponentImpl.log.info(" CCLCountry :"
						+ ccl.getJurisdiction() + "," + ccl.getClassification()
						+ "," + ccl.getReasonForControl() + ","
						+ ccl.getCountryCode());
				if (ccl != null) {
					if (gcc.get(ccl.getJurisdiction()) == null) {
						HashMap<String, String> rocmap = new HashMap<String, String>();
						rocmap.put(ccl.getReasonForControl(),
								ccl.getCountryCode());

						HashMap<String, HashMap<String, String>> classimap = new HashMap<String, HashMap<String, String>>();
						classimap.put(ccl.getClassification(), rocmap);

						gcc.put(ccl.getJurisdiction(), classimap);
					} else {
						if (gcc.get(ccl.getJurisdiction()).get(
								ccl.getClassification()) == null) {
							HashMap<String, String> rocmap = new HashMap<String, String>();
							rocmap.put(ccl.getReasonForControl(),
									ccl.getCountryCode());

							gcc.get(ccl.getJurisdiction()).put(
									ccl.getClassification(), rocmap);
						} else {
							if (gcc.get(ccl.getJurisdiction())
									.get(ccl.getClassification())
									.get(ccl.getReasonForControl()) == null) {
								gcc.get(ccl.getJurisdiction())
										.get(ccl.getClassification())
										.put(ccl.getReasonForControl(),
												ccl.getCountryCode());

							} else {

								String existingCC = gcc
										.get(ccl.getJurisdiction())
										.get(ccl.getClassification())
										.get(ccl.getReasonForControl());

								gcc.get(ccl.getJurisdiction())
										.get(ccl.getClassification())
										.put(ccl.getReasonForControl(),
												existingCC
														+ CommonConstants.SEPARATOR
														+ ccl.getCountryCode());
							}
						}
					}

				}
			}

			Set<String> jurisKeyList = gcc.keySet();
			for (String jurisdiction : jurisKeyList) {
				Set<String> classiKeyList = gcc.get(jurisdiction).keySet();
				for (String classification : classiKeyList) {
					Set<String> rocKeyList = gcc.get(jurisdiction)
							.get(classification).keySet();
					for (String roc : rocKeyList) {
						CCLCountry tempccl = new CCLCountry();
						tempccl.setJurisdiction(jurisdiction);
						tempccl.setClassification(classification);
						tempccl.setReasonForControl(roc);
						tempccl.setCountryCode(gcc.get(jurisdiction)
								.get(classification).get(roc));
						resultList.add(tempccl);
					}
				}
			}
		}
		return resultList;
	}

	public static List<KeyValueBean> getReasonForControlKeyValueList(String path) {
		UTCConfComponentImpl.log.info("Properties Path: "+path);
		
		Properties prop = loadProperties(path);
		List<KeyValueBean> kvbList = new ArrayList<KeyValueBean>();
		if (prop != null) {
			int loopend = Integer.parseInt(prop.getProperty("total_no_rfc"));
			for (int count = 1; count <= loopend; count++) {
				KeyValueBean kvb = new KeyValueBean();
				kvb.setKey(prop.getProperty("key" + count));
				kvb.setValue(prop.getProperty("value" + count));
				kvbList.add(kvb);
			}

		}
		return kvbList;
	}

	public static Properties loadProperties(String name) {
		if (name == null)
			throw new IllegalArgumentException("null input: name");

		Properties result = null;
		try {
			UTCConfComponentImpl.log.info("Properties files name : "+name);
			
		
			InputStream inputStream = UTCConfComponentImpl.class.getClassLoader().getResourceAsStream(name);
			
			UTCConfComponentImpl.log.info("Properties files exists? : "+inputStream);
			//UTCConfComponentImpl.log.info("Properties file absolute path: "+file.getAbsolutePath());
			
			if (inputStream != null) {
		
				result = new Properties();
				result.load(inputStream); // Can throw IOException
			}
		} catch (Exception e) {
			UTCConfComponentImpl.log.error("Exception e : ",e);
			result = null;
		}
		return result;
	}

	public static KeyValueBean isInputWithin(IpcountryMapping ipm,
			List<IpcountryMapping> ipmlist) {
		KeyValueBean kvb = new KeyValueBean();
		kvb.setKey("false");

		if (ipm.getCdir() != null) {
			SubNetUtils su = new SubNetUtils(ipm.getCdir());
			try {

				String highAddress = su.getInfo().getHighAddress();
				String lowAddress = su.getInfo().getLowAddress();

				for (IpcountryMapping ip : ipmlist) {
					SubNetUtils ipsu = new SubNetUtils(ip.getCdir());
					if (ipsu.getInfo().isInRange(highAddress)
							|| ipsu.getInfo().isInRange(lowAddress)) {

						kvb.setKey("true");
						kvb.setValue(ip.getCdir());
						return kvb;

					}
				}

			} catch (Exception e) {
				UTCConfComponentImpl.log.error("isInputWithin " ,e);
			}
		}
		return kvb;
	}

	public static List<IpcountryMapping> removeTheValueGoingTobeEdited(
			List<IpcountryMapping> ipmlist, String oldcdir) {
		List<IpcountryMapping> ripmlist = new ArrayList<IpcountryMapping>();
		for (IpcountryMapping ipm : ipmlist) {
			if (!ipm.getCdir().trim().equals(oldcdir))
				ripmlist.add(ipm);
		}
		return ripmlist;
	}

}
