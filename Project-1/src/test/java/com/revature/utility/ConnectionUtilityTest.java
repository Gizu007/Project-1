package com.revature.utility;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class ConnectionUtilityTest {
    @Test
    public void testConnectionToDatabase() throws SQLException {
        ConnectionUtility.getConnection();
    }
}