package main.asw.repository;

/**
 * Created by MIGUEL on 16/02/2017.
 */
public class PersistenceFactory {

    public static UserDao getUserDAO(){
        return new UserDaoImpl();
    }
}
