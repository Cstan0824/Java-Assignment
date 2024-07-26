package project.global;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlConnector {
    //Data field
    private final String URL = "jdbc:mysql://localhost:3306/furniture_db";
    private final String USER = "root";
    private final String PASSWORD = "";
    private Connection conn = null;

    //Getter & Setter methods
    public String getUrl() {
        return this.URL;
    }

    public String getUser() {
        return this.USER;
    }

    //Method
    public void Connect() {
        try {
            // Establish the connection
            this.conn = DriverManager.getConnection(this.URL, this.USER, this.PASSWORD);

        } catch (SQLException e) {

        }

    }

    public boolean Disconnect() {
        try {
            if (isConnected()) {
                this.conn.close();
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isConnected() {
        try {
            return this.conn != null && !this.conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public <T> ArrayList<T> ExecuteRead(String _query, Class<T> _typeClass) {
        if (!isConnected()) {
            return null;
        }

        try {
            ResultSet result = this.conn.createStatement().executeQuery(_query); //pending to change to prepare statement
            if (result == null) {
                return null;
            }

            return this.<T>ToArrayList(result, _typeClass);
        } catch (SQLException e) {
            return null;
        }
    }

    //for update,create,delete
    public boolean ExecuteDML(String _query) {
        if (!isConnected()) {
            return false;
        }
        try {
            PreparedStatement statement = conn.prepareStatement(_query);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    //convert the ResultSet to ArrayList
    private <T> ArrayList<T> ToArrayList(ResultSet _result,Class<T> _typeClass)
    {
        if (_result == null) {
            return null;
        }

        ArrayList<T> list = new ArrayList<>();

        try {
            while (_result.next()) {
                //Create a new instance of the object
                T instance = _typeClass.newInstance();

                for (Field field : _typeClass.getDeclaredFields()) {
                    Object value = _result.getObject(field.getName());
                    field.set(instance, value);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            return null;
        }

        return list;
    }
}