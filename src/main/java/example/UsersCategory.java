package example;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A model class that represents a single user category, with its user count, % from total users, and bot command
 */
@AllArgsConstructor @Getter @ToString @EqualsAndHashCode
public class UsersCategory {
    private String name;
    private int count;
    private double percentage;
    private String command;
}