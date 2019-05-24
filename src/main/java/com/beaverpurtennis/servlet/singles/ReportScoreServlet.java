package com.beaverpurtennis.servlet.singles;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.MailUtil;
import com.beaverpurtennis.utils.Player;
import com.beaverpurtennis.utils.SpreadSheetHelper;
import com.beaverpurtennis.utils.SpreadSheetUtilites;

/**
 * Use this class to update scores for Group round
 * @author naresh.sankaramaddi
 *
 */
public class ReportScoreServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2165669261906470361L;
	private static final Logger log = Logger.getLogger(ReportScoreServlet.class.getName());
	
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
				request.setAttribute("groups", Constants.groups);
				request.getRequestDispatcher("/jsp/singles/reportScoreForSingles.jsp").forward(request, response);
				return;
			} else if (query.equals("getPlayers")){
				log.info("Request is to display the Players list");
				String gsonString = getPlayersList(group, request, null);
				log.info("The Gson string:"+gsonString);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gsonString);
				return;
			}else if (query.equals("getPlayer2List")){
				String selectedPlayer1 = request.getParameter("selectedPlayer1");
				String gsonString = getPlayersList(group, request, selectedPlayer1.substring(0,selectedPlayer1.indexOf(";")));
				log.info("The Gson string:"+gsonString);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gsonString);
				return;
			}
			String player1 = request.getParameter("player1");
			String player2 = request.getParameter("player2");
			log.info("Request to submit scores:");
			request.setAttribute("groupRound", group);
			if (query != null && query.equals("reportScore")){
				log.severe("Report scores for player1:"+player1+"vs"+player2);
			
				String[][] scoresArray= new String[3][4];
				int indexForPlayer = player1.indexOf(";");
				scoresArray[0][0] = player1.substring(0,indexForPlayer);
				scoresArray[0][1] = request.getParameter("player1set1score");
				scoresArray[0][2] = request.getParameter("player1set2score");
				scoresArray[0][3] = request.getParameter("player1set3score");
				scoresArray[2][0] = group;
				scoresArray[2][1] = player1.substring(indexForPlayer+1);
				indexForPlayer = player2.indexOf(";");
				scoresArray[1][0] = player2.substring(0,indexForPlayer);
				scoresArray[1][1] = request.getParameter("player2set1score");
				scoresArray[1][2] = request.getParameter("player2set2score");
				scoresArray[1][3] = request.getParameter("player2set3score");

				scoresArray[2][2] = player2.substring(indexForPlayer+1);
				MailUtil.deviceUsed = request.getHeader("User-Agent");
				String status = SpreadSheetHelper.submitScoresForGame(scoresArray, Constants.TournamentType.SINGLES);
				if (status.equals("Success")){
					request.getRequestDispatcher("/jsp/singles/successfulScoresUpdate.jsp").forward(request, response);
				}else if(status.equals("Duplicate")){
					request.getRequestDispatcher("/jsp/singles/duplicateRequestToUpdateScores.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher("/jsp/singles/duplicateRequestToUpdateScores.jsp").forward(request, response);
				}
			}
		}catch (Exception ex){
			log.severe("Error in reporting scores for Singles:"+ex.toString());
			request.getRequestDispatcher("/jsp/singles/failureToUpdateScores.jsp").forward(request, response);
		}
	}
	
	@SuppressWarnings("unchecked")
	private String getPlayersList(String groupName, HttpServletRequest request, String skipPlayer){
		log.info("Selected group is:"+groupName);
		String jsonString="";
		try{
			List<Player> playersList = SpreadSheetUtilites.getDetailsForSelectedGroup(Constants.SINGLES_TOURNAMENT_FILE, 1, groupName, Constants.TournamentType.SINGLES);
			Map<String,String> gsonMapForPlayers = new LinkedHashMap<String,String>();
			for (Player player : playersList){
				if (!player.getName().equalsIgnoreCase(skipPlayer)){
					gsonMapForPlayers.put(player.getName()+";"+player.getEmailAddress(), player.getName());	
				}
			}
			jsonString = new Gson().toJson(gsonMapForPlayers);  
		}catch (Exception ex){
			log.severe("Something went wrong in gathering Players list:"+ex.toString());
			MailUtil.sendOrganizersEmailForFailure("Failure to report Score", "Failed to gather Players List for Group:"+groupName);
		}
		return jsonString;
	}
}