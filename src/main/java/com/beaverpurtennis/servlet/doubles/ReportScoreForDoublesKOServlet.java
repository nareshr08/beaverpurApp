package com.beaverpurtennis.servlet.doubles;

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
import com.beaverpurtennis.utils.SpreadSheetUtilites;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gson.Gson;

public class ReportScoreForDoublesKOServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 932190404802072667L;
	private static final Logger log = Logger.getLogger(ReportScoreForDoublesKOServlet.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String koRound = request.getParameter("koRound");
			MailUtil.deviceUsed = request.getHeader("User-Agent");
			log.info("Request to submit KO scores for KO round:"+koRound);
			String koType = request.getParameter("koType");
			if (koRound == null || koRound.equals("")){
				log.info("Request to Report scores. Return only the list of Groups available");
				request.getRequestDispatcher("/jsp/doubles/reportScoreForDoublesKO.jsp").forward(request, response);
				return;
			}
			//if we come here we got KO Type and Round
			request.setAttribute("selectedKORound", koRound);
			String query = request.getParameter("query");
			if (query == null || query.equals("")){
				//Only selected KO Type and Round. Get the teams list accordingly
				String queryString = Constants.getQueryStringForKOScoresReport(koRound);
				CellFeed cellFeed = null;
				if (koType !=null && koType.equals("Major")){
					cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.DOUBLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.KOROUND, Constants.TournamentType.DOUBLES);
				}else if (koType !=null && koType.equals("Minor")){
					cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.DOUBLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.MINORKO, Constants.TournamentType.DOUBLES);
				}else{
					cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.DOUBLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.KOROUND, Constants.TournamentType.DOUBLES);
				}
				//very bad strategy, but need to work on it for now
				List<Integer> indexForR16 = new ArrayList<Integer>();
				indexForR16.add(5);indexForR16.add(6);indexForR16.add(11);indexForR16.add(12);indexForR16.add(17);indexForR16.add(18);indexForR16.add(23);indexForR16.add(24);
				indexForR16.add(29);indexForR16.add(30);indexForR16.add(35);indexForR16.add(36);indexForR16.add(41);indexForR16.add(42);indexForR16.add(47);indexForR16.add(48);
				List<Integer> indexForR8 = new ArrayList<Integer>();
				indexForR8.add(8);indexForR8.add(9);indexForR8.add(20);indexForR8.add(21);indexForR8.add(32);indexForR8.add(33);indexForR8.add(44);indexForR8.add(45);
				List<Integer> indexForR4 = new ArrayList<Integer>();
				indexForR4.add(14);indexForR4.add(15);indexForR4.add(38);indexForR4.add(39);

				List<String> teamsList = new ArrayList<String>();
				for (CellEntry cell: cellFeed.getEntries()){
					int currentRow = cell.getCell().getRow();
					if ((koRound.equals("R32") && currentRow%3 ==0)
							||(koRound.equals("R16") && !indexForR16.contains(currentRow))
							||(koRound.equals("R8") && !indexForR8.contains(currentRow))
							||(koRound.equals("R4") && !indexForR4.contains(currentRow))
							)
						continue;
					String teamName = cell.getCell().getValue();
					teamsList.add(teamName);
				}
				Map<String,String> team1List = new LinkedHashMap<String,String>();
				int totalteamsList = teamsList.size();
				for (int idx=0; idx<totalteamsList-1; idx= idx+2){
					String team1Name = teamsList.get(idx);
					String team2Name = teamsList.get(idx+1);
					team1List.put(team1Name+";"+team2Name,team1Name);
				}
				Gson gsonMap = new Gson();
				String gsonString = gsonMap.toJson(team1List);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gsonString);
				return ;
			}else if(query.equals("reportScore")){
				String team1Name = request.getParameter("team1");
				team1Name = team1Name.split(";")[0];
				String team2Name = request.getParameter("team2");
				log.info("Reporting score for KO round:"+koRound+" between Teams:"+team1Name+" Vs "+team2Name);
				String[][]scoresArray = new String[4][4];
				scoresArray[0][0] = team1Name;
				scoresArray[0][1] = request.getParameter("team1set1score");
				scoresArray[0][2] = request.getParameter("team1set2score");
				scoresArray[0][3] = request.getParameter("team1set3score");
				scoresArray[1][0] = team2Name;
				scoresArray[1][1] = request.getParameter("team2set1score");
				scoresArray[1][2] = request.getParameter("team2set2score");
				scoresArray[1][3] = request.getParameter("team2set3score");
				scoresArray[2][0] = koRound;
				scoresArray[2][1] = koType;
				scoresArray[3][0] = "Doubles";
				String queryString = Constants.getQueryStringForKO(koRound);
				CellFeed cellFeed = null;
				if (koType !=null && koType.equals("Major")){
					cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.DOUBLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.KOROUND, Constants.TournamentType.DOUBLES);
				}else if (koType !=null && koType.equals("Minor")){
					cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.DOUBLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.MINORKO, Constants.TournamentType.DOUBLES);
				}else{
					cellFeed = SpreadSheetUtilites.querySpreadSheetForCellFeed(Constants.DOUBLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.KOROUND, Constants.TournamentType.DOUBLES);
				}
				for (CellEntry cellEntry : cellFeed.getEntries()){
					String value = cellEntry.getCell().getValue();
					int currentRow = cellEntry.getCell().getRow();
					int currentCol = cellEntry.getCell().getCol();
					if (value == null) continue;
					if (value.equals(team1Name) || value.equals(team2Name)){
						log.info("Found the teams combination:"+team1Name+"vs"+team2Name);
						int workSheetIndex = koType.equals("Major")?Constants.WorkSheetIndexes.KOROUND:Constants.WorkSheetIndexes.MINORKO;
						WorksheetEntry workSheet = SpreadSheetUtilites.getWorkSheet(Constants.DOUBLES_TOURNAMENT_FILE, workSheetIndex, Constants.TournamentType.DOUBLES);
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
					    	request.setAttribute("koType", koType);
					    	MailUtil.sendMailForKOScoresUpdate(scoresArray);
							request.getRequestDispatcher("/jsp/doubles/successfulScoresUpdateForDoublesKO.jsp").forward(request, response);
					    	return;
					    }else{
					    	log.severe("Error in updating scores");
					    	MailUtil.sendOrganizersEmailForFailure("Error in reporting Scores for KO", "There seems to be a failure in updating scores for:"+scoresArray);
					    	request.getRequestDispatcher("/jsp/failureToUpdateScores.jsp").forward(request, response);
					    	return;
					    }
					}
				}
			}
			//if we come here, that means we got the query to report scores
			request.getRequestDispatcher("/jsp/doubles/successfulScoresUpdateForDoublesKO.jsp").forward(request, response);
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
