package com.beaverpurtennis.servlet.doubles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

public class DoublesStandingsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6129977047608074138L;
	private static final Logger log = Logger.getLogger(DoublesStandingsServlet.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String selectedGroupRound = request.getParameter("groupRound");
		long startTime = new Date().getTime();
		log.info("Request start time:"+startTime);
		log.info("Selected Group for display is:"+selectedGroupRound);
		try{
			Map<String,List<Map<String,String>>> teamsList = new LinkedHashMap<String,List<Map<String,String>>>();
			if (selectedGroupRound !=  null &&  !selectedGroupRound.equals("")){
				ListFeed queryResults = SpreadSheetUtilites.querySpreadSheetForListFeed(Constants.DOUBLES_TOURNAMENT_FILE, Constants.WorkSheetIndexes.STANDINGS, selectedGroupRound, Constants.TournamentType.DOUBLES);
				teamsList.put(selectedGroupRound, populateTeams(queryResults));
			}else{
				selectedGroupRound = "All";
				log.info("Looks like User did not select any Group. So retrieving all groups");
				List<String> groups = new ArrayList<String>(Constants.doublesGroups.size());
				groups.addAll(Constants.doublesGroups.keySet());
				for (String group: groups){
					if (group.equals("All"))continue;
					log.info("Adding PlayersList for group:"+group);
					ListFeed queryResults = SpreadSheetUtilites.querySpreadSheetForListFeed(Constants.DOUBLES_TOURNAMENT_FILE, Constants.WorkSheetIndexes.STANDINGS, group, Constants.TournamentType.DOUBLES);
					teamsList.put(group, populateTeams(queryResults));
				}
			}
			request.setAttribute("teamsList",teamsList);
			request.setAttribute("selectedGroup", selectedGroupRound);
		}catch (Exception ex){
			log.severe("Error in retrieving standings information:"+ex.toString());
		}
		request.getRequestDispatcher("/jsp/doubles/teamsStandings.jsp").forward(request, response);
	}
	
	private List<Map<String,String>> populateTeams(ListFeed queryResults){
		List<Map<String,String>> teamsList = new ArrayList<Map<String,String>>();
		if (queryResults == null) return teamsList;
		List<String> requiredColumns = new ArrayList<String>();
		requiredColumns.add("teamname");requiredColumns.add("played");requiredColumns.add("wins");requiredColumns.add("loss");requiredColumns.add("points");requiredColumns.add("tie");
		requiredColumns.add("setsdiff");requiredColumns.add("gamediff");requiredColumns.add("penalty");
		for (ListEntry record: queryResults.getEntries()){
			Map<String,String> teamInfo = new HashMap<String, String>();
			for (String columnName: record.getCustomElements().getTags()){
				//log.info("Column Name:"+columnName);
				if (!requiredColumns.contains(columnName))continue;
				String columnValue = record.getCustomElements().getValue(columnName);
				columnValue = (columnValue ==null)?"":columnValue;
				if (columnName.equals("played")){
					teamInfo.put("played", columnValue);
				}else if (columnName.equals("teamname")){
					teamInfo.put("name", columnValue);
				} else if (columnName.equals("wins")){
					teamInfo.put("wins", columnValue);
				} else if (columnName.equals("loss")){
					teamInfo.put("loss", columnValue);
				} else if (columnName.equals("points")){
					teamInfo.put("points", columnValue);
				} else if (columnName.equals("tie")){
					teamInfo.put("tie", columnValue);
				} else if (columnName.equals("setsdiff")){
					teamInfo.put("setsDiff", columnValue);
				} else if (columnName.equals("gamediff")){
					teamInfo.put("gamesDiff", columnValue);
				} else if (columnName.equals("penalty")){
					teamInfo.put("penalty", columnValue);
				}
			}
			teamsList.add(teamInfo);
		}
		return teamsList;
	}
}