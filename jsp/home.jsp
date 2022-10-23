<!doctype html>
<%@page import="com.nextlabs.utc.conf.helper.CommonConstants"%>
<%@page import="com.nextlabs.utc.conf.UTCConfComponentImpl"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.nextlabs.utc.conf.helper.UserGroupHelper"%>
<%@ page
	import="com.bluejungle.destiny.webui.framework.context.AppContext"%>
<html lang="en">

<head>
<meta charset="utf-8">
<meta name="COPYRIGHT" content="© NextLabs">

<title>Configuration Manager</title>
<link rel="stylesheet" href="css/jquery.ui.all.css">
<script src="javascript/jquery-1.9.1.js"></script>
<script src="javascript/jquery.ui.core.js"></script>
<script src="javascript/jquery.ui.widget.js"></script>
<script src="javascript/jquery.ui.tabs.js"></script>
<link href="css/table/jquery-ui-1.8.16.custom.css" rel="stylesheet"
	type="text/css" />
<link href="css/table/lightgray/jtable.css" rel="stylesheet"
	type="text/css" />
<script src="javascript/home.js" type="text/javascript"></script>
<script src="javascript/jquery-ui.js" type="text/javascript"></script>
<script src="javascript/jquery.jtable.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/demos.css">
<script>
	$(document).ready(function() {
		loadData();
		$(".jtable tr:nth-child(even)").addClass("even");
		$(".jtable tr:nth-child(odd)").addClass("odd");
		loadSelectData();
		loadCountryCode();
	});
</script>
<% boolean isWriter = false;
	 String searchName = "uisearchbutton1";
	 String ipmSearchName = "uiipmsearchbutton1";%>
</head>
<body>
<body class="Text TextMedium Link LayoutP">



	<div id="cbWrapper">
		<div id="cbHeader">
			<img class="box1" src="images/header.jpg"> <span id="login">Logged
				in as: <%
				
 	AppContext myContext = AppContext.getContext(request);
 	UserGroupHelper ugh = new UserGroupHelper();
 	session.setAttribute("request", request);
 	if (myContext.getRemoteUser() != null
 			&& myContext.getRemoteUser().getPrincipalId() == 0) {
 		out.print(myContext.getRemoteUser().getUsername());
 		isWriter = true;
 	} else if (myContext.getRemoteUser() != null) {
 		ArrayList<String> groupList = ugh.getGroupForUser(myContext
 				.getRemoteUser());
 		UTCConfComponentImpl.log.info("groupList"
				+ groupList);
 		UTCConfComponentImpl.log.info("before validation isWriter"
				+ isWriter);
 		if (groupList != null
 				&& groupList.contains(CommonConstants.CONFIG_WRITE)) {
 			isWriter = true;
 		
 		}
 		UTCConfComponentImpl.log.info("isWriter"
				+ isWriter);
 		out.print(myContext.getRemoteUser().getUsername());
 	} else
 		{getServletContext().getRequestDispatcher("/login/login.jsf")
 				.forward(request, response);}
 	request.setAttribute("isWriter", isWriter);
 %> |<a href="LogOutServlet">
					Logout</a>
			</span>
		</div>
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1" onclick="loadData()">CCL Countries</a></li>
				<li><a href="#tabs-2" onclick="loadIPMData()">IP Country
						Mapping</a></li>

			</ul>
			<div id="tabs-1">
				<div class="uiaddbutton">
					<%
						if (isWriter) {
							out.print("<button class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'	role='button' onclick='addData()'><span class='ui-button-text'>Add New</span></button>");
							searchName = "uisearchbutton";
						}
					%>
				</div>
				<div class="<%=searchName%>">
					<input class="tb10" id="jurisdiction" name="jurisdiction"
						type="text"> <input class="tb10" id="classification"
						name="classification" type="text"> <input class="tb10"
						id="country-code" name="countrycode" type="text"> <input
						class="tb10" id="reason-for-control" name="reasonforcontrol"
						type="text">


					<button
						class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
						role="button" onclick="searchData()">
						<span class="ui-button-text">Search </span>
					</button>
				</div>
				<div class="ccltable-container">
					<div id="cclContainer" class="jtable-main-container">
						<table class="jtable"  style="table-layout: fixed;">
							<thead>
								<tr>
									<th class="juris">Jurisdiction</th>
									<th class="classi">Classification</th>
									<th class="cc">Country Code</th>
									<th class="roc">Reason For Control</th>
									<!-- <th class="editdelete">Edit/Delete</th>-->
								</tr>
							</thead>
							<tbody id="cclbody">

							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div id="tabs-2">
				<div class="uiipmaddbutton">
					<%
						if (isWriter) {
							out.print("<button class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'	role='button' onclick='addipmData()'><span class='ui-button-text'>Add New</span></button>");
							ipmSearchName = "uiipmsearchbutton";
						}
					%>
				</div>
				<div class="<%=ipmSearchName%>">
					<input class="tb10" id="cidr" name="cidr" type="text"> <input
						class="tb10" id="ccbox" name="ccbox" type="text"> <input
						class="tb10" id="type" name="type" type="text">



					<button
						class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
						role="button" onclick="searchipmData()">
						<span class="ui-button-text">Search </span>
					</button>
				</div>
				<div class="ipmtable-container">
					<div id="ipmContainer" class="jtable-main-container">
						<table id='ipm' class="jtable"  style="table-layout: fixed;">
							<thead>
								<tr>
									<th class="cidr">CIDR</th>
									<th class="countrycode">CountryCode</th>
									<th class="type">Type</th>

								</tr>
							</thead>
							<tbody id="ipmbody">
								<td class="cidr">CIDR
								</th>
								<td class="countrycode">CountryCode
								</th>
								<td class="type">Type
								</th>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="dialog.jsp" />
</body>
</html>
