//action to perform on selecting a menu item.
function onSelectMenu(id) {
	$.mobile.changePage('#' + id);
}

function validatePhoneNumber(event){
	var length = $('#phoneNumber');
/*	if (length == 10){
		event.preventDefault();
		return;
	}
*/	var charCode = event.charCode || event.keyCode;
	//allow end, home, left arrow, up arrow, right arrow, down arrow
	if ((charCode >= 35 && charCode <=40) || (charcode==45) ||
		//allow backspace, delete, tab	
		(charCode ==8 || charCode == 46 || charCode == 9) || charCode == 13){
		//do nothing
	}else{
		//48-57 key codes for 0-9, 96-105 key codes for num0-num9
		if ((charCode <48 || charCode >57)){
			event.preventDefault();
		}
	}
}

function gotocurrentSingles(){
	$('#players').val('');
	$.mobile.changePage('#currentSingles');
}

function gotocurrentDoubles(){
	$('#players').val('');
	$.mobile.changePage('#currentDoubles');
}

function gotocurrentTeamTennis(){
	$('#players').val('');
	$.mobile.changePage('#currentTeamTennis');
}

function gotoRegistrationsPage(){
	$.mobile.changePage('html/registration.html',{transition:"slideup"});
}

function gotoPlayersList(){
	$.mobile.changePage('html/playersList.html', {transition:"slideup"});
}

function validateRegistrationForm(e){
	var phoneNo = $('#phoneNumber').val();
	var errorMsg = '';
	if ($('#playerFirstName').val() =='' || $('#playerFirstName').val().length==0){
		errorMsg += 'First Name required';
		errorMsg +="\n";
	}
	if ($('#playerLastName').val() =='' || $('#playerLastName').val().length==0){
		errorMsg += 'Last Name required';
		errorMsg +="\n";
	}
	
	if ($('#phoneNumber').val() =='' || $('#phoneNumber').val().length==0){
		errorMsg += 'Phone Number required';
		errorMsg +="\n";
	}
/*	phoneNo = phoneNo.replace(/[^0-9]/g, '');
	phoneNo = phoneNo.replace(/(\d{3})(\d{3})(\d{4})/, "($1) $2-$3");
	alert('new phone no. is:'+phoneNo);
	$('#phoneNumber').val(phoneNo);

	if (!/^(\d\d\d)-(\d\d\d)-(\d\d\d\d)$/.test(phoneNo)){
		errorMsg += 'Invalid Phone Number';
		errorMsg +="\n";
	}*/
	var emailAddress = $('#emailAddress').val();
	if (!/\S+@\S+\.\S+/.test(emailAddress)) {
		errorMsg += 'Invalid Email Address';
		errorMsg +="\n";
	}
	
	if (errorMsg.length>0){
		alert(errorMsg);
		e.preventDefault();
		return false;
	}		
	else {
		return true;
	}
}

//back button action
function goHome(){
	$.mobile.changePage('#menuItems', {
		transition : "slideup"
	});
}

//expected results array
var arrResults = new Array();
//suggested results array
var arrSuggest = null;
var textBox = null;
//variable to hold suggestions. Usually this would be a div element positioned absolutely.
var autoText = null;
var currentPosition;

function registerAndLoadSuggestions(){
	textBox = document.getElementById('players');
	//textBox.focus();
	//getSuggestions();
	//loadSuggestions();
}

function loadSuggestions(){
	registerEventsForAutoSuggest(textBox);
	var parentDiv = document.getElementById("playerDetails");
	autoText = document.createElement("div");
	autoText.id = "divSuggest";
	autoText.className = "suggestions";
	autoText.onmousedown =
    autoText.onmouseup = 
    autoText.onmouseover =	function (oEvent){
		oEvent = oEvent || window.oEvent;
			var target = oEvent.target || oEvent.srcElement;
			if (oEvent.type == "mousedown") {
				textBox.value = target.firstChild.nodeValue;
				hideSuggestions();
			} else if (oEvent.type == "mouseover") {
				highlightSuggestion(target);			
			} else {
				textBox.focus();
			}
	};
	parentDiv.appendChild(autoText);
}

