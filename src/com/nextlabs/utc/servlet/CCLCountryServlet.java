package com.nextlabs.utc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.nextlabs.utc.conf.UTCConfComponentImpl;
import com.nextlabs.utc.conf.bean.KeyValueBean;
import com.nextlabs.utc.conf.helper.CommonConstants;
import com.nextlabs.utc.conf.helper.HibernateDataAccessor;
import com.nextlabs.utc.conf.helper.Utils;
import com.nextlabs.utc.conf.hibernate.CCLCountry;
import com.nextlabs.utc.conf.hibernate.CountryMaster;

/**
 * Servlet implementation class CCLCountryServlet
 */

public class CCLCountryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public CCLCountryServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      respons
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("application/json");
		String tableAction = request.getParameter("tableaction");
		if (tableAction != null) {
			try {
				PrintWriter pw = response.getWriter();
				HibernateDataAccessor hibernateDataAccessor = HibernateDataAccessor
						.getInstance();
				UTCConfComponentImpl.log.info(hibernateDataAccessor);
				if (hibernateDataAccessor != null) {
					if (tableAction.equals("load")) {
						List<CCLCountry> cclList = hibernateDataAccessor
								.selectCCLCountries();
						cclList = Utils.groupCountryCode(cclList);
						Gson gson = new Gson();
						String toJson = gson.toJson(cclList);

						pw.print(toJson);
					} else if (tableAction.equals("search")) {
						CCLCountry cclCountry = new CCLCountry();
						cclCountry.setJurisdiction(request
								.getParameter("jurisdiction"));
						cclCountry.setClassification(request
								.getParameter("classification"));
						cclCountry.setCountryCode(request
								.getParameter("countrycode"));
						cclCountry.setReasonForControl(request
								.getParameter("reasonforcontrol"));
						cclCountry.setcCLFlag(1);

						List<CCLCountry> cclList = hibernateDataAccessor
								.searchCCLCountries(cclCountry);
						cclList = Utils.groupCountryCode(cclList);
						Gson gson = new Gson();
						String toJson = gson.toJson(cclList);

						pw.print(toJson);

					} else if (tableAction.equals("save")) {
						boolean result;
						CCLCountry cclCountry = new CCLCountry();
						cclCountry.setJurisdiction(request
								.getParameter("jurisdiction"));
						cclCountry.setClassification(request
								.getParameter("classification"));
						cclCountry.setReasonForControl(request
								.getParameter("reasonforcontrol"));
						cclCountry.setcCLFlag(1);
						List<CCLCountry> cclList = hibernateDataAccessor
								.getCCLCountries(cclCountry);
						if (cclList.size() == 0) {
							String countryCode = request
									.getParameter("countrycode");
							if (countryCode != null) {
								StringTokenizer token = new StringTokenizer(
										countryCode, CommonConstants.SEPARATOR);
								while (token.hasMoreTokens()) {
									CCLCountry ccl = cclCountry;
									ccl.setCountryCode(token.nextToken());
									hibernateDataAccessor.addCCLCountry(ccl);
								}
							}
							result = true;
						} else {
							result = false;
						}
						pw.print(result);
					} else if (tableAction.equals("update")) {
						CCLCountry cclCountry = new CCLCountry();
						cclCountry.setJurisdiction(request
								.getParameter("jurisdiction"));
						cclCountry.setClassification(request
								.getParameter("classification"));
						cclCountry.setReasonForControl(request
								.getParameter("reasonforcontrol"));
						cclCountry.setcCLFlag(1);
						hibernateDataAccessor.deleteCCLCountry(cclCountry);
						cclCountry.setReasonForControl(request
								.getParameter("newreasonforcontrol"));
						cclCountry.setJurisdiction(request
								.getParameter("newjurisdicion"));
						cclCountry.setClassification(request
								.getParameter("newclassification"));
						String countryCode = request
								.getParameter("countrycode");
						List<CCLCountry> cclList = new ArrayList<CCLCountry>();
						if (countryCode != null) {
							StringTokenizer token = new StringTokenizer(
									countryCode, CommonConstants.SEPARATOR);

							while (token.hasMoreTokens()) {
								CCLCountry ccl = cclCountry;
								ccl.setCountryCode(token.nextToken());
								boolean result = hibernateDataAccessor
										.addCCLCountry(ccl);
								if (result) {
									cclList.add(ccl);
								}
							}

						}
						Gson gson = new Gson();
						String toJson = gson.toJson(cclList);

						pw.print(toJson);

					} else if (tableAction.equals("delete")) {
						CCLCountry cclCountry = new CCLCountry();
						cclCountry.setJurisdiction(request
								.getParameter("jurisdiction"));
						cclCountry.setClassification(request
								.getParameter("classification"));
						cclCountry.setReasonForControl(request
								.getParameter("reasonforcontrol"));
						cclCountry.setcCLFlag(1);

						boolean result = hibernateDataAccessor
								.deleteCCLCountry(cclCountry);
					
						Gson gson = new Gson();
						String toJson = gson.toJson(result);

						pw.print(toJson);

					} else if (tableAction.equals("edit")) {
						CCLCountry cclCountry = new CCLCountry();
						cclCountry.setJurisdiction(request
								.getParameter("jurisdiction"));
						cclCountry.setClassification(request
								.getParameter("classification"));
						cclCountry.setReasonForControl(request
								.getParameter("reasonforcontrol"));
						cclCountry.setcCLFlag(1);

						List<CCLCountry> cclList = hibernateDataAccessor
								.getCCLCountries(cclCountry);
						cclList = Utils.groupCountryCode(cclList);
						Gson gson = new Gson();
						String toJson = gson.toJson(cclList);

						pw.print(toJson);

					} else if (tableAction.equals("reasonforcontrol")) {
						String path = "/com/nextlabs/utc/conf/properties/commons.properties";
						List<KeyValueBean> kvblist = Utils
								.getReasonForControlKeyValueList(path);
						Gson gson = new Gson();
						String toJson = gson.toJson(kvblist);

						pw.print(toJson);

					} else if (tableAction.equals("countrycode")) {
						List<CountryMaster> cmlist = hibernateDataAccessor
								.selectCountries();
						Gson gson = new Gson();
						String toJson = gson.toJson(cmlist);

						pw.print(toJson);

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}
