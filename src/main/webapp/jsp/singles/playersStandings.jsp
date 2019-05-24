<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>Players Standing Details for Singles</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
	<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.4.0.min.js"></script>
</head>
<script type="text/javascript">
	$(document).ready(function() {
		
	});
</script>
<body>
	<div>
		<div data-role="page" data-content-theme="a" id="playersStandings" data-title="Players Standing for Singles 2019">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="/index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
				<h3>Standing Info</h3>
			</div>
			<div data-role="content">
				<form action="/playersStanding" method="POST">
					<label for="select-choice-0" class="select">Select Group:</label>
					<select name="groupRound" onchange="this.form.submit()">
	    				<c:forEach var="group" items="${groups}">
	        				<option value="${group.key}" ${group.key == selectedGroup ? 'selected="selected"' : ''}>${group.value}</option>
	    				</c:forEach>
					</select>
				</form>
				<c:forEach var="playersGroup" items="${playersList}" varStatus="counter">
					<span style="text-align:left;color:green;font-weight:bold;">Standings of Players in ${playersGroup.key}(${fn:length(playersGroup.value)})</span><br/>

					<table data-role="table" id="my-table" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
	  					<thead>
					    	<tr>
					      	<th>Player Name</th>
					      	<th>Played</th>
					      	<th>Wins</th>
					      	<th>Loss</th>
					      	<th>Points</th>
					      	<th>Penalty</th>
					      	<th>SetsDiff</th>
					      	<th>GamesDiff</th>
					    	</tr>
		  				</thead>
	  					<tbody>
							<c:forEach items="${playersGroup.value}" var="player" varStatus="counter">
			    				<tr>
									<td><c:out value="${player.name}"/></td>
									<td><c:out value="${player.matchesPlayed}"/></td>
									<td><c:out value="${player.wins}"/></td>
									<td><c:out value="${player.losses}"/></td>
									<td><c:out value="${player.points}"/></td>
									<td><c:out value="${player.penalty}"/></td>
									<td><c:out value="${player.setsDiff}"/></td>
									<td><c:out value="${player.gamesDiff}"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<br/>
				</c:forEach>
			</div>
			<div data-role="footer">
				<span style="text-align:left;font-style:italic;font-size:12px;">For more details please visit <a href="https://sites.google.com/site/beaverpuropentennis/home/season-2019/singles-tournament/standings">Desktop version</a></span>
			</div>
		</div>
	</div>
</body>
</html>