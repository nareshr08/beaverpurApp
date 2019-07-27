package com.beaverpurtennis.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Core Utility class for our Google SpreadSheets Access
 * We authenticate using our Google Client (Email) credentials. 
 * Need to find a better way, rather exposing our account details
 * @author naresh.sankaramaddi
 *
 */
public class SpreadSheetUtilites {
	private static final Logger log = Logger.getLogger(SpreadSheetUtilites.class.getName());
	//this map will store list feed for each type (ex: list of registered players for singles/doubles, list of players in group) 
	private static final Map<String,ListFeed> listFeedMap = new HashMap<String,ListFeed>();
	//below maps will be primarily used to retrieve worksheet for cell feed
	private static Map<Integer, WorksheetEntry>singlesWorkSheetsMap = new HashMap<Integer,WorksheetEntry>();
	private static Map<Integer, WorksheetEntry>doublesWorkSheetsMap = new HashMap<Integer,WorksheetEntry>();
	private static Map<Integer, WorksheetEntry>teamTennisWorkSheetsMap = new HashMap<Integer,WorksheetEntry>();
	
	private static SpreadsheetService service;
	private static Map<String,List<Player>> groupsForSingles = new HashMap<String,List<Player>>();
	private static Map<String,List<Map<String,String>>> groupsForDoubles = new HashMap<String,List<Map<String,String>>>();
	private static Map<String,List<Map<String,String>>> groupsForTeamTennis = new HashMap<String,List<Map<String,String>>>();
	private static CellFeed cellFeedUrlForMatchStats;
	
	  /**
	   * Connects to the specified {@link SpreadsheetService} and uses a batch
	   * request to retrieve a {@link CellEntry} for each cell enumerated in {@code
	   * cellAddrs}. Each cell entry is placed into a map keyed by its RnCn
	   * identifier.
	   *
	   * @param ssSvc the spreadsheet service to use.
	   * @param cellFeedUrl url of the cell feed.
	   * @param cellAddrs list of cell addresses to be retrieved.
	   * @return a map consisting of one {@link CellEntry} for each address in {@code
	   *         cellAddrs}
	   */
	  public static Map<String, CellEntry> getCellEntryMap(URL cellFeedUrl, List<CellAddress> cellAddrs)
	      throws IOException, ServiceException {
		  CellFeed batchRequest = new CellFeed();
		  for (CellAddress cellId : cellAddrs) {
			  CellEntry batchEntry = new CellEntry(cellId.row, cellId.col, cellId.value);
			  batchEntry.setId(String.format("%s/%s", cellFeedUrl.toString(), cellId.value));
			  BatchUtils.setBatchId(batchEntry, cellId.value);
			  BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.QUERY);
			  batchRequest.getEntries().add(batchEntry);
		  }

