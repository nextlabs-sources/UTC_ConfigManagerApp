<%@page import="com.nextlabs.utc.conf.UTCConfComponentImpl"%>
<%@page import="com.nextlabs.utc.conf.helper.CommonConstants"%>
<%@page import="java.util.ArrayList"%>
<div id="dialog" title="Add CCL Country Data"
	style="display: none; text-align: center">
	<H3>Add CCL Country Data</H3>
	<table class="dialogtable">
		<thead>
			<tr>
				<th>Jurisdiction</th>
				<td style="background-color: #ECE5B6;"><input class="tb10"
					id="addj" name="jurisdiction" type="text"></td>
			</tr>
			<tr>
				<th>Classification</th>
				<td style="background-color: #ECE5B6;"><input class="tb10"
					id="addc" name="class" type="text"></td>
			</tr>
			<tr>
				<th>Restricted Countries</th>
				<td style="background-color: #ECE5B6;"><Select id="addcc"
					class="tb10" multiple size="10"></Select></td>
			</tr>
			<tr>
				<th>Reason For Control</th>
				<td style="background-color: #ECE5B6;"><Select id="addroc"
					class="tb10"></Select></td>
			</tr>
			<tr>
			</tr>
		</thead>
	</table>

	<br /> <br />
	<%
		boolean isWriter = false;
		if (request.getAttribute("isWriter") != null)
			isWriter = (Boolean) request.getAttribute("isWriter");
		if (isWriter) {
			out.print("<button class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' role='button' onclick='saveData()'>");
			out.println("<span class='ui-button-text' id='dialogbutton'>Save</span></button>");
		}
	%>

	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="$('#dialog').dialog('close');">
		<span class="ui-button-text" id='dialogbutton'>Cancel</span>
	</button>
	<br /> <br /> Note:To select multiple countries, press the
	ctrl|command button and select in the restricted countries list.

</div>

<div id="editorcanceldialog" title="Data Already exists"
	style="display: none; text-align: center">
	There are existing country codes for the given jurisdiction,
	classification and reason for control. If you want to overwrite, modify
	or delete the existing data click on ok button. If you don't want to do
	anything click on cancel button.<br />
	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="editDatatransfer()">
		<span class="ui-button-text" id='dialogbutton'>Ok</span>
	</button>
	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="cancelDialog()">
		<span class="ui-button-text" id='dialogbutton'>Cancel</span>
	</button>
</div>

<div id="editdialog" title="Edit CCL Country Data"
	style="display: none; text-align: center">
	<H3>Edit CCL Country Data</H3>
	<table class="dialogtable">
		<thead>
			<tr>
				<th>Jurisdiction</th>
				<td style="background-color: #ECE5B6;"><input class="tb10"
					id="edj" name="jurisdiction" type="text"><input type="hidden" id="oldj"/></td>
			</tr>
			<tr>
				<th>Classification</th>
				<td style="background-color: #ECE5B6;"><input class="tb10"
					id="edc" name="class" type="text"><input type="hidden" id="oldc"/></td>
			</tr>
			<tr>
				<th>Restricted Countries</th>
				<td style="background-color: #ECE5B6;"><Select id="edcc"
					class="tb10" multiple size="10"></Select></td>
			</tr>
			<tr>
				<th>Reason For Control</th>
				<td style="background-color: #ECE5B6;"><Select id="edroc"
					class="tb10"></Select><input type="hidden" id="oldroc"/></td>
			</tr>
			<tr>
			</tr>
		</thead>
	</table>
	<br /> <br />
	<%
		if (isWriter) {
			out.print("<button class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' role='button' onclick='updateData()'><span class='ui-button-text' id='dialogbutton'>Save</span></button>	");
		}
	%>
	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="$('#editdialog').dialog('close');">
		<span class="ui-button-text" id='dialogbutton'>Cancel</span>
	</button>
	<%
		if (isWriter) {
			out.print("<button class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' role='button' onclick='deleteData()'><span class='ui-button-text' id='dialogbutton'>Delete</span></button>");
		}
	%>

	<br /> <br /> Note:To select multiple countries, press the
	ctrl|command button and select in the restricted countries list.
</div>
<div id="editipmdialog" title="Edit IPMData"
	style="display: none; text-align: center">
	<H3>Edit IP Country Mapping Data</H3>
	<table class="dialogtable">
		<thead>
			<tr>
				<th>CIDR</th>
				<td style="background-color: #ECE5B6;"><input class="tb10"
					id="edcr" name="edcdir" type="text"></td>
			</tr>
			<tr>
				<th>Country Code</th>
				<td style="background-color: #ECE5B6;"><Select id="edipmcc"
					class="tb10"></Select></td>
			</tr>
			<tr>
				<th>Type</th>
				<td style="background-color: #ECE5B6;"><input class="tb10"
					id="edtype" name="jurisdiction" type="text"></Select></td>
			</tr>
			<tr>
			</tr>
		</thead>
	</table>
	<br /> <br />
	<%
		if (isWriter) {
			out.print("<button class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'	role='button' onclick='updateipmData()'>	<span class='ui-button-text' id='dialogbutton'>Save</span>	</button>");
		}
	%>

	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="$('#editipmdialog').dialog('close');">
		<span class="ui-button-text" id='dialogbutton'>Cancel</span>
	</button>
	<%
		if (isWriter) {
			out.print("<button	class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'		role='button' onclick='deleteipmData()'><span class='ui-button-text' id='dialogbutton'>Delete</span>	</button>");
		}
	%>

	<br /> <br /> <input type="hidden" id='oldcidr'>
</div>
<div id="ipmdialog" title="Add IP CountryMapping Data"
	style="display: none; text-align: center">
	<H3>Add IP Country Mapping Data</H3>
	<table class="dialogtable">
		<thead>
			<tr>
				<th>CIDR</th>
				<td style="background-color: #ECE5B6;"><input class="tb10"
					id="adcr" name="edcdir" type="text"></td>
			</tr>
			<tr>
				<th>Country Code</th>
				<td style="background-color: #ECE5B6;"><Select id="adipmcc"
					class="tb10"></Select></td>
			</tr>
			<tr>
				<th>Type</th>
				<td style="background-color: #ECE5B6;"><input class="tb10"
					id="adtype" name="jurisdiction" type="text"></Select></td>
			</tr>
			<tr>
			</tr>
		</thead>
	</table>

	<br /> <br />
	<%
		if (isWriter) {
			out.print("<button 		class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'		role='button' onclick='saveIPMData()'>		<span class='ui-button-text' id='dialogbutton'>Save</span>	</button>");
		}
	%>

	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="$('#ipmdialog').dialog('close');">
		<span class="ui-button-text" id='dialogbutton'>Cancel</span>
	</button>
	<br /> <br />


</div>
<div id="editorcancelipmdialog" title="Data Already exists"
	style="display: none; text-align: center">
	The given CIDR range clashes with the existing CIDR range[<span
		id="clash"></span>]. If you want to modify the data and save once
	again click on ok button. Click on cancel button to close the dialogs.<br />
	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="$('#editorcancelipmdialog').dialog('close');">
		<span class="ui-button-text" id='dialogbutton'>Ok</span>
	</button>
	<button
		class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
		role="button" onclick="cancelipmDialog()">
		<span class="ui-button-text" id='dialogbutton'>Cancel</span>
	</button>
	<input type="hidden" id='dialogname'>
</div>