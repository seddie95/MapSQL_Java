package mapsql.sql.core;

import javax.print.DocFlavor;
import java.util.Arrays;

public class TableDescription {
    private String name;
    private Field[] fields;
    private String col;

    public TableDescription(String name, Field[] fields) {
        this.name = name;
        this.fields = fields;
    }

    public String name() {
        return name;
    }

    public Field findField(String name) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].name().equals(name)) return fields[i];
        }
        return null;
    }

    /**
     * This method resolves an array of columns into the actual column headings to
     * be returned (i.e. * is resolved to all the column names). If an invalid
     * column name is given, then this method will throw an SQLException.
     *
     * @param columns
     * @return
     * @throws SQLException
     */
    public String[] resolveColumns(String[] columns) throws SQLException {
        String[] cols;
        if (columns.length == 1 && columns[0].equals("*")) {
            cols = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                cols[i] = fields[i].name();
            }
        } else {
            cols = new String[columns.length];
            for (int i = 0; i < columns.length; i++) {
                Field field = findField(columns[i]);
                if (field == null)
                    throw new SQLException("Unknown field '" + columns[i] + "' in table: '" + name + "'");
                cols[i] = columns[i];
            }
        }
        return cols;
    }

    public Field[] fields() {
        return fields;
    }

    /**
     * Checks that no columns marked "not null" have been missed.
     *
     * @param cols
     * @throws SQLException
     */
    public void checkForNotNulls(String[] cols) throws SQLException {
        StringBuilder sb = new StringBuilder();
        int notNullCount = 0;

        //Loop through the fields
        for (int i = 0; i < fields().length; i++) {
            Field field = fields()[i];
            boolean notNull = false;

            //loop through the columns
            for (String col : cols) {
                // set notNull to be true if field is NOT NULL &  field name is column name
                if (field.isNotNull() && field.name().equals(col)) {
                    notNull = true;
                }
            }
            // If notnull is  false and field is NOT NULL append the name of  the Not Null column
            if (!notNull && field.isNotNull()) {
                if (notNullCount > 0) {
                    sb.append(", ").append(field.name());
                } else {
                    sb.append(field.name());
                    notNullCount++;
                }
            }

        }
        // Throw SQLException if missing NOT NULL column(s)
        if (notNullCount > 1) {
            throw new SQLException("Missing Value for NOT Null fields: " + sb.toString());
        } else if (notNullCount == 1) {
            throw new SQLException("Missing Value for NOT Null field: " + sb.toString());
        }
    }

}
