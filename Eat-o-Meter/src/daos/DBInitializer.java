package src.daos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {
    public static void initializerDatabase() {
        try (Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            BufferedReader reader = new BufferedReader(new FileReader("src/resources/EOM_schema.sql"))) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            statement.execute(sql.toString());
            System.out.println("Database initialized successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
