package com.beaverpurtennis.servlet.doubles;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.MailUtil;
import com.beaverpurtennis.utils.SpreadSheetUtilites;
import com.beaverpurtennis.utils.Validator;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class DoublesRegistrationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -400984162322972943L;

	private static final Logger log = Logger.getLogger(DoublesRegistrationServlet.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Request to register team for Doubles tournament");
		String teamName = request.getParameter("teamName");
		String teamAvailability = request.getParameter("teamAvailability");
		String player1Name = request.getParameter("player1Name");
		String player1EmailAddress = request.getParameter("player1EmailAddress");
		String player1PhoneNumber = request.getParameter("player1PhoneNumber");
		String player1Rating = request.getParameter("player1Rating");
		String player2Name = request.getParameter("player2Name");
		String player2EmailAddress = request.getParameter("player2EmailAddress");
		String player2PhoneNumber = request.getParameter("player2PhoneNumber");
		String player2Rating = request.getParameter("player2Rating");
		Map<String, String> teamInfo = new HashMap<String, String>();
		teamInfo.put("teamName", teamName);teamInfo.put("teamAvailability",teamAvailability);
		teamInfo.put("player1Name", player1Name);teamInfo.put("player1Email", player1EmailAddress);teamInfo.put("player1PhoneNo", player1PhoneNumber);teamInfo.put("player1Rating", player1Rating);
		teamInfo.put("player2Name", player2Name);teamInfo.put("player2Email", player2EmailAddress);teamInfo.put("player2PhoneNo", player2PhoneNumber);teamInfo.put("player2Rating", player2Rating);
		if (!Validator.validateField(teamInfo)){
			request.setAttribute("error", "Some fields are missing information.");
			request.getRequestDispatcher("/html/doublesRegistration.html").forward(request, response);
			return;
		}
		MailUtil.deviceUsed =request.getHeader("User-Agent");
		//MailUtil.sendInfoMessageToOrganizer(teamInfo.toString());
		ListFeed list;
		try {
			list = SpreadSheetUtilites.getListFeed(Constants.DOUBLES_REGISTRATION_FILE, 0);
	        SpreadSheetUtilites.addNewTeam(list,teamInfo, Constants.TournamentType.DOUBLES);
	        log.info("Registered Team successfully. Lets forward to make Payment");
	        request.setAttribute("teamName", teamName);
	        request.getRequestDispatcher("/jsp/paymentForDoubles.jsp").forward(request, response);
		} catch (AuthenticationException e) {
			log.severe("Authentication error for the app. Have user try again:"+e.toString());
			MailUtil.sendOrganizersEmailForFailure("Error in registering for Doubles", e.toString()+"\n"+teamInfo.toString());
			request.getRequestDispatcher("/html/doublesRegistration.html").forward(request, response);
		} catch (ServiceException e) {
			log.severe("Google service layer error for the app. Have user try again:"+e.toString());
			MailUtil.sendOrganizersEmailForFailure("Error in registering for Doubles", e.toString()+"\n"+teamInfo.toString());
			request.getRequestDispatcher("/html/doublesRegistration.html").forward(request, response);
		} catch (Exception ex){
			log.severe("Unknown error for the app. Have user try again:"+ex.toString());
			MailUtil.sendOrganizersEmailForFailure("Error in registering for Doubles", ex.toString()+"\n"+teamInfo.toString());
			request.getRequestDispatcher("/html/doublesRegistration.html").forward(request, response);
		}
	}
}