package com.nextlabs.utc.conf.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.expression.Order;

import com.bluejungle.framework.crypt.IDecryptor;
import com.bluejungle.framework.crypt.ReversibleEncryptor;
import com.nextlabs.utc.conf.UTCConfComponentImpl;
import com.nextlabs.utc.conf.hibernate.CCLCountry;
import com.nextlabs.utc.conf.hibernate.CountryMaster;
import com.nextlabs.utc.conf.hibernate.IpcountryMapping;

public class HibernateDataAccessor {
	SessionFactory sessionFactory;
	static HibernateDataAccessor hb = null;

	private HibernateDataAccessor(HashMap<String, String> dbMap) {
		// A SessionFactory is set up once for an application
		UTCConfComponentImpl.log.info("dbMap in constructor"+dbMap);
		try {
			Configuration config = new Configuration();
			if (dbMap != null) {
				IDecryptor decryptor = new ReversibleEncryptor();
				config.setProperty("hibernate.connection.url", dbMap.get("dburl"));
				config.setProperty("hibernate.connection.username", dbMap.get("un"));
				config.setProperty("hibernate.connection.password", decryptor.decrypt(dbMap.get("pw")));
				config.setProperty("hibernate.dialect", dbMap.get("dialect"));
				config.setProperty("hibernate.connection.driver_class",
						dbMap.get("driver"));
			}

			sessionFactory = config.configure().buildSessionFactory();

		} catch (HibernateException e) {
			UTCConfComponentImpl.log.error(
					"Error while creating session factory", e);
		}
	}

	public static HibernateDataAccessor getInstance(
			HashMap<String, String> dbMap) {
		UTCConfComponentImpl.log.info("dbMap in getinstance"+dbMap);
		if (hb == null) {
			hb = new HibernateDataAccessor(dbMap);
		}
		return hb;

	}

	public static HibernateDataAccessor getInstance() {
		if (hb == null) {
			return null;
		}
		return hb;

	}

	public List<CCLCountry> selectCCLCountries() {

		Transaction trans;
		List<CCLCountry> result = null;
		try {
			Session session = sessionFactory.openSession();
			trans = session.beginTransaction();
			Query q = session.createQuery("from CCLCountry");
			result = (List<CCLCountry>) q.list();
			trans.commit();
			session.close();
		} catch (Exception e) {
			UTCConfComponentImpl.log.error("Error while selectCCLCountries", e);
		}
		return result;
	}

	public List<IpcountryMapping> getIPCountryMappingList() {

		Transaction trans;
		List<IpcountryMapping> result = null;
		try {
			Session session = sessionFactory.openSession();
			trans = session.beginTransaction();
			Query q = session.createQuery("from IpcountryMapping");
			result = (List<IpcountryMapping>) q.list();
			trans.commit();
			session.close();
		} catch (Exception e) {
			UTCConfComponentImpl.log.error(
					"Error while getIPCountryMappingList", e);
		}
		return result;
	}

	public IpcountryMapping getIPCountryMapping(IpcountryMapping ipcm) {

		Transaction trans;
		List<IpcountryMapping> result = null;
		IpcountryMapping ip = new IpcountryMapping();
		try {
			if (ipcm != null) {
				Session session = sessionFactory.openSession();
				trans = session.beginTransaction();
				Query q = session
						.createQuery("from IpcountryMapping ipm where ipm.cdir =:cdi");

				if (ipcm.getCdir() != null)
					q.setParameter("cdi", ipcm.getCdir());

				result = (List<IpcountryMapping>) q.list();

				if (result.size() > 0)
					ip = result.get(0);
				trans.commit();
				session.close();
			}
		} catch (Exception e) {
			UTCConfComponentImpl.log
					.error("Error while getIPCountryMapping", e);
		}
		return ip;
	}

	public List<CCLCountry> getCCLCountries(CCLCountry cclCountry) {

		Transaction trans;
		List<CCLCountry> result = null;
		try {
			Session session = sessionFactory.openSession();
			trans = session.beginTransaction();
			HashMap<String, Object> likeMap = new HashMap<String, Object>();
			likeMap = constructSelectEqualCCLQuery(cclCountry);
			String lq = (String) likeMap.remove("query");
			Query q = session.createQuery(lq);
			Set<String> keyList = likeMap.keySet();
			for (String key : keyList)
				q.setParameter(key, likeMap.get(key));
			result = (List<CCLCountry>) q.list();

			trans.commit();
			session.close();
		} catch (Exception e) {
			UTCConfComponentImpl.log.error("Error while getCCLCountries", e);
		}
		return result;
	}

