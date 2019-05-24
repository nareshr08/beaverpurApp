package com.beaverpurtennis.servlet.singles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.Player;
import com.beaverpurtennis.utils.SpreadSheetUtilites;

/**
 * To retrieve information about Players for a given Group.
 * Maps to /groupInfo in web.xml
 * @author naresh.sankaramaddi
 *
 */
public class SinglesGroupRoundDetailsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1086335221076776261L;
	
	private static final Logger log = Logger.getLogger(SinglesGroupRoundDetailsServlet.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String selectedGroupRound = request.getParameter("groupRound");
		log.info("Selected Group for display is:"+selectedGroupRound);
		try {
			SortedMap<String, List<Player>> playersList = new TreeMap<String, List<Player>>();
			if (selectedGroupRound !=  null &&  !selectedGroupRound.equals("")){
				List<Player> playersListForGroup = SpreadSheetUtilites.getDetailsForSelectedGroup(Constants.SINGLES_TOURNAMENT_FILE, 1, selectedGroupRound, Constants.TournamentType.SINGLES);
				playersList.put(selectedGroupRound, playersListForGroup);
			}else{
				selectedGroupRound = "All";
				log.info("Looks like User did not select any Group. So retrieving all groups");
				List<String> sortedGroups = new ArrayList<String>(Constants.groups.size());
				sortedGroups.addAll(Constants.groups.keySet());
				Collections.sort(sortedGroups);
				for (String group: sortedGroups){
					if (group.equals("All"))continue;
					List<Player> playersListForGroup = SpreadSheetUtilites.getDetailsForSelectedGroup(Constants.SINGLES_TOURNAMENT_FILE, 1, group, Constants.TournamentType.SINGLES);
					playersList.put(group, playersListForGroup);
				}
			}
			request.setAttribute("playersList",playersList);
			request.setAttribute("groups",Constants.groups);
			request.setAttribute("selectedGroup", selectedGroupRound);
		} catch (Exception e){
			log.severe("Error in accessing SpreadSheet:"+e.toString());
		}
		request.getRequestDispatcher("/jsp/singles/groupRoundDetails.jsp").forward(request, response);
	}
}