function highlightSuggestion(suggestion){
	for (var i=0; i < autoText.childNodes.length; i++) {
        var oNode = autoText.childNodes[i];
        if (oNode == suggestion) {
            oNode.className = "current";
            currentPosition = i;
        } else if (oNode.className == "current") {
            oNode.className = "";
        }
    }
}

function hideSuggestions(){
	autoText.style.visibility = "hidden";
    autoText.style.display = "none";
    var playerName = $('#players').val();
    if (playerName != '' || !playerName){
    	//retrieve player details via ajax call and populate in div id="playerInfomration"
    	//alert('selected playername is:'+playerName);
    	$.ajax({
    		type	: 'POST',
    		url		: 'getPlayerDetails.html',
    		data	: {name:$('#players').val()},
    		success	: populatePlayerDetails,
    		error	: function (jqXHR, responseText, errorThrown){
    			alert('Error occurred:'+responseText);
    		}
    	});
    	//$.post('getPlayerDetails.html',{name:playerName}, function (data){populatePlayerDetails(data);});
    }
}

function populatePlayerDetails(data,textResponse,jqXHR){
	/*$('#player123').jqgrid({
		dataType	: 'json',
		url			: 'getPlayers.html',
		colName		: ['Player ID', 'Player Name', 'Email Address', 'Phone Number', 'Rank', 'Registration Number'],
		colModel	: [
		        	   {name: 'playerID', id: 'playerID', style:hidden},
		        	   {name: 'playerName', id: 'playerName', width: 50},
		        	   {name: 'emailAddress', id: 'emailAddress', width: 50},
		        	   {name: 'phoneNumber', id: 'phoneNumber', width: 30},
		        	   {name: 'rank', id: 'rank', width: 20},
		        	   {name: 'registartionNumber', id:'registrationNumber', width: 20}],
		rowNum		: 10,
		rowList		: [10,20,30],
		pager		: '#pager',
		sortname	: 'playerName',
		viewrecords	: true,
		caption		: 'Players'
	});
	jQuery("#player123").jqGrid('navGrid','#pager',{edit:false,add:false,del:false});*/
}

function registerEventsForAutoSuggest(element){
	if (element.addEventListener)
	{
		element.addEventListener('keyup',showSuggestions,false);
		//element.addEventListener('blur',hideSuggestions,false);
	}else if (window.addEvent)
	{
		element.addEvent('onkeyup',showSuggestions);
		element.addEvent('onblur', hideSuggestions);
	}
}

function showSuggestions(e){
	var keycode=0;
	if (window.event)
	{
		keycode = window.event.keyCode;
	}else if (e)
	{
		keycode = e.which;
	}

	if (keycode == 38 || keycode == 40 || keycode == 13)
	{
		switch (keycode)
		{
		case 38://up arrow
			//get previous suggestion;
			break;
		case 40://down arrow
			//get next suggestion
			break;
		case 13://enter
			//hide suggestion
			hideSuggestions();
			break;
		}
	}else{
		currentPosition = -1;
		arrSuggest = new Array();
		autoText.innerHTML = "";
		var currentTextValue = textBox.value.toLowerCase();
		if (currentTextValue.length >0)
		{
			for (var idx=0; idx<arrResults.length; idx++)
			{
				var currentArrayValue = arrResults[idx].toLowerCase();
				if (currentArrayValue.indexOf(currentTextValue) == 0)
				{
					arrSuggest.push(arrResults[idx]);
				}
			}
			if (arrSuggest.length>0)//found some suggestions. add to the div element.
			{
				for (var idx=0; idx<arrSuggest.length; idx++)
				{
					var suggestion = document.createElement("div");
					suggestion.style.padding = "5px";
					suggestion.appendChild(document.createTextNode(arrSuggest[idx]));
					autoText.style.visibility = "visible";
					autoText.style.display = "block";
					autoText.appendChild(suggestion);
				}
			}else{
				hideSuggestions();
			}
		}else{
				hideSuggestions();
		}
	}
}

