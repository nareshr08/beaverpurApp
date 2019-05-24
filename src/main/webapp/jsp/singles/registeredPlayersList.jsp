<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>Registration Details</title>
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
		<div data-role="page" data-content-theme="a" id="registeredPlayersList" data-title="Registerd Players for Singles 2018">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="#" data-rel="back" data-icon="arrow-l" data-theme="c">Back</a>
				<h3>Players List</h3>
			</div>
			<div data-role="content">
				<span style="text-align:left;color:green;font-weight:bold;">Total players registered till now:${fn:length(playersList)}</span>
			<table data-role="table" id="my-table" data-mode="reflow" class="ui-responsive table-stroke table-stripe">
  				<thead>
				    <tr>
				      <th>No.</th>
				      <th>Player Name</th>
				      <th>Self Rating</th>
				      <!-- <th>Email Address</th>
 				      <th>Contact Number</th> -->
				      <th><abbr title="Payment Status from PayPal">Registration Status</abbr></th>
				    </tr>
  				</thead>
  				<tbody>
						<c:forEach items="${playersList}" var="player" varStatus="counter">
	    				<tr>
							<td><c:out value="${counter.count}"/></td>
							<td><c:out value="${player.name}"/></td>
							<td><c:out value="${player.rating}"/></td>
 							<%-- <td> <a href="mailto:${player.emailAddress}" class="ui-link-inherit"><c:out value="${player.emailAddress}"/></td>
							<td><c:out value="${player.contactNo}"/></td> --%>
							<td valign="top">
								<c:choose>
									<c:when test="${player.paymentStatus=='CONFIRMED'}">
										<p style="color:green;"><c:out value="${player.paymentStatus}"/></p>		
									</c:when>
									<c:otherwise>
										<p style="color:red;"><c:out value="${player.paymentStatus}"/></p>	
									</c:otherwise>
								</c:choose>
								
							</td>
						</tr>
						</c:forEach>
				</tbody>
			</table>
			</div>
		</div>
	</div>
</body>
</html>