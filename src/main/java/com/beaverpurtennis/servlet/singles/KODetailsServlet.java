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
import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;

public class KODetailsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8507659026424212878L;

	private static final Logger log = Logger.getLogger(KODetailsServlet.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Request to retrieve Major league details");
		String koRound = request.getParameter("koRound");
		if (koRound == null|| koRound.equals("")){
			koRound = "R64";
		}
		String queryString = Constants.getQueryStringForSinglesKO(koRound);
		CellFeed cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.SINGLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.KOR64, Constants.TournamentType.SINGLES);
		int noOfRecords = cellFeed.getEntries().size();
		System.out.println("Total records:"+noOfRecords);
		String[][] playerDetails = null;
		int columnOffSet = 0;
		if (koRound.equals("R64")) {
			playerDetails = new String[64][4];
			columnOffSet = 3;
		} else if (koRound.equals("R32")) {
			playerDetails = new String[32][4];
			columnOffSet = 13;
		} else if (koRound.equals("R16")) {
			playerDetails = new String[16][4];
			columnOffSet = 23;
		} else if (koRound.equals("R8")) {
			playerDetails = new String[8][4];
			columnOffSet = 33;
		} else if (koRound.equals("R4")) {
			playerDetails = new String[4][4];
			columnOffSet = 43;
		}
		List<CellEntry> cellEntries = cellFeed.getEntries();
		int totalRows = cellEntries.size();
		int rowIndex = -1;
		
		for (int idx =0; idx < totalRows; idx ++) {
			Cell currentCell = cellEntries.get(idx).getCell();
			String value = currentCell.getValue();
			int column = currentCell.getCol();
			if (columnOffSet - column == 0) {
				rowIndex++;
				playerDetails [rowIndex][0] = value;
			}else {
				playerDetails [rowIndex][column-columnOffSet] = value;
			}
		}

		List<Player> playersList = new ArrayList<Player>();
		for (int idx = 0; idx < playerDetails.length; idx++) {
			Player player = new Player();
			log.info("Setting scores for player:"+playerDetails[idx][0]);
			player.setName(playerDetails[idx][0]==null?"":playerDetails[idx][0]);
			player.setSet1Score(playerDetails[idx][1]==null?"0":playerDetails[idx][1]);
			player.setSet2Score(playerDetails[idx][2]==null?"0":playerDetails[idx][2]);
			player.setSet3Score(playerDetails[idx][3]==null?"0":playerDetails[idx][3]);
			playersList.add(player);
		}
		request.setAttribute("playersList", playersList);
		request.setAttribute("selectedRound", koRound);
		request.getRequestDispatcher("/jsp/singles/koLeagueDetails.jsp").forward(request, response);
	}
}
