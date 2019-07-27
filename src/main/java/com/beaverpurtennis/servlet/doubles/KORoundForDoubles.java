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

public class KORoundForDoubles extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6164867747377961368L;
	private static final Logger log = Logger.getLogger(KORoundForDoubles.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Request to retrieve KO details");
		String koRound = request.getParameter("koRound");
		if (koRound == null|| koRound.equals("")){
			koRound = "R32";
		}
		String queryString = Constants.getQueryStringForDoublesKO(koRound);
		CellFeed cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.DOUBLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.DOUBLESKO, Constants.TournamentType.DOUBLES);
        String[][] playerDetails = null;
        int columnOffSet = 0;
        if (koRound.equals("R32")) {
            playerDetails = new String[32][4];
            columnOffSet = 3;
        } else if (koRound.equals("R16")) {
            playerDetails = new String[16][4];
            columnOffSet = 13;
        } else if (koRound.equals("R8")) {
            playerDetails = new String[8][4];
            columnOffSet = 23;
        } else if (koRound.equals("R4")) {
            playerDetails = new String[4][4];
            columnOffSet = 33;
        }

        List<CellEntry> cellEntries = cellFeed.getEntries();
        int totalRows = cellEntries.size();
        int rowIndex = -1;
        
        for (int idx =0; idx < totalRows; idx ++) {
            Cell currentCell = cellEntries.get(idx).getCell();
            String value = currentCell.getValue();
            //if (value == null)continue;
            int column = currentCell.getCol();
            
            //System.out.println("Current row:"+row+",column:"+column+", current value:"+value);
            if (columnOffSet - column == 0) {
                rowIndex++;
                playerDetails [rowIndex][0] = value;
            }else {
                playerDetails [rowIndex][column-columnOffSet] = value;
            }
        }

        List<Player> playersList = new ArrayList<Player>();
        for (int idx = 0; idx < playerDetails.length; idx++) {//though it is team we look up for, we are using player object for each reference
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
		request.getRequestDispatcher("/jsp/doubles/doublesKOLeagueDetails.jsp").forward(request, response);
	}
}