package mapsql.sql.command;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import mapsql.shell.core.SQLVisitor;
import mapsql.shell.parser.MapSQL;
import mapsql.shell.parser.ParseException;
import mapsql.shell.parser.SimpleNode;
import mapsql.sql.core.*;
import mapsql.util.List;
import mapsql.util.Position;

import java.io.*;

import java.util.Scanner;

import static mapsql.Shell.readLines;


public class Sources implements SQLCommand {
    private String filename;

    public Sources(String filename) {
        this.filename = filename;
    }


    @Override
    public String execute(SQLManager manager) throws SQLException, IOException, ParseException {
        // Read the contents of the file
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            // Import read Lines static function from Shell to parse text
            readLines(manager, scanner);
        }
        return "Successfully parsed";
    }
}
