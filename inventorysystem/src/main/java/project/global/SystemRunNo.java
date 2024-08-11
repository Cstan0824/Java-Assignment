package project.global;

import java.util.ArrayList;

public class SystemRunNo {
    public static int Get(String _prefix) {
        SqlConnector connector = new SqlConnector();
        connector.Connect();

        if (!connector.isConnected()) {
            return -1;
        }

        String query = "SELECT Sys_Next FROM Sys_RunNo WHERE Sys_Prefix = ?";
        ArrayList<Integer> result = connector.PrepareExecuteRead(query, _prefix);

        if (result == null || result.isEmpty()) {
            return -1;
        }

        if(Update(_prefix) == false) {
            return -1;
        }

        return result.get(0);
    }

    private static boolean Update(String _prefix) {
        
        SqlConnector connector = new SqlConnector();

        connector.Connect();

        if (!connector.isConnected()) {
            return false;
        }

        String query = "UPDATE Sys_RunNo SET Sys_Next = Sys_Next + 1 WHERE Sys_Prefix = ?";

        boolean QueryExecuted = connector.PrepareExecuteDML(query, _prefix);

        connector.Disconnect();

        return QueryExecuted;
    }
}
