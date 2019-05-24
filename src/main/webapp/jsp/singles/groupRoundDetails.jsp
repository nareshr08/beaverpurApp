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
		<div data-role="page" data-content-theme="a" id="groupRoundInfo" data-title="Group Round info for Singles 2019">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="/index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
				<h6>Group Round</h6>
			</div>
			<div data-role="content">
				<form action="/singlesGroupInfo" method="POST">
					<label for="select-choice-0" class="select">Select Group:</label>
					<select name="groupRound" onchange="this.form.submit()">
	    				<c:forEach var="group" items="${groups}">
	        				<option value="${group.key}" ${group.key == selectedGroup ? 'selected="selected"' : ''}>${group.value}</option>
	    				</c:forEach>
					</select>
				</form>
				<c:forEach var="playersGroup" items="${playersList}">
					<span style="text-align:left;color:green;font-weight:bold;">Total players for ${playersGroup.key}: ${fn:length(playersGroup.value)}</span><br/>
					<table data-role="table" id="my-table" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
	  					<thead>
					    	<tr>
					      		<!-- <th>Cans to Collect</th> -->
					      		<th>Player Name</th>
					      		<th>Email Address</th>
					      		<th>Contact Number</th>
					      		<th>Rating</th>
					    	</tr>
		  				</thead>
	  					<tbody>
							<c:forEach items="${playersGroup.value}" var="player" varStatus="counter">
			    				<tr>
									<%-- <td style="text-align:center;">
									<c:choose>
										<c:when test="${player.cansProvided>0}">
											<span style="color:red;font-weight:bold;"><c:out value="${player.cansProvided}"/></span>
										</c:when>
										<c:otherwise>
											<span style="color:green;font-weight:bold;"><c:out value="${player.cansProvided}"/></span>
										</c:otherwise>
									</c:choose>
									</td> --%>
									<td><c:out value="${player.name}"/></td>
									<td> <a href="mailto:${player.emailAddress}" class="ui-link-inherit"><c:out value="${player.emailAddress}"/></td>
									<td><c:out value="${player.contactNo}"/></td>
									<td><c:out value="${player.rating}"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<br/>
				</c:forEach>
				<div id="warning">
					<span style="text-align:left;color:red;font-weight:bold;">* stands for Kids/Minors in your group</span>
				</div>
			</div>
			<div data-role="footer">
				<span style="text-align:left;font-style:italic;font-size:10px;">For more details please visit <a href="https://sites.google.com/site/beaverpuropentennis/home/season-2019/singles-tournament/group-round">Desktop version</a></span>
			</div>
		</div>
	</div>
</body>
</html>