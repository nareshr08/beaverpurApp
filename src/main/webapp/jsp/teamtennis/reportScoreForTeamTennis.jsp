<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>Submit scores for Team Tennis Game</title>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
	<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
	<script src="../javascript/jquery/jquery.mobile-1.4.0.min.js"></script>
	<style type="text/css">
		label {
    	text-align: center;
    	font-weight: bold;
		}
		/* stack all grids below 40em (640px) */
		@media all and (max-width: 35em) {
			.my-breakpoint .ui-block-a,
			.my-breakpoint .ui-block-b,
			.my-breakpoint .ui-block-c,
			.my-breakpoint .ui-block-d,
			.my-breakpoint .ui-block-e {
				width: 100%;
				float: none;
			}
		}
	</style>
</head>

<script type="text/javascript">
	$(document).ready(function() {
		$('#formSubmitDiv').hide();
		$('#setScores').hide();
		$('#team1Info').hide();
		$('#team2Info').hide();
		$('#groupName').change(function(event) {
			$('#team1Info').hide();
			$('#formSubmitDiv').hide();
			$('#setScores').hide();
			$('#team2Info').hide();
	        var $groupName=$("select#groupName").val();
		 	$.get('/reportScoreForTeamTennis',{query:'getTeams',groupName:$groupName},function(responseJson) {
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
			$.get('/reportScoreForTeamTennis',{query:'getTeam2List',groupName:$groupName,selectedTeam1:$team1},function(responseJson) {
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
			var $selectedTeam1 = $('#team1').val();
			var $selectedTeam2 = $('#team2').val();
			//our team name is returned in json format:teamname;<list of email ids of players>,<player names>
			$selectedTeam1 = $selectedTeam1.substring(0,$selectedTeam1.indexOf(';'));//this we retrieve team name
			$selectedTeam2 = $selectedTeam2.substring(0,$selectedTeam2.indexOf(';'));
			$('.team1Name').each(function(){
				var $currentLabel = $(this);
				$currentLabel.text($selectedTeam1);
			});
			$('.team2Name').each(function(){
				var $currentLabel = $(this);
				$currentLabel.text($selectedTeam2);
			});
			$selectedTeam1 = $('#team1').val();
			$selectedTeam2 = $('#team2').val();
			var $playersListForTeam1 = $selectedTeam1.substring($selectedTeam1.indexOf(',')).split(',');//this will retrive player names and form an array
			var $playersListForTeam2 = $selectedTeam2.substring($selectedTeam2.indexOf(',')).split(',');
			$('select.team1Players').each(function() {
				var $currentDropDown = $(this);
				$currentDropDown.find('option').remove();
		        $('<option>').val('NA').text('').appendTo($currentDropDown);
		        $.each($playersListForTeam1, function(key, value) {//iterate over the array to construct our drop down
		        	if (value && value !=''){
			        	$('<option>').val(value).text(value).appendTo($currentDropDown);      
		        	}
		        });
		        $currentDropDown.selectmenu('refresh');
			});
	        
			$('select.team2Players').each(function() {
				var $currentDropDown = $(this);
				$currentDropDown.find('option').remove();
		        $('<option>').val('NA').text('').appendTo($currentDropDown);
		        $.each($playersListForTeam2, function(key, value) {//iterate over the array to construct our drop down
		        	if (value && value !=''){
			        	$('<option>').val(value).text(value).appendTo($currentDropDown);      
		        	}
		        });
		        $currentDropDown.selectmenu('refresh');
			});

			$('#setScores').show();
			$('#formSubmitDiv').show();
		});
		$('#scoresButton').click(function(){
			$('#query').val('reportScore');
			var allValuesSelected = true;
			$('select.team1Players').each(function() {
				var $currentDropDown = $(this).val();
				if (!$currentDropDown || $currentDropDown == 'NA'){
					if (!($(this).attr('id') =='singles2Team1Player' || $(this).attr('id') =='singles2Team2Player')){
						allValuesSelected = false;
						return false;
					}
				}
			});
			if (allValuesSelected == false){
				alert('Player name was not selected from Team1 for one of the games');
				return false;
			}
			$('select.team2Players').each(function() {
				var $currentDropDown = $(this).val();
				if (!$currentDropDown || $currentDropDown == 'NA'){
					if (!($(this).attr('id') =='singles2Team1Player' || $(this).attr('id') =='singles2Team2Player')){
						allValuesSelected = false;
						return false;
					}
				}
			});
			if (allValuesSelected == false){
				alert('Player name was not selected from Team2 for one of the games');
				return false;
			}
		});
	});
</script>
<body>
	<div>
		<div data-role="page" data-content-theme="a" id="reportScoreForTeamTennis" data-title="Report scores for TeamTennis 2017">
			<div data-role="header" data-content-theme="a" data-add-back-btn="true">
				<a href="#" data-rel="back" data-icon="arrow-l" data-theme="c">Back</a>
				<h3>Score Reporter</h3>
			</div>
			<div data-role="content">
				<form action="/reportScoreForTeamTennis" method="POST" id="reportScoreForm">
					<input type="hidden" id="query" name="query" value=""/>
					<label for="group" class="select">Select Group:</label>
					<select name="groupName" id="groupName">
						<option value="NA">--</option>
	    				<option value="All" ${selectedGroup=='All'? 'selected="selected"' : ''}>All</option>
<%-- 	    				<option value="Beta" ${selectedGroup=='Beta'? 'selected="selected"' : ''}>Beta</option>
 --%>					</select>
					<div id="team1Info">
						<label for="team1" class="select">Team1:</label>
						<select name="team1" id="team1">
							<option value="NA"></option>
							<c:forEach var="team" items="${teamsList}">
								<option value="${team.key}" ${selectedTeam1 == team.key ? 'selected="selected"' : ''}>${team.value}</option>
							</c:forEach>
						</select>
					</div>
					<div id="team2Info">
						<label for="team2" class="select">Team2:</label>
						<select name="team2" id="team2">
							<option value="NA"></option>
						</select>
					</div>
					<div id="setScores">
						<div class="ui-grid-solo">
							<div class="ui-block-a"><button type="v" data-theme="b">Singles (S1) Info</button></div>
						</div>
						<fieldset class="ui-grid-b">
							<div class="ui-block-a"><label for="singles1TeamName">Team</label></div>
							<div class="ui-block-b"><label for="singles1PlayerName">Player Name</label></div>
							<div class="ui-block-c"><label for="singles1SetScore">SetScore</label></div>
						</fieldset>
						<fieldset class="ui-grid-b">
						    <div class="ui-block-a"><label for="singles1TeamName" id="team1Name" class="team1Name"></label></div>
						    <div class="ui-block-b">
						    	<select name="singles1Team1Player" id="singles1Team1Player" class="team1Players required">
									<option value="NA"></option>
								</select>
							</div>
						    <div class="ui-block-c">
						    	<select name="singles1Team1Score" id="singles1Team1Score">
									<c:forEach var="counter" begin="0" end="9">
										<option value="${9-counter}" ${counter=='1'? 'selected="selected"' : ''}>${9-counter}</option>
									</c:forEach>
								</select>
						    </div>
						</fieldset>
						<fieldset class="ui-grid-b">
						    <div class="ui-block-a"><label for="singles1TeamName" id="team2Name" class="team2Name required"></label></div>
						    <div class="ui-block-b">
						    	<select name="singles1Team2Player" id="singles1Team2Player" class="team2Players">
									<option value="NA"></option>
								</select>
							</div>
						    <div class="ui-block-c">
						    	<select name="singles1Team2Score" id="singles1Team2Score">
									<c:forEach var="counter" begin="0" end="9">
										<option value="${counter}">${counter}</option>
									</c:forEach>
								</select>
						    </div>
						</fieldset>
						<div class="ui-grid-solo">
							<div class="ui-block-a"><button type="v" data-theme="b">Doubles (D1) Info</button></div>
						</div>
						<fieldset class="ui-grid-c">
							<div class="ui-block-a"><label for="doubles1TeamName">Team</label></div>
							<div class="ui-block-b"><label for="doubles1PlayerName">Player1 Name</label></div>
							<div class="ui-block-c"><label for="doubles1PlayerName">Player2 Name</label></div>
							<div class="ui-block-d"><label for="doubles1SetScore">SetScore</label></div>
						</fieldset>
						<fieldset class="ui-grid-c">
						    <div class="ui-block-a"><label for="doubles1TeamName" class="team1Name"></label></div>
						    <div class="ui-block-b">
						    	<select name="doubles1Team1Player1" id="doubles1Team1Player1" class="team1Players required">
									<option value="NA"></option>
								</select>
							</div>
						    <div class="ui-block-c">
						    	<select name="doubles1Team1Player2" id="doubles1Team1Player2" class="team1Players required">
									<option value="NA"></option>
								</select>
							</div>
						    <div class="ui-block-d">
						    	<select name="doubles1Team1Score" id="doubles1Team1Score">
									<c:forEach var="counter" begin="0" end="9">
										<option value="${9-counter}" ${counter=='1'? 'selected="selected"' : ''}>${9-counter}</option>
									</c:forEach>
								</select>
						    </div>
						</fieldset>
						<fieldset class="ui-grid-c">
						    <div class="ui-block-a"><label for="doubles1TeamName" class="team2Name"></label></div>
						    <div class="ui-block-b">
						    	<select name="doubles1Team2Player1" id="doubles1Team2Player1" class="team2Players required">
									<option value="NA"></option>
								</select>
							</div>
						    <div class="ui-block-c">
						    	<select name="doubles1Team2Player2" id="doubles1Team2Player2" class="team2Players required">
									<option value="NA"></option>
								</select>
							</div>
						    <div class="ui-block-d">
						    	<select name="doubles1Team2Score" id="doubles1Team2Score">
									<c:forEach var="counter" begin="0" end="9">
										<option value="${counter}">${counter}</option>
									</c:forEach>
								</select>
						    </div>
						</fieldset>
						<div class="ui-grid-solo">
							<div class="ui-block-a"><button type="v" data-theme="b">Singles (S2) Info (Optional for Tie breaker, winners will get 1 game*)</button></div>
						</div>
						<fieldset class="ui-grid-b">
							<div class="ui-block-a"><label for="singles2TeamName">Team</label></div>
							<div class="ui-block-b"><label for="singles2PlayerName">Player Name</label></div>
							<div class="ui-block-c"><label for="singles2SetScore">SetScore</label></div>
						</fieldset>
						<fieldset class="ui-grid-b">
						    <div class="ui-block-a"><label for="singles2TeamName" class="team1Name"></label></div>
						    <div class="ui-block-b">
						    	<select name="singles2Team1Player" id="singles2Team1Player" class="team1Players">
									<option value="NA"></option>
								</select>
							</div>
						    <div class="ui-block-c">
						    	<select name="singles2Team1Score" id="singles2Team1Score">
									<c:forEach var="counter" begin="0" end="1">
										<option value="${1-counter}" ${counter=='1'? 'selected="selected"' : ''}>${1-counter}</option>
									</c:forEach>
								</select>
						    </div>
						</fieldset>
						<fieldset class="ui-grid-b">
						    <div class="ui-block-a"><label for="singles2TeamName" class="team2Name"></label></div>
						    <div class="ui-block-b">
						    	<select name="singles2Team2Player" id="singles2Team2Player" class="team2Players">
									<option value="NA"></option>
								</select>
							</div>
						    <div class="ui-block-c">
						    	<select name="singles2Team2Score" id="singles2Team2Score">
									<c:forEach var="counter" begin="0" end="1">
										<option value="${counter}">${counter}</option>
									</c:forEach>
								</select>
						    </div>
						</fieldset>
					</div>
					<br/>
					<div id="formSubmitDiv">
						<input type="submit" data-theme="b" name="submit" id="scoresButton" value="Report Score">
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>