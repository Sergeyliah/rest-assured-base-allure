package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data  // Generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor  // Generates a no-argument constructor
@AllArgsConstructor  // Generates an all-arguments constructor
public class User {
    private String username;
    private String email;
}
