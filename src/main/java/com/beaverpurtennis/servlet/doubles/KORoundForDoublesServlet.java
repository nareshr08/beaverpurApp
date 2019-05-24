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
import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;

public class KORoundForDoublesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6164867747377961368L;
	private static final Logger log = Logger.getLogger(KORoundForDoublesServlet.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Request to retrieve Major league details");
		String koRound = request.getParameter("koRound");
		String koType = request.getParameter("koType");
		if (koRound == null|| koRound.equals("")){
			koRound = "R16";
		}
		String queryString = Constants.getQueryStringForKO(koRound);
		CellFeed cellFeed = null;
		if (koType !=null && koType.equals("Major")){
			cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.DOUBLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.KOROUND, Constants.TournamentType.DOUBLES);
		}else if (koType !=null && koType.equals("Minor")){
			cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.DOUBLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.MINORKO, Constants.TournamentType.DOUBLES);
		}else{
			cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.DOUBLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.KOROUND, Constants.TournamentType.DOUBLES);
		}
		
		String[][] playerDetails = null;
		int columnOffSet = 0;
		if (koRound.equals("R32")){
			playerDetails = new String[32][4];
			columnOffSet = 2;
		} else if (koRound.equals("R16")){
			playerDetails = new String[16][4];
			columnOffSet = 12;
		}else if (koRound.equals("R8")){
			playerDetails = new String[8][4];
			columnOffSet = 22;
		}else if (koRound.equals("R4")){
			playerDetails = new String[4][4];
			columnOffSet = 32;
		}
		int rowIndex=0;
		int noOfRecords = cellFeed.getEntries().size();
		List<CellEntry> cellEntries = cellFeed.getEntries();
		//very bad strategy, but need to work on it for now
		List<Integer> indexForR16 = new ArrayList<Integer>();
		indexForR16.add(5);indexForR16.add(6);indexForR16.add(11);indexForR16.add(12);indexForR16.add(17);indexForR16.add(18);indexForR16.add(23);indexForR16.add(24);
		indexForR16.add(29);indexForR16.add(30);indexForR16.add(35);indexForR16.add(36);indexForR16.add(41);indexForR16.add(42);indexForR16.add(47);indexForR16.add(48);
		List<Integer> indexForR8 = new ArrayList<Integer>();
		indexForR8.add(8);indexForR8.add(9);indexForR8.add(20);indexForR8.add(21);indexForR8.add(32);indexForR8.add(33);indexForR8.add(44);indexForR8.add(45);
		List<Integer> indexForR4 = new ArrayList<Integer>();
		indexForR4.add(14);indexForR4.add(15);indexForR4.add(38);indexForR4.add(39);

		int startingRow = 3;
		for (int row =0; row<noOfRecords; row++){
			Cell currentCell = cellEntries.get(row).getCell();
			int currentColumn = currentCell.getCol();
			int currentRow = currentCell.getRow();
			if ((koRound.equals("R32") && currentRow%startingRow ==0)
					||(koRound.equals("R16") && !indexForR16.contains(currentRow))
					||(koRound.equals("R8") && !indexForR8.contains(currentRow))
					||(koRound.equals("R4") && !indexForR4.contains(currentRow))
					)
				continue;
			log.info("Current column is:"+currentColumn);
			String inputValue = currentCell.getValue();
			if (currentColumn-columnOffSet ==0){
				playerDetails[rowIndex][0]=inputValue;
			}else{
				playerDetails[rowIndex][currentColumn-columnOffSet]=inputValue;
			}
			if (row <noOfRecords-1){
				Cell nextCell = cellEntries.get(row+1).getCell();
				int nextColumn = nextCell.getCol();
				if (nextColumn - columnOffSet ==0){
					rowIndex++;
				}
			}
		}

		List<Player> playersList = new ArrayList<Player>();
		for (int idx=0; idx<playerDetails.length; idx++){//though it is team we look up for, we are using player object for each reference
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
		if (koType !=null && koType.equals("Major")){
			request.getRequestDispatcher("/jsp/doubles/koLeagueDetails.jsp").forward(request, response);
		}else if (koType !=null && koType.equals("Minor")){
			request.getRequestDispatcher("/jsp/doubles/koDetailsForMinorLeague.jsp").forward(request, response);
		}else{
			request.getRequestDispatcher("/jsp/doubles/koLeagueDetails.jsp").forward(request, response);
		}
	}
}