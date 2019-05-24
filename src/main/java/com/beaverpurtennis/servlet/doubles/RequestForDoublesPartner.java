package com.beaverpurtennis.servlet.doubles;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.MailUtil;
import com.beaverpurtennis.utils.Player;
import com.beaverpurtennis.utils.SpreadSheetUtilites;
import com.beaverpurtennis.utils.Validator;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;

public class RequestForDoublesPartner extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4669568069505530806L;

	private static final Logger log = Logger.getLogger(RequestForDoublesPartner.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Request for doubles partner");
		String playerName = request.getParameter("playerName");
		String playerEmail = request.getParameter("emailAddress");
		String playerContact = request.getParameter("contactNumber");
		String comments = request.getParameter("comments");
		String playerRating = request.getParameter("rating");
		log.info("Player:"+playerName+" with emailAddress:"+playerEmail+" and contact no."+playerContact+" requested for a partner. He has some comments to add:"+comments);
		Map<String, String>keyFields = new HashMap<String, String>();
		keyFields.put("playerName", playerName);keyFields.put("playerEmail", playerEmail);keyFields.put("playerContact", playerContact);
		if (!Validator.validateField(keyFields)){
			request.setAttribute("error", "Values required for Name/Email/Contact No");
			request.getRequestDispatcher("/jsp/doubles/requestForDoublesPartner.jsp").forward(request, response);
			return;
		}
		ListFeed list;
		try{
			list = SpreadSheetUtilites.getListFeed(Constants.DOUBLES_REGISTRATION_FILE, 1);
			ListEntry entry = new ListEntry();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			entry.getCustomElements().setValueLocal("requesteddate", sdf.format(Calendar.getInstance().getTime()));
			entry.getCustomElements().setValueLocal("playername", playerName);
			entry.getCustomElements().setValueLocal("emailaddress", playerEmail);
			entry.getCustomElements().setValueLocal("contactno", playerContact);
			entry.getCustomElements().setValueLocal("rating", playerRating);
			entry.getCustomElements().setValueLocal("comments", (comments==null || comments.equals("")?"No Comments":comments));
			list.insert(entry);
			list = list.getSelf();
			MailUtil.sendInfoMessageToOrganizer(playerName+" with email"+playerEmail+" requested for a doubles partner");
			List<Player> playersList = new ArrayList<Player>();
			for(ListEntry listEntry: list.getEntries()){
				Player player = new Player();
				for (String tag: listEntry.getCustomElements().getTags()){
					String tagValue = listEntry.getCustomElements().getValue(tag);
					if (tag.equalsIgnoreCase("playername")){
						player.setName(tagValue);
					} else if (tag.equalsIgnoreCase("emailaddress")){
						player.setEmailAddress(tagValue);
					} else if (tag.equalsIgnoreCase("contactno")){
						player.setContactNo(tagValue);
					} else if (tag.equals("rating")){
						player.setPoints(tagValue);
					}else if (tag.equalsIgnoreCase("comments")){
						player.setPaymentStatus(tagValue==null?"":tagValue);
					}
				}
				playersList.add(player);
			}
			request.setAttribute("playersList", playersList);
		}catch (Exception ex){
			log.severe("Error in adding record");
			request.setAttribute("error", "Error in recording your request. Please try again");
			request.getRequestDispatcher("/jsp/doubles/requestForDoublesPartner.jsp").forward(request, response);
			return;
		}
		request.setAttribute("success", "We received your request");
		request.getRequestDispatcher("/jsp/doubles/listOfPartnerRequests.jsp").forward(request, response);
	}
}
