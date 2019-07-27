package com.beaverpurtennis.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Constants {
	//Define some constants and use them everywhere.
	public static final String SINGLES_REGISTRATION_FILE = "Singles_Registration_2019";
	public static final String DOUBLES_REGISTRATION_FILE = "Doubles_Registration_2019";
	public static final String TEAMTENNIS_REGISTRATION_FILE = "TeamTennis_Registration_2019";
	public static final String SINGLES_TOURNAMENT_FILE = "2019_Singles_draw";
	public static final String DOUBLES_TOURNAMENT_FILE = "2019_Doubles_draw";
	public static final String TEAMTENNIS_TOURNAMENT_FILE = "2019_TeamTennis_Draw";
	
	public static final String LOGIN_USERNAME = "beaverpurtennis@gmail.com";
	public static final String PAYMENT_STATUS = "";
	public static final String ORGANIZER_EMAIL = "beaverpurtennis@gmail.com";
	
	public static SortedMap<String, String> groups = new TreeMap<String,String>();
	public static Map<String, String> doublesGroups = new LinkedHashMap<String,String>();
	public static Map<String, String> teamTennisGroups = new LinkedHashMap<String,String>();
	public static class WorkSheetIndexes {
		public static final int GROUPS        = 1;
		public static final int MATCHSTATS    = 4;
		public static final int STANDINGS     = 5;
		public static final int KOROUND	      = 6;
		public static final int MINORKO	      = 7;
		public static final int KOR64	      = 8;
		public static final int DOUBLESKO     = 8;
	}
	
	public static class TournamentType{
		public static int SINGLES	= 1;
		public static int DOUBLES	= 2;
		public static int TEAMTENNIS	= 3;
	}
	
	static {
		doublesGroups.put("All", "All");
		doublesGroups.put("Red", "Red");
		doublesGroups.put("Blue", "Blue");
		doublesGroups.put("Green", "Green");
		//doublesGroups.put("Yellow", "Yellow");
	}
	static {
		groups.put("All", "All");
		groups.put("GroupA", "Group A");groups.put("GroupB", "Group B");groups.put("GroupC", "Group C");groups.put("GroupD", "Group D");
		groups.put("GroupE", "Group E");groups.put("GroupF", "Group F");groups.put("GroupG", "Group G");groups.put("GroupH", "Group H");
	}
	static {
		teamTennisGroups.put("All", "All");
		//teamTennisGroups.put("GroupA", "GroupA");teamTennisGroups.put("GroupB", "GroupB");
		//teamTennisGroups.put("Alpha", "Alpha");teamTennisGroups.put("Beta", "Beta");
	}
	
	public static String getQueryStringForScoreReporting(String group, int columnIndex){
		String query="?";
		if (group.equals("GroupD")){
			if (columnIndex ==0){
				query += "min-row=2&max-row=54&min-col=2&max-col=2";//2	
			}else{
				query += "min-row=2&max-row=54&min-col=12&max-col=12";//12
			}
		} else if (group.equals("GroupC")){
			if (columnIndex ==0){
				query += "min-row=2&max-row=54&min-col=22&max-col=22";//22	
			}else{
				query += "min-row=2&max-row=54&min-col=32&max-col=32";//32
			}
		} else if (group.equals("GroupB")){
			if (columnIndex ==0){
				query += "min-row=2&max-row=54&min-col=42&max-col=42";//42	
			}else{
				query += "min-row=2&max-row=54&min-col=52&max-col=52";//52
			}
		}else if (group.equals("GroupA")){
			if (columnIndex ==0){
				query += "min-row=2&max-row=54&min-col=62&max-col=62";//62	
			}else{
				query += "min-row=2&max-row=54&min-col=72&max-col=72";//72
			}
		}else if (group.equals("GroupE")){
			if (columnIndex ==0){
				query += "min-row=57&max-row=109&min-col=2&max-col=2";//2	
			}else{
				query += "min-row=57&max-row=109&min-col=12&max-col=12";//12
			}
		}else if (group.equals("GroupF")){
			if (columnIndex ==0){
				query += "min-row=57&max-row=109&min-col=22&max-col=22";//22	
			}else{
				query += "min-row=57&max-row=109&min-col=32&max-col=32";//32
			}
		}else if (group.equals("GroupG")){
			if (columnIndex ==0){
				query += "min-row=57&max-row=109&min-col=42&max-col=42";//42	
			}else{
				query += "min-row=57&max-row=109&min-col=52&max-col=52";//52
			}
		}else if (group.equals("GroupH")){
			if (columnIndex ==0){
				query += "min-row=57&max-row=109&min-col=62&max-col=62";//62	
			}else{
				query += "min-row=57&max-row=109&min-col=72&max-col=72";//72
			}
		}else if (group.equals("Red")){
			if (columnIndex ==0){
				query += "min-row=2&max-row=54&min-col=2&max-col=2";//2	
			}else{
				query += "min-row=2&max-row=54&min-col=12&max-col=12";//12
			}
		}else if (group.equals("Blue")){
			if (columnIndex ==0){
				query += "min-row=2&max-row=54&min-col=22&max-col=22";//22	
			}else{
				query += "min-row=2&max-row=54&min-col=32&max-col=32";//32
			}
		}else if (group.equals("Green")){
			if (columnIndex ==0){
				query += "min-row=2&max-row=54&min-col=42&max-col=42";//42	
			}else{
				query += "min-row=2&max-row=54&min-col=52&max-col=52";//52
			}
		}else if (group.equals("Yellow")){
			if (columnIndex ==0){
				query += "min-row=2&max-row=54&min-col=62&max-col=62";//62	
			}else{
				query += "min-row=2&max-row=54&min-col=72&max-col=72";//72
			}
		}else if (group.equals("All")){
			if (columnIndex == 0){
				query += "min-row=3&max-row=88&min-col=2&max-col=2";//2
			}else{
				query += "min-row=3&max-row=88&min-col=19&max-col=19";//19
			}
		}else if (group.equals("Alpha")){
			query += "min-row=3&max-row=88&min-col=2&max-col=2";//2
		}else if (group.equals("Beta")){
			query += "min-row=3&max-row=88&min-col=19&max-col=19";//19
		}
		return query;
	}
	
	public static int getColumnIndexForGroup(String group, int startingIndex){
		int columnIndex = 0;
		if (group.equals("GroupD") || group.equals("GroupE") || group.equals("Red")){
			if (startingIndex ==0){
				columnIndex=2;
			}else{
				columnIndex=12;
			}
		} else if (group.equals("GroupC") || group.equals("GroupF") || group.equals("Blue")){
			if (startingIndex ==0){
				columnIndex=22;
			}else{
				columnIndex=32;
			}
		} else if (group.equals("GroupB") || group.equals("GroupG") || group.equals("Green")){
			if (startingIndex ==0){
				columnIndex=42;
			}else{
				columnIndex=52;
			}
		}else if (group.equals("GroupA")|| group.equals("GroupH") || group.equals("Yellow")){
			if (startingIndex ==0){
				columnIndex=62;
			}else{
				columnIndex=72;
			}
		}else if (group.equals("All")){
			if (startingIndex ==0){
				columnIndex=2;
			}else{
				columnIndex=19;
			}
		} else if (group.equals("Alpha")){
			columnIndex=2;
		} else if (group.equals("Beta")){
			columnIndex=19;
		}
			
		return columnIndex;
	}
	
	public static int getRowIndexForGroup(String group){
		int rowIndex = 0;
		if (group.equals("GroupA") || group.equals("GroupB") || group.equals("GroupC") || group.equals("GroupD") 
				|| group.equals("Red") || group.equals("Blue") || group.equals("Green") || group.equals("Yellow")){
			rowIndex = 0;
		} else if (group.equals("All")){
			rowIndex = 0;
		}else{
			rowIndex = 55;
		}
		return rowIndex;
	}
	
	public static String getQueryStringForKO(String round){
		String query="?";
		if (round.equals("R32")){
			query += "min-row=4&max-row=50&min-col=2&max-col=5";//2
		}else if (round.equals("R16")){
			query += "min-row=5&max-row=48&min-col=12&max-col=15";//12
		}else if (round.equals("R8")){
			query += "min-row=8&max-row=45&min-col=22&max-col=25";//22
		}else if (round.equals("R4")){
			query += "min-row=14&max-row=39&min-col=32&max-col=35";//32
		}
		return query;
	}
	
	public static String getQueryStringForKOScoresReport(String round){
		String query="?";
		if (round.equals("R32")){
			query += "min-row=4&max-row=50&min-col=2&max-col=2";	
		}else if (round.equals("R16")){
			query += "min-row=5&max-row=48&min-col=12&max-col=12";	
		}else if (round.equals("R8")){
			query += "min-row=8&max-row=45&min-col=22&max-col=22";
		}else if (round.equals("R4")){
			query += "min-row=14&max-row=39&min-col=32&max-col=32";
		}
		return query;
	}
	
	public static int getColumnIndexForKO(String round){
		if (round.equals("R32")){
			return 3;
		}else if (round.equals("R16")){
			return 13;
		}else if (round.equals("R8")){
			return 23;
		}else if (round.equals("R4")){
			return 33;
		}
		return 3;
	}
	
	public static String getQueryStringForSinglesKO (String round) {
		String query = "?sq=groupname="+round+"&";
		if (round.equals("R64")) {
			query += "min-col=3&max-col=6&min-row=4&max-row=129";
		} else if (round.equals("R32")) {
			query += "min-col=13&max-col=16&min-row=6&max-row=127";
		} else if (round.equals("R16")) {
			query += "min-col=23&max-col=26&min-row=10&max-row=123";
		} else if (round.equals("R8")) {
			query += "min-col=33&max-col=36&min-row=18&max-row=115";
		} else if (round.equals("R4")) {
			query += "min-col=43&max-col=46&min-row=34&max-row=96";
		}
		return query;
	}

    public static String getQueryStringForDoublesKO(String round) {
        String query = "?sq=groupname=" + round + "&";
        if (round.equals("R32")) {
            query += "min-col=3&max-col=6&min-row=4&max-row=65";
        } else if (round.equals("R16")) {
            query += "min-col=13&max-col=16&min-row=6&max-row=63";
        } else if (round.equals("R8")) {
            query += "min-col=23&max-col=26&min-row=10&max-row=59";
        } else if (round.equals("R4")) {
            query += "min-col=33&max-col=36&min-row=18&max-row=51";
        } 
        return query;
    }
}
