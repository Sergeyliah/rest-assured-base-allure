package repositories;

import model.User;
import repositories.base.GenericMySqlRepository;

public class UserRepository extends GenericMySqlRepository {

    private static final String USER_BY_UID = "SELECT * FROM user WHERE uid = ?";

    public User getUserByUid(String uid) {
        return this.executeSingleResultQuery(USER_BY_UID, User::mapFrom
                , stmt -> {
                    stmt.setString(1, uid);
                    return stmt;
                });
    }
}
