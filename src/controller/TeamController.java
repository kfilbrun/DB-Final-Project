package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import bo.Player;
import bo.PlayerSeason;
import bo.Team;
import bo.TeamSeason;
import dataaccesslayer.HibernateUtil;
import view.TeamView;;

public class TeamController extends BaseController {

	@Override
	public void init(String query) {
        System.out.println("building dynamic html for team");
        view = new TeamView();
        process(query);
	}
	
    @Override
    protected void performAction() {
        String action = keyVals.get("action");
        System.out.println("teamcontroller performing action: " + action);
        if (action.equalsIgnoreCase(ACT_SEARCHFORM)) {
            processSearchForm();
        } else if (action.equalsIgnoreCase(ACT_SEARCH)) {
            processSearch();
        } else if (action.equalsIgnoreCase(ACT_DETAIL)) {
            processDetails();
        } else if (action.equalsIgnoreCase(ACT_ROSTER)){
        	processRoster();
        }
    }
    
    protected void processSearchForm() {
        view.buildSearchForm();
    }
    
    protected final void processSearch() {
        String name = keyVals.get("name");
        if (name == null) {
            return;
        }
        String v = keyVals.get("exact");
        boolean exact = (v != null && v.equalsIgnoreCase("on"));
        List<Team> bos = HibernateUtil.retrieveTeamsByName(name, exact);
        view.printSearchResultsMessage(name, exact);
        buildSearchResultsTableTeam(bos);
        view.buildLinkToSearch();
    }

    protected final void processDetails() {
        String id = keyVals.get("id");
        if (id == null) {
            return;
        }
        Team t = (Team) HibernateUtil.retrieveTeamById(Integer.valueOf(id));
        if (t == null) return;
        buildSearchResultsTableTeamDetail(t);
        view.buildLinkToSearch();
    }
    
    protected final void processRoster() {
        String id = keyVals.get("id");
        String year = keyVals.get("year");
        if (id == null) {
            return;
        }
        TeamSeason ts = (TeamSeason) HibernateUtil.retrieveTeamSeasonByTeamYear(Integer.valueOf(id), Integer.valueOf(year));
        if (ts == null) return;
        buildSearchResultsTableRosterDetail(ts);
        view.buildLinkToSearch();
    }

    private void buildSearchResultsTableTeam(List<Team> bos) {
        // need a row for the table headers
        String[][] table = new String[bos.size() + 1][5];
        table[0][0] = "Id";
        table[0][1] = "Name";
        table[0][2] = "League";
        table[0][3] = "Year Founded";
        table[0][4] = "Most Recent Year";
        for (int i = 0; i < bos.size(); i++) {
            Team t = bos.get(i);
            String tid = t.getID().toString();
            table[i + 1][0] = view.encodeLink(new String[]{"id"}, new String[]{tid}, tid, ACT_DETAIL, SSP_TEAM);
            table[i + 1][1] = t.getName();
            table[i + 1][2] = t.getLeague();
            table[i + 1][3] = t.getYearFounded().toString();
            table[i + 1][4] = t.getYearLast().toString();
        }
        view.buildTable(table);
    }
    
    private void buildSearchResultsTableTeamDetail(Team t) {
    	Set<TeamSeason> seasons = t.getSeasons();
    	List<TeamSeason> list = new ArrayList<TeamSeason>(seasons);
    	Collections.sort(list, TeamSeason.teamSeasonsComparator);
    	// build 2 tables.  first the team details, then the season details
        // need a row for the table headers
        String[][] teamTable = new String[2][4];
        teamTable[0][0] = "Name";
        teamTable[0][1] = "League";
        teamTable[0][2] = "Year Founded";
        teamTable[0][3] = "Most Recent Year";
        teamTable[1][0] = t.getName();
        teamTable[1][1] = t.getLeague();
        teamTable[1][2] = t.getYearFounded().toString();
        teamTable[1][3] = t.getYearLast().toString();
        
        view.buildTable(teamTable);
        
        // now for seasons
        String[][] seasonTable = new String[seasons.size()+1][7];
        seasonTable[0][0] = "Year";
        seasonTable[0][1] = "Games Played";
        seasonTable[0][2] = "Roster";
        seasonTable[0][3] = "Wins";
        seasonTable[0][4] = "Losses";
        seasonTable[0][5] = "Rank";
        seasonTable[0][6] = "Attendance";
        
        int i = 0;
        String tID = t.getID().toString();
        for (TeamSeason ts: list) {
        	i++;
        	String year = Integer.toString(ts.getYear());
        	seasonTable[i][0] = year;
        	seasonTable[i][1] = Integer.toString(ts.getGamesPlayed());
        	seasonTable[i][2] = view.encodeLink(new String[]{"id", "year"}, new String[]{tID, year}, "Roster", ACT_ROSTER, SSP_TEAM);
        	seasonTable[i][3] = Integer.toString(ts.getWins());
        	seasonTable[i][4] = Integer.toString(ts.getLosses());
        	seasonTable[i][5] = Integer.toString(ts.getRank());
        	seasonTable[i][6] = Integer.toString(ts.getTotalAttendance());
        }
        view.buildTable(seasonTable);
    }
    
    private void buildSearchResultsTableRosterDetail(TeamSeason ts){
    	Team t = ts.getTeam();
    	int year = ts.getYear();
    	Set<Player> players = ts.getPlayers();
    	
    	double totalSal = 0;
    	for(Player p : players){
    		totalSal += p.getPlayerSeason(year).getSalary();
    	}
    	
        String[][] teamYearTable = new String[2][4];
        teamYearTable[0][0] = "Name";
        teamYearTable[0][1] = "League";
        teamYearTable[0][2] = "Year";
        teamYearTable[0][3] = "Player Payroll";
        teamYearTable[1][0] = t.getName();
        teamYearTable[1][1] = t.getLeague();
        teamYearTable[1][2] = Integer.toString(year);
        teamYearTable[1][3] = DOLLAR_FORMAT.format(totalSal);
        
        view.buildTable(teamYearTable);
    	
        String[][] playerTable = new String[3][players.size() + 1];
        
        playerTable[0][0] = "Name";
        playerTable[0][1] = "Games Played";
        playerTable[0][2] = "Salary";
        
        int i = 0;
    	for(Player p : players){
    		i++;
    		String pid = p.getId().toString();
    		PlayerSeason ps = p.getPlayerSeason(year);
    		playerTable[i][0] = view.encodeLink(new String[]{"id"}, new String[]{pid}, p.getName(), ACT_DETAIL, SSP_PLAYER);
    		playerTable[i][1] = ps.getGamesPlayed().toString();
			playerTable[i][2] = DOLLAR_FORMAT.format(ps.getSalary());
    	}

    	view.buildTable(playerTable);
    }
}