	public List<CountryMaster> selectCountries() {

		Transaction trans;
		List<CountryMaster> result = null;
		try {
			Session session = sessionFactory.openSession();
			trans = session.beginTransaction();
			Query q = session.createQuery("from CountryMaster");
			result = (List<CountryMaster>) q.list();
			trans.commit();
			session.close();
		} catch (Exception e) {
			UTCConfComponentImpl.log.error("Error while selectCCLCountries", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<CCLCountry> searchCCLCountries(CCLCountry cclCountry) {

		Transaction trans;
		List<CCLCountry> result = null;
		try {
			Session session = sessionFactory.openSession();
			trans = session.beginTransaction();
			HashMap<String, Object> likeMap = new HashMap<String, Object>();
			likeMap = constructSelectCCLQuery(cclCountry);
			String lq = (String) likeMap.remove("query");
			Query q = session.createQuery(lq);
			Set<String> keyList = likeMap.keySet();
			for (String key : keyList)
				q.setParameter(key, likeMap.get(key));
			result = (List<CCLCountry>) q.list();
			trans.commit();
			session.close();
		} catch (HibernateException e) {
			UTCConfComponentImpl.log.error("Search CCL Countries", e);
		} catch (Exception e) {
			UTCConfComponentImpl.log.error("Search CCL Countries", e);
		}

		return result;

	}

	private HashMap<String, Object> constructSelectCCLQuery(
			CCLCountry cclCountry) {
		String lq = "";
		StringBuilder likeQuery = new StringBuilder();
		HashMap<String, Object> likeMap = new HashMap<String, Object>();
		if (cclCountry != null) {
			if (cclCountry.getJurisdiction() != null
					&& (!cclCountry.getJurisdiction().trim().isEmpty())) {
				likeQuery.append(" cc.jurisdiction like :juris and ");
				likeMap.put("juris", "%" + cclCountry.getJurisdiction() + "%");
			}
			if (cclCountry.getClassification() != null
					&& (!cclCountry.getClassification().trim().isEmpty())) {
				likeQuery.append(" cc.classification like :class and ");
				likeMap.put("class", "%" + cclCountry.getClassification() + "%");
			}
			if (cclCountry.getCountryCode() != null
					&& (!cclCountry.getCountryCode().trim().isEmpty())) {
				likeQuery.append(" cc.countryCode like :country and ");
				likeMap.put("country", "%" + cclCountry.getCountryCode() + "%");
			}
			if (cclCountry.getReasonForControl() != null
					&& (!cclCountry.getReasonForControl().trim().isEmpty())) {
				likeQuery.append(" cc.reasonForControl like :roc and ");
				likeMap.put("roc", "%" + cclCountry.getReasonForControl() + "%");
			}
			if (cclCountry.getcCLFlag() > -1 && cclCountry.getcCLFlag() < 2) {
				likeQuery.append(" cc.cCLFlag=:ccl and");
				likeMap.put("ccl", Integer.toString(cclCountry.getcCLFlag()));
			}
			lq = likeQuery.toString();
			if (lq.contains("and")) {
				lq = lq.substring(0, lq.lastIndexOf("and"));
				lq = " where " + lq;
			}
		}

		lq = "from CCLCountry cc " + lq;
		likeMap.put("query", lq);
		return likeMap;
	}

	private HashMap<String, Object> constructSelectEqualCCLQuery(
			CCLCountry cclCountry) {
		String lq = "";
		StringBuilder likeQuery = new StringBuilder();
		HashMap<String, Object> likeMap = new HashMap<String, Object>();
		if (cclCountry != null) {
			if (cclCountry.getJurisdiction() != null
					&& (!cclCountry.getJurisdiction().trim().isEmpty())) {
				likeQuery.append(" cc.jurisdiction =:juris and ");
				likeMap.put("juris", cclCountry.getJurisdiction());
			}
			if (cclCountry.getClassification() != null
					&& (!cclCountry.getClassification().trim().isEmpty())) {
				likeQuery.append(" cc.classification =:class and ");
				likeMap.put("class", cclCountry.getClassification());
			}
			if (cclCountry.getCountryCode() != null
					&& (!cclCountry.getCountryCode().trim().isEmpty())) {
				likeQuery.append(" cc.countryCode =:country and ");
				likeMap.put("country", cclCountry.getCountryCode());
			}
			if (cclCountry.getReasonForControl() != null
					&& (!cclCountry.getReasonForControl().trim().isEmpty())) {
				likeQuery.append(" cc.reasonForControl =:roc and ");
				likeMap.put("roc", cclCountry.getReasonForControl());
			}
			if (cclCountry.getcCLFlag() > -1 && cclCountry.getcCLFlag() < 2) {
				likeQuery.append(" cc.cCLFlag=:ccl and");
				likeMap.put("ccl", Integer.toString(cclCountry.getcCLFlag()));
			}
			if (cclCountry.getCclid() > 0) {
				likeQuery.append(" cc.cclid=:ccli and");
				likeMap.put("ccli", cclCountry.getCclid());
			}

			lq = likeQuery.toString();
			if (lq.contains("and")) {
				lq = lq.substring(0, lq.lastIndexOf("and"));
				lq = " where " + lq;
			}
		}

		lq = "from CCLCountry cc " + lq;

		likeMap.put("query", lq);
		return likeMap;
	}

	public boolean addCCLCountry(CCLCountry cclCountry) {
		boolean result = false;
		try {
			Transaction trans;
			Session session = sessionFactory.openSession();
			trans = session.beginTransaction();
			List<CCLCountry> resultList = null;
			if (cclCountry != null) {
				HashMap<String, Object> likeMap = new HashMap<String, Object>();
				likeMap = constructSelectEqualCCLQuery(cclCountry);
				String lq = (String) likeMap.remove("query");
				Query q = session.createQuery(lq);
				Set<String> keyList = likeMap.keySet();
				for (String key : keyList)
					q.setParameter(key, likeMap.get(key));
				resultList = (List<CCLCountry>) q.list();
				if (resultList.size() == 0) {
					CCLCountry ccl = (CCLCountry) session
							.createCriteria(CCLCountry.class)
							.addOrder(Order.desc("cclid")).setMaxResults(1)
							.uniqueResult();
					if (ccl != null)
						cclCountry.setCclid(ccl.getCclid() + 1);
					else
						cclCountry.setCclid(1);
					session.save(cclCountry);
					result = true;
				}
			}
			trans.commit();
			session.close();
		} catch (HibernateException e) {
			UTCConfComponentImpl.log.error("addCCLCountry", e);
		} catch (Exception e) {
			UTCConfComponentImpl.log.error("addCCLCountry", e);
		}
		return result;
	}

	public boolean addIPCountryMapping(IpcountryMapping ipm) {
		boolean result = false;
		try {
			Transaction trans;
			Session session = sessionFactory.openSession();
			trans = session.beginTransaction();

			if (ipm != null) {

				IpcountryMapping ccl = (IpcountryMapping) session
						.createCriteria(IpcountryMapping.class)
						.addOrder(Order.desc("id")).setMaxResults(1)
						.uniqueResult();
				if (ccl != null)
					ipm.setId(ccl.getId() + 1);
				else
					ipm.setId(1);
				session.save(ipm);
				result = true;

			}
			trans.commit();
			session.close();
		} catch (HibernateException e) {
			UTCConfComponentImpl.log.error("addIPCountryMapping", e);
		} catch (Exception e) {
			UTCConfComponentImpl.log.error("addIPCountryMapping", e);
		}
		return result;
	}

	public boolean updateCCLCountry(CCLCountry cclCountry) {
		boolean result = false;
		try {
			Transaction trans;
			Session session = sessionFactory.openSession();

			List<CCLCountry> resultList = null;
			if (cclCountry != null) {
				CCLCountry ccl = (CCLCountry) session
						.createCriteria(CCLCountry.class)
						.addOrder(Order.desc("cclid")).setMaxResults(1)
						.uniqueResult();
				if (cclCountry.getCclid() <= ccl.getCclid()) {
					session.close();
					session = sessionFactory.openSession();
					trans = session.beginTransaction();
					session.update(cclCountry);
					trans.commit();
					result = true;
				}

			}

			session.close();
		} catch (HibernateException e) {
			UTCConfComponentImpl.log.error(" updateCCLCountry", e);
		} catch (Exception e) {
			UTCConfComponentImpl.log.error(" updateCCLCountry", e);
		}
		return result;
	}

	public boolean deleteCCLCountry(CCLCountry cclCountry) {
		boolean result = false;
		List<CCLCountry> resultList = null;
		try {
			Transaction trans;
			Session session = sessionFactory.openSession();
			trans = session.beginTransaction();
			HashMap<String, Object> likeMap = new HashMap<String, Object>();
			likeMap = constructSelectEqualCCLQuery(cclCountry);
			String lq = (String) likeMap.remove("query");
			Query q = session.createQuery(lq);
			Set<String> keyList = likeMap.keySet();
			for (String key : keyList)
				q.setParameter(key, likeMap.get(key));
			resultList = (List<CCLCountry>) q.list();
			for (CCLCountry ccl : resultList) {
				if (ccl != null) {
					session.delete(ccl);
					result = true;
				}
			}

			trans.commit();
			session.close();
		} catch (HibernateException e) {
			UTCConfComponentImpl.log.error("deleteCCLCountry", e);
		} catch (Exception e) {
			UTCConfComponentImpl.log.error("deleteCCLCountry", e);
		}
		return result;
	}

	public static void main(String args[]) {
		CCLCountry cclCountry = new CCLCountry();
		/*
		 * cclCountry.setcCLFlag(1); cclCountry.setClassification("VIId.k9");
		 * cclCountry.setJurisdiction("ECL");
		 * cclCountry.setReasonForControl("AT");
		 * cclCountry.setCountryCode("AF");
		 */
		cclCountry.setCclid(3);
		IpcountryMapping ipm = new IpcountryMapping();
		ipm.setCdir("100.12.23.0/28");
		IDecryptor decryptor = new ReversibleEncryptor();
		System.out.println(decryptor.decrypt("sa1f78f49e437288039751654ece96ede"));
		// HibernateDataAccessor hdAccessor =
		// HibernateDataAccessor.getInstance(dbMap);

	}

	public List<IpcountryMapping> searchIPCountryMapping(IpcountryMapping ipm) {

		String lq = "";
		StringBuilder likeQuery = new StringBuilder();
		HashMap<String, Object> likeMap = new HashMap<String, Object>();
		if (ipm != null) {
			if (ipm.getCdir() != null && (!ipm.getCdir().trim().isEmpty())) {
				likeQuery.append(" ip.cdir like :cdi and ");
				likeMap.put("cdi", "%" + ipm.getCdir() + "%");
			}
			if (ipm.getCountryCode() != null
					&& (!ipm.getCountryCode().trim().isEmpty())) {
				likeQuery.append(" ip.countryCode like :cc and ");
				likeMap.put("cc", "%" + ipm.getCountryCode() + "%");
			}
			if (ipm.getType() != null && (!ipm.getType().trim().isEmpty())) {
				likeQuery.append(" ip.type like :iptype and ");
				likeMap.put("iptype", "%" + ipm.getType() + "%");
			}
			lq = likeQuery.toString();
			if (lq.contains("and")) {
				lq = lq.substring(0, lq.lastIndexOf("and"));
				lq = " where " + lq;
			}
		}

		lq = "from IpcountryMapping ip  " + lq;
		Transaction trans;
		List<IpcountryMapping> result = null;
		try {
			Session session = sessionFactory.openSession();
			trans = session.beginTransaction();
			Query q = session.createQuery(lq);
			Set<String> keyList = likeMap.keySet();
			for (String key : keyList)
				q.setParameter(key, likeMap.get(key));
			result = (List<IpcountryMapping>) q.list();

			trans.commit();
			session.close();
		} catch (Exception e) {
			UTCConfComponentImpl.log.error(
					"Error while searchIPCountryMapping", e);
		}
		return result;

	}

	public boolean deleteIPCountryMapping(IpcountryMapping ipm) {
		boolean result = false;
		List<IpcountryMapping> resultList = null;
		try {
			Transaction trans;
			Session session = sessionFactory.openSession();
			String lq = "";
			trans = session.beginTransaction();
			HashMap<String, Object> likeMap = new HashMap<String, Object>();
			StringBuilder likeQuery = new StringBuilder();
			if (ipm != null) {
				if (ipm.getCdir() != null && (!ipm.getCdir().trim().isEmpty())) {
					likeQuery.append(" ip.cdir =:cdi and ");
					likeMap.put("cdi", ipm.getCdir());
				}
				if (ipm.getCountryCode() != null
						&& (!ipm.getCountryCode().trim().isEmpty())) {
					likeQuery.append(" ip.countryCode =:cc and ");
					likeMap.put("cc", ipm.getCountryCode());
				}
				if (ipm.getType() != null && (!ipm.getType().trim().isEmpty())) {
					likeQuery.append(" ip.type like :iptype and ");
					likeMap.put("iptype", ipm.getType());
				}
				lq = likeQuery.toString();
				if (lq.contains("and")) {
					lq = lq.substring(0, lq.lastIndexOf("and"));
					lq = " where " + lq;
				}
			}

			lq = "from IpcountryMapping ip  " + lq;
			Query q = session.createQuery(lq);
			Set<String> keyList = likeMap.keySet();
			for (String key : keyList)
				q.setParameter(key, likeMap.get(key));
			resultList = (List<IpcountryMapping>) q.list();
			for (IpcountryMapping ip : resultList) {
				if (ip != null) {
					session.delete(ip);
					result = true;
				}
			}

			trans.commit();
			session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			UTCConfComponentImpl.log.error(
					"Error while deleteIPCountryMapping", e);
		}
		return result;

	}

}
