# CopyTables
it's worked сode
copying tables between two databases on java

if you want to copy from mysql to oracle or to another database, just chenge connection url

#in this part of the code you insert column, youmust prescribe which tables to insert

-- insertPreparedStatement.setInt(1, resultSet.getInt("id")); 
--------------------------------------------------------------------------
-- insertPreparedStatement.setString(2, resultSet.getString("statuscol"));
--------------------------------------------------------------------------
for testing project via esql, need create Queue name "INPUT" and "OUTPUT", then then deploy in integration server BAR file (IN JAVA NODE YOU NEED ADD YOURE URL, USER and PASS)
------
connection = DriverManager.getConnection("jdbc:oracle:thin:@//url","username", "password");------------
-------------------------------------------------------------------------------------------------------------
