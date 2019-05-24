<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>Scores update failed</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css" />
	<script src="../javascript/jquery/jquery-1.9.1.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.3.2.min.js"></script>
	<script src="../javascript/tennisStore.js"></script>
	<script src="../javascript/jquery/jquery.validate.js"></script>
</head>
<body>
	<div data-role="page" data-content-theme="b" id="failedScoresUpdate" data-title="Reporting Scores for Singles 2017">
		<div data-role="header" data-content-theme="b"
			data-add-back-btn="true">
			<a href="index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
			<h6>Failed to update scores</h6>
		</div>
		<div data-role="content">
			<p>Sorry!! Something went wrong in reporting score. But we emailed Organizers with your scores. They will review and update them manually for you. For any clarifications <a href="mailto:beaverpurtennis@gmail.com" class="ui-link-inherit">email</a> Organizers.</p>
			<br/>
			
		</div>
	</div>
</body>
</html>