	function loadCountryCode()
	{
		$.post("CCLCountryServlet", {
			tableaction : "countrycode"
			}).done(function(data) {
				var select='';
				 $.each(data, function(i, item) {
				 select+='<option value="';
				 select+=item.countryCode;
				 select+='">';
				 select+=$.trim(item.name);
				 select+='</option>';
				 });
				 
				 $('#addcc').html(select);
				 $('#edcc').html(select);
				 $('#edipmcc').html(select);
				 $('#adipmcc').html(select);
	
		});
	}
	function loadSelectData()
	{
		$.post("CCLCountryServlet", {
			tableaction : "reasonforcontrol"
			}).done(function(data) {
				var select='';
				 $.each(data, function(i, item) {
				 select+='<option value="';
				 select+=item.key;
				 select+='">';
				 select+=$.trim(item.value);
				 select+='</option>';
				 });
				 
				 $('#addroc').html(select);
				 $('#edroc').html(select);
	
		});
	}
	function loadData()
	{
		$.post("CCLCountryServlet", {
			tableaction : "load"
			}).done(function(data) {
			constructccltable(data);
	
		});
	}
	function searchData()
	{
		
		if(!isAllSearchTextBoxEmpty()){
			$.post("CCLCountryServlet", {
				tableaction : "search",
				jurisdiction:$.trim($('#jurisdiction').val()),
				classification:$.trim($('#classification').val()),
				countrycode:$.trim($('#country-code').val()),
				reasonforcontrol:$.trim($('#reason-for-control').val())
				
				}).done(function(data) {
				constructccltable(data);
		
			});
		emptySearchboxes();
		}
		else
			{
			alert("No Data to search.Enter some data in the search text box placed above the table for searching.");
			}
		
	}
	function isAllSearchTextBoxEmpty()
	{
		if ($.trim($('#jurisdiction').val())==""&&
				$.trim($('#classification').val())==""&&
				$.trim($('#country-code').val())==""&&
				$.trim($('#reason-for-control').val())==""
				)
			return true;
		else
			return false;
	}
	function isAllSearchipmTextBoxEmpty()
	{
		if ($.trim($('#cidr').val())==""&&
				$.trim($('#ccbox').val())==""&&
				$.trim($('#type').val())==""
				)
			return true;
		else
			return false;
	}
	function isAllSaveTextBoxEmpty()
	{
		if ($.trim($('#addj').val())==""&&
				$.trim($('#addc').val())==""&&
				$.trim($('#addcc').val())==""&&
				$.trim($('#addroc').val())==""
				)
			return true;
		else
			return false;
	}
	function isAlledSaveTextBoxEmpty()
	{
		if ($.trim($('#edj').val())==""&&
				$.trim($('#edc').val())==""&&
				$.trim($('#edcc').val())==""&&
				$.trim($('#edroc').val())==""
				)
			return true;
		else
			return false;
	}
	function emptySearchboxes()
	{
	$('#jurisdiction').val("");
		$('#classification').val("");
		$('#country-code').val("");
		$('#reason-for-control').val("");
	
	}
	
	function emptySearchipmboxes(){
		$('#cidr').val("");
		$('#ccbox').val("");
		$('#type').val("");
	}
	
	function emptySaveboxes()
	{
	$('#addj').val("");
		$('#addc').val("");
		$('#addcc').val("");
		$('#addroc').val("");
	
	}
	function emptyupdateboxes()
	{
	$('#edj').val("");
		$('#edc').val("");
		$('#edcc').val("");
		$('#edroc').val("");
	
	}
	function constructccltable(data)
	{
		var perhtml = '';
		  $.each(data, function(i, item) {
			  var temp="";
			  var html='';
				  if(typeof item.jurisdiction != 'undefined'){
         	 	  html += '<td class="juris">'+item.jurisdiction+'</td>';
          		  temp+=item.jurisdiction+"&$#";
				  }
				  else{
			 	 html += '<td class="juris">null</td>';
			 	 temp+="null&$#";
				  }
				  if(typeof item.classification != 'undefined'){
                	html += '<td class="classi">'+item.classification+'</td>';
            	 temp+=item.classification+"&$#";
           		  }
			     else{
				 html += '<td class="classi">null</td>';
				 temp+="null&$#";
			     }
				 if(typeof item.countryCode != 'undefined'){
	             html += '<td class="cc">'+item.countryCode+'</td>';
	             temp+=item.countryCode+"&$#";
	             }
			     else{
			     html += '<td class="roc">null</td>';
			     temp+="null&$#";
			     }
			     if(typeof item.reasonForControl != 'undefined'){
			     html += '<td class="roc">'+item.reasonForControl+'</td>';
			      temp+=item.reasonForControl+"&$#";
			      }
				 else{
				 html += '<td class="roc">null</td>';
				 temp+="null&$#";
				 }
				  
				// html+='<td class="editdelete"><img class="icon" src="images/edit.jpg" alt="Edit Record" onclick=editData("'+temp+'")>&nbsp;&nbsp;&nbsp;<img class="icon" src="images/delete.jpg" alt="Delete Record" onclick=deleteData("'+temp+'")> </td>';
			  perhtml += '<tr onclick=editData("'+temp+'")>'+html;
			  perhtml+='</tr>';
        });
		  $('#cclbody').html(perhtml);
	}
	function constructipmtable(data)
	{
		var perhtml = '';
		  $.each(data, function(i, item) {
			  var temp="";
			  var html='';
				  if(typeof item.cdir != 'undefined'){
         	 	  html += '<td class="cidr">'+item.cdir+'</td>';
          		  temp+=item.cdir+"&$#";
				  }
				  else{
			 	 html += '<td class="cidr">null</td>';
			 	 temp+="null&$#";
				  }
				  if(typeof item.countryCode != 'undefined'){
                	html += '<td class="countrycode">'+item.countryCode+'</td>';
            	 temp+=item.countryCode+"&$#";
           		  }
			     else{
				 html += '<td class="countrycode">null</td>';
				 temp+="null&$#";
			     }
				 if(typeof item.type != 'undefined'){
	             html += '<td class="type">'+item.type+'</td>';
	             temp+=item.type+"&$#";
	             }
			     else{
			     html += '<td class="type">null</td>';
			     temp+="null&$#";
			     }
	
				  
				// html+='<td class="editdelete"><img class="icon" src="images/edit.jpg" alt="Edit Record" onclick=editData("'+temp+'")>&nbsp;&nbsp;&nbsp;<img class="icon" src="images/delete.jpg" alt="Delete Record" onclick=deleteData("'+temp+'")> </td>';
			  perhtml += '<tr onclick=editIPMData("'+temp+'")>'+html;
			  perhtml+='</tr>';
        });
		  $('#ipmbody').html(perhtml);
	}
	function addData()
	{
		 $( "#dialog" ).dialog({
             width: 600,
             height: 450
		 });
	}
	function isSaveDataValid(){
		
		var juris=$.trim($('#addj').val());
		var classi=$.trim($('#addc').val());
	
		if(juris.length>50)  {
			return false;}
			if(classi.length>50){
				return false;}
		return true;
	}
	function isedSaveDataValid(){
		
		var juris=$.trim($('#edj').val());
		var classi=$.trim($('#edc').val());
	
		if(juris.length>50)  {
			return false;}
			if(classi.length>50){
				return false;}
		return true;
	}
	function saveData()
	{
		
		if(!isAllSaveTextBoxEmpty() && isSaveDataValid()){
		$.post("CCLCountryServlet", {
			tableaction : "save",
			jurisdiction:$.trim($('#addj').val()),
			classification:$.trim($('#addc').val()),
			countrycode:$.trim($('#addcc').val()),
			reasonforcontrol:$.trim($('#addroc').val())			
			}).done(function(data) {
				if(data)
				 {
					loadData();
					 emptySaveboxes();
					 $( "#dialog" ).dialog("close");
				 }
				else
					{
					 $( "#editorcanceldialog" ).dialog({
			             width: 355,
			             height: 150
					 });
					} 
		});
		
	
		}
		else
			{
			alert("Some parameter are empty.Fill in all the details.");
			}
	}
	function editData(vals)
	{
		var str=vals.split("&$#");
		$.post("CCLCountryServlet", {
			tableaction : "edit",
			jurisdiction:str[0],
			classification:str[1],
			reasonforcontrol:str[3]			
			}).done(function(data) {
				 $.each(data, function(i, item) {
						$('#edj').val(item.jurisdiction);
						$('#edc').val(item.classification);
						$('#oldj').val(item.jurisdiction);
						$('#oldc').val(item.classification);
						$('#edroc').val(item.reasonForControl);
						$('#oldroc').val(item.reasonForControl);
						var cc=item.countryCode.split(",");
						for(i=0;i<cc.length;i++)
							{
							$('#edcc  option[value="' + cc[i] + '"]').prop('selected',true);
							}
				 });
		});
		
		
		
		 $( "#editdialog" ).dialog({ width: 600,
             height: 450});
	}
	
	function editIPMData(vals)
	{
		var str=vals.split("&$#");
		$.post("IPMCountryMappingServlet", {
			tableaction : "edit",
			cdir:str[0],
			countrycode:str[1],
			type:str[2]			
			}).done(function(data) {
				$('#oldcidr').val(data.cdir);
						$('#edcr').val(data.cdir);
						$('#edtype').val(data.type);
						var cc=data.countryCode;
							$('#edipmcc  option[value="' + cc + '"]').prop('selected',true);
				 		});
		
	
		
		 $( "#editipmdialog" ).dialog({ width: 600,
             height: 450});
	}

	
	function deleteData()
	{
		
		$.post("CCLCountryServlet", {
			tableaction : "delete",
			jurisdiction:$('#edj').val(),
			classification:$('#edc').val(),
			reasonforcontrol:$('#edroc').val()			
			}).done(function(data) {
				loadData();
				$('#editdialog').dialog('close');
			});
		
	}
	function editDatatransfer()
	{
		var str=$('#addj').val();
		str+="&$#";
		str+=$('#addc').val();
		str+="&$#AT&$#";
		str+=$('#addroc').val();
		 emptySaveboxes();
		 $( "#dialog" ).dialog("close");
		 $( "#editorcanceldialog" ).dialog("close");
		 editData(str);
		
	}
	function cancelDialog()
	{ emptySaveboxes();
		 $( "#dialog" ).dialog("close");
		 $( "#editorcanceldialog" ).dialog("close");
	}
	function updateData()
	{
		
		if(!isAlledSaveTextBoxEmpty() && isedSaveDataValid()){
		$.post("CCLCountryServlet", {
			tableaction : "update",
			jurisdiction:$.trim($('#oldj').val()),
			classification:$.trim($('#oldc').val()),
			countrycode:$.trim($('#edcc').val()),
			reasonforcontrol:$.trim($('#oldroc').val()),
			newreasonforcontrol:$.trim($('#edroc').val()),
			newjurisdicion:$.trim($('#edj').val()),
			newclassification:$.trim($('#edc').val())
			}).done(function(data) {
				emptyupdateboxes();
				 loadData();
				 $( "#editdialog" ).dialog("close");
			});
	
		
		}
		else
			{
			alert("Some parameter are empty.Fill in all the details. Reason for control length should be 2.");
			}
	}
	$(function() {
		$("#tabs").tabs();
	});
	function loadIPMData()
	{
		$.post("IPMCountryMappingServlet", {
			tableaction : "load"
			}).done(function(data) {
			constructipmtable(data);
	
		});
	}
	function addipmData()
	{
		 $( "#ipmdialog" ).dialog({
             width: 600,
             height: 270
		 });
	}
	function isAllIPMSaveTextBoxEmpty()
	{
		if ($.trim($('#adcr').val())==""&&
				$.trim($('#adipmcc').val())==""&&
				$.trim($('#adtype').val())==""
				)
			return true;
		else
			return false;
	}
	function isIPMSaveDataValid()
	{
		if($.trim($('#adtype').val()).length>20)
			return false;
		var reg = /(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([1-2]\d|3[0-2]|\d))/;
		if (!(reg.test($('#adcr').val()))) {
		  return false;
		}
		return true;
		
	}
	function isAlleditIPMSaveTextBoxEmpty()
	{
		if ($.trim($('#edcr').val())==""&&
				$.trim($('#edipmcc').val())==""&&
				$.trim($('#edtype').val())==""
				)
			return true;
		else
			return false;
	}
	function iseditIPMSaveDataValid()
	{
		if($.trim($('#edtype').val()).length>20)
			return false;
		var reg = /(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([1-2]\d|3[0-2]|\d))/;
		if (!(reg.test($('#edcr').val()))) {
		  return false;
		}
		return true;
		
	}
	function saveIPMData()
	{
		if(!isAllIPMSaveTextBoxEmpty() && isIPMSaveDataValid()){
			$.post("IPMCountryMappingServlet", {
				tableaction : "save",
				cdir:$.trim($('#adcr').val()),
				countrycode:$.trim($('#adipmcc').val()),
				type:$.trim($('#adtype').val())
				}).done(function(data) {
					
					if(data.key=="false")
					 {
						loadIPMData();
						 emptyipmSaveboxes();
						 $( "#ipmdialog" ).dialog("close");
					 }
					else
						{
						$("#clash").html(data.value);
						$('#dialogname').val("#ipmdialog");
						
						 $( "#editorcancelipmdialog" ).dialog({
				             width: 355,
				             height: 150
						 });
						} 
			});
			
		
			}
			else
				{
				alert("CIDR should be of following fomat IPV4 Address/Integer values eg 192.36.54.2/12. Type should be of length less than 20.Select a country.");
				}
	}
	function updateipmData()
	{
		if(!isAlleditIPMSaveTextBoxEmpty() && iseditIPMSaveDataValid()){
			$.post("IPMCountryMappingServlet", {
				tableaction : "update",
				cdir:$.trim($('#edcr').val()),
				countrycode:$.trim($('#edipmcc').val()),
				type:$.trim($('#edtype').val()),
				oldcdir:$.trim($('#oldcidr').val())
				
				}).done(function(data) {
					
					if(data.key=="false")
					 {
						loadIPMData();
						 $( "#editipmdialog" ).dialog("close");
					 }
					else
						{
						$("#clash").html(data.value);
						$('#dialogname').val("#editipmdialog");
						 $( "#editorcancelipmdialog" ).dialog({
				             width: 355,
				             height: 150
						 });
						} 
			});
			
		
			}
			else
				{
				alert("CIDR should be of following fomat IPV4 Address/Integer values eg 192.36.54.2/12. Type should be of length less than 20.Select a country.");
				}
	}
	function searchipmData()
	{
		if(!isAllSearchipmTextBoxEmpty()){
			$.post("IPMCountryMappingServlet", {
				tableaction : "search",
				cdir:$.trim($('#cidr').val()),
				countrycode:$.trim($('#ccbox').val()),
				type:$.trim($('#type').val())
				
				}).done(function(data) {
				constructipmtable(data);
		
			});
		emptySearchipmboxes();
		}
		else
			{
			alert("No Data to search.Enter some data in the search text box placed above the table for searching.");
			}
	}
	
	function deleteipmData()
	{
		$.post("IPMCountryMappingServlet", {
			tableaction : "delete",
			cdir:$.trim($('#edcr').val()),
			countrycode:$.trim($('#edipmcc').val()),
			type:$.trim($('#edtype').val())
			
			}).done(function(data) {
				 loadIPMData();
				 $( "#editipmdialog" ).dialog("close");
	
		});
	}
	function cancelipmDialog()
	{
		emptyipmSaveboxes();
		 $( $('#dialogname').val()).dialog("close");
		 $( "#editorcancelipmdialog" ).dialog("close");
		 
	}
	function emptyipmSaveboxes()
	{
		$('#adcr').val("");
		$('#adipmcc').val("");
		$('#adtype').val("");
	}
