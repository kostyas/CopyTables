import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Main {

    public static void main(String[] args){

        Connection connection = null;
        //Connection secondConnection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement insertPreparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;
        int BatchSizeInsert = 1000; //

        try {

            connection = DriverManager.getConnection("jdbc:oracle:thin:@//10.255.201.59:1521/ESBLOG_F.oschadbank.ua","iiblog", "password");
           // secondConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sdbl?useSSL=false", "root" ,"0123" );
            preparedStatement = connection.prepareStatement("SELECT * FROM JDBC_TEST_ST");
            insertPreparedStatement = connection.prepareStatement("INSERT INTO JDBC_TEST_FT (ID,PACKETNUMBER,STATUS,TEXT) VALUES (?,?,?,?)");
            //preparedStatement.setInt(1, 3);
            // preparedStatement.setString(1, "OK");
            //preparedStatement.setString(2, "OK");
            resultSet = preparedStatement.executeQuery();

            if (connection!=null) {

                while (resultSet.next()) {
                    //String lastName = resultSet.getString("statuscol");
                    insertPreparedStatement.setInt(1, resultSet.getInt("ID"));
                    insertPreparedStatement.setString(2, resultSet.getString("PACKETNUMBER"));
                    insertPreparedStatement.setString(3, resultSet.getString("STATUS"));
                    insertPreparedStatement.setString(4, resultSet.getString("TEXT"));
                    System.out.println("Row complete");

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
                System.out.println("******************************");
                System.out.println("Complete");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
