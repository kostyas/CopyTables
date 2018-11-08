import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Main {

    public static void main(String[] args){

        Connection connection = null;
        Connection secondConnection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement insertPreparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;
        int BatchSizeInsert = 1000; //

        try {

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbl?useSSL=false","root", "0123");
            secondConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sdbl?useSSL=false", "root" ,"0123" );
            preparedStatement = connection.prepareStatement("SELECT * FROM dbl.status");
            insertPreparedStatement = connection.prepareStatement("INSERT INTO sdbl.status (id, statuscol) VALUES (?,?)");
            //preparedStatement.setInt(1, 3);
            // preparedStatement.setString(1, "OK");
            //preparedStatement.setString(2, "OK");
            resultSet = preparedStatement.executeQuery();

            if (connection!=null) {

                while (resultSet.next()) {
                    //String lastName = resultSet.getString("statuscol");
                    insertPreparedStatement.setInt(1, resultSet.getInt("id"));
                    insertPreparedStatement.setString(2, resultSet.getString("statuscol"));


                    insertPreparedStatement.addBatch();
                    count++;
                    if (count % BatchSizeInsert == 0) {
                        insertPreparedStatement.executeBatch();
                    }

                    if (count % BatchSizeInsert != 0) {
                        insertPreparedStatement.executeBatch();
                    }
                }
                connection.close();
                System.out.println("Complete");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
