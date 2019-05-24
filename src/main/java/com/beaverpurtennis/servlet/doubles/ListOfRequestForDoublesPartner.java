package com.beaverpurtennis.servlet.doubles;

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

public class ListOfRequestForDoublesPartner extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7693169941646376094L;

	private static final Logger log = Logger.getLogger(ListOfRequestForDoublesPartner.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Request to retrieve requested players for partner,list");
		try {
			ListFeed list;
			list = SpreadSheetUtilites.getListFeed(Constants.DOUBLES_REGISTRATION_FILE, 1);
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
			request.getRequestDispatcher("/jsp/doubles/listOfPartnerRequests.jsp").forward(request, response);
		} catch (Exception e){
			log.severe("Unknown exception:"+e.toString());
		}
	}
}
