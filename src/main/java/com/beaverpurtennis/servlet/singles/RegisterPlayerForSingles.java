package com.beaverpurtennis.servlet.singles;

import java.io.IOException;
import java.util.HashMap;
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

/**
 * Register a player for Singles tournament
 * @author naresh.sankaramaddi
 *
 */
public class RegisterPlayerForSingles extends HttpServlet {

	private static final long serialVersionUID = -381528468328219899L;
	private static final Logger log = Logger.getLogger(RegisterPlayerForSingles.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Retrieve request parameters
		String playerFirstName = request.getParameter("playerFirstName");
		String playerLastName = request.getParameter("playerLastName");
		String emailAddress = request.getParameter("emailAddress");
		String contactNo = request.getParameter("phoneNumber");
		log.info("Registration request for:"+playerFirstName+" "+playerLastName+", with Contact no.:"+contactNo+" and Email Address:"+emailAddress);
		String userAgentInfo = request.getHeader("User-Agent");
		MailUtil.deviceUsed =userAgentInfo;
		//MailUtil.sendInfoMessageToOrganizer(userAgentInfo);//initial troubleshoot to analyze the type of devices used by players.
		ListFeed list;
		try {
			HashMap<String, String> playerDetails = new HashMap<String,String>();
			playerDetails.put("firstName",playerFirstName);
			playerDetails.put("lastName",playerLastName);
			playerDetails.put("emailAddress",emailAddress);
			playerDetails.put("contactNo", contactNo);
			playerDetails.put("rating", request.getParameter("rating"));
			if (!Validator.validateField(playerDetails)){
				request.setAttribute("error", "Some fields are missing information.");
				request.getRequestDispatcher("/jsp/singles/registerPlayerForSingles.jsp").forward(request, response);
				return;
			}
			list = SpreadSheetUtilites.getListFeed(Constants.SINGLES_REGISTRATION_FILE, 0);
	        boolean status = SpreadSheetUtilites.addNewPlayer(list,playerDetails);
	        if (status){
	        	log.info("Registered Player successfully. Lets forward him to make Payment");
	        	//String tournamentType = request.getParameter("tournyType");
	        	/*if ("rapidKO".equals(tournamentType)){
	        		request.getRequestDispatcher("/html/registrationSuccess.html").forward(request, response);
	        		return;
	        	}*/
	        	request.setAttribute("playerName", playerFirstName+" "+playerLastName);
	        	request.getRequestDispatcher("/jsp/payment.jsp").forward(request, response);
				return;
	        }else{
	        	log.severe("Player already registered");
	        	request.setAttribute("error", "Player already registered.");
	        	request.getRequestDispatcher("/jsp/singles/registerPlayerForSingles.jsp").forward(request, response);
				return;
	        }
		} catch (AuthenticationException e) {
			log.severe("Authentication error for the app. Have user try again:"+e.toString());
			MailUtil.sendOrganizersEmailForFailure("Failure to register for Singles", e.toString()+"\n"+playerFirstName+" "+playerLastName);
        	request.setAttribute("error", "Error occurred in registering Player. Please try again");
        	request.getRequestDispatcher("/jsp/singles/registerPlayerForSingles.jsp").forward(request, response);
		} catch (ServiceException e) {
			log.severe("Google service layer error for the app. Have user try again:"+e.toString());
			MailUtil.sendOrganizersEmailForFailure("Failure to register for Singles", e.toString()+"\n"+playerFirstName+" "+playerLastName);
        	request.setAttribute("error", "Error occurred in registering Player. Please try again");
        	request.getRequestDispatcher("/jsp/singles/registerPlayerForSingles.jsp").forward(request, response);
		} catch (Exception ex){
			log.severe("Unknown error for the app. Have user try again:"+ex.toString());
			MailUtil.sendOrganizersEmailForFailure("Failure to register for Singles", ex.toString()+"\n"+playerFirstName+" "+playerLastName);
        	request.setAttribute("error", "Error occurred in registering Player. Please try again");
        	request.getRequestDispatcher("/jsp/singles/registerPlayerForSingles.jsp").forward(request, response);
		}
	}
}