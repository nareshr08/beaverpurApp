<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>Singles Registration</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
	<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.4.0.min.js"></script>
	<script src="../javascript/tennisStore.js"></script>
	<script src="../javascript/jquery/jquery.validate.js"></script>
	<style>
	label.error { 
		float: left; 
		color: red; 
		padding-top: .5em; 
		vertical-align: top; 
		font-weight:bold
	}
	</style>
</head>
<script type="text/javascript">
	$(document).ready(function() {
		$("#registrationForm").validate();
	});
</script>
<body>
	<div>
		<div data-role="page" data-content-theme="a" id="singlesRegistration" data-title="Registrations for Season 2018">
			<div data-role="header" data-content-theme="a"
				data-add-back-btn="true">
				<a href="/index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
				<h6>Registration Form</h6>
			</div>
			<div data-role="content">
				<c:if test="${error != null}">
					<span style="color: red;font-style:italic;font-size:12px;"><c:out value="${error}"/></span>
				</c:if>
				<form action="/processRegistrationForSingles" id="registrationForm" method="post">
					<fieldset data-role="fieldcontain">
						<label for="playerFirstName">First Name:</label>
						<input type="text" name="playerFirstName" id="playerFirstName" class="required" value=""/>
					</fieldset>
					<fieldset data-role="fieldcontain">
						<label for="playerLastName">Last Name:</label>
						<input type="text" name="playerLastName" id="playerLastName" class="required" value=""/>
					</fieldset>
					<fieldset data-role="fieldcontain">
						<label for="emailAddress">Email Address:</label>
						<input type="email" name="emailAddress" placeholder="example@company.com" class="required email" id="emailAddress" value=""/>
					</fieldset>
					<fieldset data-role="fieldcontain">
						<label for="phoneNumber">Phone Number:</label>
						<input type="number" name="phoneNumber" required="required" placeholder="5555555555" id="phoneNumber" class="required" value=""/>
					</fieldset>
					<fieldset data-role="fieldcontain">
						<label for="rating">USTA/Self Rating:</label>
						<input type="text" name="rating" required="required" placeholder="3.0" max="5.0" min="2.0" id="rating" class="required" value=""/>
					</fieldset>
						<input type="submit" data-theme="a" name="submit" value="Next">
						<span>*Payment option will be provided in next screen</span>
				</form>
			</div>
			<div data-role="footer"></div>		
		</div>
	</div>
</body>
</html>