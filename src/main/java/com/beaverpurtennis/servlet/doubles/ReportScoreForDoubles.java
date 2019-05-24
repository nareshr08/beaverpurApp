package com.beaverpurtennis.servlet.doubles;

import java.io.IOException;
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

public class ReportScoreForDoubles extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 418253603070897924L;
	private static final Logger log = Logger.getLogger(ReportScoreForDoubles.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String group = request.getParameter("groupName");
			String query = request.getParameter("query");
			MailUtil.deviceUsed = request.getHeader("User-Agent");
			if (query == null || query.equals("")){
				log.info("Request to Report scores. Return only the list of Groups available");
				request.getRequestDispatcher("/jsp/doubles/reportScoreForDoubles.jsp").forward(request, response);
				return;
			} else if (query.equals("getTeams")){
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
			if (query != null && query.equals("reportScore")){
				log.severe("Report scores for team1:"+team1+"vs"+team2);
			
				String[][] scoresArray= new String[3][4];
				int indexForTeam = team1.indexOf(";");
				scoresArray[0][0] = team1.substring(0,indexForTeam);
				scoresArray[0][1] = request.getParameter("team1set1score");
				scoresArray[0][2] = request.getParameter("team1set2score");
				scoresArray[0][3] = request.getParameter("team1set3score");
				scoresArray[2][0] = group;
				scoresArray[2][1] = team1.substring(indexForTeam+1);
				indexForTeam = team2.indexOf(";");
				scoresArray[1][0] = team2.substring(0,indexForTeam);
				scoresArray[1][1] = request.getParameter("team2set1score");
				scoresArray[1][2] = request.getParameter("team2set2score");
				scoresArray[1][3] = request.getParameter("team2set3score");

				scoresArray[2][2] = team2.substring(indexForTeam+1);
				String status = SpreadSheetHelper.submitScoresForGame(scoresArray,Constants.TournamentType.DOUBLES);
				if (status.equals("Success")){
					request.getRequestDispatcher("/jsp/doubles/successfulScoresUpdate.jsp").forward(request, response);
				}else if (status.equals("Duplicate")){
					request.getRequestDispatcher("/jsp/doubles/duplicateRequestToUpdateScores.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher("/jsp/failureToUpdateScores.jsp").forward(request, response);
				}
			}			
		}catch (Exception ex){
			log.severe("Error in reporting score:"+ex.toString());
			request.getRequestDispatcher("/jsp/failureToUpdateScores.jsp").forward(request, response);
		}
	}
	
	@SuppressWarnings("unchecked")
	private String getTeamsList(String groupName, HttpServletRequest request, String skipTeam){
		log.info("Selected group is:"+groupName);
		String jsonString="";
		try{
			List<Map<String,String>> teamsList = SpreadSheetUtilites.getDetailsForSelectedGroup(Constants.DOUBLES_TOURNAMENT_FILE, 1, groupName, Constants.TournamentType.DOUBLES);
			Map<String,String> gsonMapForTeams = new LinkedHashMap<String,String>();
			for (Map<String, String> teamInfo : teamsList){
				String teamName = teamInfo.get("teamName");
				if (!teamName.equalsIgnoreCase(skipTeam)){
					String captainEmail = teamInfo.get("player1EmailAddress"); String playerEmail = teamInfo.get("player2EmailAddress");
					gsonMapForTeams.put(teamName+";"+captainEmail+";"+playerEmail, teamName);	
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
