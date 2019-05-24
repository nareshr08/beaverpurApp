<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>RESTFul Testing</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css" />
	<script src="../javascript/jquery/jquery-1.9.1.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.3.2.min.js"></script>
	<script src="../javascript/tennisStore.js"></script>
	<script src="../javascript/jquery/jquery.validate.js"></script>
</head>
<script type="text/javascript">
	$(document).ready(function() {
		/* $("#restfulTestingForm").validate(); */
		$('#scoresButton').click(function(){
			var $walletID = $('#walletID').val();
			if (!$walletID || $walletID==''){
				alert('walletID required');
				return false;
			}
			$.get('/restfulTest',{WalletID:$walletID},function(responseJson) {
		        var $table = $('#walletList');           
		        if(responseJson.ResponseCode == "0"){
		        	var strHtml ='';
		        	for (var row=0; row<responseJson.WalletDevice.length;row++){
		        		strHtml += 
		        			'<tr>'+
								+'<td>'+row+
								+'<td>'+responseJson.WalletDevice[row].AccountNumberLast4+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].ChargeExpirationMMYY+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].TemporaryAccountNumberToken+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].DeviceType+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].PaymentTypeCD+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].CardHolderFirstName+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].CardHolderLastName+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].CardHolderAddressLine1+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].CardHolderAddressLine2+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].CardHolderCity+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].CardHolderRegion+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].CardHolderPostalCode+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].CardHolderCountryCode+'</td>'+
								+'<td>'+responseJson.WalletDevice[row].MerchantDeviceDescriptor+'</td>'+
								+'</tr>';
		        		;
		        	}
		        	$("table#walletList tbody").html(strHtml);
		        }else{
		        	$("#error").html("<p>Failed to return list: ResponseCode:"+responseJson.ResponseCode+",with ResponseText="+responseJson.ResponseText+"</p>");
		        }
			});
		});
	});
</script>
<body>
	<div data-role="page" data-content-theme="b" id="restfulTesting" data-title="Testing Restful API solution">
		<div data-role="header" data-content-theme="b"
			data-add-back-btn="true">
			<a href="index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
			<h6>RESTFul testing</h6>
		</div>
		<div data-role="content">
			<p>Enter WalletID to retrieve PDOFs list</p>
			<br/>
			<!-- <form action="/restfulTest" method="post" id="restfulTestingForm" onsubmit="submitRequest();"> -->
				<input type="hidden" id="submitRequest" name="submitRequest" value=""/>
				<div data-role="fieldcontain">
					<fieldset data-role="controlgroup" data-type="horizontal">
						<legend>WalletID:</legend>
					    <label for="month">WalletID</label>
						<input type="text" name="walletID" id="walletID" class="required" value=""/>
					</fieldset>
				</div>
				<input type="button" data-theme="b" name="scoresButton" id="scoresButton" value="Submit Request">
				<!-- <div id="formSubmitDiv">
						<input type="submit" data-theme="b" name="submit" id="scoresButton" value="Submit Request">
				</div>
			</form> -->
			<div id="error" style="font-color:red;">Test</div>
			<p>Below is the list of Payment Devices:</p>
			<table data-role="table" id="walletList" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
  				<thead>
				    <tr>
				      <th>No.</th>
				      <th>AccountNumberLast4</th>
				      <th>ChargeExpirationMMYY</th>
				      <th>TemporaryAccountNumberToken</th>
				      <th>DeviceType</th>
				      <th>PaymentTypeCD</th>
				      <th>CardHolderFirstName</th>
				      <th>CardHolderLastName</th>
				      <th>CardHolderAddressLine1</th>
				      <th>CardHolderAddressLine2</th>
				      <th>CardHolderCity</th>
				      <th>CardHolderRegion</th>
				      <th>CardHolderPostalCode</th>
				      <th>CardHolderCountryCode</th>
				      <th>MerchantDeviceDescriptor</th>
				    </tr>
  				</thead>
  				<tbody>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>