package com.beaverpurtennis.servlet;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.MailUtil;
import com.beaverpurtennis.utils.Player;
import com.beaverpurtennis.utils.SpreadSheetUtilites;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

/**
 * A simple servlet to organize balls distribution
 * @author naresh.sankaramaddi
 *
 */
public class BallsDistributionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8541662674390870023L;

	private static final Logger log = Logger.getLogger(BallsDistributionServlet.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("");
		//String player = request.getParameter("requestedBy");
		String requestToSubmit = request.getParameter("submitRequest");
		try {
			//change the worksheet for each tournament type
			WorksheetEntry workSheet = SpreadSheetUtilites.getWorkSheet(Constants.SINGLES_TOURNAMENT_FILE, 8, Constants.TournamentType.SINGLES);
			String requestedDate = request.getParameter("selectedDay");
			List<String> nextFewDays = getNextFewDays(requestedDate);
			String queryString = "";
			boolean insertRecord = true;
			ListEntry entry = new ListEntry();
			if (requestToSubmit == null || requestToSubmit.equals("")){
				queryString= "?sq=date="+nextFewDays.get(0);
				insertRecord = false;
				request.setAttribute("todayDate",nextFewDays.get(0));
			}
			URL urlListFeed = new URI(workSheet.getListFeedUrl().toString()+queryString).toURL();
			ListFeed listFeed = SpreadSheetUtilites.getService().getFeed(urlListFeed, ListFeed.class);
			if (insertRecord){
				entry.getCustomElements().setValueLocal("date", requestedDate);
				entry.getCustomElements().setValueLocal("requestedby", request.getParameter("playerName"));
				entry.getCustomElements().setValueLocal("location", request.getParameter("location"));
				listFeed.insert(entry);
				listFeed.getSelf();
			}
			List<Player> playersList = new ArrayList<Player>();
			for (ListEntry row: listFeed.getEntries()){
				Player player = new Player();
				player.setName(row.getCustomElements().getValue("requestedby"));
				player.setEmailAddress(row.getCustomElements().getValue("location"));
				playersList.add(player);
			}
			request.setAttribute("playersList", playersList);
			if (insertRecord){
				request.getRequestDispatcher("/html/ballsDistroRequestSuccess.html").forward(request, response);
				MailUtil.deviceUsed = request.getHeader("User-Agent");
				MailUtil.sendMailToOrganizersForBallsDistroRequest(request.getParameter("playerName"), request.getParameter("location"),requestedDate);
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.getRequestDispatcher("/jsp/ballsDistribution.jsp").forward(request, response);
	}
	
	private List<String> getNextFewDays(String inputDate){
		String date = "";
		Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> nextFewDays = new ArrayList<String>();
		Calendar calendar = Calendar.getInstance();
		date = sdf.format(calendar.getTime());
		nextFewDays.add(date);
		for (int idx=1; idx<6; idx++){
			calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, idx);
			nextFewDays.add(sdf.format(calendar.getTime()));
		}
		return nextFewDays;
	}
}