package lab.travel;

import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URISyntaxException;
import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import cz.vsb.fei.kelvin.unittest.TextFileContains;

class TravelAgencyTest {

    @ParameterizedTest
    // @formatter:off
    @CsvSource({
        "Person.java,@ToString",
        "Person.java,@NoArgsConstructor",
        "Person.java,@AllArgsConstructor",
        "Person.java,@Builder",
        "Person.java,@Getter",
        "Person.java,@Setter",
        "Person.java,@ToString\\.Exclude",
        "Ticket.java,@Getter",
        "Ticket.java,@Setter",
        "Ticket.java,@ToString",
        "Ticket.java,@EqualsAndHashCode",
        "BoatTicket.java,@Getter",
        "BoatTicket.java,@Setter",
        "BoatTicket.java,@ToString\\(callSuper\\s* =\\s*true\\)",
        "BoatTicket.java,@EqualsAndHashCode\\(callSuper\\s*=\\s*true\\)",
        "CinemaTicket.java,@Getter",
        "CinemaTicket.java,@Setter",
        "CinemaTicket.java,@ToString\\(callSuper\\s* =\\s*true\\)",
        "CinemaTicket.java,@EqualsAndHashCode\\(callSuper\\s*=\\s*true\\)"
    })
    // @formatter:on
    void anotaceTest(String file, String annotation) throws URISyntaxException {
        assertThat(TextFileContains.getProjectRoot(getClass()), new TextFileContains(file, annotation));
    }

    @Test
    void personToStringNoPasswordTest() {
        assertThat(Person.generate().toString(),Matchers.containsString("email"));
        assertThat(Person.generate().toString(),Matchers.containsString("firstName"));
        assertThat(Person.generate().toString(),Matchers.containsString("lastName"));
        assertThat(Person.generate().toString(), Matchers.not(Matchers.containsString("password")));
    }

    @Test
    void testCinemaTicketToString() {
        assertThat(CinemaTicket.generate().toString(), Matchers.containsString("name"));
    }

    @Test
    void testBoatTicketToString() {
        assertThat(BoatTicket.generate().toString(), Matchers.containsString("name"));
    }

    @Test
    void testCinemaTicketEquals() {
        assertThat(new CinemaTicket("a", 10, LocalDateTime.of(2025, 4, 1, 14, 15), "sal 1", 'C', 10),
            Matchers.equalTo(new CinemaTicket("a", 10, LocalDateTime.of(2025, 4, 1, 14, 15), "sal 1", 'C', 10)));

        assertThat(new CinemaTicket("b", 10, LocalDateTime.of(2025, 4, 1, 14, 15), "sal 1", 'C', 10), Matchers.not(
            Matchers.equalTo(new CinemaTicket("a", 10, LocalDateTime.of(2025, 4, 1, 14, 15), "sal 1", 'C', 10))));
    }

    @Test
    void testBoatTicketEquals() {
        assertThat(new BoatTicket("a", 10, LocalDateTime.of(2025, 4, 1, 14, 15), 10, true),
            Matchers.equalTo(new BoatTicket("a", 10, LocalDateTime.of(2025, 4, 1, 14, 15), 10, true)));

        assertThat(new BoatTicket("b", 10, LocalDateTime.of(2025, 4, 1, 14, 15), 10, true),
            Matchers.not(Matchers.equalTo(new BoatTicket("a", 10, LocalDateTime.of(2025, 4, 1, 14, 15), 10, true))));
    }
}
