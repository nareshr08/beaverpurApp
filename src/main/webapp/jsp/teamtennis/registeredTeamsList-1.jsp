<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>Registered Teams List</title>
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
		<div data-role="page" data-content-theme="a" id="registeredTeamsList" data-title="Registerd Teams for TeamTennis 2017">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="/index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
				<h3>Teams List</h3>
			</div>
			<div data-role="content">
				<span style="text-align:left;color:green;font-weight:bold;">Total teams registered till now:${fn:length(teamsList)}</span>
			<table data-role="table" id="my-table" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
  				<thead>
				    <tr>
				      <th>No.</th>
				      <th>Team Name</th>
				      <th>Captain Name</th>
				      <th>Captain Rating</th>
				      <th>Player2</th>
				      <th>Player2 Rating</th>
				      <th>Player3 </th>
				      <th>Player3 Rating</th>
					  <th>Player4</th>
					  <th>Player4 Rating</th>
					  <th>Player5</th>
					  <th>Player5 Rating</th>
					  <th>Player6</th>
					  <th>Player6 Rating</th>
					  <th>Player7</th>
					  <th>Player7 Rating</th>
					  <th>Player8</th>
					  <th>Player8 Rating</th>
					  <th>Player9</th>
					  <th>Player9 Rating</th>
					  <th>Player10</th>
					  <th>Player10 Rating</th>
					  <th><abbr title="Payment Status from PayPal">Registration Status</abbr></th>
				    </tr>
  				</thead>
  				<tbody>
						<c:forEach items="${teamsList}" var="team" varStatus="counter">
	    				<tr>
							<td><c:out value="${counter.count}"/></td>
							<td><c:out value="${team.teamname}"/></td>
							<td><c:out value="${team.captainname}"/></td>
							<td><c:out value="${team.captainrating}"/></td>
							<td><c:out value="${team.player2name}"/></td>
							<td><c:out value="${team.player2rating}"/></td>
							<td><c:out value="${team.player3name}"/></td>
							<td><c:out value="${team.player3rating}"/></td>
							<td><c:out value="${team.player4name}"/></td>
							<td><c:out value="${team.player4rating}"/></td>
							<td><c:out value="${team.player5name}"/></td>
							<td><c:out value="${team.player5rating}"/></td>
							<td><c:out value="${team.player6name}"/></td>
							<td><c:out value="${team.player6rating}"/></td>
							<td><c:out value="${team.player7name}"/></td>
							<td><c:out value="${team.player7rating}"/></td>
							<td><c:out value="${team.player8name}"/></td>
							<td><c:out value="${team.player8rating}"/></td>
							<td><c:out value="${team.player9name}"/></td>
							<td><c:out value="${team.player9rating}"/></td>
							<td><c:out value="${team.player10name}"/></td>
							<td><c:out value="${team.player10rating}"/></td>
							<c:choose>
								<c:when test="${team.registrationstatus=='CONFIRMED'}">
									<td style="color:green;"><c:out value="${team.registrationstatus}"/></td>		
								</c:when>
								<c:otherwise>
									<td style="color:red;">UNCONFIRMED</td>	
								</c:otherwise>
							</c:choose>
						</tr>
						</c:forEach>
				</tbody>
			</table>
			</div>
			<!-- <div data-role="footer">
				<span style="text-align:left;font-style:italic;font-size:12px;">For more details please visit <a href="https://sites.google.com/site/beaverpuropentennis/home/season2016/team-tennis-tournament/registered-teams">Desktop version</a></span>
			</div> -->
		</div>
	</div>
</body>
</html>