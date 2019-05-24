package com.beaverpurtennis.servlet.teamtennis;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.MailUtil;
import com.beaverpurtennis.utils.SpreadSheetHelper;
import com.beaverpurtennis.utils.SpreadSheetUtilites;
import com.google.gson.Gson;


public class ReportScoreForTeamTennisServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4957875678731355211L;
	private static final Logger log = Logger.getLogger(ReportScoreForTeamTennisServlet.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			log.info("Report score");
			String group = request.getParameter("groupName");
			String query = request.getParameter("query");
			if (query == null || query.equals("")){
				log.info("Request to retrieve team names for group:"+group);
				//request.setAttribute("teamsList", getTeamsList("All",request,null));
				request.getRequestDispatcher("/jsp/teamtennis/reportScoreForTeamTennis.jsp").forward(request, response);
				return;
			}else if (query.equals("getTeams")){
				log.info("Request is to display the Teams list");
				String gsonString = getTeamsList(group, request, null);
				log.info("The Gson string:"+gsonString);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gsonString);
				return;
			}else if (query.equals("getTeam2List")){
				String selectedTeam1Name = request.getParameter("selectedTeam1");
				String gsonString = getTeamsList(group, request, selectedTeam1Name.substring(0,selectedTeam1Name.indexOf(";")));
				log.info("The Gson string:"+gsonString);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gsonString);
				return;
			}
			
			String team1 = request.getParameter("team1");
			String team2 = request.getParameter("team2");
			log.info("Request to submit scores:");
			request.setAttribute("groupRound", group);
			
			if (query.equals("reportScore")){
				String[][] scoresArray = new String[3][7];
				scoresArray[0][0] = team1.substring(0,team1.indexOf(";"));//team name
				scoresArray[1][0] = team2.substring(0,team2.indexOf(";"));//team name
				
				scoresArray[2][0] = team1.substring(team1.indexOf(";")+1,team1.indexOf(","));//email addresses
				scoresArray[2][1] = team2.substring(team2.indexOf(";")+1,team2.indexOf(","));//email addresses
				scoresArray[2][2] = group;
				//singles1 team1 info
				scoresArray[0][1] = request.getParameter("singles1Team1Player");
				scoresArray[0][2] = request.getParameter("singles1Team1Score");
				//singles1 team2 info
				scoresArray[1][1] = request.getParameter("singles1Team2Player");
				scoresArray[1][2] = request.getParameter("singles1Team2Score");
				//singles2 team1 info
				String singles2Team1Player = request.getParameter("singles2Team1Player");
				String singles2Team2Player = request.getParameter("singles2Team2Player");
				if (singles2Team1Player.equals("NA") && singles2Team2Player.equals("NA")){
					scoresArray[0][3] = "";
					scoresArray[0][4] = "";
					//singles2 team2 info
					scoresArray[1][3] = "";
					scoresArray[1][4] = "";

				}else
				{
					scoresArray[0][3] = request.getParameter("singles2Team1Player");
					scoresArray[0][4] = request.getParameter("singles2Team1Score");
					//singles2 team2 info
					scoresArray[1][3] = request.getParameter("singles2Team2Player");
					scoresArray[1][4] = request.getParameter("singles2Team2Score");
				}
/*				//singles3 team1 info
				scoresArray[0][5] = request.getParameter("singles3Team1Player");
				scoresArray[0][6] = request.getParameter("singles3Team1Score");
				//singles3 team2 info
				scoresArray[1][5] = request.getParameter("singles3Team2Player");
				scoresArray[1][6] = request.getParameter("singles3Team2Score");
*/				//doubles1 team1 info
				scoresArray[0][5] = request.getParameter("doubles1Team1Player1")+"/"+request.getParameter("doubles1Team1Player2");
				scoresArray[0][6] = request.getParameter("doubles1Team1Score");
				//doubles1 team2 info
				scoresArray[1][5] = request.getParameter("doubles1Team2Player1")+"/"+request.getParameter("doubles1Team2Player2");
				scoresArray[1][6] = request.getParameter("doubles1Team2Score");
				//doubles2 team1 info
/*				scoresArray[0][7] = request.getParameter("doubles2Team1Player1")+"/"+request.getParameter("doubles2Team1Player2");
				scoresArray[0][8] = request.getParameter("doubles2Team1Score");
				//doubles2 team2 info
				scoresArray[1][7] = request.getParameter("doubles2Team2Player1")+"/"+request.getParameter("doubles2Team2Player2");
				scoresArray[1][8] = request.getParameter("doubles2Team2Score");
				//doubles3 team1 info
				scoresArray[0][9] = request.getParameter("doubles3Team1Player1")+"/"+request.getParameter("doubles3Team1Player2");
				scoresArray[0][10] = request.getParameter("doubles3Team1Score");
				//doubles3 team2 info
				scoresArray[1][9] = request.getParameter("doubles3Team2Player1")+"/"+request.getParameter("doubles3Team2Player2");
				scoresArray[1][10] = request.getParameter("doubles3Team2Score");
*/				
				String status = SpreadSheetHelper.submitScoresForGame(scoresArray,Constants.TournamentType.TEAMTENNIS);
				if (status.equals("Success")){
					request.getRequestDispatcher("/jsp/teamtennis/successfulScoresUpdate.jsp").forward(request, response);
				}else if (status.equals("Duplicate")){
					request.getRequestDispatcher("/jsp/teamtennis/duplicateRequestToUpdateScores.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher("/jsp/failureToUpdateScores.jsp").forward(request, response);
				}
			}
		}catch(Exception ex){
			log.severe("Error in reporting score:"+ex.toString());	
		}
	}
	
	/*@SuppressWarnings("unchecked")
	private Map<String,String> getTeamsList(String groupName,String skipTeam){
		log.info("Selected group is:"+groupName);
		Map<String,String> gsonMapForTeams = new LinkedHashMap<String,String>();
		try{
			List<Map<String,String>> teamsList = SpreadSheetUtilites.getDetailsForSelectedGroup(Constants.TEAMTENNIS_TOURNAMENT_FILE, 1, groupName, Constants.TournamentType.TEAMTENNIS);
			for (Map<String, String> teamInfo : teamsList){
				String teamName = teamInfo.get("teamName");
				if (teamName.equals("")){
					continue;
				}
				String emailList ="";
				String playerList = "";
				if (!teamName.equalsIgnoreCase(skipTeam)){
					Iterator<String> iterator = teamInfo.keySet().iterator();
					while (iterator.hasNext()){
						String key = iterator.next();
						if (key.indexOf("playeremailaddress")>-1){
							String email = teamInfo.get(key);
							if (email !=null && !email.equals("")){
								if (emailList.length()==0){
									emailList = email;
								}else{
									emailList += ";"+email;
								}
							}
						}else if (key.indexOf("playername")>-1){
							String playerName = teamInfo.get(key);
							if (playerList.length() == 0){
								playerList = ","+playerName;
							}else{
								playerList += ","+playerName;
							}
						}
					}
					gsonMapForTeams.put(teamName+";"+emailList+playerList, teamName);	
				}
			}
		}catch (Exception ex){
			log.severe("Something went wrong in gathering Teams list:"+ex.toString());
			MailUtil.sendOrganizersEmailForFailure("Failure to report Score", "Failed to gather Teams List for Group:"+groupName);
		}
		return gsonMapForTeams;
	}*/
	
	@SuppressWarnings("unchecked")
	private String getTeamsList(String groupName, HttpServletRequest request, String skipTeam){
		log.info("Selected group is:"+groupName);
		String jsonString="";
		try{
			List<Map<String,String>> teamsList = SpreadSheetUtilites.getDetailsForSelectedGroup(Constants.TEAMTENNIS_TOURNAMENT_FILE, 1, groupName, Constants.TournamentType.TEAMTENNIS);
			Map<String,String> gsonMapForTeams = new LinkedHashMap<String,String>();
			for (Map<String, String> teamInfo : teamsList){
				String teamName = teamInfo.get("teamName");
				if (teamName.equals("")){
					continue;
				}
				String emailList ="";
				String playerList = "";
				if (!teamName.equalsIgnoreCase(skipTeam)){
					Iterator<String> iterator = teamInfo.keySet().iterator();
					while (iterator.hasNext()){
						String key = iterator.next();
						if (key.indexOf("player1emailaddress")>-1){
							String email = teamInfo.get(key);
							if (email !=null && !email.equals("")){
								if (emailList.length()==0){
									emailList = email;
								}else{
									emailList += ";"+email;
								}
							}
						}else if (key.indexOf("name")>-1){
							String playerName = teamInfo.get(key);
							if(playerName.length()>0){
								if (playerList.length() == 0){
									playerList = ","+playerName;
								}else{
									playerList += ","+playerName;
								}
							}
						}
					}
					gsonMapForTeams.put(teamName+";"+emailList+playerList, teamName);	
				}
			}
			jsonString = new Gson().toJson(gsonMapForTeams);  
		}catch (Exception ex){
			log.severe("Something went wrong in gathering Teams list:"+ex.toString());
			MailUtil.sendOrganizersEmailForFailure("Failure to report Score", "Failed to gather Teams List for Group:"+groupName);
		}
		return jsonString;
	}
}