//get initial array of results. this would be used while retrieving suggestions.
var ajaxRequest = null;
function getSuggestions(){
	ajaxRequest = createAjaxRequest();
	try{
		//var url =window.location.hostname;
		ajaxRequest.open("GET","getPlayersTest.html",true);
		ajaxRequest.onreadystatechange = populateAjaxResponseToArray;
		ajaxRequest.send(null);
		/*$.ajax({
				type: 'GET',
				url : 'http://localhost:8080/TennisAppStore/getStates.html',
				success: function(data,status,xhr){
					alert('got data:'+data);
					arrResults = data.split("\n");
				},
			    error: function(xhr, status, error){
			        alert("Error!" + xhr.status+" error message is:"+error);
			    },
			    complete: function(){
			        alert('completed ajax call');
			    },
			    dataType: "json"
		});*/
		
	}catch (e)
	{
		arrResults = [
            "Alabama","Alaska","Arizona","Arkansas",
			"California","Colorado","Connecticut",
			"Delaware",
			"Florida",
			"Georgia",
			"Hawaii",
			"Idaho","Illinois","Indiana","Iowa",
			"Kansas","Kentucky",
			"Louisiana",
			"Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana",
			"Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota",
			"Ohio","Oklahoma","Oregon",
			"Pennsylvania",
			"Rhode Island",
			"South Carolina","South Dakota",
			"Tennessee","Texas",
			"Utah",
			"Vermont","Virginia",
			"Washington","West Virginia","Wisconsin","Wyoming",
			"District of Columbia",
			"Puerto Rico",
			"Guam",
			"American Samoa",
			"U.S. Virgin Islands",
			"Northern Mariana Islands"
        ];
	}
}

function populateAjaxResponseToArray(){
	try{
		if (ajaxRequest.readyState == 4)
		{
			if (ajaxRequest.status == 200)
			{
				arrayResult = new Array();
				var response = ajaxRequest.responseXML.getElementsByTagName('Player');
				for (var ij = 0; ij< response.length; ij++){
					var node = response.item(ij);
					arrResults[ij] = node.firstChild.nodeValue;
				}
			}
		}
	}catch (e)
	{
		arrResults = [
            "Alabama","Alaska","Arizona","Arkansas",
			"California","Colorado","Connecticut",
			"Delaware",
			"Florida",
			"Georgia",
			"Hawaii",
			"Idaho","Illinois","Indiana","Iowa",
			"Kansas","Kentucky",
			"Louisiana",
			"Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana",
			"Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota",
			"Ohio","Oklahoma","Oregon",
			"Pennsylvania",
			"Rhode Island",
			"South Carolina","South Dakota",
			"Tennessee","Texas",
			"Utah",
			"Vermont","Virginia",
			"Washington","West Virginia","Wisconsin","Wyoming",
			"District of Columbia",
			"Puerto Rico",
			"Guam",
			"American Samoa",
			"U.S. Virgin Islands",
			"Northern Mariana Islands"
        ];
	}
}

//standard method to create an ajax request.
function createAjaxRequest(){
	if (typeof XMLHttpRequest != "undefined")
	{
		return new XMLHttpRequest();
	}else if (window.ActiveXObject)
	{
		var activeXVersions = ["Microsoft.XMLHTTP", "MSXML2.XMLHTTP.2.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP.4.0", "MSXML2.XMLHTTP.5.0"];
		for (var idx=0; idx<activeXVersions ; idx++)
		{
			try{
				return new window.ActiveXObject(activeXVersion[idx]);
			}catch (e){
				//Do nothing;
			}
		}
	}
}