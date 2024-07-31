package project.global;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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
            System.out.println("Connection Error: " + e.getMessage());
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
            System.out.println("Disconnection Error: " + e.getMessage());
            return false;
        }
    }

    public boolean isConnected() {
        try {
            return this.conn != null && !this.conn.isClosed();
        } catch (SQLException e) {
            System.out.println("Check Connection Error: " + e.getMessage());
            return false;
        }
    }

    public <T> ArrayList<T> ExecuteRead(String _query, Class<T> _typeClass) {
        if (!isConnected()) {
            return null;
        }

        try {
            ResultSet result = this.conn.createStatement().executeQuery(_query); 
            if (result == null) {
                return null;
            }

            return this.<T>ToArrayList(result, _typeClass);
        } catch (SQLException e) 
        {
            System.out.println("Read Error: " + e.getMessage());
            return null;
        }
    }

    //for update,create,delete
    public boolean ExecuteDML(String _query) {
        if (!isConnected()) {
            return false;
        }
        try {
            this.conn.createStatement().executeUpdate(_query);
            return true;
        } catch (SQLException e) {
            System.out.println("DML Error: " + e.getMessage());
            return false;
        }

    }

    public boolean PrepareExecuteDML(String _query, Object... values) 
    {
        if (!isConnected()) {
            return false;
        }
        try {
            PreparedStatement statement = this.conn.prepareStatement(_query);
            if (values.length != statement.getParameterMetaData().getParameterCount()) {
                return false;
            }
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Prepare DML Error: " + e.getMessage());
            return false;
        }
    }
    
    public <T> ArrayList<T> PrepareExecuteRead(String _query, Class<T> _typeClass, Object... values) 
    {
        if (!isConnected()) {
            return null;
        }
        try {
            PreparedStatement statement = this.conn.prepareStatement(_query);
            if (values.length != statement.getParameterMetaData().getParameterCount()) {
                return null;
            }
            for (int i = 0; i < values.length; i++) 
            {
                statement.setObject(i + 1, values[i]);
            }
            ResultSet result = statement.executeQuery();
            if (result == null) {
                return null;
            }
            return this.<T>ToArrayList(result, _typeClass);
        } catch (SQLException e) {
            System.out.println("Prepare Read Error: " + e.getMessage());
            return null;
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

                    field.setAccessible(true); //set the data field accessible temp
                    Object value = _result.getObject(field.getName());
                    if (field.getType() == double.class && value instanceof BigDecimal) {
                        value = ((BigDecimal) value).doubleValue();
                    } else if (field.getType() == int.class && value instanceof Integer) {
                        value = ((Integer) value);
                    }
                    //System.out.println(field.getName() + " : " + value); --print out the data, for debug use
                    field.set(instance, value);
                }
                list.add(instance);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException
                | SQLException e) {
            System.out.println("Convert Error: " + e.getMessage());
            return null;
        }

        return list;
    }
}