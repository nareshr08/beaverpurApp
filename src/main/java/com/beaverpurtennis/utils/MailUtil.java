package com.beaverpurtennis.utils;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

	private static Properties props = new Properties();
	private static Session session = Session.getDefaultInstance(props, null);
	private static Logger logger = Logger.getLogger(MailUtil.class.getName());
	public static String deviceUsed="";

	/**
	 * Email Player for Registration
	 * @param playerEmail
	 * @param playerName
	 * @param tournamentType TODO
	 * @param deviceUsed
	 */
	public static void sendSuccessfullRegistrationEmail(String playerEmail, String playerName, int tournamentType){
		try {
			if (playerEmail == null || playerEmail.equals("")) {
				logger.severe("Invalid Email address");
				return;
			}
			if (playerName == null || playerName.trim().equals("")){
				logger.severe("Invalid Player Name");
				return;
			}
			//String subject ="Registered for Singles Tournament 2019";
			String subject = "Registered for Doubles Tournament 2019";
			//String subject = "Registered for Team Tennis Tournament 2019";
			String messageBody = "<html><body>Hello "+playerName+"<br/>";
			//messageBody += "Thank you!!We have received your payment and this email is to confirm your participation in 2019 Singles tournament.<br/>To view the list of players registered for the event, click link:<a href=\"http://beaverpurtennis.appspot.com/playersList\">Registered Players</a>";
			messageBody += "Thank you!!We have received your payment and this email is to confirm your participation in 2019 Doubles tournament.<br/>To view the list of teams registered for the event, click link:<a href=\"http://beaverpurtennis.appspot.com/getRegisteredTeamsList\">Registered Teams</a>";
			//messageBody += "Thank you!!We have received your payment and this email is to confirm your participation in 2019 Team Tennis tournament.<br/>To view the list of teams registered for the event, click link:<a href=\"http://beaverpurtennis.appspot.com/getRegisteredTeamsList\">Registered Teams</a>";
			messageBody += "<br/>Thanks<br/>Beaverpur Organizers</body></html>";

			//javax mail service--commenting out to try sendgrid email
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(Constants.ORGANIZER_EMAIL,"Beaverpur Organizers"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress("beaverpurtennis@gmail.com", "Organizers"));
			msg.setSubject(subject);
			msg.setContent(messageBody,"text/html; charset=utf-8");
			if (tournamentType == Constants.TournamentType.DOUBLES){
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(playerEmail.split(";")[0]));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(playerEmail.split(";")[1]));
			}else{
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(playerEmail));
			}

		    //logger.info("Sending Successful registration message to player:"+playerName+" with provided email address:"+playerEmail);
			//logger.severe("User device info:"+deviceUsed);
			//logger.severe("Sending email:"+messageBody);
		    Transport.send(msg);
		}catch (Exception ex){
			logger.severe("Error in sending email to player:"+playerName+", error is:"+ex.toString());
		}
	}
	
	/**
	 * Email Organizer for any failure in app. Helps to understand fix any bugs we may find
	 * @param subject TODO
	 * @param message
	 * @param deviceUsed
	 */
	public static void sendOrganizersEmailForFailure(String subject, String message){
		try{
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(Constants.LOGIN_USERNAME));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(Constants.ORGANIZER_EMAIL, "Organizers"));
			msg.setSubject(subject);
			String messageBody = "<html><body>Hello Organizers<br/>";
			messageBody += "There seems to be an error in your new application. <br/>Log trace:"+message;
			messageBody += "<br/>Thanks<br/>Beaverpur Organizers</body></html>";
			msg.setContent(messageBody,"text/html; charset=utf-8");
		    Transport.send(msg);
		}catch (Exception ex){
			logger.severe("Some error:"+ex.toString());
		}
	}
	
	/**
	 * Info message to track users behavior like what device used to access our app etc
	 * @param deviceUsed
	 */
	public static void sendInfoMessageToOrganizer(String info){
		try{
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(Constants.LOGIN_USERNAME));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress("nareshever4u@gmail.com"));
			msg.setSubject("User behavior info");
			String messageBody = "<html><body>Hello Organizer<br/>";
			messageBody += "Some one accessed our application via device:"+deviceUsed+"<br/>"+((info !=null)?info:"");
			messageBody += "<br/>Thanks<br/>Beaverpur Organizers</body></html>";
			msg.setContent(messageBody,"text/html; charset=utf-8");
		    Transport.send(msg);
		}catch (Exception ex){
			logger.severe("Some error:"+ex.toString());
		}
	}
	
	/**
	 * Report Scores update
	 * @param scoresArray
	 * @param tournamentType TODO
	 */
	public static void sendMailForScoresUpdate(String[][] scoresArray, int tournamentType){
		try{
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(Constants.LOGIN_USERNAME));
			String team1Name = scoresArray[0][0];
			String team2Name = scoresArray[1][0];
			String groupName = scoresArray[2][0];
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(Constants.LOGIN_USERNAME, "Beaverpur Organizers"));
			if (tournamentType==Constants.TournamentType.SINGLES){
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(scoresArray[2][1], team1Name));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(scoresArray[2][2], team2Name));
			}else if (tournamentType == Constants.TournamentType.DOUBLES){
				String team1Email = scoresArray[2][1];
				String team2Email = scoresArray[2][2];
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(team1Email.split(";")[0], team1Email.split(";")[0]));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(team1Email.split(";")[1], team1Email.split(";")[1]));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(team2Email.split(";")[0], team2Email.split(";")[0]));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(team2Email.split(";")[1], team2Email.split(";")[1]));
			}else if (tournamentType == Constants.TournamentType.TEAMTENNIS){
				String team1Email = scoresArray[2][0];
				String team2Email = scoresArray[2][1];
				groupName = scoresArray[2][2];
				InternetAddress[] addressArray = InternetAddress.parse(team1Email.replace(";", ","));
				msg.addRecipients(Message.RecipientType.TO, addressArray);
				addressArray = InternetAddress.parse(team2Email.replace(";", ","));
				msg.addRecipients(Message.RecipientType.TO, addressArray);
				//msg.addRecipient(Message.RecipientType.TO,new InternetAddress(Constants.ORGANIZER_EMAIL, "Naresh"));
			}
			String messageBody="";
			if (tournamentType == Constants.TournamentType.TEAMTENNIS){
				int team1Score =0;
				int team2Score =0;
				int setsDifference =0;
				for (int idx=2;idx<7; idx=idx+2){
					String tempTeam1Score = scoresArray[0][idx];
					String tempTeam2Score = scoresArray[1][idx];
					int currentSetScoreForTeam1=0;
					if (!tempTeam1Score.equals("")){
						currentSetScoreForTeam1 = Integer.valueOf(scoresArray[0][idx]);	
					}
					team1Score += currentSetScoreForTeam1;

					int currentSetScoreForTeam2 =0;
					if (!tempTeam2Score.equals("")){
						currentSetScoreForTeam2 = Integer.valueOf(scoresArray[1][idx]);	
					}
					
					if (currentSetScoreForTeam1>currentSetScoreForTeam2){
						setsDifference++;
					}else{
						setsDifference--;
					}
					team2Score += currentSetScoreForTeam2;
				}
				boolean team1Winner = false; boolean team2Winner = false;
				if (team1Score>team2Score){
					team1Winner = true;
				}else if (team2Score>team1Score){
					team2Winner = true;
				}else if (setsDifference>0){
					team1Winner = true;
				}else if (setsDifference<0){
					team2Winner = true;
				}
				msg.setSubject("Scores Updated for TeamTennis group:"+groupName+" game between:"+team1Name+"vs"+team2Name);
				messageBody= "<html><body>Hello Captains:<br/>Your match scores have been updated with below details:<br/>";
				messageBody+= "<table border='1'><th>Team Name</th><th>Singles Game1 (S1)</th><th>Singles Game2 (S2)</th><th>Doubles Game1 (D1)</th>";
				//messageBody+= "<th>Doubles Game2 (D2)</th><th>Doubles Game3 (D3)</th><th>Total</th>";
				messageBody+= "<tr><td style=\"text-align:right;\">"+(team1Winner?"<span style=\"color:green;\">&#10004;</span> ":"")+scoresArray[0][0]+"</td>";
				messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+scoresArray[0][1]+"</td><td style=\"text-align:right;\">"+scoresArray[0][2]+"</td></table></td>";
				messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+scoresArray[0][3]+"</td><td style=\"text-align:right;\">"+scoresArray[0][4]+"</td></table></td>";
				messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+scoresArray[0][5]+"</td><td style=\"text-align:right;\">"+scoresArray[0][6]+"</td></table></td>";
				//messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+scoresArray[0][7]+"</td><td style=\"text-align:right;\">"+scoresArray[0][8]+"</td></table></td>";
				//messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+scoresArray[0][9]+"</td><td style=\"text-align:right;\">"+scoresArray[0][10]+"</td></table></td>";
				messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+team1Score+"</td></table></td></tr>";
				messageBody+= "<tr><td style=\"text-align:right;\">"+(team2Winner?"<span style=\"color:green;\">&#10004;</span> ":"")+scoresArray[1][0]+"</td>";
				messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+scoresArray[1][1]+"</td><td style=\"text-align:right;\">"+scoresArray[1][2]+"</td></table></td>";
				messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+scoresArray[1][3]+"</td><td style=\"text-align:right;\">"+scoresArray[1][4]+"</td></table></td>";
				messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+scoresArray[1][5]+"</td><td style=\"text-align:right;\">"+scoresArray[1][6]+"</td></table></td>";
				//messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+scoresArray[1][7]+"</td><td style=\"text-align:right;\">"+scoresArray[1][8]+"</td></table></td>";
				//messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+scoresArray[1][9]+"</td><td style=\"text-align:right;\">"+scoresArray[1][10]+"</td></table></td>";
				messageBody+= "<td><table style=\"width:100%;\"><tr><td style=\"text-align:left;\">"+team2Score+"</td></table></td></tr></table>";
				messageBody += "<br/>Please review and let us know if anything needs to be updated.<br/>";
				messageBody += "\nTo review the standings in your group, click below link:<br/>";
			}else{
				msg.setSubject("Scores Updated for:"+groupName+ (tournamentType==Constants.TournamentType.SINGLES?" Singles":" Doubles" )+" game between:"+team1Name+" vs "+team2Name);
				messageBody = "<html><body>Hello Players from:"+groupName+" group<br/>Your game scores have been updated with below details:<br/>";
				messageBody += "<table border='1'><th>Player Name</th><th>Set1</th><th>Set2</th><th>Set3</th>";
				messageBody += "<tr><td>"+scoresArray[0][0]+"</td><td style='text-align: center;'>"+scoresArray[0][1]+"</td><td style='text-align: center;'>"+scoresArray[0][2]+"</td><td style='text-align: center;'>"+scoresArray[0][3]+"</td></tr>";
				messageBody += "<tr><td>"+scoresArray[1][0]+"</td><td style='text-align: center;'>"+scoresArray[1][1]+"</td><td style='text-align: center;'>"+scoresArray[1][2]+"</td><td style='text-align: center;'>"+scoresArray[1][3]+"</td></tr></table>";
				messageBody += "<br/>Please review and let us know if anything needs to be updated.<br/>";
				messageBody += "\nTo review the standings in your group, click below link:<br/>";
			}
			
			if (tournamentType == Constants.TournamentType.SINGLES){
				messageBody += "<a href=\"http://www.beaverpurtennis.appspot.com/playersStanding?groupRound="+groupName+"\">Standings</a>";
			}else if (tournamentType == Constants.TournamentType.DOUBLES){
				messageBody += "<a href=\"http://www.beaverpurtennis.appspot.com/doublesStandings?groupRound="+groupName+"\">Standings</a>";
			}else if (tournamentType == Constants.TournamentType.TEAMTENNIS){
				messageBody += "<a href=\"http://www.beaverpurtennis.appspot.com/teamTennisStanding?groupRound="+groupName+"\">Standings</a>";
			}
			messageBody += "<br/>Thanks<br/>Beaverpur Organizers</body></html>";
			//messageBody += "Device Info:"+deviceUsed;
			msg.setContent(messageBody,"text/html; charset=utf-8");
			logger.info("Message to be sent:"+messageBody);
		    Transport.send(msg);
		}catch (Exception ex){
			logger.severe("Some error:"+ex.toString());
		}
	}
	
	public static void sendMailForKOScoresUpdate(String[][] scoresArray){
		try{
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(Constants.LOGIN_USERNAME));
			String player1Name = scoresArray[0][0];
			String player2Name = scoresArray[1][0];
			String koRound = scoresArray[2][0];
			String koType = scoresArray[2][1];
			String tournamentType = scoresArray[3][0];
			String messageBody ="";
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(Constants.LOGIN_USERNAME, "Beaverpur Organizers"));
			if (tournamentType.equals("Doubles")){
				String team1Emails = MailUtil.getEmailIdForPlayer(Constants.DOUBLES_TOURNAMENT_FILE,player1Name,Constants.TournamentType.DOUBLES);
				String team2Emails = MailUtil.getEmailIdForPlayer(Constants.DOUBLES_TOURNAMENT_FILE,player2Name,Constants.TournamentType.DOUBLES);
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(team1Emails.split(";")[0],team1Emails.split(";")[0]));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(team1Emails.split(";")[1],team1Emails.split(";")[1]));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(team2Emails.split(";")[0],team2Emails.split(";")[0]));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(team2Emails.split(";")[1],team2Emails.split(";")[1]));
				//msg.setSubject("Scores Updated for:"+koType+" "+koRound+" Doubles Game between:"+player1Name+"vs"+player2Name);
				msg.setSubject("Scores Updated for:"+koRound+" between:"+player1Name+"vs"+player2Name);
				messageBody = "<html><body>Hello Teams: <br/>Your game scores have been updated with below details for:"+koRound+"<br/>";
				messageBody += "<table border='1'><th>Team Name</th><th>Set1</th><th>Set2</th><th>Set3</th>";
			}else if (tournamentType.equals("TeamTennis")){
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(MailUtil.getEmailIdForPlayer(Constants.SINGLES_TOURNAMENT_FILE,player1Name,Constants.TournamentType.TEAMTENNIS), player1Name));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(MailUtil.getEmailIdForPlayer(Constants.SINGLES_TOURNAMENT_FILE,player2Name,Constants.TournamentType.TEAMTENNIS), player2Name));
				msg.setSubject("Scores Updated for:"+koRound+" Doubles Game between:"+player1Name+"vs"+player2Name);
				messageBody += "<table border='1'><th>Player Name</th><th>Set1</th><th>Set2</th><th>Set3</th>";
			}else{
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(MailUtil.getEmailIdForPlayer(Constants.SINGLES_TOURNAMENT_FILE,player1Name,Constants.TournamentType.SINGLES), player1Name));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(MailUtil.getEmailIdForPlayer(Constants.SINGLES_TOURNAMENT_FILE,player2Name,Constants.TournamentType.SINGLES), player2Name));
				//msg.setSubject("Scores Updated for:"+koType+" "+koRound+" Singles Game between:"+player1Name+"vs"+player2Name);
				msg.setSubject("Scores Updated for:"+koRound+" Singles Game between:"+player1Name+"vs"+player2Name);
				//messageBody = "<html><body>Hello Players: <br/>Your game scores have been updated with below details for:"+koType+" "+koRound+"<br/>";
				messageBody = "<html><body>Hello Players: <br/>Your game scores have been updated with below details for:"+koRound+"<br/>";
				messageBody += "<table border='1'><th>Player Name</th><th>Set1</th><th>Set2</th><th>Set3</th>";
			}
			
			messageBody += "<tr><td>"+scoresArray[0][0]+"</td><td style='text-align: center;'>"+scoresArray[0][1]+"</td><td style='text-align: center;'>"+scoresArray[0][2]+"</td><td style='text-align: center;'>"+scoresArray[0][3]+"</td></tr>";
			messageBody += "<tr><td>"+scoresArray[1][0]+"</td><td style='text-align: center;'>"+scoresArray[1][1]+"</td><td style='text-align: center;'>"+scoresArray[1][2]+"</td><td style='text-align: center;'>"+scoresArray[1][3]+"</td></tr></table>";
			messageBody += "<br/>Please review and let us know if anything needs to be updated.<br/>";
			//messageBody += "\nTo review the standings in your group, click below link:<br/>";
			if (koType.equals("Major")){
				if (tournamentType.equals("Doubles")){
				    messageBody += "<a href=\"http://www.beaverpurtennis.appspot.com/koRoundForDoubles?koRound="+koRound+"\">KO Round</a>";
					//messageBody += "<a href=\"http://www.beaverpurtennis.appspot.com/koRoundForDoubles?koType="+koType+"&koRound="+koRound+"\">KO Round</a>";
				}else{
					//messageBody += "<a href=\"http://www.beaverpurtennis.appspot.com/koMajorForSingles?koRound="+koRound+"\">KO Round</a>";
					messageBody += "<a href=\"http://www.beaverpurtennis.appspot.com/singlesKO?koRound="+koRound+"\">KO Round</a>";
				}
			}else if (koType.equals("Minor")){
				if (tournamentType.equals("Doubles")){
					messageBody += "<a href=\"http://www.beaverpurtennis.appspot.com/koRoundForDoubles?koType=Minor&koRound="+koRound+"\">KO Round</a>";
				}else{
					messageBody += "<a href=\"http://www.beaverpurtennis.appspot.com/koMinorForSingles?koRound="+koRound+"\">KO Round</a>";
				}
			}else if (koType.equals("TeamTennis")){
				messageBody += "<a href=\"http://www.beaverpurtennis.appspot.com/koRoundForDoubles?koRound="+koRound+"\">KO Round</a>";
			}

			messageBody += "<br/>Thanks<br/>Beaverpur Organizers</body></html>";
			msg.setContent(messageBody,"text/html; charset=utf-8");
			logger.info("Message to be sent:"+messageBody);
		    Transport.send(msg);
		}catch (Exception ex){
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public static String getEmailIdForPlayer(String fileName, String playerName,int tournamentType){
		if (tournamentType == Constants.TournamentType.SINGLES){
			for(String group: Constants.groups.keySet()){
				List<Player> playersList = SpreadSheetUtilites.getDetailsForSelectedGroup(fileName, Constants.WorkSheetIndexes.GROUPS, group, tournamentType);
				for (Player player : playersList){
					if (player.getName().equals(playerName)){
						return player.getEmailAddress();
					}
				}
			}
		}else if (tournamentType == Constants.TournamentType.DOUBLES){
			for (String group : Constants.doublesGroups.keySet()){
				List<Map<String,String>> teamsList = SpreadSheetUtilites.getDetailsForSelectedGroup(fileName, Constants.WorkSheetIndexes.GROUPS, group, tournamentType);
				for (Map<String, String> teamInfo: teamsList){
					if (playerName.equals(teamInfo.get("teamName"))){
						return teamInfo.get("player1EmailAddress")+";"+teamInfo.get("player2EmailAddress");
					}
				}
			}
		}
		return "";
	}
	
	/**
	 * Email for Balls distribution request to Organizers
	 * @param message
	 */
	public static void sendMailToOrganizersForBallsDistroRequest(String playerName, String location, String day){
		try{
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(Constants.LOGIN_USERNAME));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(Constants.ORGANIZER_EMAIL, "Organizers"));
			msg.setSubject("Request for Balls Distribution");
			String messageBody = "<html><body>Hello Organizers<br/>";
			messageBody += playerName+" requested to collect balls from Location:"+location+".<br/> Device used by player:"+deviceUsed;
			messageBody += "<br/>Thanks<br/>Beaverpur Organizers</body></html>";
			msg.setContent(messageBody,"text/html; charset=utf-8");
		    Transport.send(msg);
		}catch (Exception ex){
			logger.severe("Some error:"+ex.toString());
		}
	}
}
