package mapsql.sql.core;

import mapsql.shell.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Main interface for SQL Commands - these are special commands that
 * are not part of SQL, but which provide auxiliary functions necessary
 * for a database management system (e.g. loading a file, quitting, ...)
 * 
 * @author Rem Collier
 *
 */
public interface SQLCommand extends SQLOperation {
	public String execute(SQLManager manager) throws SQLException, IOException, ParseException;
}
