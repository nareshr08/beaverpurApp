<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>Looking for Doubles Partner</title>
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
	<div data-role="page" data-content-theme="a" id="ballsDistribution" data-title="Request for Doubles Partner">
		<div data-role="header" data-content-theme="a"
			data-add-back-btn="true">
			<a href="index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
			<h6>Request for Doubles Partner</h6>
		</div>
		<div data-role="content">
			<c:if test="${success != null}">
				<span style="color: green;font-style:italic;font-size:12px;">We received your request. Below is the list of players who have requested too. You may contact them.</span>
			</c:if>
			<br/>
			<table data-role="table" id="my-table" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
  				<thead>
				    <tr>
				      <th>No.</th>
				      <th>Player Name</th>
				      <th>Email Address</th>
				      <th>Contact Number</th>
				      <th>Rating</th>
				      <th>Comments</th>
				    </tr>
  				</thead>
  				<tbody>
						<c:forEach items="${playersList}" var="player" varStatus="counter">
	    				<tr>
							<td><c:out value="${counter.count}"/></td>
							<td><c:out value="${player.name}"/></td>
							<td> <a href="mailto:${player.emailAddress}" class="ui-link-inherit"><c:out value="${player.emailAddress}"/></td>
							<td><c:out value="${player.contactNo}"/></td>
							<td><c:out value="${player.points}"/></td>
							<td><c:out value="${player.paymentStatus}"/></td>
						</tr>
						</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>