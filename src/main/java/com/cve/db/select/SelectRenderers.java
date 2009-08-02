package com.cve.db.select;

import com.cve.db.SQL;
import com.cve.db.Select;
import com.cve.db.dbio.DBDriver;

/**
 * For rendering a select statement a database-specific SQL.
 * @author curt
 */
final class SelectRenderers {


    static SQL render(Select select, DBDriver driver) {
        if (driver.equals(DBDriver.MySql)) {
            return MySQLSelectRenderer.of().render(select);
        }
        if (driver.equals(DBDriver.H2)) {
            return H2SelectRenderer.of().render(select);
        }
        if (driver.equals(DBDriver.MsSqlTds)) {
            return MsSqlSelectRenderer.of().render(select);
        }
        throw new IllegalArgumentException(driver.toString());
    }

}
