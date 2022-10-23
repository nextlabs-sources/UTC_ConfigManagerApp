package com.nextlabs.utc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.nextlabs.utc.conf.bean.KeyValueBean;
import com.nextlabs.utc.conf.helper.HibernateDataAccessor;
import com.nextlabs.utc.conf.helper.Utils;
import com.nextlabs.utc.conf.hibernate.IpcountryMapping;

/**
 * Servlet implementation class IPMCountryMappingServlet
 */

public class IPMCountryMappingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IPMCountryMappingServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
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

			PrintWriter pw;
			try {
				pw = response.getWriter();
				HibernateDataAccessor hibernateDataAccessor = HibernateDataAccessor
						.getInstance();
				if (hibernateDataAccessor != null) {
					if (tableAction.equals("load")) {
						List<IpcountryMapping> ipmlist = hibernateDataAccessor
								.getIPCountryMappingList();
						Gson gson = new Gson();
						String toJson = gson.toJson(ipmlist);
						pw.print(toJson);
					} else if (tableAction.equals("edit")) {
						IpcountryMapping ipm = new IpcountryMapping();
						ipm.setCdir(request.getParameter("cdir"));

						ipm = hibernateDataAccessor.getIPCountryMapping(ipm);
						Gson gson = new Gson();
						String toJson = gson.toJson(ipm);

						pw.print(toJson);

					} else if (tableAction.equals("search")) {
						IpcountryMapping ipm = new IpcountryMapping();
						ipm.setCdir(request.getParameter("cdir"));
						ipm.setCountryCode(request.getParameter("countrycode"));
						ipm.setType(request.getParameter("type"));

						List<IpcountryMapping> ipmlist = hibernateDataAccessor
								.searchIPCountryMapping(ipm);
						Gson gson = new Gson();
						String toJson = gson.toJson(ipmlist);

						pw.print(toJson);

					} else if (tableAction.equals("delete")) {
						IpcountryMapping ipm = new IpcountryMapping();
						ipm.setCdir(request.getParameter("cdir"));
						ipm.setCountryCode(request.getParameter("countrycode"));
						ipm.setType(request.getParameter("type"));

						boolean result = hibernateDataAccessor
								.deleteIPCountryMapping(ipm);
						pw.print(result);

					} else if (tableAction.equals("save")) {
						IpcountryMapping ipm = new IpcountryMapping();
						ipm.setCdir(request.getParameter("cdir"));
						ipm.setCountryCode(request.getParameter("countrycode"));
						ipm.setType(request.getParameter("type"));

						List<IpcountryMapping> ipmlist = hibernateDataAccessor
								.getIPCountryMappingList();
						KeyValueBean kvb = Utils.isInputWithin(ipm, ipmlist);
						if (Boolean.parseBoolean(kvb.getKey())) {

							Gson gson = new Gson();
							String toJson = gson.toJson(kvb);
							pw.print(toJson);
						} else {
							hibernateDataAccessor.addIPCountryMapping(ipm);
							Gson gson = new Gson();
							String toJson = gson.toJson(kvb);
							pw.print(toJson);
						}
					} else if (tableAction.equals("update")) {
						IpcountryMapping ipm = new IpcountryMapping();
						ipm.setCdir(request.getParameter("cdir"));
						ipm.setCountryCode(request.getParameter("countrycode"));
						ipm.setType(request.getParameter("type"));

						String oldcdir = request.getParameter("oldcdir");
						List<IpcountryMapping> ipmlist = hibernateDataAccessor
								.getIPCountryMappingList();
						ipmlist = Utils.removeTheValueGoingTobeEdited(ipmlist,
								oldcdir);
						KeyValueBean kvb = Utils.isInputWithin(ipm, ipmlist);
						if (Boolean.parseBoolean(kvb.getKey())) {
							Gson gson = new Gson();
							String toJson = gson.toJson(kvb);
							pw.print(toJson);
						} else {
							IpcountryMapping ip = new IpcountryMapping();
							ip.setCdir(oldcdir);
							hibernateDataAccessor.deleteIPCountryMapping(ip);
							hibernateDataAccessor.addIPCountryMapping(ipm);
							Gson gson = new Gson();
							String toJson = gson.toJson(kvb);
							pw.print(toJson);
						}
					}
				}
			} catch (IOException e) {

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
