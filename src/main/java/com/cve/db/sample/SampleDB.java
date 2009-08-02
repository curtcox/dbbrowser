package com.cve.db.sample;

import com.cve.db.DBTable;
import com.cve.db.Database;
import java.sql.SQLException;

/**
 * A tiny sample database of countires, states and cities.
 * @author Curt
 */
public final class SampleDB {

    private static final Database GEO = Database.serverName(SampleServer.SAMPLE, "GEO");

    /**
     * The static initializer does all the work -- once.
     */
    public static void load() {}

    static {
        loadServer();
    }

    static void loadServer() {
        try {
            SampleServer.createSchema(GEO);
            loadCountries();
            loadStates();
            loadCities();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void loadCountries() throws SQLException {
        DBTable table = GEO.tableName("countries");
        SampleServer.makeTable(table, "country_id INT, name VARCHAR(255), population INT")
            .add(1, "USA",   300000000)
            .add(2, "Canada",100000000)
        ;
    }

    static void loadStates() throws SQLException {
        DBTable table = GEO.tableName("states");
        SampleServer.makeTable(table,"state_id INT, name VARCHAR(255), country_id INT, population INT")
            .add(1, "Illinois",  1, 11000000)
            .add(2, "Missouri",  1,  6000000)
            .add(3, "Alabama",   1,  6000000)
            .add(4, "Alaska",    1,  6000000)
            .add(5, "Montana",   1,  6000000)
            .add(5, "Virginia",  1,  6000000)
        ;
    }

    static void loadCities() throws SQLException {
        DBTable table = GEO.tableName("cities");
        SampleServer.makeTable(table,"city_id INT, name VARCHAR(255),state_id INT,population INT")
            .add(1, "Chicago",     1, 3000000)
            .add(2, "Lincoln",     1,   20000)
            .add(3, "Virginia",    1,    1500)
            .add(3, "Mason City",  1,    1500)
            .add(3, "Springfield", 1,  100000)
        ;
        
    }

}
