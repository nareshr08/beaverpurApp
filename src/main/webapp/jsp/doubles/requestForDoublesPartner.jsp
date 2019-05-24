<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>Looking for Doubles Partner</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.0/jquery.mobile-1.4.0.min.css" />
	<script src="../javascript/jquery/jquery-1.10.2.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.4.0.min.js"></script>
	<script src="../javascript/tennisStore.js"></script>
	<script src="../javascript/jquery/jquery.validate.js"></script>
</head>
<body>
	<div data-role="page" data-content-theme="a" id="ballsDistribution" data-title="Request for Doubles Partner">
		<div data-role="header" data-content-theme="a"
			data-add-back-btn="true">
			<a href="/index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
			<h6>Request for Doubles Partner</h6>
		</div>
		<div data-role="content">
			<c:if test="${error != null}">
				<span style="color: red;font-style:italic;font-size:12px;"><c:out value="${error}"/></span>
			</c:if>
			<p>Please enter your details here and we will help you in finding a partner. 
			<br/>
			<form action="/requestForPartner" method="post" id="partnerForDoubles" onsubmit="submitRequest();">
				<fieldset data-role="fieldcontain">
					<label for="playerName">Name:</label>
					<input type="text" name="playerName" id="playerName" class="required" value=""/>
				</fieldset>
				<fieldset data-role="fieldcontain">
					<label for="emailAddress">Email Address:</label>
					<input type="email" name="emailAddress" id="emailAddress" class="required" value=""/>
				</fieldset>
				<fieldset data-role="fieldcontain">
					<label for="contactNumber">Phone No:</label>
					<input type="number" name="contactNumber" id="contactNumber" class="required" value=""/>
				</fieldset>
				<fieldset data-role="fieldcontain">
					<label for="rating">Rating (Self or USTA)*:</label>
						<select name="rating" id="rating">
							<option value="2.0">Beginner (2.0-2.5)</option>
							<option value="3.0">Intermediate (3.0)</option>
							<option value="3.5">Intermediate Advanced (3.5)</option>
							<option value="4.0">Advanced (4.0+)</option>
						</select>
					<label for="rating">*We will use it to match a player of your caliber</label>
				</fieldset>
				<fieldset data-role="fieldcontain">
					<label for="comments">Any comments(optional):</label>
					<textarea cols="20" rows="8" name="comments" id="comments"></textarea>
				</fieldset>
				<div id="formSubmitDiv">
						<input type="submit" data-theme="a" name="submit" id="scoresButton" value="Submit Request">
				</div>
			</form>
		</div>
	</div>
</body>
</html>