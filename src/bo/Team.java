package bo;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity(name = "team")
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer teamId;
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="id.team")
	@Fetch(FetchMode.JOIN)
	Set<TeamSeason> seasons = new HashSet<TeamSeason>();
	
	@Column
	String name;
	
	@Column
	String league;
	
	@Column
	int yearFounded;
	
	@Column
	int yearLast;
	
	
	//Getters & Setters
	public TeamSeason getTeamSeason(Integer year) {
		for (TeamSeason ts : seasons) {
			if (ts.getYear() == year)
				return ts;
		}
		return null;
	}
	
	public void addSeason(TeamSeason s){
		seasons.add(s);
	}
	
	public Set<TeamSeason> getSeasons(){
		return seasons;
	}
	
	public Integer getID(){
		return teamId;
	}
	
	public void setID(Integer id){
		teamId = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getLeague(){
		return league;
	}
	
	public void setLeague(String l){
		league = l;
	}
	
	public Integer getYearFounded(){
		return teamId;
	}
	
	public void setYearFounded(Integer year){
		yearFounded = year;
	}
	
	public Integer getYearLast(){
		return yearLast;
	}
	
	public void setYearLast(Integer year){
		yearLast = year;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Team)){
			return false;
		}
		Team other = (Team) obj;
		return (this.getName().equalsIgnoreCase(other.getName()) &&
				this.getLeague().equalsIgnoreCase(other.getLeague()));
	}

	@Override
	public int hashCode() {
		Integer hash = 0;
		if (this.getName()!=null) hash += this.getName().hashCode(); 
		if (this.getLeague()!=null) hash += this.getLeague().hashCode();
		return hash;
	}
}