package com.cve.sample.db;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class SampleServerTest {

    @Test
    public void of() throws SQLException, IOException {
        SampleH2Server.of();
    }
}
