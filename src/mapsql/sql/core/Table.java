package mapsql.sql.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import mapsql.sql.condition.Equals;
import mapsql.util.LinkedList;
import mapsql.util.List;
import mapsql.util.Position;

public class Table {
    private TableDescription description;
    private List<Row> rows = new LinkedList<Row>();

    public Table(TableDescription description) {
        this.description = description;
    }

    public TableDescription description() {
        return description;
    }

    public List<Row> select(Condition where) throws SQLException {
        List<Row> list = new LinkedList<Row>();
        for (Row row : rows) {
            if (row.satisfies(where, description)) list.insertLast(row);
        }
        return list;
    }

    public void insert(String[] columns, String[] values) throws SQLException {
        Map<String, String> data = new HashMap<String, String>();

        for (int i = 0; i < columns.length; i++) {
            Field field = description.findField(columns[i]);
            if (field.isUnique()) {
                if (!select(new Equals(columns[i], values[i])).isEmpty()) {
                    throw new SQLException("Column '" + columns[i] + "' is UNIQUE - a row with '" + values[i] + "' already exists");
                }
            }
            data.put(columns[i], field.validate(values[i]));
        }

        Field[] fields = description.fields();
        for (int i = 0; i < fields.length; i++) {
            if (!data.containsKey(fields[i].name())) {
                String val = fields[i].defaultValue();

                if (fields[i].isUnique()) {
                    if (!select(new Equals(fields[i].name(), val)).isEmpty()) {
                        throw new SQLException("Column '" + fields[i].name() + "' is UNIQUE - a row with '" + val + "' already exists");
                    }
                }
                data.put(fields[i].name(), val);
            }
        }
        rows.insertLast(new Row(data));
    }

    public void update(String[] columns, String[] values, Condition where) throws SQLException {
        // Set current as the first node in rows linked list
        Position<Row> current = rows.first();

        while (current != null) {
            //set row as the current element
            Row row = current.element();

            // Remove the row if it satisfies the where clause
            if (row.satisfies(where, description)) {
                // Loop through the columns and replace the values
                for (int i = 0; i < columns.length; i++) {
                    row.data.put(columns[i], values[i]);
                }
            }
            // Set the current Node as the next node
            current = rows.next(current);
        }
    }

    public void delete(Condition where) throws SQLException {
        // Set current as the first node in rows linked list
        Position<Row> current = rows.first();

        while (current != null) {
            //set row as the current element
            Row row = current.element();

            // Remove the row if it satisfies the where clause
            if (row.satisfies(where, description)) {
                rows.remove(current);
            }
            current = rows.next(current);
        }
    }
}
