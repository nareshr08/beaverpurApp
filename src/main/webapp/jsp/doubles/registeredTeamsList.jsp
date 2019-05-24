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
		<div data-role="page" data-content-theme="a" id="registeredTeamsList" data-title="Registerd Teams for Doubles 2015">
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
				      <th>Player1 Name</th>
				      <th>Player2 Name</th>
				      <th><abbr title="Payment Status from PayPal">Registration Status</abbr></th>
				    </tr>
  				</thead>
  				<tbody>
						<c:forEach items="${teamsList}" var="team" varStatus="counter">
	    				<tr>
							<td><c:out value="${counter.count}"/></td>
							<td><c:out value="${team.teamname}"/></td>
							<td><c:out value="${team.player1name}"/></td>
<%-- 							<td> <a href="mailto:${team.player1email}" class="ui-link-inherit"><c:out value="${team.player1email}"/></td>
							<td><c:out value="${team.player1phoneno}"/></td>
 --%>							<td><c:out value="${team.player2name}"/></td>
<%-- 							<td> <a href="mailto:${team.player2email}" class="ui-link-inherit"><c:out value="${team.player2email}"/></td>
							<td><c:out value="${team.player2phoneno}"/></td>
 --%>							<c:choose>
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
		</div>
	</div>
</body>
</html>