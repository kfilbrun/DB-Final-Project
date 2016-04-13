package bo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	public void addSeason(TeamSeason s){
		seasons.add(s);
	}
	
	public TeamSeason getTeamSeason(Integer year) {
		for (TeamSeason ts : seasons) {
			if (ts.getYear().equals(year))
				return ts;
		}
		return null;
	}
}
