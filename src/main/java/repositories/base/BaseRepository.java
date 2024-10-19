package repositories.base;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public abstract class BaseRepository {
    protected Function<PreparedStatement, PreparedStatement> NO_PARAMS = stmt -> {
        return stmt;
    };

    public abstract Connection getConnection();

    public <T> List<T> executeQuery(String query, ToEntityMapper<T> mapper, SQLParameterSetter setter){
        final List<T> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setter.setParams(statement);
            try (ResultSet rs = statement.executeQuery()){
                while (rs.next()){
                    list.add(mapper.map(rs));
                }
                if(log.isTraceEnabled()){
                    log.trace("executeQuery: [{}] - size: [{}], Values: {}", query, list.size(), list);
                }
                return list;

            }
        } catch (SQLException e){
            log.error("Error executing query: {}", e.getMessage(), e);
            throw new RuntimeException("Error executing query", e);
        }
    }

    public <T> T executeSingleResultQuery(String query, ToEntityMapper<T> mapper, SQLParameterSetter setter){
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setter.setParams(statement);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapper.map(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e){
            log.error("Error executing query: {}", e.getMessage(), e);
            throw new RuntimeException("Error executing query", e);
        }
    }

    public int executeUpdate(String query, SQLParameterSetter setter){
        try (Connection connection = getConnection();
             final PreparedStatement statement = connection.prepareStatement(query)) {
            setter.setParams(statement);
            int updatedCount = statement.executeUpdate();
            log.debug("executeUpdate() -> OK. Updated: [{}] - Query: {}", updatedCount, query);
            return updatedCount;
        } catch (SQLException e){
            log.error("Error executing update query: {}", e.getMessage(), e);
            throw new RuntimeException("Error executing update query", e);
        }
    }

}
