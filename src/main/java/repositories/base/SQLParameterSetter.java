package repositories.base;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLParameterSetter {
    PreparedStatement setParams(PreparedStatement stmt) throws SQLException;
}