		  CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
		  CellFeed queryBatchResponse =
				  SpreadSheetUtilites.getService().batch(new URL(cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM).getHref()),
						  batchRequest);

		  Map<String, CellEntry> cellEntryMap = new HashMap<String, CellEntry>(cellAddrs.size());
		  for (CellEntry entry : queryBatchResponse.getEntries()) {
			  cellEntryMap.put(BatchUtils.getBatchId(entry), entry);
			  System.out.printf("batch %s {CellEntry: id=%s editLink=%s inputValue=%s\n",
					  BatchUtils.getBatchId(entry), entry.getId(), entry.getEditLink().getHref(), entry.getCell().getInputValue());
		  }

		  return cellEntryMap;
	  }
	  
	 /**
	   * A basic struct to store cell row/column information and the associated RnCn
	   * identifier.
	   */
	private static class CellAddress {
		public final int row;
		public final int col;
		public final String value;

		/**
		 * Constructs a CellAddress representing the specified {@code row} and
		 * {@code col}. The idString will be set in 'RnCn' notation.
		 */
		public CellAddress(int row, int col, String value) {
			this.row = row;
			this.col = col;
			this.value = String.format("R%sC%s", row, col);
		}
	}
	
	/**
	 * Our Utility class entry point. This will be used only to test any specific functionality
	 * @param args
	 * @throws AuthenticationException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws AuthenticationException,
			MalformedURLException, IOException, ServiceException, URISyntaxException {
		getService();
		/*String koRound = "R32";
		String queryString = Constants.getQueryStringForKO(koRound);
		CellFeed cellFeed = SpreadhSheetUtilites.querySpreadSheetForCellFeed(Constants.SINGLES_TOURNAMENT_FILE,queryString,Constants.WorkSheetIndexes.KOROUND, 0);
		String[][] playerDetails = null;
		int maxRowIndex=0,columnIndex=0;
		if (koRound.equals("R32")){
			playerDetails = new String[32][4];
			maxRowIndex=31;
		} else if (koRound.equals("R16")){
			playerDetails = new String[16][4];
			maxRowIndex=15;
		}else if (koRound.equals("R8")){
			playerDetails = new String[8][4];
			maxRowIndex=7;
		}else if (koRound.equals("R4")){
			playerDetails = new String[4][4];
			maxRowIndex=3;
		}
		int rowIndex=0;
		int twoSuccessiveNames = 0;
		int noOfRecords = cellFeed.getEntries().size();
		for (CellEntry cell : cellFeed.getEntries()){
			String inputValue = cell.getCell().getValue();
			boolean isCurrentValueSetScore = true;
			try{
				Integer.parseInt(inputValue);
			}catch (Exception ex){
				isCurrentValueSetScore = false;
				if (twoSuccessiveNames ==0)playerDetails[rowIndex][columnIndex]=String.valueOf(inputValue);
				twoSuccessiveNames++;
			}
			if (isCurrentValueSetScore){
				playerDetails[rowIndex][++columnIndex]=inputValue;twoSuccessiveNames=0;
			}else{
				if (twoSuccessiveNames>1){//two successive names. so add setscores for current row index as "0"
					playerDetails[rowIndex][++columnIndex]="0";playerDetails[rowIndex][++columnIndex]="0";playerDetails[rowIndex][++columnIndex]="0";
				}
			}
			if (columnIndex==3){//we added set scores, now reset column index and move to next row
				rowIndex++;	
				columnIndex=0;
				playerDetails[rowIndex][columnIndex]=inputValue;
			}
			
			if (rowIndex >= maxRowIndex){
				if (noOfRecords == rowIndex+1){
					playerDetails[rowIndex][++columnIndex]="0";playerDetails[rowIndex][++columnIndex]="0";playerDetails[rowIndex][++columnIndex]="0";//we are done no more records
				}
				break;
			}
		}
		for (int idx=0; idx<playerDetails.length; idx++){
			for (int column=0; column<3; column++){
				System.out.println("Player details for row:"+idx+", are:"+playerDetails[idx][0]+":"+playerDetails[idx][column+1]+"\t");
			}
		}
		WorksheetEntry workSheet = getWorkSheet(Constants.SINGLES_TOURNAMENT_FILE,4, Constants.TournamentType.SINGLES);
		URL cellFeedUrl = new URI(workSheet.getCellFeedUrl().toString()).toURL();//new URI(workSheet.getCellFeedUrl().toString()+"?min-row=2&max-row=54&min-col=62&max-col=62").toURL();
		List<CellAddress> cellAddrs = new ArrayList<CellAddress>();
		int startingColumn = Constants.getColumnIndexForGroup("GroupA", 1);
		for (int row=50; row <= 51; row++) {
		      for (int col = startingColumn; col <= startingColumn+3; col++) {
		        cellAddrs.add(new CellAddress(row, col,""));
		      }
		}
		CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
		CellFeed batchRequest = new CellFeed();
		String sampleValue ="test";
		int counter=0;
		for (CellAddress  cellAddr : cellAddrs) {
            CellEntry batchEntry = new CellEntry(cellAddr.row, cellAddr.col, sampleValue+counter);counter++;
             batchEntry.setId(String.format("%s/%s", cellFeedUrl.toString(), cellAddr.value));         
             BatchUtils.setBatchId(batchEntry, cellAddr.value);
             BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);  
             batchRequest.getEntries().add(batchEntry);
       }
		cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);      
        Link batchLink =  cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);

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
		
		List<CellEntry> cellValues = cellFeed.getEntries();
		 int lastRow = cellValues.size();
		if (lastRow == 0){
			log.info("Ok no rows in required coulmn, so skip to first column section");
			cellFeedUrl = new URI(workSheet.getCellFeedUrl().toString()+"?min-row=2&max-row=2&min-col=62&max-col=62").toURL();
			cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
			cellValues = cellFeed.getEntries();
			lastRow = cellValues.size();
		}
		log.info("Total records:"+lastRow);
		//we need to figure out which section to use
		if (lastRow >0){
			//first identify which index to start with
			String playerNameAtGivenIndex = cellFeed.getEntries().get(10).getCell().getInputValue();
			if (playerNameAtGivenIndex != null){
				log.info("Ok we move to the second section of our cell range to update scores");
			}
		}
		for (CellEntry cell : cellFeed.getEntries()) {
		      // Print the cell's address in A1 notation
		      System.out.print(cell.getTitle().getPlainText() + "\t");
		      // Print the cell's address in R1C1 notation
		      System.out.print(cell.getId().substring(cell.getId().lastIndexOf('/') + 1) + "\t");
		      // Print the cell's formula or text value
		      System.out.print(cell.getCell().getInputValue() + "\t");
		      // Print the cell's calculated value if the cell's value is numeric
		      // Prints empty string if cell's value is not numeric
		      System.out.print(cell.getCell().getNumericValue() + "\t");
		      // Print the cell's displayed value (useful if the cell has a formula)
		      System.out.println(cell.getCell().getValue() + "\t");
		}
		List<String> sortedGroups = new ArrayList<String>(Constants.groups.size());
		sortedGroups.addAll(Constants.groups.keySet());
		Collections.sort(sortedGroups);
		for (String group: sortedGroups){
			String queryExcel = "?sq=groupname=";
			queryExcel += group;
			if (group.equalsIgnoreCase("ALL")){System.out.println("We don't want to query for group:ALL");continue;}
			log.info("Retrieving Players details for Group:"+group);
			URL urlListFeed = new URI(workSheet.getListFeedUrl().toString()+queryExcel).toURL();
			ListFeed listFeed = service.getFeed(urlListFeed, ListFeed.class);
			for (ListEntry row : listFeed.getEntries()){
				//log.info(row.getTitle().getPlainText() + "\t");
				// Iterate over the remaining columns, and print each cell value
				String valueinCurrentRow = "";
				for (String tag : row.getCustomElements().getTags()) {
					valueinCurrentRow = row.getCustomElements().getValue(tag);
					//log.info("Tag name:"+tag+" and its value:"+valueinCurrentRow);
					System.out.println("Column Name:"+tag+" and its value:"+valueinCurrentRow);
				}
			}

		}*/
	}
	public static SpreadsheetService getService(){
		//identify the scopes required
		if (service != null){
			WorksheetEntry worksheet = null;
			if (!singlesWorkSheetsMap.isEmpty()){
				worksheet = singlesWorkSheetsMap.get(1);
			}else if (!doublesWorkSheetsMap.isEmpty()){
				worksheet = doublesWorkSheetsMap.get(1);
			}else if (!teamTennisWorkSheetsMap.isEmpty()){
				worksheet = teamTennisWorkSheetsMap.get(1);
			}
			if (worksheet != null){
				try{
					worksheet.getSelf();
				}catch (Exception ex){
					ex.printStackTrace();
					authenticate();
					singlesWorkSheetsMap = new HashMap<Integer,WorksheetEntry>();
					doublesWorkSheetsMap =new HashMap<Integer,WorksheetEntry>();
					teamTennisWorkSheetsMap = new HashMap<Integer,WorksheetEntry>();
				}
			}
			return service;
		}
		
		String [] SCOPESArray= {"https://spreadsheets.google.com/feeds", "https://spreadsheets.google.com/feeds/spreadsheets/private/full", "https://docs.google.com/feeds"};
		final List<String> scopes = Arrays.asList(SCOPESArray);
		//service account requires to use its credentials and security key
		try{
			JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			GoogleCredential credential = new GoogleCredential.Builder()
					.setTransport(httpTransport)
					.setJsonFactory(JSON_FACTORY)
					.setServiceAccountId("990144034969-5qtvvrskkdnpkv3boql6tqc8rktuspct@developer.gserviceaccount.com")
					.setServiceAccountPrivateKeyFromP12File(new File("keyFiles/beaverpurtennis-84a14004ec8f.p12"))
					.setServiceAccountScopes(scopes)
					//.setServiceAccountUser(Constants.LOGIN_USERNAME)
					.build();
			//credential.refreshToken();
			//log.info("Credential is set to expire in :"+(credential.getExpirationTimeMilliseconds()/(1000*60))+" minutes");
			service = new SpreadsheetService("beaverpuropentennis-v1");
			service.setOAuth2Credentials(credential);
		}catch (Exception e){
			System.out.println("Error:"+e.toString());
			e.printStackTrace();
		}
		return service;
	}
	
	public static void authenticate(){
		String [] SCOPESArray= {"https://spreadsheets.google.com/feeds", "https://spreadsheets.google.com/feeds/spreadsheets/private/full", "https://docs.google.com/feeds"};
		final List<String> scopes = Arrays.asList(SCOPESArray);
		//service account requires to use its credentials and security key
		try{
			JsonFactory JSON_FACTORY = JacksonFactory.class.newInstance();
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			GoogleCredential credential = new GoogleCredential.Builder()
					.setTransport(httpTransport)
					.setJsonFactory(JSON_FACTORY)
					.setServiceAccountId("990144034969-5qtvvrskkdnpkv3boql6tqc8rktuspct@developer.gserviceaccount.com")
					.setServiceAccountPrivateKeyFromP12File(new File("keyFiles/beaverpurtennis-84a14004ec8f.p12"))
					.setServiceAccountScopes(scopes)
					//.setServiceAccountUser(Constants.LOGIN_USERNAME)
					.build();
			service = new SpreadsheetService("beaverpuropentennis-v1");
			service.setOAuth2Credentials(credential);
		}catch (Exception e){
			log.severe("Error in re-authenticating:"+e.toString());
			e.printStackTrace();
			//MailUtil.sendInfoMessageToOrganizer("Error in re-authenticating:"+e.toString());
		}
	}
	
	/**
	 * Get a static instance of required Worksheet. If there already exists a loaded worksheet, 
	 * just return the updated version by invoking .getSelf()
	 * Use this method, if you want a customized set of rows/columns from the specified worksheet
	 * @param fileName
	 * @param workSheetIndex
	 * @param tournamentType TODO
	 * @return
	 * @throws AuthenticationException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 * @throws URISyntaxException
	 */
	public static WorksheetEntry getWorkSheet(String fileName, int workSheetIndex, int tournamentType)throws AuthenticationException,
		MalformedURLException, IOException, ServiceException, URISyntaxException {
		
		WorksheetEntry workSheetFromMap = null;
		if (tournamentType == Constants.TournamentType.SINGLES){
			workSheetFromMap = singlesWorkSheetsMap.get(workSheetIndex);
		}else if (tournamentType == Constants.TournamentType.DOUBLES){
			workSheetFromMap = doublesWorkSheetsMap.get(workSheetIndex);
		}else if (tournamentType == Constants.TournamentType.TEAMTENNIS){
			workSheetFromMap = teamTennisWorkSheetsMap.get(workSheetIndex);
		}
		if (workSheetFromMap != null){
			try{
				return workSheetFromMap.getSelf();
			}catch (Exception ex)
			{
				log.severe("Error in getting sheet details, re-authenticate and try?:"+ex.toString());
				authenticate();
			}
		}
		service = getService();
		URL SPREADSHEET_FEED_URL = new URL(
				"https://spreadsheets.google.com/feeds/spreadsheets/private/full");

		// Make a request to the API and get all spreadsheets.
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
				SpreadsheetFeed.class);

		List<SpreadsheetEntry> spreadSheets = feed.getEntries();

		SpreadsheetEntry spreadsheet = spreadSheets.get(0);
		for (SpreadsheetEntry spreadSheet : spreadSheets) {
			if (spreadSheet.getTitle().getPlainText()
					.equals(fileName)) {
				spreadsheet = spreadSheet;
				log.info("Ok found our spreadsheet");
				break;
			}
		}

		// Get the worksheet for the passed in index.
		WorksheetFeed worksheetFeed = service.getFeed(
				spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();

		WorksheetEntry workSheet = worksheets.get(workSheetIndex);
		if (tournamentType == Constants.TournamentType.SINGLES){
			singlesWorkSheetsMap.put(workSheetIndex,workSheet);
		}else if (tournamentType == Constants.TournamentType.DOUBLES){
			doublesWorkSheetsMap.put(workSheetIndex,workSheet);
		}else if (tournamentType == Constants.TournamentType.TEAMTENNIS){
			teamTennisWorkSheetsMap.put(workSheetIndex,workSheet);
		}
		
		return workSheet;
	}
	
	/**
	 * Return a CellFeed link for MatchStats spreadsheet
	 * @return
	 * @throws Exception
	 */
	public static CellFeed getCellFeedForMatchStats(String tournamentFile, int tournamentType) throws Exception{
		if (cellFeedUrlForMatchStats != null){
			try {
				cellFeedUrlForMatchStats.getSelf();
			} catch (IOException | ServiceException e) {
				log.severe("A severe error in retrieving cellFeed");
				e.printStackTrace();
			}
			return cellFeedUrlForMatchStats;
		}
		URL cellFeedUrl = new URI(getWorkSheet(tournamentFile, Constants.WorkSheetIndexes.MATCHSTATS, tournamentType).getCellFeedUrl().toString()).toURL();
		cellFeedUrlForMatchStats = getService().getFeed(cellFeedUrl, CellFeed.class);
		return cellFeedUrlForMatchStats;
	}
	
	/**
	 * Get the ListFeed. By the time we request for this, we already know which worksheet in a Spreadsheet we are looking for
	 * Use this, if you want to return entire rows/columns of a worksheet
	 * @param fileName
	 * @param workSheetIndex
	 * @return
	 * @throws AuthenticationException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	public static ListFeed getListFeed(String fileName, int workSheetIndex) throws AuthenticationException,
			MalformedURLException, IOException, ServiceException {
		if (listFeedMap.containsKey(fileName+workSheetIndex)){
			return listFeedMap.get(fileName+workSheetIndex).getSelf();
		}
		service = getService();
		
		// Define the URL to request. This should never change.
		URL SPREADSHEET_FEED_URL = new URL(
				"https://spreadsheets.google.com/feeds/spreadsheets/private/full");

		// Make a request to the API and get all spreadsheets.
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
				SpreadsheetFeed.class);

		List<SpreadsheetEntry> spreadSheets = feed.getEntries();

		SpreadsheetEntry spreadsheet = spreadSheets.get(0);
		for (SpreadsheetEntry spreadSheet : spreadSheets) {
			String spreadSheetName = spreadSheet.getTitle().getPlainText();
			//System.out.println("The sheet name is:"+spreadSheetName);
			if (spreadSheet.getTitle().getPlainText()
					.equals(fileName)) {
				spreadsheet = spreadSheet;
				log.info("Ok found our spreadsheet");
				break;
			}
		}

		// Get the first worksheet of the first spreadsheet.
		WorksheetFeed worksheetFeed = service.getFeed(
				spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();

		WorksheetEntry worksheet = worksheets.get(workSheetIndex);

		// Fetch the list feed of the worksheet.
		URL listFeedUrl = worksheet.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		listFeedMap.put(fileName+workSheetIndex, listFeed);
		return listFeed;
	}
	
	/**
	 * Register a new Player
	 * @param list
	 * @param responseMap
	 * @return
	 */
	public static boolean addNewPlayer (ListFeed list, Map<String,String> responseMap){
		log.info("Total players registered till now:"+list.getEntries().size());
		String playerName = responseMap.get("firstName")+" "+responseMap.get("lastName");
		String contactNo = responseMap.get("contactNo");
		String emailAddress = responseMap.get("emailAddress");
		String rating = responseMap.get("rating");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		boolean alreadyExists=false;
		for (ListEntry row : list.getEntries()) {
			// Print the first column's cell value
			log.info(row.getTitle().getPlainText() + "\t");
			// Iterate over the remaining columns, and print each cell value
			String valueinCurrentRow = "";
			for (String tag : row.getCustomElements().getTags()) {
				valueinCurrentRow = row.getCustomElements().getValue(tag);
				if (tag.equalsIgnoreCase("playername")){
					if (valueinCurrentRow.equals(playerName)){
						log.severe("Player already registered");
						alreadyExists = true;
					}
				}else if (tag.equalsIgnoreCase("emailaddress")){
					if (valueinCurrentRow.equals(emailAddress)){
						log.severe("Player already registered");
						alreadyExists = true;
					}
				}
			}
			if (alreadyExists)break;
		}
		if (!alreadyExists){
			log.severe("No duplicate record found for our request:"+responseMap.toString());
			ListEntry entry = new ListEntry();
			entry.getCustomElements().setValueLocal("registrationdate", sdf.format(Calendar.getInstance().getTime()));
			entry.getCustomElements().setValueLocal("playername", playerName);
			entry.getCustomElements().setValueLocal("emailaddress", emailAddress);
			entry.getCustomElements().setValueLocal("rating", rating);
			entry.getCustomElements().setValueLocal("contactnumber", "("+contactNo.substring(0,3)+")"+contactNo.substring(3,6)+"-"+contactNo.substring(6));
			try {
				list.insert(entry);
			} catch (ServiceException e) {
				log.severe("Some Google service error in inserting record:"+e.toString());
				e.printStackTrace();
			} catch (IOException e) {
				log.severe("Some IO error in inserting record:"+e.toString());
				e.printStackTrace();
			} catch (Exception ex){
				log.severe("Unknown error in inserting record:"+ex.toString());
				ex.printStackTrace();
				
			}
			
		}else{
			return false;
		}
		return true;
	}
	
	/**
	 * Register a New team
	 * @param listFeed
	 * @param teamInfo
	 * @param tournamentType
	 * @return
	 */
	public static boolean addNewTeam(ListFeed listFeed, Map<String,String> teamInfo, int tournamentType){
		String teamName = teamInfo.get("teamName");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		ListEntry entry = new ListEntry();
		entry.getCustomElements().setValueLocal("dateofregistration", sdf.format(Calendar.getInstance().getTime()));
		entry.getCustomElements().setValueLocal("teamname", teamName);
		entry.getCustomElements().setValueLocal("teamavailability", teamInfo.get("teamAvailability"));
		if (tournamentType == Constants.TournamentType.DOUBLES){
			String player1Name = teamInfo.get("player1Name");
			String player1ContactNo = teamInfo.get("player1PhoneNo");
			String player1emailAddress = teamInfo.get("player1Email");
			String player1Rating = teamInfo.get("player1Rating");
			String player2Name = teamInfo.get("player2Name");
			String player2ContactNo = teamInfo.get("player2PhoneNo");
			String player2emailAddress = teamInfo.get("player2Email");
			String player2Rating = teamInfo.get("player2Rating");
			entry.getCustomElements().setValueLocal("player1name", player1Name);
			entry.getCustomElements().setValueLocal("player1email", player1emailAddress);
			entry.getCustomElements().setValueLocal("player1phoneno", "("+player1ContactNo.substring(0,3)+")"+player1ContactNo.substring(3,6)+"-"+player1ContactNo.substring(6));
			entry.getCustomElements().setValueLocal("player1rating", player1Rating);
			entry.getCustomElements().setValueLocal("player2name", player2Name);
			entry.getCustomElements().setValueLocal("player2email", player2emailAddress);
			entry.getCustomElements().setValueLocal("player2phoneno", "("+player2ContactNo.substring(0,3)+")"+player2ContactNo.substring(3,6)+"-"+player2ContactNo.substring(6));
			entry.getCustomElements().setValueLocal("player2rating", player2Rating);
		}else if (tournamentType == Constants.TournamentType.TEAMTENNIS){
			Iterator<String> keySet = teamInfo.keySet().iterator();
			while (keySet.hasNext()){
				String key = keySet.next();
				String value = (teamInfo.get(key)==null)?"":teamInfo.get(key);
				entry.getCustomElements().setValueLocal(key, value);
			}
		}
		try {
			listFeed.insert(entry);
			return true;
		} catch (ServiceException e) {
			log.severe("Some Google service error in inserting record:"+e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("Some IO error in inserting record:"+e.toString());
			e.printStackTrace();
		} catch (Exception ex){
			log.severe("Unknown error in inserting record:"+ex.toString());
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Update Player registration with PayPal transaction ID and Payment status
	 * @param list
	 * @param responseMap
	 * @param tournamentType TODO
	 * @return
	 */
	public static boolean updatePaymentStatus(ListFeed list, Map<String,String> responseMap, int tournamentType){
		String paymentStatus = responseMap.get("payment_status");
		String transactionID = responseMap.get("TransactionID");
		String customValue = responseMap.get("custom");
		String emailTo = "";
		String playerName = "";
		if (customValue !=null && !customValue.equals("")) {
			customValue = customValue.replace("+"," ");
		}else{
			customValue = responseMap.get("payer_email").replace("%40","@");
		}
		boolean foundEntry = false;
		log.severe("No of players/teams registered till now:"+list.getEntries().size());
		iterateList: for (ListEntry row : list.getEntries()) {
			// Print the first column's cell value
			log.info(row.getTitle().getPlainText() + "\t");
			// Iterate over the remaining columns, and print each cell value
			String valueinCurrentRow = "";
			for (String tag : row.getCustomElements().getTags()) {
				valueinCurrentRow = row.getCustomElements().getValue(tag);
				if (tag.equalsIgnoreCase("playername") || tag.equalsIgnoreCase("teamname") || tag.equalsIgnoreCase("captainname") || tag.equalsIgnoreCase("emailaddress") || tag.equalsIgnoreCase("captainemailaddress")){
					if (valueinCurrentRow != null && valueinCurrentRow.equalsIgnoreCase(customValue)){
						//for singles
						//emailTo = row.getCustomElements().getValue("emailaddress");
						//playerName = row.getCustomElements().getValue("playername");
						//for doubles
						emailTo = row.getCustomElements().getValue("player1email")+";"+row.getCustomElements().getValue("player2email");//get the registeree's email address, to whom we should send instead of payer
						playerName = row.getCustomElements().getValue("teamname");
						
						//for team tennis
						//emailTo = row.getCustomElements().getValue("captainEmailAddress");//email only to captain
						//playerName = row.getCustomElements().getValue("teamname");
						row.getCustomElements().setValueLocal("registrationstatus", (paymentStatus.equalsIgnoreCase("SUCCESS")?"CONFIRMED":"UNCONFIRMED"));
						row.getCustomElements().setValueLocal("paypaltransactionid", transactionID);
						foundEntry = true;
						try {
							row.update();
						} catch (IOException e) {
							log.severe("Error in updating Record:"+e.toString());
						} catch (ServiceException e) {
							log.severe("Error in updating Record:"+e.toString());						
						}
						break iterateList;
					}
				}
			}
		}
		if (!foundEntry){
			log.info("We couldn't find matching record for the Payer");
			//send email to Organizers about the mismatch
			if (tournamentType == Constants.TournamentType.SINGLES)
				MailUtil.sendOrganizersEmailForFailure("Failure to update Payment", "Failure to find Payer's info in our Registration list:"+customValue+" with transactionID:"+transactionID);
			else if (tournamentType == Constants.TournamentType.DOUBLES)
				MailUtil.sendOrganizersEmailForFailure("Failure to update Payment", "Failure to find Team's info in our Registration list:"+customValue+" with transactionID:"+transactionID);
			else if (tournamentType == Constants.TournamentType.TEAMTENNIS)
				MailUtil.sendOrganizersEmailForFailure("Failure to update Payment", "Failure to find Team's info in our Registration list:"+customValue+" with transactionID:"+transactionID);
		}else{
        	MailUtil.sendSuccessfullRegistrationEmail((emailTo.equals("")?responseMap.get("payer_email"):emailTo), playerName, tournamentType);
		}
		return foundEntry;
	}
	
	/**
	 * Return the list of Players for given Group in a given work sheet
	 * Since the group information will be static, lets persist them in memory.
	 * This needs to be customized to be useful for all type of tournaments
	 * @param fileName
	 * @param workSheetIndex
	 * @param group
	 * @param tournamentType TODO
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getDetailsForSelectedGroup(String fileName, int workSheetIndex, String group, int tournamentType){
		List list = new ArrayList();
		try{
			if (tournamentType == Constants.TournamentType.SINGLES && groupsForSingles.containsKey(group)){
				return groupsForSingles.get(group);
			} else if (tournamentType == Constants.TournamentType.DOUBLES && groupsForDoubles.containsKey(group)){
				return groupsForDoubles.get(group);
			} else if (tournamentType == Constants.TournamentType.TEAMTENNIS && groupsForTeamTennis.containsKey(group)) {
				return groupsForTeamTennis.get(group);
			}
			String queryExcel = "?sq=groupname="+group;
			service = getService();
			WorksheetEntry workSheet = getWorkSheet(fileName, workSheetIndex, tournamentType);
			URL urlListFeed = new URI(workSheet.getListFeedUrl().toString()+queryExcel).toURL();
			ListFeed listFeed = service.getFeed(urlListFeed, ListFeed.class);
			log.info("Total players/teams count for the Queried Group:"+group+" is:"+listFeed.getEntries().size());
			if (tournamentType == Constants.TournamentType.SINGLES){
				for (ListEntry entry: listFeed.getEntries()){
					Player player = new Player();
					for (String tag: entry.getCustomElements().getTags()){
						//log.info("Column Name:"+tag);
						String tagValue = entry.getCustomElements().getValue(tag);
						//log.info("Column Value:"+tagValue);
						if (tag.equals("cans")){
							player.setCansProvided(tagValue);
						}else if (tag.equals("playername")){
							player.setName(tagValue);
						} else if (tag.equals("emailaddress")){
							player.setEmailAddress(tagValue);
						} else if (tag.equals("contactno")){
							player.setContactNo(tagValue);
						} else if (tag.equals("rating")){
							player.setRating(tagValue);
						}
					}
					list.add(player);
				}
				groupsForSingles.put(group, list);
			}else if (tournamentType == Constants.TournamentType.DOUBLES){
				for (ListEntry entry: listFeed.getEntries()){
					Map<String,String> teamInfo = new HashMap<String,String>();
					for (String tag : entry.getCustomElements().getTags()){
						log.info("Column name:"+tag);
						String tagValue = entry.getCustomElements().getValue(tag);
						tagValue = (tagValue==null)?"":tagValue;
						if (tag.equals("teamname")){
							teamInfo.put("teamName", tagValue);
						}else if (tag.equals("cans")){
							teamInfo.put("cans", tagValue);
						}else if (tag.equals("availability")){
							teamInfo.put("availability", tagValue);
						}else if (tag.equals("player1name")){
							teamInfo.put("player1Name", tagValue);
						}else if (tag.equals("player1email")){
							teamInfo.put("player1EmailAddress", tagValue);
						}else if (tag.equals("player1contact")){
							teamInfo.put("player1Contact", tagValue);
						}else if (tag.equals("player2name")){
							teamInfo.put("player2Name", tagValue);
						}else if (tag.equals("player2email")){
							teamInfo.put("player2EmailAddress", tagValue);
						}else if (tag.equals("player2contact")){
							teamInfo.put("player2Contact", tagValue);
						}else if (tag.equals("player1rating")){
							teamInfo.put("player1Rating", tagValue);
						}else if (tag.equals("player2rating")){
							teamInfo.put("player2Rating", tagValue);
						}
					}
					list.add(teamInfo);
				}
				groupsForDoubles.put(group, list);
			}else if (tournamentType == Constants.TournamentType.TEAMTENNIS){

				for (ListEntry entry: listFeed.getEntries()){
					Map<String,String> teamInfo = new HashMap<String,String>();
					for (String tag : entry.getCustomElements().getTags()){
						log.info("Column name:"+tag);
						String tagValue = entry.getCustomElements().getValue(tag);
						tagValue = (tagValue==null)?"":tagValue;
						if (tag.equals("groupname")){
							continue;
						}
						if (tag.equals("teamname")){
							teamInfo.put("teamName", tagValue);
						}else if (tag.equals("cans")){
							teamInfo.put("cans", tagValue);
						}else if (tag.equals("captainemailaddress")){
							teamInfo.put("player1emailaddress", tagValue);
						}else if (tag.equals("teamavailability")){
							teamInfo.put("availability", tagValue);
						}else if (tag.indexOf("name")>-1 || tag.indexOf("rating")>-1 || tag.indexOf("number")>-1){
							teamInfo.put(tag, tagValue);
						}
					}
					list.add(teamInfo);
				}
				groupsForTeamTennis.put(group, list);
			}
		}catch (AuthenticationException e) {
			log.severe("AuthenticationException while retrieving Players list for Group info:"+e.toString());
		} catch (ServiceException e) {
			log.severe("ServiceException while retrieving Players list for Group info:"+e.toString());
			e.printStackTrace();
		} catch (Exception e){
			log.severe("Error while retrieving Players list for Group info:"+e.toString());
		}
		return list;
	}
	
	/**
	 * Generic method to return a ListFeed for the given query parameters
	 * @param fileName
	 * @param workSheetIndex
	 * @param group
	 * @param tournamentType TODO
	 * @return
	 */
	public static ListFeed querySpreadSheetForListFeed(String fileName, int workSheetIndex, String group, int tournamentType){
		try{
			String queryExcel = "?sq=groupname="+group;
			//service = getService();
			WorksheetEntry workSheet = getWorkSheet(fileName, workSheetIndex, tournamentType);
			URL urlListFeed = new URI(workSheet.getListFeedUrl().toString()+queryExcel).toURL();
			ListFeed listFeed = service.getFeed(urlListFeed, ListFeed.class);
			log.info("Total players count for the Queried Group:"+group+" is:"+listFeed.getEntries().size());
			return listFeed;
		}catch (AuthenticationException e) {
			log.severe("AuthenticationException while retrieving Players list for Group info:"+e.toString());
			e.printStackTrace();
			//MailUtil.sendInfoMessageToOrganizer("Error in authentication:"+e.toString());
		} catch (ServiceException e) {
			log.severe("ServiceException while retrieving Players list for Group info:"+e.toString());
			e.printStackTrace();
			//MailUtil.sendInfoMessageToOrganizer("Error in authentication:"+e.toString());
		} catch (Exception e){
			log.severe("Error while retrieving Players list for Group info:"+e.toString());
			e.printStackTrace();
			//MailUtil.sendInfoMessageToOrganizer("Error in authentication:"+e.toString());
		}
		return null;
	}
	
	/**
	 * Method to return CellFeed
	 * @param fileName
	 * @param queryString
	 * @param workSheetIndex
	 * @param tournamentType TODO
	 * @return
	 */
	public static CellFeed querySpreadSheetForCellFeed(String fileName, String queryString, int workSheetIndex, int tournamentType){
		try{
			WorksheetEntry workSheet = getWorkSheet(fileName, workSheetIndex, tournamentType);
			URL cellFeedUrl = new URL(workSheet.getCellFeedUrl().toString()+queryString);
			CellFeed cellFeed = getService().getFeed(cellFeedUrl, CellFeed.class);
			return cellFeed;
		}catch(Exception ex){
			log.severe("Error in retrieving MatchStats info from:"+fileName+" for:"+queryString+", with error:"+ex.toString());
		}
		return null;
	}
}