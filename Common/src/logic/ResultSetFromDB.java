package logic;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetFromDB {
    private ResultSet resultSet;
    private boolean isEmpty;
    private int rowCount;
    private int columnCount;

    public ResultSetFromDB(ResultSet resultSet) {
        this.resultSet = resultSet;
        processResultSet();
    }

    private void processResultSet() {
        try {
            if (resultSet != null) {
                // Get the number of columns
                ResultSetMetaData metaData = resultSet.getMetaData();
                columnCount = metaData.getColumnCount();

                // Get the number of records
                rowCount = 0;
                while (resultSet.next()) {
                    rowCount++;
                }
                
                // Reset the cursor to the beginning
                resultSet.beforeFirst();
                
                isEmpty = rowCount == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }
    
    public List<Object> getRecordAt(int rowIndex) {
        List<Object> record = new ArrayList<>();
        try {
            if (resultSet.absolute(rowIndex)) {
                for (int i = 1; i <= columnCount; i++) {
                    record.add(resultSet.getObject(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return record;
    }
}
