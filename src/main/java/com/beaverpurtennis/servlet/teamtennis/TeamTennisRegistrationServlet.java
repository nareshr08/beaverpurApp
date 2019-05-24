package com.beaverpurtennis.servlet.teamtennis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.MailUtil;
import com.beaverpurtennis.utils.SpreadSheetUtilites;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class TeamTennisRegistrationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6772662333152024864L;
	private static final Logger log = Logger.getLogger(TeamTennisRegistrationServlet.class.getName());
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Request to register team for TeamTennis tournament");
		String teamName = request.getParameter("teamName");
		log.info("Request to register team:"+teamName+" for Team Tennis");
		if (teamName == null || teamName.equals("")){
			request.getRequestDispatcher("/html/teamTennisRegistration.html").forward(request, response);
			return;
		}
		Map requestParameters = request.getParameterMap();
		Iterator<String> keySet = requestParameters.keySet().iterator();
		Map<String,String>teamInfo = new HashMap<String,String>();
		teamInfo.put("teamName", teamName);
		teamInfo.put("captainRating", request.getParameter("captainRating"));
		teamInfo.put("teamAvailability", request.getParameter("teamAvailability"));
		while (keySet.hasNext()){
			String key = (String)keySet.next();
			if (key.indexOf("Name")>-1 || key.indexOf("EmailAddress")>-1 || key.indexOf("PhoneNumber")>-1 || key.indexOf("Rating")>-1){
				String value = request.getParameter(key);
				log.info("Current Key:"+key+" value:"+value);
				teamInfo.put(key, value);
			}
		}
		try {
			ListFeed list = SpreadSheetUtilites.getListFeed(Constants.TEAMTENNIS_REGISTRATION_FILE, 0);
	        SpreadSheetUtilites.addNewTeam(list,teamInfo, Constants.TournamentType.TEAMTENNIS);
	        log.info("Registered Team successfully. Lets forward to make Payment");
	        request.setAttribute("playerName", teamName);
	        request.getRequestDispatcher("/jsp/paymentForTeamTennis.jsp").forward(request, response);
		} catch (AuthenticationException e) {
			log.severe("Authentication error for the app. Have user try again:"+e.toString());
			MailUtil.sendOrganizersEmailForFailure("Error in registering for Team Tennis", e.toString()+"\n"+teamInfo.toString());
			request.getRequestDispatcher("/html/teamTennisRegistration.html").forward(request, response);
		} catch (ServiceException e) {
			log.severe("Google service layer error for the app. Have user try again:"+e.toString());
			MailUtil.sendOrganizersEmailForFailure("Error in registering for Team Tennis", e.toString()+"\n"+teamInfo.toString());
			request.getRequestDispatcher("/html/teamTennisRegistration.html").forward(request, response);
		} catch (Exception ex){
			log.severe("Unknown error for the app. Have user try again:"+ex.toString());
			MailUtil.sendOrganizersEmailForFailure("Error in registering for Team Tennis", ex.toString()+"\n"+teamInfo.toString());
			request.getRequestDispatcher("/html/teamTennisRegistration.html").forward(request, response);
		}
	}
}
