<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>Submit scores for Doubles KO Game</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.0/jquery.mobile-1.4.0.min.css" />
	<script src="../javascript/jquery/jquery-1.10.2.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.4.0.min.js"></script>
</head>
<script type="text/javascript">
	$(document).ready(function() {
		$('#formSubmitDiv').hide();
		$('#setScores').hide();
		$('#team2Info').hide();
		$('#team1Info').hide();
		$('#koRound').change(function(event) {
			$('#team1Info').hide();
			$('#team2Info').hide();
			$('#formSubmitDiv').hide();
			$('#setScores').hide();
	        var $koRound=$("select#koRound").val();
		 	$.get('/reportScoreForDoublesKO',{koRound:$koRound},function(responseJson) {
		 		if (!responseJson || Object.getOwnPropertyNames(responseJson).length === 0){
		 			alert('Reporting not avaliable for the selected Round');
		 			return;
		 		}
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
			if ($team1 =='NA'){
				alert('Please select a team');
				return;
			}
			var res = $team1.split(";");
        	var $team2 = $('#team2');
        	$team2.val(res[1]);
        	$team2.attr("readonly","readonly") 
			$('#team2Info').show();
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
		<div data-role="page" data-content-theme="a" id="reportScoreForDoublesKO" data-title="Report scores for KO Doubles 2019">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="#" data-rel="back" data-icon="arrow-l" data-theme="c">Back</a>
				<h3>Score Reporter</h3>
			</div>
			<div data-role="content">
				<form action="/reportScoreForDoublesKO" method="POST" id="reportScoreForm">
					<input type="hidden" id="query" name="query" value=""/>
					
					<div id="koRoundDiv">
						<label for="koRoundText" class="select">Select KO Round:</label>
						<select name="koRound" id="koRound">
							<option value="NA">--</option>
		    				<option value="R32" ${selectedRound == 'R32' ? 'selected="selected"' : ''}>Round32</option>
		    				<option value="R16" ${selectedRound == 'R16' ? 'selected="selected"' : ''}>Pre-QuarterFinals</option>
		    				<option value="R8" ${selectedRound == 'R8' ? 'selected="selected"' : ''}>Quater Finals</option>
		    				<option value="R4" ${selectedRound == 'R4' ? 'selected="selected"' : ''}>Semis</option>
						</select>
					</div>
					<div id="team1Info">
						<label for="team1" class="select">team1:</label>
						<select name="team1" id="team1">
							<option value="NA"></option>
						</select>
					</div>
					<div id="team2Info">
						<label for="team2" class="select">team2:</label>
						<input type="text" name="team2" id="team2" value=""/>
					</div>
					<div id="setScores" data-role="fieldcontain">
						<fieldset data-role="controlgroup" data-type="horizontal">
							<legend>team1 Scores:</legend>
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
							<legend>team2 Scores:</legend>
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