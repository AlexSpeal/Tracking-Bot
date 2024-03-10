package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class MigrationTest extends IntegrationTest {
    @Test
    public void chatTest() throws SQLException {
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement sqlQuery = connection.prepareStatement("SELECT * FROM chat");
            String result = sqlQuery.executeQuery().getMetaData().getColumnName(1);
            assertThat(result).isEqualTo("chat_id");
        }
    }

    @Test
    public void linkTableTest() throws SQLException {
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement sqlQuery = connection.prepareStatement("SELECT * FROM link");
            String firstColumn = sqlQuery.executeQuery().getMetaData().getColumnName(1);
            String secondColumn = sqlQuery.executeQuery().getMetaData().getColumnName(2);
            String thirdColumn = sqlQuery.executeQuery().getMetaData().getColumnName(3);
            assertThat(firstColumn).isEqualTo("link_id");
            assertThat(secondColumn).isEqualTo("url");
            assertThat(thirdColumn).isEqualTo("updated_at");
        }
    }

    @Test
    public void connectionTableTest() throws SQLException {
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement sqlQuery = connection.prepareStatement("SELECT * FROM chat_link");
            String firstColumn = sqlQuery.executeQuery().getMetaData().getColumnName(1);
            String secondColumn = sqlQuery.executeQuery().getMetaData().getColumnName(2);
            assertThat(firstColumn).isEqualTo("chat_id");
            assertThat(secondColumn).isEqualTo("link_id");
        }
    }
}
