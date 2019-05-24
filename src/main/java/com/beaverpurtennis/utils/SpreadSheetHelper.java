package com.beaverpurtennis.utils;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

/**
 * Another helper class to manage reporting scores for singles group round
 * @author naresh.sankaramaddi
 *
 */
public class SpreadSheetHelper {

	private static final Logger log = Logger.getLogger(SpreadSheetHelper.class.getName());
	
	private static String reportedSuccessfully = "Success";
	private static String tournamentFile = "";
	private static int tournamentType = Constants.TournamentType.SINGLES;//lets default to a type and replace with appropriate value
	
	/**
	 * Submit scores for the given paramters
	 * @param scoresArray
	 * @param tournamentType TODO
	 * @param deviceUsed TODO
	 * @return
	 * @throws Exception
	 */
	public static String submitScoresForGame(String[][] scoresArray, int tournamentType) throws Exception{
		try{
			SpreadSheetHelper.tournamentType = tournamentType; 
			String group = scoresArray[2][0];
			if (tournamentType == Constants.TournamentType.SINGLES){
				tournamentFile = Constants.SINGLES_TOURNAMENT_FILE;
			}else if (tournamentType == Constants.TournamentType.DOUBLES){
				tournamentFile = Constants.DOUBLES_TOURNAMENT_FILE;
			}else if (tournamentType == Constants.TournamentType.TEAMTENNIS){
				tournamentFile = Constants.TEAMTENNIS_TOURNAMENT_FILE;
				//group = "TeamTennis";
				group = scoresArray[2][2];
			}
			log.info("Reporting score for:"+scoresArray);
			String team1NameForScores = scoresArray[0][0];
			String team2NameForScores = scoresArray[1][0];
			//First we will start with second column section, if we find data, then its presumed we need to enter data in this column section.
			//log.info("Query spreadsheet for specific Column group:"+group);
			String queryString = Constants.getQueryStringForScoreReporting(group, 0);
			CellFeed cellFeedForFirstColumnSection = SpreadSheetUtilites.querySpreadSheetForCellFeed(tournamentFile, queryString, Constants.WorkSheetIndexes.MATCHSTATS,tournamentType);
			List<CellEntry> cellFeedDataForFirstColumnSection = cellFeedForFirstColumnSection.getEntries();
			queryString = Constants.getQueryStringForScoreReporting(group, 1);
			CellFeed cellFeedForSecondColumnSection = SpreadSheetUtilites.querySpreadSheetForCellFeed(tournamentFile, queryString, Constants.WorkSheetIndexes.MATCHSTATS,tournamentType);
			List<CellEntry> cellFeedDataForSecondColumnSection = cellFeedForSecondColumnSection.getEntries();
			boolean combinationFound = false;
			int countOfRecordsInSecondColumnSection = cellFeedDataForSecondColumnSection.size();
			int countOfRecordsInFirstColumnSection = cellFeedDataForFirstColumnSection.size();
			//log.severe("No of records found in first Column section:"+countOfRecordsInFirstColumnSection);
			//log.severe("No of records found in second Column section:"+countOfRecordsInSecondColumnSection);
			if (cellFeedDataForSecondColumnSection != null){
				if (countOfRecordsInSecondColumnSection >0 && tournamentType != Constants.TournamentType.TEAMTENNIS){//exclude the check for TeamTennis
					combinationFound = checkForExistingCombination(cellFeedDataForSecondColumnSection, team1NameForScores, team2NameForScores);
				}
			}
			if (!combinationFound && cellFeedDataForFirstColumnSection != null && countOfRecordsInFirstColumnSection>0 && tournamentType != Constants.TournamentType.TEAMTENNIS){
				combinationFound = checkForExistingCombination(cellFeedDataForFirstColumnSection, team1NameForScores, team2NameForScores);
			}
			if (!combinationFound){
				log.info("Ok we confirmed the players/teams combination for reporting doesn't exist. So lets insert them carefully");
				//check if we have to insert in second column section?
				if (countOfRecordsInSecondColumnSection>0 || countOfRecordsInFirstColumnSection==36){
					log.info("Looks like we are done with first column section, so moving to second section.");
					//now total count is equivalent to the last record in our specified column
					updateSheetWithScores(cellFeedForSecondColumnSection, scoresArray, countOfRecordsInSecondColumnSection, 1, group);
				}else{
					updateSheetWithScores(cellFeedForFirstColumnSection, scoresArray, countOfRecordsInFirstColumnSection, 0, group);
				}
			}else{
				log.warning("Scores were already reported for these player");
				reportedSuccessfully="Duplicate";
			}
		}catch (Exception ex){
			log.severe("Error in updating Scores for:"+scoresArray[0].toString()+"vs"+scoresArray[1].toString()+". Exception is:"+ex.toString());
			ex.printStackTrace();
			String messageBody = "<html><body>";
			messageBody += "<table border='1'><th>Player Name</th><th>Set1</th><th>Set2</th><th>Set3</th>";
			messageBody += "<tr><td>"+scoresArray[0][0]+"</td><td style='text-align: center;'>"+scoresArray[0][1]+"</td><td style='text-align: center;'>"+scoresArray[0][2]+"</td><td style='text-align: center;'>"+scoresArray[0][3]+"</td></tr>";
			messageBody += "<tr><td>"+scoresArray[1][0]+"</td><td style='text-align: center;'>"+scoresArray[1][1]+"</td><td style='text-align: center;'>"+scoresArray[1][2]+"</td><td style='text-align: center;'>"+scoresArray[1][3]+"</td></tr></table>";
			messageBody += "<br/>Thanks<br/>Beaverpur Organizers</body></html>";
			MailUtil.sendOrganizersEmailForFailure("Failure to report Scores for Singles", "Error in reporting scores for:"+messageBody+"\nStack Trace:"+ex.getMessage());
			throw new Exception(ex);
		}
		return reportedSuccessfully;
	}

