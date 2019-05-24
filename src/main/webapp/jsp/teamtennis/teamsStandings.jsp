<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>Standing Details for Team Tennis</title>
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
		<div data-role="page" data-content-theme="a" id="teamssStandings" data-title="Teams Standing for Team Tennis 2018">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="/index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
				<h3>Standing Info</h3>
			</div>
			<div data-role="content">
				<%-- <form action="/teamTennisStanding" method="POST">
					<label for="select-choice-0" class="select">Select Group:</label>
					<select name="groupRound" onchange="this.form.submit()">
						<option value="All" selected>All</option>
						<option value="Alpha" ${selectedGroup=='Alpha'? 'selected="selected"' : ''}>Alpha</option>
						<option value="Beta" ${selectedGroup=='Beta'? 'selected="selected"' : ''}>Beta</option>
					</select>
				</form> --%>
				<c:forEach var="teamsGroup" items="${teamsList}">
					<span style="text-align:left;color:green;font-weight:bold;">Standings</span><br/>
					<table data-role="table" id="my-table" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
	  					<thead>
					    	<tr>
					      	<th>Team Name</th>
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
							<c:forEach items="${teamsGroup.value}" var="team" varStatus="counter">
			    				<tr>
									<td><c:out value="${team.name}"/></td>
									<td><c:out value="${team.played}"/></td>
									<td><c:out value="${team.wins}"/></td>
									<td><c:out value="${team.loss}"/></td>
									<td><c:out value="${team.points}"/></td>
									<td><c:out value="${team.penalty}"/></td>
									<td><c:out value="${team.setsDiff}"/></td>
									<td><c:out value="${team.gamesDiff}"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<br/>
				</c:forEach>
					<br/>
			</div>
			<div data-role="footer">
				<span style="text-align:left;font-style:italic;font-size:12px;">For more details please visit <a href="https://sites.google.com/site/beaverpuropentennis/home/season-2018/team-tennis-tournament/standings">Desktop version</a></span>
			</div>
		</div>
	</div>
</body>
</html>