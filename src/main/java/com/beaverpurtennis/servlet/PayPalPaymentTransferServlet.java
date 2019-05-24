package com.beaverpurtennis.servlet;

import com.beaverpurtennis.utils.Constants;
import com.beaverpurtennis.utils.MailUtil;
import com.beaverpurtennis.utils.SpreadSheetUtilites;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class PayPalPaymentTransferServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2217995865288154806L;
	private static final Logger log = Logger.getLogger(PayPalPaymentTransferServlet.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	private final String USER_AGENT = "Mozilla/5.0";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String transactionID = request.getParameter("tx");
		System.out.println("TransactionID is:"+transactionID);
		String authToken = "ITLTS7uAdlEFZfJlardAVA-mrK5Excv7Gr5_YHc3mW0RURyBIQyZdgY1EP8";//Prod token 
		//String authToken = "VNntCu34umfPNYJDodAxYr6abxtGkfM5tQjiH6yieljS-2DgHoJwPp_F87S"; //Test token
		String command = "_notify-synch";
		String url = "https://www.paypal.com/cgi-bin/webscr";//prod URL
		//String url = "https://www.sandbox.paypal.com/cgi-bin/webscr";
		Map<String,String> responseMap = new HashMap<String,String>();
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		String userAgentInfo = request.getHeader("User-Agent");
		//MailUtil.deviceUsed = userAgentInfo;

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			String urlParameters = "tx="+transactionID+"&at="+authToken+"&cmd="+command;// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			//log.info("content Type returned:"+con.getContentType());
			StringBuffer urlresponse = new StringBuffer();
			responseMap.put("TransactionID", transactionID);
			int index=0;
			ArrayList<String> keyTokens = new ArrayList<String>();
			keyTokens.add("custom");keyTokens.add("payer_email");keyTokens.add("first_name");keyTokens.add("last_name");
			while ((inputLine = in.readLine()) != null) {
				if (index==0) {
					index++;
					responseMap.put("payment_status", inputLine);
					continue;
				}
				String key = inputLine.split("=")[0];
				if (keyTokens.contains(key)){
					try{
						String value = inputLine.split("=")[1];
						responseMap.put(key, value);	
					}catch (Exception ex){
						log.severe("Error in extracting value for:"+key+", msg is:"+ex.toString());
					}
				}
				urlresponse.append(inputLine+"\n");
			}
			in.close();
			//print result
			//log.info(urlresponse.toString());
			ListFeed list = SpreadSheetUtilites.getListFeed(Constants.SINGLES_REGISTRATION_FILE, 0);
			//ListFeed list = SpreadSheetUtilites.getListFeed(Constants.DOUBLES_REGISTRATION_FILE, 0);
			//ListFeed list = SpreadSheetUtilites.getListFeed(Constants.TEAMTENNIS_REGISTRATION_FILE, 0);
            boolean status = SpreadSheetUtilites.updatePaymentStatus(list,responseMap, Constants.TournamentType.SINGLES);
            if (status){
            	log.info("Updated our record successfully");
            	request.getRequestDispatcher("/html/success.html").forward(request, response);
            }else{
            	log.severe("error in updating record");
            	request.getRequestDispatcher("/html/success.html").forward(request, response);
            }
		}catch (ServiceException e) {
			log.severe("Unexpected error in invoking Google services:"+e.toString());
			writer.println("Error in registering");
			MailUtil.sendOrganizersEmailForFailure("Failure to update PaymentDetails", e.toString()+"\n"+responseMap.toString());
			request.getRequestDispatcher("/html/failure.html").forward(request, response);
		}catch (Exception ex){
			log.severe("Error in getting response from PayPal:"+ex.toString());
			writer.println("Error in registering");
			MailUtil.sendOrganizersEmailForFailure("Failure to update PaymentDetails", ex.toString()+"\n"+responseMap.toString());
			request.getRequestDispatcher("/html/failure.html").forward(request, response);
		}
	}
}