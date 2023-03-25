package repository;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager dabaBaseManager_instance= new DatabaseManager();
    private Connection connection;



    public static DatabaseManager getInstance(){
        return dabaBaseManager_instance;
    }

    public void setupDBConnection(){
        String host="jdbc:mysql://localhost";
        String port="3306";
        String DBname="quiz_application";
        String username="root";
        String password="Dieuest1.";

        String connectionUrl = host +":"+port+'/'+ DBname ;
        try {
            connection= DriverManager.getConnection(connectionUrl,username,password);
        }catch (SQLException exception){
            exception.printStackTrace();
        }
    }
    public Connection getConnection() throws SQLException {
        if (this.connection == null || this.connection.isClosed()){
            this.setupDBConnection();
        }
        return this.connection;
    }

    public void closeConnections(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (connection != null) connection.close();
            if (preparedStatement !=null) preparedStatement.close();
            if (resultSet !=null) resultSet.close();
        }catch (Exception exception){
            System.out.println(exception.getClass() + ": " + exception.getMessage());
        }
    }
}
