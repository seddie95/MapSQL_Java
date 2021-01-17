package mapsql.sql.condition;

import java.util.Map;

import com.sun.jndi.ldap.LdapCtx;
import mapsql.shell.parser.TokenMgrError;

import mapsql.sql.core.SQLException;
import mapsql.sql.core.TableDescription;


public class Like extends AbstractCondition {
    private String column;
    private String value;

    public Like(String column, String value) {
        this.column = column;
        this.value = value;
    }

    @SuppressWarnings("checked")
    @Override
    public boolean evaluate(TableDescription description, Map<String, String> data) throws SQLException {
        try {
            // Instantiate StringBuilder to store parsed value
            StringBuilder valueString = new StringBuilder(value.toLowerCase());
            int last = value.length() - 1;
            String dataLower = data.get(column).toLowerCase();

            // Check if valid input
            if (value.length() <= 1 || !value.contains("%")) {
                throw new SQLException("Incorrect or no value for Like clause!");
            }

            // Check if '%' appears before, after or both
            if (value.charAt(0) == '%' && value.charAt(last) != '%') {
                return dataLower.endsWith(valueString.deleteCharAt(0).toString());
            } else if (value.charAt(0) != '%' && value.charAt(last) == '%') {
                return dataLower.startsWith(valueString.deleteCharAt(last).toString());
            } else if (value.charAt(0) == '%' && value.charAt(last) == '%') {
                valueString.deleteCharAt(0);
                valueString.deleteCharAt(last - 1);
                return dataLower.contains(valueString.toString());
            }

        } catch (TokenMgrError e) {
            return false;
        }

        return false;
    }
}
