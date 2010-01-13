package com.cve.sample.db;

import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import java.sql.SQLException;
import static com.cve.util.Check.notNull;

/**
 * A tiny sample database of countries, states and cities.
 * @author Curt
 */
public final class SampleDB {

    final SampleH2Server server;

    private static final Database GEO = Database.serverName(SampleH2Server.SAMPLE, "GEO");

    private SampleDB(SampleH2Server server) {
        this.server = notNull(server);
    }

    static SampleDB of(SampleH2Server server) {
        return new SampleDB(server);
    }
    
    void createAndLoadTables() {
        try {
            SampleH2Server.createSchema(GEO);
            loadCountries();
            loadStates();
            loadCities();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCountries() throws SQLException {
        DBTable table = GEO.tableName("countries");
        server.makeTable(table, "country_id INT, name VARCHAR(255), population INT")
            .add(1, "USA",   300000000)
            .add(2, "Canada",100000000)
        ;
    }

    private void loadStates() throws SQLException {
        DBTable table = GEO.tableName("states");
        server.makeTable(table,"state_id INT, name VARCHAR(255), country_id INT, population INT")
            .add(1, "Illinois",  1, 11000000)
            .add(2, "Missouri",  1,  6000000)
            .add(3, "Alabama",   1,  6000000)
            .add(4, "Alaska",    1,  6000000)
            .add(5, "Montana",   1,  6000000)
            .add(5, "Virginia",  1,  6000000)
        ;
    }

    private void loadCities() throws SQLException {
        DBTable table = GEO.tableName("cities");
        server.makeTable(table,"city_id INT, name VARCHAR(255),state_id INT,population INT")
            .add(1, "Chicago",     1, 3000000)
            .add(2, "Lincoln",     1,   20000)
            .add(3, "Virginia",    1,    1500)
            .add(3, "Mason City",  1,    1500)
            .add(3, "Springfield", 1,  100000)
        ;
        
    }

}
