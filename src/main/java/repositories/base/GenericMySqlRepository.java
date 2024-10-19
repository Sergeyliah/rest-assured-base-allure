package repositories.base;

import utils.DatabaseUtils;

import java.sql.Connection;

public class GenericMySqlRepository extends BaseRepository{

    @Override
    public Connection getConnection(){
        return DatabaseUtils.getMySqlConnection();
    }
}
