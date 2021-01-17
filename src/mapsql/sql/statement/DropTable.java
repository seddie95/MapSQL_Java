package mapsql.sql.statement;

import java.sql.SQLOutput;
import java.util.Map;

import mapsql.sql.condition.Equals;
import mapsql.sql.core.Condition;
import mapsql.sql.core.Row;
import mapsql.sql.core.SQLException;
import mapsql.sql.core.SQLResult;
import mapsql.sql.core.SQLStatement;
import mapsql.sql.core.Table;
import mapsql.sql.core.TableDescription;
import mapsql.util.List;

public class DropTable implements SQLStatement {
    private String name;

    public DropTable(String name) {
        this.name = name;

    }

    @Override
    public SQLResult execute(Map<String, Table> tables) throws SQLException {
        // Display SQLException if table is mapsql.tables
        if (name.equals("mapsql.tables")) {
            throw new SQLException("Table mapsql.tables cannot be modified!");
        }
        // If table exists remove it and its reference in mapsql.tables
        else if (tables.get(name) != null) {
            tables.remove(name);
            // Retrieve mapsql.tables and remove table name from column name
            Table mapsql = tables.get("mapsql.tables");
            Equals equals = new Equals("table", name);
            mapsql.delete(equals);

        } // Display SQLException if table does not exist
        else if (tables.get(name) == null) {
            throw new SQLException("Table " + name + " does not exist!");
        }

        return new SQLResult() {
            public String toString() {
                return "Table " + name + " is deleted";
            }


            @Override
            public TableDescription description() {
                return null;
            }

            @Override
            public List<Row> rows() {
                return null;
            }
        };

    }

}
