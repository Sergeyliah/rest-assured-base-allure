package repositories.base;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ToEntityMapper<T> {

    T map(ResultSet rs) throws SQLException;
}
