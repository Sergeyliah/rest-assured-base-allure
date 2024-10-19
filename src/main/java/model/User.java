package model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data  // Generates getters, setters, toString, equals, and hashCode methods
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Integer uid;
    String username;
    String email;
    Boolean userActive;
    Date modifyDate;

    public static User mapFrom(ResultSet rs) throws SQLException{
        final User user = new User();
        user.setUid(rs.getInt("uid"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setUserActive(rs.getBoolean("userActive"));
        user.setModifyDate(new Date(rs.getTimestamp("modifyDate").getTime()));

        return user;
    }
}
