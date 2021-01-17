package mapsql;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import mapsql.shell.core.SQLVisitor;
import mapsql.shell.parser.MapSQL;
import mapsql.shell.parser.ParseException;
import mapsql.shell.parser.SimpleNode;
import mapsql.sql.core.SQLException;
import mapsql.sql.core.SQLManager;
import mapsql.sql.core.SQLOperation;
import mapsql.util.List;

public class Shell {
    @SuppressWarnings({"unchecked", "resource"})
    public static void main(String[] args) {
        SQLManager manager = new SQLManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("mapsql>");
            readLines(manager, scanner);
        }
    }

    public static void readLines(SQLManager manager, Scanner scanner) {
        // Parse the contents of the scanner
        String sql = scanner.nextLine();

        try {
            SimpleNode n = new MapSQL(new ByteArrayInputStream(sql.getBytes())).Start();

            List<SQLOperation> operations = (List<SQLOperation>) n.jjtAccept(new SQLVisitor(), null);
            for (SQLOperation operation : operations) {
                System.out.println(manager.execute(operation));
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
