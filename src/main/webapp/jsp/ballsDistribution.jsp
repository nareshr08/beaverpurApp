<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>Balls Distribution</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
	<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.4.0.min.js"></script>
	<script src="../javascript/tennisStore.js"></script>
	<script src="../javascript/jquery/jquery.validate.js"></script>
</head>
<script type="text/javascript">
	$(document).ready(function() {
		$("#ballsDistroForm").validate();
		$('#scoresButton').click(function(){
			var playerName = $('#playerName').val();
			var location = $('#location').val();
			if (!playerName || playerName==''){
				alert('Player Name required');
				return false;
			}
			if (!location || location == 'NA'){
				alert('Location required');
				return false;
			}
			$('#submitRequest').val('submit');
		});
	});
</script>
<body>
	<div data-role="page" data-content-theme="a" id="ballsDistribution" data-title="Scheduling balls Distribution for Singles 2015">
		<div data-role="header" data-content-theme="a"
			data-add-back-btn="true">
			<a href="index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
			<h6>Balls collection schedule</h6>
		</div>
		<div data-role="content">
			<p>Please choose a location where you would like to collect your balls from us (Organizers). We would review the list at 5PM for a given day and make ourselves available at that location between 6PM-7PM. 
			<br/>
			<form action="/ballsDistro" method="post" id="ballsDistroForm" onsubmit="submitRequest();">
				<input type="hidden" id="submitRequest" name="submitRequest" value=""/>
				<div data-role="fieldcontain">
					<fieldset data-role="controlgroup" data-type="horizontal">
						<legend>Date for Pickup:</legend>
						<label for="selectedDay">Day</label>
						<select name="selectedDay" id="selectedDay">
								<option value="${todayDate}">${todayDate}</option>
						</select>
					</fieldset>
				</div>
				<fieldset data-role="fieldcontain">
					<label for="playerName">Player Name:</label>
					<input type="text" name="playerName" id="playerName" class="required" value=""/>
				</fieldset>
				<fieldset data-role="fieldcontain">
					<label for="group" class="select">Select Location:</label>
					<select name="location" id="location">
						<option value="NA">--</option>
						<option value="PCC">PCC Rock Creek</option>
						<option value="5 Oaks">5 Oaks</option>
					</select>
				</fieldset>
				<div id="formSubmitDiv">
						<input type="submit" data-theme="a" name="submit" id="scoresButton" value="Submit Request">
				</div>
			</form>
			<p>Below is the list of Players who requested for Balls today:</p>
			<table data-role="table" id="my-table" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
  				<thead>
				    <tr>
				      <th>No.</th>
				      <th>Player Name</th>
				      <th>Location</th>
				    </tr>
  				</thead>
  				<tbody>
					<c:forEach items="${playersList}" var="player" varStatus="counter">
		   				<tr>
							<td><c:out value="${counter.count}"/></td>
							<td><c:out value="${player.name}"/></td>
							<td><c:out value="${player.emailAddress}"/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>