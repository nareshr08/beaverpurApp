<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>Scores update failed</title>
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
	<div data-role="page" data-content-theme="a" id="failedScoresUpdateForSingles" data-title="Reporting Scores for Doubles 2015">
		<div data-role="header" data-content-theme="a"
			data-add-back-btn="true">
			<a href="index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
			<h6>Scores already reported!!</h6>
		</div>
		<div data-role="content">
			<p>Looks like scores were already reported for this combination. Click below link to review the standings and <a href="mailto:beaverpurtennis@gmail.com" class="ui-link-inherit">email</a> Organizers for any questions.</p>
			<br/>
			<a href="/doublesStandings?groupRound=${groupRound}">Standings</a>
		</div>
	</div>
</body>
</html>