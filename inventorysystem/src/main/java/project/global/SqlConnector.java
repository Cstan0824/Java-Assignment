package project.global;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


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
        } catch (SQLException e) {
            System.out.println("Read Error: " + e.getMessage());
            return null;
        }
    }
    
    public <T> ArrayList<T> ExecuteRead(String _query) {
        if (!isConnected()) {
            return null;
        }

        try {
            ResultSet result = this.conn.createStatement().executeQuery(_query);
            if (result == null) {
                return null;
            }

            return this.<T>ToArrayList(result);
        } catch (SQLException e) {
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
            for (int i = 0; i < values.length; i++) {
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
    
    public <T> ArrayList<T> PrepareExecuteRead(String _query, Object... _values) {
        if (!isConnected()) {
            return null;
        }
        try {
            PreparedStatement statement = this.conn.prepareStatement(_query);
            if (_values.length != statement.getParameterMetaData().getParameterCount()) {
                return null;
            }
            for (int i = 0; i < _values.length; i++) {
                statement.setObject(i + 1, _values[i]);
            }
            ResultSet result = statement.executeQuery();
            if (result == null) {
                return null;
            }
            return this.<T>ToArrayList(result);
        } catch (SQLException e) {
            System.out.println("Prepare Read Error: " + e.getMessage());
            return null;
        }
    }
    
    public void PrepareDMLStackBatch(String _query, Object... _values) {
        if (!isConnected()) {
            return;
        }
        try {
            PreparedStatement statement = this.conn.prepareStatement(_query);
            if (_values.length != statement.getParameterMetaData().getParameterCount()) {
                return;
            }
            for (int i = 0; i < _values.length; i++) {
                statement.setObject(i + 1, _values[i]);
            }
            statement.addBatch();

        } catch (SQLException e) {
            System.out.println("Prepare DML Error: " + e.getMessage());
        }
    }
    
    public boolean ExecuteStackBatch()
    {
        if (!isConnected()) {
            return false;
        }
        try {
            this.conn.createStatement().executeBatch();
            return true;
        } catch (SQLException e) {
            System.out.println("DML Error: " + e.getMessage());
            return false;
        }
    }

    //convert the ResultSet to ArrayList
    //IDK WHAT 7 IS THIS BUT IT WORKS - 16/08/2024
    private <T> ArrayList<T> ToArrayList(ResultSet _result, Class<T> _typeClass) 
    {
        if (_result == null) {
            return null;
        }

        ArrayList<T> list = new ArrayList<>();
        ArrayList<Field> fields = new ArrayList<>(Arrays.asList(_typeClass.getDeclaredFields())); // Get declared fields from the current class
                fields.addAll(Arrays.asList(_typeClass.getSuperclass().getDeclaredFields()));
        try {
            while (_result.next()) {
                //Create a new instance of the object
                T instance = _typeClass.newInstance();

                

                for (Field field : fields) {
                    if (field.getType().isArray()) {
                        continue;
                    }

                    field.setAccessible(true); //set the data field accessible temp   
                    Object value = (!(field.getType().equals(Date.class) || field.getType().equals(String.class))
                            && Object.class.isAssignableFrom(field.getType()))
                                    ? _result.getObject(field.getName() + "_ID")
                                    : _result.getObject(field.getName());

                    if (field.getType() == double.class) {
                        value = ((BigDecimal) value).doubleValue();
                    } else if (field.getType() == int.class) {
                        value = ((Integer) value);
                    } else if (!(field.getType().equals(Date.class) || field.getType().equals(String.class))
                            && Object.class.isAssignableFrom(field.getType())) {
                        String tempID = field.getName() + "_ID";
                        tempID = tempID.substring(0, 1).toUpperCase() + tempID.substring(1);

                        Class<?> tempClass = field.getType();
                        Object tempInstance = tempClass.newInstance();

                        Field tempField = tempClass.getDeclaredField(tempID);
                        tempField.setAccessible(true);
                        tempField.set(tempInstance, value);
                        value = tempInstance;
                    }
                    //System.out.println(". " + field.getName() + " : " + value); //--print out the data, for debug use
                    field.set(instance, value);
                }
                list.add(instance);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException
                | SQLException | NoSuchFieldException e)

        {
            System.out.println("Convert Error: " + e.getMessage());
            System.out.println("Convert Error: " + e.getClass());
            return null;
        }

        return list;
    }

    public String[] GetColumnNames(String _tableName){

        if (!isConnected()) {
            return null;
        }

        try {
            DatabaseMetaData metaData = conn.getMetaData();
            String tableName = _tableName;

            ArrayList<String> columnNamesList = new ArrayList<>();
            
            try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    columnNamesList.add(columnName);
                }
            }
            
            // Convert ArrayList to Array
            String[] columnNamesArray = columnNamesList.toArray(new String[0]);
            return columnNamesArray;

        }catch (SQLException e) {
            System.out.println("Get Column Names Error: " + e.getMessage());
            return null;
        }
    }

    public String[] getDistinctDocNos(String prefix) {
        String query = "SELECT DISTINCT Doc_No FROM `transaction` WHERE Doc_No LIKE '" + prefix + "%' ORDER BY Transaction_Date DESC;";
        ArrayList<String> docNoList = new ArrayList<>();

        if (!isConnected()) {
            return null;
        }
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String docNo = rs.getString("Doc_No");
                docNoList.add(docNo);
            }

        } catch (SQLException e) {
            System.out.println("Get Distinct Doc No Error: " + e.getMessage());
            return null;
        }

        // Convert ArrayList to String Array
        return docNoList.toArray(new String[0]);
    }
    
    public String[] getDistinctDocNos(String prefix, String recipient) {
        String query = "SELECT DISTINCT Doc_No FROM `transaction` WHERE Doc_No LIKE '" + prefix + "%' AND Transaction_Recipient = '" + recipient + "' ORDER BY Transaction_Date DESC;";
        ArrayList<String> docNoList = new ArrayList<>();

        if (!isConnected()) {
            return null;
        }
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String docNo = rs.getString("Doc_No");
                docNoList.add(docNo);
            }

        } catch (SQLException e) {
            System.out.println("Get Distinct Doc No Error: " + e.getMessage());
            return null;
        }

        // Convert ArrayList to String Array
        return docNoList.toArray(new String[0]);
    }
    
    public String[] getDistinctPendingDocNos(String prefix1, String prefix2) {
        String query = "SELECT DISTINCT Doc_No FROM TRANSACTION WHERE DOC_NO LIKE '"+ prefix1 + "%' AND DOC_NO NOT IN (SELECT Source_Doc_No FROM Transaction WHERE DOC_NO LIKE '" + prefix2 + "%');";
        ArrayList<String> docNoList = new ArrayList<>();

        if (!isConnected()) {
            return null;
        }
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String docNo = rs.getString("Doc_No");
                docNoList.add(docNo);
            }

        } catch (SQLException e) {
            System.out.println("Get Distinct Doc No Error: " + e.getMessage());
            return null;
        }

        // Convert ArrayList to String Array
        return docNoList.toArray(new String[0]);
    }

    public String[] getDistinctPendingDODocNos() {
        String query = "SELECT DISTINCT Doc_No FROM TRANSACTION WHERE DOC_NO LIKE 'DO%' AND DOC_NO NOT IN (SELECT Doc_No FROM Schedule);";
        ArrayList<String> docNoList = new ArrayList<>();

        if (!isConnected()) {
            return null;
        }
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String docNo = rs.getString("Doc_No");
                docNoList.add(docNo);
            }

        } catch (SQLException e) {
            System.out.println("Get Distinct Doc No Error: " + e.getMessage());
            return null;
        }

        // Convert ArrayList to String Array
        return docNoList.toArray(new String[0]);
    }
    
    public String[] getDistinctPassedSODocNos() {
        String query = "SELECT DISTINCT Doc_No FROM TRANSACTION WHERE Doc_No LIKE 'SO%' AND Doc_No IN ( SELECT DISTINCT Source_Doc_No FROM TRANSACTION WHERE Doc_No LIKE 'DO%' AND Doc_No IN ( SELECT Doc_No FROM Schedule WHERE (Schedule_Date < CURDATE()) OR (Schedule_Date = CURDATE() AND Time_Slot < CURTIME()) ) );";
        ArrayList<String> docNoList = new ArrayList<>();

        if (!isConnected()) {
            return null;
        }
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String docNo = rs.getString("Doc_No");
                docNoList.add(docNo);
            }

        } catch (SQLException e) {
            System.out.println("Get Distinct Doc No Error: " + e.getMessage());
            return null;
        }

        // Convert ArrayList to String Array
        return docNoList.toArray(new String[0]);
    }
    
    @SuppressWarnings("unchecked")
    private <T> ArrayList<T> ToArrayList(ResultSet _result)
    {
        if (_result == null) {
            return null;
        }

        ArrayList<T> list = new ArrayList<>();
        try {
            while (_result.next()) {
                //Create a new instance of the object
                T value = (T) _result.getObject(1);
                list.add(value);
            }
        } catch (SQLException e) {
            System.out.println("Convert Error: " + e.getMessage());
            return null;
        }
        return list;
    }
}