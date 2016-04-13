package bo;

import java.io.Serializable;
import java.util.Comparator;
import javax.persistence.JoinColumns;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import bo.PlayerSeason.PlayerSeasonId;
import bo.TeamSeason.TeamSeasonId;

@SuppressWarnings("serial")
@Entity(name = "teamseasonplayer")
public class TeamSeasonPlayer {
	
	@EmbeddedId
	TeamSeasonPlayerId id;
	
	@Embeddable
	static class TeamSeasonPlayerId implements Serializable {
		@ManyToOne
		@JoinColumns({
			@JoinColumn(name = "teamid", referencedColumnName = "teamid", insertable = false, updatable = false),
			@JoinColumn(name = "year", referencedColumnName = "year", insertable = false, updatable = false) 
		})
		TeamSeason ts;
		@Column(name="player")
		Player player;
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof TeamSeasonPlayerId)){
				return false;
			}
			TeamSeasonPlayerId other = (TeamSeasonPlayerId)obj;
			// in order for two different object of this type to be equal,
			// they must be for the same year and for the same player
			return (this.ts == other.ts &&
					this.player == other.player);
		}
		 
		@Override
		public int hashCode() {
			Integer hash = 0;
			if (this.ts != null) hash += this.ts.hashCode();
			if (this.player != null) hash += this.player.hashCode();
			return hash;
		}
	}
	
	// Hibernate needs a default constructor
	public TeamSeasonPlayer() {}
	
	public TeamSeasonPlayer(Player p, TeamSeason ts) {
		TeamSeasonPlayerId tspi = new TeamSeasonPlayerId();
		tspi.player = p;
		tspi.ts = ts;
		this.id = tspi;
	}
	
	public TeamSeasonPlayerId getId(){
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TeamSeasonPlayer)){
			return false;
		}
		TeamSeasonPlayer other = (TeamSeasonPlayer)obj;
		return other.getId().equals(this.getId());
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
}
