import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Start_JAVANODECOPY extends MbJavaComputeNode {
	
	Connection connection = null;
    PreparedStatement preparedStatement = null;
    PreparedStatement insertPreparedStatement = null;
    ResultSet resultSet = null;
    int count = 0;
    int BatchSizeInsert = 1000; //
    
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {

            connection = DriverManager.getConnection("jdbc:oracle:thin:@//url","username", "password");
            preparedStatement = connection.prepareStatement("SELECT * FROM JDBC_TEST_ST");
            insertPreparedStatement = connection.prepareStatement("INSERT INTO JDBC_TEST_FT (ID,PACKETNUMBER,STATUS,TEXT) VALUES (?,?,?,?)");
            resultSet = preparedStatement.executeQuery();

            if (connection!=null) {

                while (resultSet.next()) {
                    insertPreparedStatement.setInt(1, resultSet.getInt("ID"));
                    insertPreparedStatement.setString(2, resultSet.getString("PACKETNUMBER"));
                    insertPreparedStatement.setString(3, resultSet.getString("STATUS"));
                    insertPreparedStatement.setString(4, resultSet.getString("TEXT"));

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
            }
			
			MbMessage outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);

		} catch (MbException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new MbUserException(this, "evaluate()", "", "", e.toString(),
					null);
		}
		out.propagate(outAssembly);
	}
}
