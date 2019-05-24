<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>Group Details for Team Tennis</title>
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
		<div data-role="page" data-content-theme="a" id="teamTennisGroupRoundInfo" data-title="Group Round info for Team Tennis 2018">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
				<h6>Group Round</h6>
			</div>
			<div data-role="content">
				<form action="/teamTennisGroupRoundInfo" method="POST">
					<label for="select-choice-0" class="select">Select Group:</label>
					<select name="groupRound" onchange="this.form.submit()">
						<option value="All" selected>All</option>
						<option value="Alpha" ${selectedGroup=='Alpha'? 'selected="selected"' : ''}>Alpha</option>
						<option value="Beta" ${selectedGroup=='Beta'? 'selected="selected"' : ''}>Beta</option>
					</select>
				</form>
				<table data-role="table" id="my-table" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
	  				<tbody>
						<c:forEach var="teamsGroup" items="${teamsList}">
							<span style="text-align:left;color:green;font-weight:bold;">Total Teams for ${teamsGroup.key}: ${fn:length(teamsGroup.value)}</span><br/>
							<table data-role="table" id="my-table" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
								<thead>
						    		<tr>
							      	<!-- <th>Cans</th>-->
								      	<th>Team Name</th>
								      	<th>Availability</th>
								      	<th>Captain</th>
								      	<th>Email Address</th>
								      	<th>Captain Rating</th>
								      	<th>Contact Number</th>
								      	<th>Player2 Name</th>
								      	<th>Player2 Rating</th>
								      	<th>Player3 Name</th>
								      	<th>Player3 Rating</th>
								      	<th>Player4 Name</th>
								      	<th>Player4 Rating</th>
						    		</tr>
		  						</thead>
		  						<tbody>
									<c:forEach items="${teamsGroup.value}" var="team" varStatus="counter">
						    			<tr>
											<td><c:out value="${team.teamName}"/></td>
											<td><c:out value="${team.availability}"/></td>
											<td><c:out value="${team.captainname}"/></td>
											<td> <a href="mailto:${team.player1emailaddress}" class="ui-link-inherit"><c:out value="${team.player1emailaddress}"/></td>
											<td><c:out value="${team.captainrating}"/></td>
											<td><c:out value="${team.captaincontactnumber}"/></td>
											<td><c:out value="${team.player2name}"/></td>
											<td><c:out value="${team.player2rating}"/></td>
											<td><c:out value="${team.player3name}"/></td>
											<td><c:out value="${team.player3rating}"/></td>
											<td><c:out value="${team.player4name}"/></td>
											<td><c:out value="${team.player4rating}"/></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<br/>
						</c:forEach>
					</tbody>
				</table>
				<br/>
			</div>
			<div data-role="footer">
				<span style="text-align:left;font-style:italic;font-size:10px;">For more details please visit <a href="https://sites.google.com/site/beaverpuropentennis/home/season-2017/team-tennis-tournament/groups">Desktop version</a></span>
			</div>
		</div>
	</div>
</body>
</html>