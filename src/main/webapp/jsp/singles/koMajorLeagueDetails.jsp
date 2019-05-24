<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.beaverpurtennis.utils.Player" %>
<!DOCTYPE html>
<html>
<head>
	<title>KO Major Details for Singles</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
	<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.4.0.min.js"></script>
	<style type="text/css">
		table{
			clear:both;
			position:relative;
			top:21px;
			width:500px;
			height:75px;
			background:url('../images/draw_table_final_bg.jpg') left top no-repeat;
			background-size:500px 70px;
		}
		
	</style>
</head>
<script type="text/javascript">
	$(document).ready(function() {
		
	});
</script>
<body>
	<div>
		<div data-role="page" data-content-theme="a" id="KORoundInfo" data-title="KO Round info for Singles 2018">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="/index.html" data-icon="home" data-iconpos="notext" data-direction="reverse">Home</a>
				<h6>KO Singles(Major)</h6>
			</div>
			<div data-role="content">
				<c:choose>
					<c:when test="${selectedRound == 'R32'}">
						<span style="color:red;font-weight:bold;">Round 32 game should be played between 05/21-05/23</span>
					</c:when>
					<c:when test="${selectedRound == 'R16'}">
						<span style="color:red;font-weight:bold;">Pre-Quarter finals game should be played between 05/24-05/26</span>
					</c:when>
					<c:when test="${selectedRound == 'R8'}">
						<span style="color:red;font-weight:bold;">Quarter finals to be played between 05/27-05/29</span>
					</c:when>
					<c:when test="${selectedRound == 'R4'}">
						<span style="color:red;font-weight:bold;">Semifinals to be played between 05/30-05/31</span>
					</c:when>
					<c:otherwise>
						<span style="color:red;font-weight:bold;">R32 game should be played between 05/21-05/23</span>
					</c:otherwise>
				</c:choose>
				<form action="/koMajorForSingles" method="POST">
					<label for="select-choice-0" class="select">Select KO Round:</label>
					<select name="koRound" onchange="this.form.submit()">
	    				<option value="R32" ${selectedRound == 'R32' ? 'selected="selected"' : ''}>Round32</option>
	    				<option value="R16" ${selectedRound == 'R16' ? 'selected="selected"' : ''}>Pre-QuarterFinals</option>
	    				<option value="R8" ${selectedRound == 'R8' ? 'selected="selected"' : ''}>Quater Finals</option>
	    				<option value="R4" ${selectedRound == 'R4' ? 'selected="selected"' : ''}>Semis</option>
					</select>
				</form>
				<%
					List<Player> playersList = (ArrayList<Player>)request.getAttribute("playersList");
					for (int row=0; row <playersList.size()-1; row= row+2){
						Player player1 = playersList.get(row);
						Player player2 = playersList.get(row+1);
						int player1Set1Score = Integer.parseInt(player1.getSet1Score());
						int player1Set2Score = Integer.parseInt(player1.getSet2Score());
						int player1Set3Score = Integer.parseInt(player1.getSet3Score());
						int player2Set1Score = Integer.parseInt(player2.getSet1Score());
						int player2Set2Score = Integer.parseInt(player2.getSet2Score());
						int player2Set3Score = Integer.parseInt(player2.getSet3Score());
						int setsDifference = (player1Set1Score>player2Set1Score?1:-1)+(player1Set2Score>player2Set2Score?1:-1)+(player1Set3Score>player2Set3Score?1:-1);
						String winner = "";
						String player1Name = player1.getName();
						String palyer2Name = player2.getName();
						if (player1Name.length()>15){
							int indexForLastName = player1Name.indexOf(' ');
							player1Name = player1Name.substring(0,indexForLastName)+" "+player1Name.substring(indexForLastName+1, indexForLastName+2);
						}
						String player2Name = player2.getName();
						if (player2Name.length()>15){
							int indexForLastName = player2Name.indexOf(' ');
							player2Name = player2Name.substring(0,indexForLastName)+" "+player2Name.substring(indexForLastName+1, indexForLastName+2);
						}
						int gamesDiff = (player1Set1Score+player1Set2Score+player1Set3Score) - (player2Set1Score+player2Set2Score+player2Set3Score);
						if (gamesDiff >0){
							winner = player1Name;
						}else if (gamesDiff < 0){
							winner = player2Name;
						}else if (gamesDiff == 0 && setsDifference != -3){
							if (setsDifference > 0){
								winner = player1Name;
							}else if (setsDifference < 0){
								winner = player2Name;
							}
						}
				%>
				<div id="koRoundTable" >
					<table>
						<tbody>
							<tr style="width:285px;font-size:12px;">
								<td style="padding-left:45px;width:120px;padding-top:10px;"><%=player1Name %></td>
								<td style="padding-left:20px;width:10px;"><%=player1.getSet1Score() %></td>
								<td style="padding-left:20px;width:10px;"><%=player1.getSet2Score() %></td>
								<td style="padding-left:20px;width:10px;"><%=player1.getSet3Score() %></td>
								<td colspan="3" style="padding-left:20px;width:30px;">&nbsp;</td>
							</tr>
							<tr style="width:120px;font-size:12px;">
								<td colspan="5">&nbsp;</td>
								<td colspan="3" style="float:right;padding-right:35px;"><%=winner %></td>
								
							</tr>
							<tr style="width:285px;font-size:12px;">
								<td style="padding-left:45px;width:120px;"><%=player2Name %></td>
								<td style="padding-left:20px;width:10px;"><%=player2.getSet1Score() %></td>
								<td style="padding-left:20px;width:10px;"><%=player2.getSet2Score() %></td>
								<td style="padding-left:20px;width:10px;"><%=player2.getSet3Score() %></td>
								<td colspan="3" style="padding-left:20px;width:30px;">&nbsp;</td>
							</tr>
						</tbody>
					</table>
					<br/>
				</div>
				<%
					}
				%>
				
				<div id="warning">
					<span style="text-align:left;color:red;font-weight:bold;">* stands for Kids/Minors in your group</span>
				</div>
			</div>
			<div data-role="footer">
				<span style="text-align:left;font-style:italic;font-size:10px;">For more details please visit <a href="https://sites.google.com/site/beaverpuropentennis/home/season-2018/singles-tournament/major-ko">Desktop version</a></span>
			</div>
		</div>
	</div>
</body>
</html>