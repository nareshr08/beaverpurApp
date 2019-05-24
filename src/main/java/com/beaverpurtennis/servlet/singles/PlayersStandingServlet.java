package com.beaverpurtennis.servlet.singles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;

public class PlayersStandingServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4597795018541609105L;

	private static final Logger log = Logger.getLogger(PlayersStandingServlet.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String selectedGroupRound = request.getParameter("groupRound");
		long startTime = new Date().getTime();
		log.info("Request start time:"+startTime);
		log.info("Selected Group for display is:"+selectedGroupRound);
		try {
			SortedMap<String, List<Player>> playersList = new TreeMap<String, List<Player>>();
			if (selectedGroupRound !=  null &&  !selectedGroupRound.equals("")){
				ListFeed queryResults = SpreadSheetUtilites.querySpreadSheetForListFeed(Constants.SINGLES_TOURNAMENT_FILE, Constants.WorkSheetIndexes.STANDINGS, selectedGroupRound, Constants.TournamentType.SINGLES);
				playersList.put(selectedGroupRound, populatePlayers(queryResults));
			}else{
				selectedGroupRound = "All";
				log.info("Looks like User did not select any Group. So retrieving info for all groups");
				List<String> sortedGroups = new ArrayList<String>(Constants.groups.size());
				sortedGroups.addAll(Constants.groups.keySet());
				Collections.sort(sortedGroups);
				for (String group: sortedGroups){
					if (group.equals("All"))continue;
					log.info("Adding PlayersList for group:"+group);
					ListFeed queryResults = SpreadSheetUtilites.querySpreadSheetForListFeed(Constants.SINGLES_TOURNAMENT_FILE, Constants.WorkSheetIndexes.STANDINGS, group, Constants.TournamentType.SINGLES);
					playersList.put(group, populatePlayers(queryResults));
				}
			}
			request.setAttribute("playersList",playersList);
			request.setAttribute("groups",Constants.groups);
			request.setAttribute("selectedGroup", selectedGroupRound);
		} catch (Exception e){
			log.severe("Error in accessing SpreadSheet:"+e.toString());
		}
		long endTime = new Date().getTime();
		log.info("Time to process request in secs:"+(endTime-startTime)/1000);
		request.getRequestDispatcher("/jsp/singles/playersStandings.jsp").forward(request, response);
	}
	
	private List<Player> populatePlayers(ListFeed queryResults){
		List<Player> playersList = new ArrayList<Player>();
		if (queryResults == null) return playersList;
		List<String> requiredColumns = new ArrayList<String>();
		requiredColumns.add("player");requiredColumns.add("played");requiredColumns.add("wins");requiredColumns.add("loss");requiredColumns.add("points");requiredColumns.add("tie");
		requiredColumns.add("setsdiff");requiredColumns.add("gamediff");requiredColumns.add("penalty");
		for (ListEntry record: queryResults.getEntries()){
			Player player = new Player();
			for (String columnName: record.getCustomElements().getTags()){
				//log.info("Column Name:"+columnName);
				if (!requiredColumns.contains(columnName))continue;
				String columnValue = record.getCustomElements().getValue(columnName);
				if (columnName.equals("played")){
					player.setMatchesPlayed(columnValue);
				}else if (columnName.equals("player")){
					player.setName(columnValue);
				} else if (columnName.equals("wins")){
					player.setWins(columnValue);
				} else if (columnName.equals("loss")){
					player.setLosses(columnValue);
				} else if (columnName.equals("points")){
					player.setPoints(columnValue);
				} else if (columnName.equals("tie")){
					if (columnValue == null || columnValue == "") columnValue="0";
					player.setTie(columnValue);
				} else if (columnName.equals("setsdiff")){
					player.setSetsDiff(columnValue);
				} else if (columnName.equals("gamediff")){
					player.setGamesDiff(columnValue);
				} else if (columnName.equals("penalty")){
					if (columnValue == null || columnValue == "") columnValue="0";
					player.setPenalty(columnValue);
				}
			}
			playersList.add(player);
		}
		return playersList;
	}
}
