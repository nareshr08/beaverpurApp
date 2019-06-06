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
	$(document).ready(function() {
		$('#formSubmitDiv').hide();
		$('#setScores').hide();
		$('#player2Info').hide();
		$('#player1Info').hide();
		$('#koRound').change(function(event) {
			$('#player1Info').hide();
			$('#player2Info').hide();
			$('#formSubmitDiv').hide();
			$('#setScores').hide();
	        var $koRound=$("select#koRound").val();
	        //alert('selected KO round is:'+$koRound);
		 	$.get('/reportScoreForKO',{koRound:$koRound},function(responseJson) {
		 		if (!responseJson || Object.getOwnPropertyNames(responseJson).length === 0){
		 			alert('Reporting not avaliable for the selected Round');
		 			return;
		 		}
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
			if ($player1 =='NA'){
				alert('Please select a Player');
				return;
			}
			var res = $player1.split(";");
        	var $player2 = $('#player2');
        	$player2.val(res[1]);
        	$player2.attr("readonly","readonly") 
			$('#player2Info').show();
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
		<div data-role="page" data-content-theme="a" id="reportScoresForKO" data-title="Report scores for KO Singles 2019">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="#" data-rel="back" data-icon="arrow-l" data-theme="c">Back</a>
				<h3>Score Reporter</h3>
			</div>
			<div data-role="content">
				<form action="/reportScoreForKO" method="POST" id="reportScoreForm">
					<input type="hidden" id="query" name="query" value=""/>
					<div id="koRoundDiv">
						<label for="koRoundText" class="select">Select KO Round:</label>
						<select name="koRound" id="koRound">
							<option value="NA">--</option>
		    				<option value="R64" ${selectedRound == 'R64' ? 'selected="selected"' : ''}>Round64</option>
		    				<option value="R32" ${selectedRound == 'R32' ? 'selected="selected"' : ''}>Round32</option>
		    				<option value="R16" ${selectedRound == 'R16' ? 'selected="selected"' : ''}>Pre-QuarterFinals</option>
		    				<option value="R8" ${selectedRound == 'R8' ? 'selected="selected"' : ''}>Quater Finals</option>
		    				<option value="R4" ${selectedRound == 'R4' ? 'selected="selected"' : ''}>Semis</option>
						</select>
					</div>

					<div id="player1Info">
						<label for="player1" class="select">Player1:</label>
						<select name="player1" id="player1">
							<option value="NA"></option>
						</select>
					</div>
					<div id="player2Info">
						<label for="player2" class="select">Player2:</label>
						<input type="text" name="player2" id="player2" value=""/>
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
						<input type="submit" data-theme="a" name="submit" id="scoresButton" value="Report Score">
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>