	/**
	 * Verify if scores were already report for given combination of Players
	 * @param cellFeed
	 * @param player1Name
	 * @param player2Name
	 * @return
	 */
	private static boolean checkForExistingCombination(List<CellEntry> cellFeed, String player1Name, String player2Name){
		int noOfRecords = cellFeed.size();
		for (int idx=0; idx<noOfRecords-1; idx += 2){
			String player1FromSheet = cellFeed.get(idx).getCell().getInputValue();
			String player2FromSheet = cellFeed.get(idx+1).getCell().getInputValue();
			if ((player1Name.equals(player1FromSheet) && player2Name.equals(player2FromSheet)) || (player2Name.equals(player1FromSheet) && player1Name.equals(player2FromSheet))){
				log.info("OK we see that these two players combination is already reported for Scores");
				MailUtil.sendOrganizersEmailForFailure("Failure to Report Score", "Someone tried to report scores again for players combination:"+player1Name+"vs"+player2Name);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * After verifying that scores weren't reported now we will update spreadsheet using Batch utilities
	 * @param cellFeed
	 * @param scoresArray
	 * @param noOfRecords
	 * @param columnIndexTouse
	 * @param group
	 * @throws Exception
	 */
	private static void updateSheetWithScores(CellFeed cellFeed, String[][] scoresArray, int noOfRecords, int columnIndexTouse, String group) throws Exception{
		try {
			int columnToBeginWith = Constants.getColumnIndexForGroup(group, columnIndexTouse);
			int rowToBeginWith = Constants.getRowIndexForGroup(group)+(noOfRecords+(noOfRecords/2)+2);//Here it involves some math to identify the row to start with. 
																									  //For singles and doubles, we will have two entries and an empty row following next record 
			if (tournamentType == Constants.TournamentType.TEAMTENNIS){
				if (noOfRecords == 0 || noOfRecords==36){//move over to next column
					rowToBeginWith = 3;
				}else{
					rowToBeginWith = 3+noOfRecords+((noOfRecords/2)*3);
				}
			}
			WorksheetEntry workSheet = SpreadSheetUtilites.getWorkSheet(tournamentFile, Constants.WorkSheetIndexes.MATCHSTATS, tournamentType);
			URL cellFeedUrl = new URI(workSheet.getCellFeedUrl().toString()).toURL();
			cellFeed = SpreadSheetUtilites.getCellFeedForMatchStats(tournamentFile,tournamentType);
			CellFeed batchRequest = new CellFeed();
			int rowIndex=0;
			//log.severe("No duplicates found so reporting scores for:"+scoresArray[0][0]+"Vs"+scoresArray[1][0]);
			//log.info("Derived Row to start with:"+rowToBeginWith+" and corresponding column:"+columnToBeginWith);
			int totalColumnsToNavigate = 0;
			for (int row=rowToBeginWith; row <= rowToBeginWith+1; row++) {
				int columnIndex=0;
				if (tournamentType == Constants.TournamentType.TEAMTENNIS){
					totalColumnsToNavigate = columnToBeginWith+6;
				}else{
					totalColumnsToNavigate = columnToBeginWith+3;
				}
				for (int col = columnToBeginWith; col <= totalColumnsToNavigate; col++) {
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

		    //System.out.println(isSuccess ? "\nBatch operations successful." : "\nBatch operations failed");
		    reportedSuccessfully = (isSuccess)?"Success":"Failure";
		    if (isSuccess){
		    	MailUtil.sendMailForScoresUpdate(scoresArray, tournamentType);
		    }else{
		    	MailUtil.sendOrganizersEmailForFailure("Failure to report scores", "There seems to be a failure in updating scores for:"+scoresArray);
		    }
			
		}catch (Exception e) {
			e.printStackTrace();
			String messageBody = "<html><body>";
			messageBody += "<table border='1'><th>Player Name</th><th>Set1</th><th>Set2</th><th>Set3</th>";
			messageBody += "<tr><td>"+scoresArray[0][0]+"</td><td style='text-align: center;'>"+scoresArray[0][1]+"</td><td style='text-align: center;'>"+scoresArray[0][2]+"</td><td style='text-align: center;'>"+scoresArray[0][3]+"</td></tr>";
			messageBody += "<tr><td>"+scoresArray[1][0]+"</td><td style='text-align: center;'>"+scoresArray[1][1]+"</td><td style='text-align: center;'>"+scoresArray[1][2]+"</td><td style='text-align: center;'>"+scoresArray[1][3]+"</td></tr></table>";
			messageBody += "<br/>Thanks<br/>Beaverpur Organizers</body></html>";
			MailUtil.sendOrganizersEmailForFailure("Failure to report scores", "Error in reporting scores for:"+messageBody+"\nStack Trace:"+e.getMessage());
			throw new Exception(e);
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