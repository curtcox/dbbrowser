package com.cve.sample.db;

import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import java.sql.SQLException;

/**
 * A tiny sample database of countries, states and cities.
 * @author Curt
 */
public final class SampleGeoDB {

    final SampleH2Server h2;

    public static final Database GEO = Database.serverName(SampleH2Server.SAMPLE, "GEO");

    public static final DBTable STATES = GEO.tableName("states");
    public static final DBTable COUNTRIES = GEO.tableName("countries");
    public static final DBTable CITIES = GEO.tableName("cities");

    private SampleGeoDB(SampleH2Server h2) {
        this.h2 = h2;
    }

    static SampleGeoDB of(SampleH2Server h2) {
        return new SampleGeoDB(h2);
    }
    
    void createAndLoadTables() throws SQLException {
        h2.createSchema(GEO);
        loadCountries();
        loadStates();
        loadCities();
    }

    private void loadCountries() throws SQLException {
        h2.makeTable(COUNTRIES, "country_id INT, name VARCHAR(255), population INT")
            .add(1, "USA",   300000000)
            .add(2, "Canada",100000000)
        ;
    }

    private void loadStates() throws SQLException {
        h2.makeTable(STATES,"state_id INT, name VARCHAR(255), country_id INT, population INT")
            .add(1, "Illinois",  1, 11000000)
            .add(2, "Missouri",  1,  6000000)
            .add(3, "Alabama",   1,  6000000)
            .add(4, "Alaska",    1,  6000000)
            .add(5, "Montana",   1,  6000000)
            .add(5, "Virginia",  1,  6000000)
        ;
    }

    private void loadCities() throws SQLException {
        h2.makeTable(CITIES,"city_id INT, name VARCHAR(255),state_id INT,population INT")
            .add(1, "Chicago",     1, 3000000)
            .add(2, "Lincoln",     1,   20000)
            .add(3, "Virginia",    1,    1500)
            .add(4, "Mason City",  1,    1500)
            .add(5, "Springfield", 1,  100000)
        ;
        
    }

}
