package bo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;

@SuppressWarnings("serial")
@Entity(name = "teamseason")
public class TeamSeason implements Serializable {

	@EmbeddedId
	TeamSeasonId id;

	@Embeddable
	static class TeamSeasonId implements Serializable {
		@ManyToOne
		@JoinColumn(name = "teamid", referencedColumnName = "teamid", insertable = false, updatable = false)
		Team team;
		@Column(name="year")
		Integer teamYear;
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof TeamSeasonId)){
				return false;
			}
			TeamSeasonId other = (TeamSeasonId)obj;
			// in order for two different object of this type to be equal,
			// they must be for the same year and for the same player
			return (this.team == other.team &&
					this.teamYear == other.teamYear);
		}
		 
		@Override
		public int hashCode() {
			Integer hash = 0;
			if (this.team != null) hash += this.team.hashCode();
			if (this.teamYear != null) hash += this.teamYear.hashCode();
			return hash;
		}
	}
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "teamseasonplayer", joinColumns = {
		@JoinColumn(name = "teamId", referencedColumnName = "teamId", insertable = false, updatable = false),
		@JoinColumn(name = "year", referencedColumnName = "year", insertable = false, updatable = false) },
		inverseJoinColumns = {
		@JoinColumn(name = "playerId", referencedColumnName = "playerId", insertable = false, updatable = false),
	})
	private Set<Player> players = new HashSet<Player>(0);
	
	public Set<Player> getPlayers(){
		return players;
	}
	
	public void setPlayers(Set<Player> ps){
		this.players = ps;
	}
	
	@Column
	Integer gamesPlayed;
	@Column
	Integer wins;
	@Column
	Integer losses;
	@Column
	Integer rank;
	@Column
	Integer totalAttendance;
	
	// Hibernate needs a default constructor
	public TeamSeason() {
		id = new TeamSeasonId();
	}
	
	public TeamSeason(Team t, Integer year) {
		TeamSeasonId tsi = new TeamSeasonId();
		tsi.team = t;
		tsi.teamYear = year;
		this.id = tsi;
	}
	
	public TeamSeason(Team t, Integer year, Set<Player> ps) {
		TeamSeasonId tsi = new TeamSeasonId();
		tsi.team = t;
		tsi.teamYear = year;
		this.id = tsi;
		this.players = ps;
	}

	//Getters and setters
	public TeamSeasonId getId(){
		return id;
	}
	
	public int getYear(){
		return id.teamYear;
	}
	
	public void addPlayer(Player p){
		players.add(p);
	}
	
	public void setYear(Integer y){
		id.teamYear = y;
	}
	
	public Team getTeam(){
		return id.team;
	}
	
	public void setTeam(Team t){
		id.team = t;
	}

	public int getGamesPlayed(){
		return gamesPlayed;
	}
	
	public void setGamesPlayed(Integer games){
		gamesPlayed = games;
	}
	
	public int getWins(){
		return wins;
	}
	
	public void setWins(Integer w){
		wins = w;
	}
	
	public int getLosses(){
		return losses;
	}
	
	public void setLosses(Integer l){
		losses = l;
	}

	public int getRank(){
		return rank;
	}
	
	public void setRank(Integer r){
		rank = r;
	}
	
	public int getTotalAttendance(){
		return totalAttendance;
	}
	
	public void setTotalAttendance(Integer tA){
		totalAttendance = tA;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TeamSeason)){
			return false;
		}
		TeamSeason other = (TeamSeason)obj;
		return other.getId().equals(this.getId());
	}
	 
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	public static Comparator<TeamSeason> teamSeasonsComparator = new Comparator<TeamSeason>() {
		public int compare(TeamSeason ts1, TeamSeason ts2) {
			Integer year1 = ts1.getYear();
			Integer year2 = ts2.getYear();
			return year1.compareTo(year2);
		}
	};
}
