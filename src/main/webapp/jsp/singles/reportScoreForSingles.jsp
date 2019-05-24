<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>Submit scores for Singles Game</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
	<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.4.0.min.js"></script>
</head>
<script type="text/javascript">
//style="display:none"
	$(document).ready(function() {
		//alert('are we trying to load??');
		$('#formSubmitDiv').hide();
		$('#setScores').hide();
		$('#player2Info').hide();
		$('#player1Info').hide();
		$('#groupName').change(function(event) {
			$('#player1Info').hide();
			$('#formSubmitDiv').hide();
			$('#setScores').hide();
			$('#player2Info').hide();
	        var $groupName=$("select#groupName").val();
		 	$.get('/reportScore',{query:'getPlayers',groupName:$groupName},function(responseJson) {
	        	var $select = $('#player1');                           
	            $select.find('option').remove();
	            $('<option>').val('NA').text('').appendTo($select);
	            $.each(responseJson, function(key, value) {               
	            	$('<option>').val(key).text(value).appendTo($select);      
	            });
	            $select.selectmenu('refresh');
	            $('#player1Info').show();
	      	});
	    });
		$('#player1').change(function (event){
			var $player1=$("select#player1").val();
			var $groupName=$("select#groupName").val();
			if ($player1 =='NA'){
				alert('Please select a Player');
				return;
			}
			$.get('/reportScore',{query:'getPlayer2List',groupName:$groupName,selectedPlayer1:$player1},function(responseJson) {
		        var $select = $('#player2');                           
		        $select.find('option').remove();
		        $('<option>').val('NA').text('').appendTo($select);
		        $.each(responseJson, function(key, value) {              
		        $('<option>').val(key).text(value).appendTo($select);      
		        });
		        $select.selectmenu('refresh');
				$('#player2Info').show();
			});
		});
		$('#player2').change(function (event){
			var $player2=$("select#player2").val();
			if ($player2 =='NA'){
				alert('Please select a Player');
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
		<div data-role="page" data-content-theme="a" id="reportScores" data-title="Report scores for Singles 2015">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="#" data-rel="back" data-icon="arrow-l" data-theme="c">Back</a>
				<h3>Score Reporter</h3>
			</div>
			<div data-role="content">
				<form action="/reportScore" method="POST" id="reportScoreForm">
					<input type="hidden" id="query" name="query" value=""/>
					<label for="group" class="select">Select Group:</label>
					<select name="groupName" id="groupName">
						<option value="NA">--</option>
	    				<c:forEach var="group" items="${groups}">
	    					<c:if test="${group.value ne 'All' }">
	        				<option value="${group.key}" ${group.key == selectedGroup ? 'selected="selected"' : ''}>${group.value}</option>
	        				</c:if>
	    				</c:forEach>
					</select>
					<div id="player1Info">
						<label for="player1" class="select">Player1:</label>
						<select name="player1" id="player1">
							<option value="NA"></option>
						</select>
					</div>
					<div id="player2Info">
						<label for="player2" class="select">Player2:</label>
						<select name="player2" id="player2">
							<option value="NA"></option>
						</select>
					</div>
					<div id="setScores" data-role="fieldcontain">
						<fieldset data-role="controlgroup" data-type="horizontal">
							<legend>Player1 Scores:</legend>
							<label for="player1set1score">Set1</label>
							<select name="player1set1score" id="player1set1score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${7-counter}">${7-counter}</option>
								</c:forEach>
							</select>
							<label for="player1set2score">Set2</label>
							<select name="player1set2score" id="player1set2score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${7-counter}">${7-counter}</option>
								</c:forEach>
							</select>
							<label for="player1set3score">Set3</label>
							<select name="player1set3score" id="player1set3score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${counter}">${counter}</option>
								</c:forEach>
							</select>
						</fieldset>
						<br/>
						<fieldset data-role="controlgroup" data-type="horizontal">
							<legend>Player2 Scores:</legend>
							<label for="player2set1score">Set1</label>
							<select name="player2set1score" id="player2set1score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${counter}">${counter}</option>
								</c:forEach>
							</select>
							<label for="player2set2score">Set2</label>
							<select name="player2set2score" id="player2set2score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${counter}">${counter}</option>
								</c:forEach>
							</select>
							<label for="player2set3score">Set3</label>
							<select name="player2set3score" id="player2set3score">
								<c:forEach var="counter" begin="0" end="7">
									<option value="${counter}">${counter}</option>
								</c:forEach>
							</select>
						</fieldset>
					</div>
					<div id="formSubmitDiv">
						<input type="submit" data-theme="b" name="submit" id="scoresButton" value="Report Score">
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>