package com.beaverpurtennis.servlet.teamtennis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.Player;
import com.beaverpurtennis.utils.SpreadSheetUtilites;


public class TeamTennisGroupInfoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5156967876601628204L;
	private static final Logger log = Logger.getLogger(TeamTennisGroupInfoServlet.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String selectedGroupRound = request.getParameter("groupRound");
		log.info("Selected Group for display is:"+selectedGroupRound);
		try {
			Map<String, List> teamsList = new LinkedHashMap<String, List>();
			if (selectedGroupRound !=  null &&  !selectedGroupRound.equals("")){
				List<Player> playersListForGroup = SpreadSheetUtilites.getDetailsForSelectedGroup(Constants.TEAMTENNIS_TOURNAMENT_FILE, 1, selectedGroupRound, Constants.TournamentType.TEAMTENNIS);
				teamsList.put(selectedGroupRound, playersListForGroup);
			}else{
				selectedGroupRound = "All";
				log.info("Looks like User did not select any Group. So retrieving all groups");
				List<String> groups = new ArrayList<String>(Constants.teamTennisGroups.size());
				groups.addAll(Constants.teamTennisGroups.keySet());
				//Collections.sort(sortedGroups);
				for (String group: groups){
					//if (group.equals("All"))continue;
					List playersListForGroup = SpreadSheetUtilites.getDetailsForSelectedGroup(Constants.TEAMTENNIS_TOURNAMENT_FILE, 1, group, Constants.TournamentType.TEAMTENNIS);
					teamsList.put(group, playersListForGroup);
				}
			}
			request.setAttribute("teamsList",teamsList);
			request.setAttribute("selectedGroup", selectedGroupRound);
		} catch (Exception e){
			log.severe("Error in accessing SpreadSheet:"+e.toString());
		}
		request.getRequestDispatcher("/jsp/teamtennis/groupRoundDetails.jsp").forward(request, response);
	}

}
