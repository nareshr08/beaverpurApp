package com.beaverpurtennis.servlet.singles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.Player;
import com.beaverpurtennis.utils.SpreadSheetUtilites;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;

/**
 * This API will list all registered players list
 * @author naresh.sankaramaddi
 *
 */
public class ListRegisteredPlayersListForSingles extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8792790880150394196L;
	private static final Logger log = Logger.getLogger(ListRegisteredPlayersListForSingles.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Request to retrieve Registered Players list");
		try {
			ListFeed listFeed = SpreadSheetUtilites.getListFeed(Constants.SINGLES_REGISTRATION_FILE, 0);
			List<Player> playersList = new ArrayList<Player>();
			for (ListEntry entry: listFeed.getEntries()){
				Player player = new Player();
				for (String tag: entry.getCustomElements().getTags()){
					String tagValue = entry.getCustomElements().getValue(tag);
					if (tag.equalsIgnoreCase("playername")){
						player.setName(tagValue);
					} else if (tag.equalsIgnoreCase("rating")){
						player.setRating(tagValue);
					} /*else if (tag.equalsIgnoreCase("contactNumber")){
						player.setContactNo(tagValue);
					} else if (tag.equalsIgnoreCase("emailaddress")){
						player.setEmailAddress(tagValue);
					}*/ else if (tag.equalsIgnoreCase("registrationstatus")){
						if (tagValue == null || tagValue.equals("")){
							tagValue = "UNCONFIRMED";
						}
						player.setPaymentStatus(tagValue);
					}
				}
				playersList.add(player);
			}
			request.setAttribute("playersList",playersList);
			request.getRequestDispatcher("/jsp/singles/registeredPlayersList.jsp").forward(request, response);
		} catch (Exception e){
			log.severe("Unknown exception:"+e.toString());
		}
	}
}