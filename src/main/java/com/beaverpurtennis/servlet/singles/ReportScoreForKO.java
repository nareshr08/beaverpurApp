package com.beaverpurtennis.servlet.singles;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
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
import com.beaverpurtennis.utils.MailUtil;
import com.beaverpurtennis.utils.Player;
import com.beaverpurtennis.utils.SpreadSheetUtilites;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gson.Gson;

public class ReportScoreForKO extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 250031431908226151L;
	private static final Logger log = Logger.getLogger(ReportScoreForKO.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String koRound = request.getParameter("koRound");
			log.info("Request to submit KO scores for:"+koRound);
			//if we come here we got KO Type and Round
			if (koRound == null) {
				request.getRequestDispatcher("/jsp/singles/reportScoreForKO.jsp").forward(request, response);
				return;
			}
			request.setAttribute("selectedKORound", koRound);
			String query = request.getParameter("query");
			log.info("Query value is:"+ query);
			if (query == null || query.equals("")){
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
				Map<String,String> player1List = new LinkedHashMap<String,String>();
				int totalPlayersList = playersList.size();
				for (int idx=0; idx<totalPlayersList-1; idx= idx+2){
					String player1Name = playersList.get(idx).getName();
					String player2Name = playersList.get(idx+1).getName();
					player1List.put(player1Name+";"+player2Name,player1Name);
				}
				Gson gsonMap = new Gson();
				String gsonString = gsonMap.toJson(player1List);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gsonString);
				return ;
			}else if(query.equals("reportScore")){
				String player1Name = request.getParameter("player1");
				player1Name = player1Name.split(";")[0];
				String player2Name = request.getParameter("player2");
				String[][]scoresArray = new String[4][4];
				scoresArray[0][0] = player1Name;
				scoresArray[0][1] = request.getParameter("player1set1score");
				scoresArray[0][2] = request.getParameter("player1set2score");
				scoresArray[0][3] = request.getParameter("player1set3score");
				scoresArray[1][0] = player2Name;
				scoresArray[1][1] = request.getParameter("player2set1score");
				scoresArray[1][2] = request.getParameter("player2set2score");
				scoresArray[1][3] = request.getParameter("player2set3score");
				scoresArray[2][0] = koRound;
				scoresArray[2][1] = "Major";
				scoresArray[3][0] = "Singles";
				String queryString = Constants.getQueryStringForSinglesKO(koRound);
				CellFeed cellFeed = null;
				cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.SINGLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.KOR64, 0);
				for (CellEntry cellEntry : cellFeed.getEntries()){
					String value = cellEntry.getCell().getValue();
					int currentRow = cellEntry.getCell().getRow();
					int currentCol = cellEntry.getCell().getCol();
					if (value == null) continue;
					if (value.equals(player1Name) || value.equals(player2Name)){
						log.info("Found the Players combination:"+player1Name+"vs"+player2Name);
						int workSheetIndex = Constants.WorkSheetIndexes.KOR64;
						WorksheetEntry workSheet = SpreadSheetUtilites.getWorkSheet(Constants.SINGLES_TOURNAMENT_FILE, workSheetIndex, Constants.TournamentType.SINGLES);
						CellFeed batchRequest = new CellFeed();
						URL cellFeedUrl = new URI(workSheet.getCellFeedUrl().toString()).toURL();
						int rowIndex=0;
						for (int row=currentRow;row<currentRow+2;row++){
							int columnIndex=1;
							for (int col = currentCol+1; col <= currentCol+3; col++) {
								CellAddress cell = new CellAddress(row, col);
						        CellEntry batchEntry = new CellEntry(cell.row, cell.col,scoresArray[rowIndex][columnIndex]);
					             batchEntry.setId(String.format("%s/%s", cellFeedUrl.toString(), cell.value));         
					             BatchUtils.setBatchId(batchEntry, cell.value);
					             BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);  
					             batchRequest.getEntries().add(batchEntry);
					             columnIndex++;
							}
							rowIndex++;
						}
						log.info("Invoking a batch update to insert details in spread sheet cells");
				        Link batchLink =  cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
				        SpreadsheetService service = SpreadSheetUtilites.getService();
				        service.setHeader("If-Match", "*");
				        CellFeed batchResponse = service.batch(new URL(batchLink.getHref()), batchRequest);
				        service.setHeader("If-Match", null);
						 // Check the results
					    boolean isSuccess = true;
					    for (CellEntry entry : batchResponse.getEntries()) {
					      String batchId = BatchUtils.getBatchId(entry);
					      if (!BatchUtils.isSuccess(entry)) {
					        isSuccess = false;
					        BatchStatus status = BatchUtils.getBatchStatus(entry);
					        System.out.printf("%s failed (%s) %s", batchId, status.getReason(), status.getContent());
					      }
					    }

					    System.out.println(isSuccess ? "\nBatch operations successful." : "\nBatch operations failed");
					    if (isSuccess){
					    	log.info("Successfully updated scores");
					    	MailUtil.sendMailForKOScoresUpdate(scoresArray);
							request.getRequestDispatcher("/jsp/singles/successfulScoresUpdateForKO.jsp").forward(request, response);
					    	return;
					    }else{
					    	log.severe("Error in updating scores");
					    	MailUtil.sendOrganizersEmailForFailure("Error in reporting Scores for KO", "There seems to be a failure in updating scores for:"+scoresArray);
					    	request.getRequestDispatcher("/jsp/singles/failureToUpdateScores.jsp").forward(request, response);
					    	return;
					    }
					}
				}
			}
			//if we come here, that means we got the query to report scores
			request.getRequestDispatcher("/jsp/singles/successfulScoresUpdateForKO.jsp").forward(request, response);
		}catch (Exception ex){
			log.severe("Error in reporting scores for KO Round:"+ex.toString());
		}
	}

	/**
	 * A basic struct to store cell row/column information and the associated
	 * RnCn identifier.
	 */
	private static class CellAddress {
		public final int row;
		public final int col;
		public final String value;

		/**
		 * Constructs a CellAddress representing the specified {@code row} and
		 * {@code col}. The idString will be set in 'RnCn' notation.
		 */
		public CellAddress(int row, int col) {
			this.row = row;
			this.col = col;
			this.value = String.format("R%sC%s", row, col);
		}
	}
}
