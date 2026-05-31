package lab.data;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lab.Tools;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "scores")
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Score  implements MyEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int points;
	@Enumerated(EnumType.STRING)
	private Difficult level;

    @ManyToOne
	private Player player;
    @ManyToOne
	private Game game;


	public static Score generate(List<Player> players, List<Game> games) {
		return new Score(null, Tools.RANDOM.nextInt(100, 2000), Tools.random(Difficult.values()), Tools.randomElementFrom(players), Tools.randomElementFrom(games));
	}

	public enum Difficult {
		EASY,  MEDIUM, HARD;
	}
}
