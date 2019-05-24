<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>Submit scores for Doubles Game</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.0/jquery.mobile-1.4.0.min.css" />
	<script src="../javascript/jquery/jquery-1.10.2.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.4.0.min.js"></script>
</head>
<script type="text/javascript">
//style="display:none"
	$(document).ready(function() {
		//alert('are we trying to load??');
		$('#formSubmitDiv').hide();
		$('#setScores').hide();
		$('#team2Info').hide();
		$('#team1Info').hide();
		$('#groupName').change(function(event) {
			$('#team1Info').hide();
			$('#formSubmitDiv').hide();
			$('#setScores').hide();
			$('#team2Info').hide();
	        var $groupName=$("select#groupName").val();
		 	$.get('/reportScoreForDoubles',{query:'getTeams',groupName:$groupName},function(responseJson) {
	        	var $select = $('#team1');                           
	            $select.find('option').remove();
	            $('<option>').val('NA').text('').appendTo($select);
	            $.each(responseJson, function(key, value) {               
	            	$('<option>').val(key).text(value).appendTo($select);      
	            });
	            $select.selectmenu('refresh');
	            $('#team1Info').show();
	      	});
	    });
		$('#team1').change(function (event){
			var $team1=$("select#team1").val();
			var $groupName=$("select#groupName").val();
			if ($team1 =='NA'){
				alert('Please select a Team');
				return;
			}
			$.get('/reportScoreForDoubles',{query:'getTeam2List',groupName:$groupName,selectedTeam1:$team1},function(responseJson) {
		        var $select = $('#team2');                           
		        $select.find('option').remove();
		        $('<option>').val('NA').text('').appendTo($select);
		        $.each(responseJson, function(key, value) {              
		        $('<option>').val(key).text(value).appendTo($select);      
		        });
		        $select.selectmenu('refresh');
				$('#team2Info').show();
			});
		});
		$('#team2').change(function (event){
			var $team2=$("select#team2").val();
			if ($team2 =='NA'){
				alert('Please select a Team');
				return;
			}
			$('#setScores').show();
			$('#formSubmitDiv').show();
		});
		$('#scoresButton').click(function(){
			$('#query').val('reportScore');
		});
	});
</script>
<body>
	<div>
		<div data-role="page" data-content-theme="a" id="reportScoresForDoubles" data-title="Report scores for Doubles 2018">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="#" data-rel="back" data-icon="arrow-l" data-theme="c">Back</a>
				<h3>Score Reporter</h3>
			</div>
			<div data-role="content">
				<form action="/reportScoreForDoubles" method="POST" id="reportScoreForm">
					<input type="hidden" id="query" name="query" value=""/>
					<label for="group" class="select">Select Group:</label>
					<select name="groupName" id="groupName">
						<option value="NA">--</option>
	    				<option value="Red" ${selectedGroup=='Red'? 'selected="selected"' : ''}>Red</option>
	    				<option value="Blue" ${selectedGroup=='Red'? 'selected="selected"' : ''}>Blue</option>
	    				<option value="Green" ${selectedGroup=='Red'? 'selected="selected"' : ''}>Green</option>
	    				<option value="Yellow" ${selectedGroup=='Red'? 'selected="selected"' : ''}>Yellow</option>
					</select>
					<div id="team1Info">
						<label for="team1" class="select">Team1:</label>
						<select name="team1" id="team1">
							<option value="NA"></option>
						</select>
					</div>
					<div id="team2Info">
						<label for="team2" class="select">Team2:</label>
						<select name="team2" id="team2">
							<option value="NA"></option>
						</select>
					</div>
					<div id="setScores" data-role="fieldcontain">
						<fieldset data-role="controlgroup" data-type="horizontal">
							<legend>Team1 Scores:</legend>
							<label for="team1set1score">Set1</label>
							<select name="team1set1score" id="team1set1score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${7-counter}">${7-counter}</option>
								</c:forEach>
							</select>
							<label for="team1set2score">Set2</label>
							<select name="team1set2score" id="team1set2score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${7-counter}">${7-counter}</option>
								</c:forEach>
							</select>
							<label for="team1set3score">Set3</label>
							<select name="team1set3score" id="team1set3score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${counter}">${counter}</option>
								</c:forEach>
							</select>
						</fieldset>
						<br/>
						<fieldset data-role="controlgroup" data-type="horizontal">
							<legend>Team2 Scores:</legend>
							<label for="team2set1score">Set1</label>
							<select name="team2set1score" id="team2set1score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${counter}">${counter}</option>
								</c:forEach>
							</select>
							<label for="team2set2score">Set2</label>
							<select name="team2set2score" id="team2set2score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${counter}">${counter}</option>
								</c:forEach>
							</select>
							<label for="team2set3score">Set3</label>
							<select name="team2set3score" id="team2set3score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${counter}">${counter}</option>
								</c:forEach>
							</select>
						</fieldset>
					</div>
					<div id="formSubmitDiv">
						<input type="submit" data-theme="a" name="submit" id="scoresButton" value="Report Score">
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>