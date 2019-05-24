package com.beaverpurtennis.servlet.doubles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.SpreadSheetUtilites;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;

public class RegisteredTeamsListForDoubles extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6402118090023186248L;
	private static final Logger log = Logger.getLogger(RegisteredTeamsListForDoubles.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Request to retrieve Registered Teams list");
		try {
			ListFeed listFeed = SpreadSheetUtilites.getListFeed(Constants.DOUBLES_REGISTRATION_FILE, 0);
			List<Map<String,String>> teamsList = new ArrayList<Map<String,String>>();
			List<String> keyElements = new ArrayList<String>();
			keyElements.add("player1rating");keyElements.add("player2rating");keyElements.add("paypaltransactionid");
			for (ListEntry entry: listFeed.getEntries()){
				Map<String,String> teamInfo = new HashMap<String,String>();
				for (String tag: entry.getCustomElements().getTags()){
					String tagValue = entry.getCustomElements().getValue(tag);
					if (!keyElements.contains(tag)){
						teamInfo.put(tag, tagValue);
					}
				}
				teamsList.add(teamInfo);
			}
			request.setAttribute("teamsList",teamsList);
			request.getRequestDispatcher("/jsp/doubles/registeredTeamsList.jsp").forward(request, response);
		} catch (Exception e){
			log.severe("Unknown exception:"+e.toString());
		}
	}
}
