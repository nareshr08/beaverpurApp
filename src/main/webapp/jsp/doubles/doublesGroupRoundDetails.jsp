<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>Group Details for Singles</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.0/jquery.mobile-1.4.0.min.css" />
	<script src="../javascript/jquery/jquery-1.10.2.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.4.0.min.js"></script>
</head>
<script type="text/javascript">
	$(document).ready(function() {
		
	});
</script>
<body>
	<div>
		<div data-role="page" data-content-theme="a" id="groupRoundInfo" data-title="Group Round info for Doubles 2017">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
				<h6>Group Round</h6>
			</div>
			<div data-role="content">
				<form action="/doublesGroupRound" method="POST">
					<label for="select-choice-0" class="select">Select Group:</label>
					<select name="groupRound" onchange="this.form.submit()">
						<option value="All" selected>All</option>
						<option value="Red" ${selectedGroup=='Red'? 'selected="selected"' : ''}>Red</option>
						<option value="Blue" ${selectedGroup=='Blue'? 'selected="selected"' : ''}>Blue</option>
						<option value="Green" ${selectedGroup=='Green'? 'selected="selected"' : ''}>Green</option>
						<option value="Yellow" ${selectedGroup=='Yellow'? 'selected="selected"' : ''}>Yellow</option>
					</select>
				</form>
				<c:forEach var="teamsGroup" items="${teamsList}">
					<span style="text-align:left;color:green;font-weight:bold;">Total teams for ${teamsGroup.key}: ${fn:length(teamsGroup.value)}</span><br/>
					<table data-role="table" id="my-table" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
	  					<thead>
					    	<tr>
					      	<th>Cans</th>
					      	<th>Team Name</th>
					      	<th>Availability</th>
					      	<th>Captain</th>
					      	<th>Email Address</th>
					      	<th>Contact No</th>
					      	<th>Captain Rating</th>
					      	<th>Player2 Name</th>
					      	<th>Player2 Email</th>
					      	<th>Player2 Contact</th>
					      	<th>Player2 Rating</th>
					    	</tr>
		  				</thead>
	  					<tbody>
							<c:forEach items="${teamsGroup.value}" var="team" varStatus="counter">
			    				<tr>
									<td>
									<c:choose>
										<c:when test="${team.cans == 0}">
											<span style="color:green;font-weight:bold;"><c:out value="${team.cans}"/></span>
										</c:when>
										<c:otherwise>
											<span style="color:red;font-weight:bold;"><c:out value="${team.cans}"/></span>
										</c:otherwise>
									</c:choose>
									</td>
									<td><c:out value="${team.teamName}"/></td>
									<td><c:out value="${team.availability}"/></td>
									<td><c:out value="${team.player1Name}"/></td>
									<td> <a href="mailto:${team.player1EmailAddress}" class="ui-link-inherit"><c:out value="${team.player1EmailAddress}"/></td>
									<td><c:out value="${team.player1Contact}"/></td>
									<td><c:out value="${team.player1Rating}"/></td>
									<td><c:out value="${team.player2Name}"/></td>
									<td> <a href="mailto:${team.player2EmailAddress}" class="ui-link-inherit"><c:out value="${team.player2EmailAddress}"/></td>
									<td><c:out value="${team.player2Contact}"/></td>
									<td><c:out value="${team.player2Rating}"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<br/>
				</c:forEach>
				<div id="warning">
					<!-- <span style="text-align:left;color:red;font-weight:bold;">* stands for Kids/Minors in your group</span> -->
				</div>
			</div>
			<div data-role="footer">
				<span style="text-align:left;font-style:italic;font-size:10px;">For more details please visit <a href="https://sites.google.com/site/beaverpuropentennis/home/season-2018/doubles-tournament/group-round">Desktop version</a></span>
			</div>
		</div>
	</div>
</body>
</html>