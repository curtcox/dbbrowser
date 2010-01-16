package com.cve.sample.db;

import java.sql.SQLException;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class SampleServerTest {

    @Test
    public void of() throws SQLException {
        SampleH2Server.of();
    }
}
