package example.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor @Getter @ToString @EqualsAndHashCode
public class UsersCategory {
    private String name;
    private int count;
    private double percentage;
    private String command;
}