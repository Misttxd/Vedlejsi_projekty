package lab.data;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lab.Tools;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Game implements MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = Score_.GAME)
    private List<Score> scores;

    public static Game generate() {
        return new Game(null, Tools.randomGameName(), null);
    }

    public static Game generateAny() {
        return switch (Tools.RANDOM.nextInt(3)) {
            case 0 -> generate();
            case 1 -> PlatformGame.generate();
            case 2 -> FirstPersonShooter.generate();
            default -> generate();
        };
    }
}
