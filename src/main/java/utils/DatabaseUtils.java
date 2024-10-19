package utils;

import exception.DatabaseConnectionException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;

import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
@UtilityClass
public class DatabaseUtils {

    //for using config.properties file
    //static ProjectConfig config = ConfigFactory.create(ProjectConfig.class, System.getProperties());

    private final String MYSQL_URL;
    private final String MYSQL_USERNAME;
    private final String MYSQL_PASSWORD;
    private final String POSTGRESQL_URL;
    private final String POSTGRESQL_USERNAME;
    private final String POSTGRESQL_PASSWORD;
    private final String H2_URL;
    private final String H2_USERNAME;
    private final String H2_PASSWORD;

    static {
        MYSQL_URL = PropertiesUtils.fetchProperty("application.datasource.mysql.url");
        MYSQL_USERNAME = PropertiesUtils.fetchProperty("application.datasource.mysql.username");
        MYSQL_PASSWORD = PropertiesUtils.fetchProperty("application.datasource.mysql.password");

        log.info("MySQL: {}", MYSQL_URL);

        POSTGRESQL_URL = PropertiesUtils.fetchProperty("application.datasource.postgresql.url");
        POSTGRESQL_USERNAME = PropertiesUtils.fetchProperty("application.datasource.postgresql.username");
        POSTGRESQL_PASSWORD = PropertiesUtils.fetchProperty("application.datasource.postgresql.password");

        log.info("Postgresql: {}", POSTGRESQL_URL);

        H2_URL = PropertiesUtils.fetchProperty("application.datasource.h2.url");
        H2_USERNAME = PropertiesUtils.fetchProperty("application.datasource.h2.username");
        H2_PASSWORD = PropertiesUtils.fetchProperty("application.datasource.h2.password");
    }

    //for using config.properties file
//    static {
//        MYSQL_URL = config.mysql_url();
//        MYSQL_USERNAME = config.mysql_username();
//        MYSQL_PASSWORD = config.mysql_password();
//    }

    public Connection getMySqlConnection(){
        Connection connection;
        try {
            connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
        }catch (Exception e){
            throw new DatabaseConnectionException("Failed to connect to MySql database at " + MYSQL_URL, e);
        }
        return connection;
    }

    public Connection getPostgresConnection(){
        Connection connection;
        try {
            connection = DriverManager.getConnection(POSTGRESQL_URL, POSTGRESQL_USERNAME, POSTGRESQL_PASSWORD);
        }catch (Exception e){
            throw new DatabaseConnectionException("Failed to connect to Postgres database at " + POSTGRESQL_URL, e);
        }
        return connection;
    }

    public Connection getH2Connection(){
        Connection connection;
        try {
            connection = DriverManager.getConnection(H2_URL, H2_USERNAME, H2_PASSWORD);
        }catch (Exception e){
            throw new DatabaseConnectionException("Failed to connect to h2 database at " + H2_URL, e);
        }
        return connection;
    }
}
