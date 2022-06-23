package com.auto.framework.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import com.auto.framework.base.BaseTestCase;
import com.auto.framework.base.TestProperties;
import com.auto.framework.utils.ExceptionUtil;
import com.auto.framework.utils.TestClassUtils;

/**
 * DBTestTools contains the utility methods to access the database(select,insert,update and delete)
 */
public class DBTestTool extends Tool implements ITool
{
	private static final String	RESULT_IS_AN_UPDATE_COUNT_OR_THERE_IS_NO_RESULT	= "Result is an update count or there is no result";
	private static final String	FAILED_IN_EXECUTING_DML_QUERY					= "Failed in executing DML query:: ";
	private TestProperties		prop;
	private Connection			connection;

	private DBTestTool()
	{
		// cannot be instantiated from outside
	}

	/**
	 * Get instance of the Database Test Tool
	 */
	public static DBTestTool getInstance(BaseTestCase baseTestCase)
	{
		DBTestTool ttDB = new DBTestTool();
		ttDB.prop = baseTestCase.getProperties();
		ttDB.connection = ttDB.getConnection();
		return ttDB;
	}

	/**
	 * Gets database connection based on connection properties
	 * @param strTestConfig Test Configuration containing details about database connection
	 * @return <code>Connection</code> object
	 */
	private Connection getConnection()
	{
		String dataBaseDriver = this.prop.getPropertyValue("DB_DRIVER");
		String dataBaseURL = this.prop.getPropertyValue("DB_URL");
		String userName = this.prop.getPropertyValue("DB_USER");
		String password = this.prop.getPropertyValue("DB_PASSWORD");
		return getConnection(dataBaseDriver, dataBaseURL, userName, password);
	}

	/**
	 * Get instance of the Database Test Tool
	 * @throws IOException
	 */
	public static DBTestTool getInstance(BaseTestCase baseTestCase, String strPropertyFile)
	{

		DBTestTool ttDB = new DBTestTool();
		File file = new File(strPropertyFile);
		try (FileInputStream fileInput = new FileInputStream(file))
		{
			Properties properties = new Properties();
			properties.load(fileInput);
			String strDatabaseDriver = getPropertyValue(baseTestCase, properties, "DB_DRIVER");
			String strDatbaseURL = getPropertyValue(baseTestCase, properties, "DB_URL");
			String strUserName = getPropertyValue(baseTestCase, properties, "DB_USER");
			String strPassword = getPropertyValue(baseTestCase, properties, "DB_PASSWORD");
			ttDB.connection = ttDB.getConnection(strDatabaseDriver, strDatbaseURL, strUserName, strPassword);
		}
		catch (FileNotFoundException e)
		{
			LogManager.getRootLogger().error("File Not Found " + ExceptionUtil.stackTraceToString(e));
		}
		catch (IOException e)
		{
			LogManager.getRootLogger().error("File Not Open " + ExceptionUtil.stackTraceToString(e));
		}
		return ttDB;
	}

	private static String getPropertyValue(BaseTestCase baseTestCase, Properties prop, String strPropertyName)
	{
		String strEnvConfig = baseTestCase.getEnvConfig();
		if (strEnvConfig != null && !strEnvConfig.equals(""))
		{
			strPropertyName = strPropertyName + "[" + strEnvConfig + "]";
		}
		return prop.getProperty(strPropertyName);
	}

	/**
	 * Get database connection based on passed argument
	 * @param strDatabaseDriver - Driver of database to load
	 * @param strDatbaseURL - URL of database
	 * @param strUserName - User name to login in database
	 * @param strPassword - Password of database to login of specified user
	 * @return <code>Connection</code> object
	 */
	private Connection getConnection(String strDatabaseDriver, String strDatbaseURL, String strUserName,
		String strPassword)
	{
		Connection con = null;
		try
		{
			Class.forName(strDatabaseDriver);
		}
		catch (ClassNotFoundException ex)
		{
			LogManager.getRootLogger().error("Failed to load the driver:" + ExceptionUtil.stackTraceToString(ex));
		}

		try
		{
			con = DriverManager.getConnection(strDatbaseURL, strUserName, strPassword);
		}
		catch (SQLException e)
		{
			LogManager.getRootLogger().error("Fail to open connection " + ExceptionUtil.stackTraceToString(e));
		}
		return con;
	}

	/**
	 * The method executes a SQL SELECT query and returns a list of records for same. It will throw exception when
	 * there is no result set or query returns update or delete count
	 * @param strStmt SELECT SQL query
	 * @return List of records with multiple columns
	 */
	public List<List<Object>> executeMultipleRowSqlWithResults(String strStmt)
	{
		TestClassUtils.assertNotEmpty(strStmt);
		List<List<Object>> lstRows = new ArrayList<List<Object>>();
		PreparedStatement stmt = null;

		try
		{
			stmt = connection.prepareStatement(strStmt);
			boolean bType = stmt.execute();
			if (!bType)
			{
				LogManager.getRootLogger().error(RESULT_IS_AN_UPDATE_COUNT_OR_THERE_IS_NO_RESULT);
			}

			try (ResultSet r = stmt.getResultSet())
			{
				int nColumns = r.getMetaData().getColumnCount();
				while (r.next())
				{
					List<Object> lstRow = new ArrayList<Object>();
					for (int nIndex = 1; nIndex <= nColumns; nIndex++)
					{
						lstRow.add(r.getObject(nIndex));
					}
					lstRows.add(lstRow);
				}
				stmt.close();
			}
		}
		catch (SQLException ex)
		{
			LogManager.getRootLogger().error(FAILED_IN_EXECUTING_DML_QUERY + ExceptionUtil.stackTraceToString(ex));
		}
		finally
		{
			closeStatement(stmt);
		}
		return lstRows;
	}

	/**
	 * The method executes a SQL SELECT query and is expected that the query returns single row of record. It will
	 * throw exception when there is no result set or more than one record or query returns update or delete count
	 * @param strStmt SELECT SQL query
	 * @return Single record with multiple columns
	 */
	public List<Object> executeSingleRowSqlWithResults(String strStmt)
	{
		TestClassUtils.assertNotEmpty(strStmt);
		List<Object> lstColumns = new ArrayList<Object>();
		PreparedStatement stmt = null;

		try
		{
			stmt = connection.prepareStatement(strStmt);
			boolean bType = stmt.execute();
			if (!bType)
			{
				LogManager.getRootLogger().error(RESULT_IS_AN_UPDATE_COUNT_OR_THERE_IS_NO_RESULT);
			}

			try (ResultSet r = stmt.getResultSet())
			{
				int nColumns = r.getMetaData().getColumnCount();
				if (!r.next())
				{
					LogManager.getRootLogger().error("Sql did not return a row");
				}

				for (int nIndex = 1; nIndex <= nColumns; nIndex++)
				{
					lstColumns.add(r.getObject(nIndex));
				}
				stmt.close();
			}
		}
		catch (SQLException ex)
		{
			LogManager.getRootLogger().error(FAILED_IN_EXECUTING_DML_QUERY + ExceptionUtil.stackTraceToString(ex));
		}
		finally
		{
			closeStatement(stmt);
		}
		return lstColumns;
	}

	/**
	 * The method executes a SQL SELECT query and is expected to return single row and column. It will throw
	 * exception when there is no result set or more than one record or record with multiple columns or query
	 * returns update or delete count
	 * @param strStmt SELECT SQL query
	 * @return Single record with single column
	 */
	public Object executeSingleValueSql(String strStmt)
	{
		TestClassUtils.assertNotEmpty(strStmt);
		Object objValue = "";
		PreparedStatement stmt = null;

		try
		{
			stmt = connection.prepareStatement(strStmt);
			boolean bType = stmt.execute();
			if (!bType)
			{
				LogManager.getRootLogger().error(RESULT_IS_AN_UPDATE_COUNT_OR_THERE_IS_NO_RESULT);
			}

			try (ResultSet r = stmt.getResultSet())
			{
				r.next();
				if (r.getMetaData().getColumnCount() != 1)
				{
					LogManager.getRootLogger()
						.error("Sql returned more than one column (" + r.getMetaData().getColumnCount() + ")");
				}
				objValue = r.getObject(1);

				boolean bMoreRows = r.next();
				if (bMoreRows)
				{
					LogManager.getRootLogger().error("Sql returned more than one row");
				}
				stmt.close();
			}
		}
		catch (SQLException ex)
		{
			LogManager.getRootLogger().error(FAILED_IN_EXECUTING_DML_QUERY + ExceptionUtil.stackTraceToString(ex));
		}
		finally
		{
			closeStatement(stmt);
		}
		return objValue;
	}

	/**
	 * The method executes a SQL SELECT COUNT query and is expected to count of records. It will throw exception
	 * when there is result set or query returns update or delete count
	 * @param strStmt SELECT SQL query
	 * @return Single record with single column
	 */
	public int executeCountSql(String strStmt)
	{
		TestClassUtils.assertNotEmpty(strStmt);
		int nVal = 0;
		PreparedStatement stmt = null;

		try
		{
			stmt = connection.prepareStatement(strStmt);
			boolean bType = stmt.execute();

			try (ResultSet rs = stmt.getResultSet())
			{
				if (!bType)
				{
					LogManager.getRootLogger().error("Result returned a result set and not a count");
				}
				if (rs.next())
				{
					nVal = rs.getInt(1);
				}
				stmt.close();
			}

		}
		catch (SQLException ex)
		{
			LogManager.getRootLogger().error(FAILED_IN_EXECUTING_DML_QUERY + ExceptionUtil.stackTraceToString(ex));
		}
		finally
		{
			closeStatement(stmt);
		}
		return nVal;
	}

	private void closeStatement(PreparedStatement stmt)
	{
		if (stmt != null)
		{
			try
			{
				stmt.close();
			}
			catch (SQLException e)
			{
				// do nothing
			}
		}
	}

	/**
	 * This method will run the <code>DML SQL query</code>(for update or delete row)
	 * @param strDmlQuery - DML Query to run
	 * @return Count of updated/deleted row
	 */
	public int executeSQL(String strDmlQuery)
	{
		int nRowCount = 0;
		try (Statement stmnt = connection.createStatement())
		{
			LogManager.getRootLogger().info("Executing: " + strDmlQuery);
			nRowCount = stmnt.executeUpdate(strDmlQuery);
			connection.commit();
		}
		catch (SQLException ex)
		{
			LogManager.getRootLogger()
				.error("Failed in executing DML query:" + ExceptionUtil.stackTraceToString(ex));
		}
		return nRowCount;
	}

	/**
	 * This method will run the all the <code>DML SQL query</code> added in the <code>List</code>(for update or
	 * delete row)
	 * @param lstDmlQuery - List of DML Queries to run
	 * @return Count of updated/deleted row
	 * @throws AutomationException
	 * @see {@link #executeSQL(Connection, String)}
	 */
	public int executeSQL(List<String> lstDmlQuery)
	{
		int nRowCount = 0;
		try
		{
			connection.setAutoCommit(false);
			for (String strQuery : lstDmlQuery)
			{
				try (Statement stmnt = connection.createStatement())
				{
					LogManager.getRootLogger().info("Executing: " + lstDmlQuery);
					int nResult = stmnt.executeUpdate(strQuery);
					nRowCount += nResult;
				}
			}
			connection.commit();
		}
		catch (SQLException ex)
		{
			LogManager.getRootLogger()
				.error("Failed in executing SQLquery:" + ExceptionUtil.stackTraceToString(ex));
			try
			{
				connection.rollback();
				nRowCount = 0;
			}
			catch (SQLException e)
			{
				LogManager.getRootLogger().error("Failed in rollback: " + ExceptionUtil.stackTraceToString(e));
			}
		}
		return nRowCount;
	}

	/**
	 * The method executes a SQL SELECT query and returns a list of records for same.
	 * @param strQuery - query to execute
	 * @return List of Map where in Map's key is the column name and value is the String version of the column's
	 *         data, each <code>Map</code> in <code>list</code> will define one row, while whole <code>list</code>
	 *         will describe whole table
	 * @see #getRecords(String, Properties)
	 */
	public List<Map<String, String>> executeSqlWithResults(String strQuery)
	{
		List<Map<String, String>> lstRecordList = new ArrayList<Map<String, String>>();

		try (Statement stmnt = connection.createStatement())
		{
			LogManager.getRootLogger().info("Fetching records: " + strQuery);
			try (ResultSet resultSet = stmnt.executeQuery(strQuery))
			{
				// If some records are expected
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				int nColumnCount = resultSetMetaData.getColumnCount();
				while (resultSet.next())
				{
					Map<String, String> mapRecord = new HashMap<String, String>();
					for (int i = 1; i <= nColumnCount; i++)
					{
						String strColumnName = resultSetMetaData.getColumnName(i);
						String strValue = resultSet.getString(strColumnName);
						mapRecord.put(strColumnName, strValue);
					}
					lstRecordList.add(mapRecord);
				}
			}
			catch (SQLException e)
			{
				LogManager.getRootLogger()
					.error("Failed in executing select Query: " + ExceptionUtil.stackTraceToString(e));
			}
		}
		catch (SQLException ex)
		{
			LogManager.getRootLogger()
				.error("Failed in executing select Query: " + ExceptionUtil.stackTraceToString(ex));
		}
		return lstRecordList;
	}

	/**
	 * Compare values in <code>expectedRecord</code> and <code>expectedSize</code> with list
	 * <code>dbComparisonResult</code>
	 * <ul>
	 * <br>
	 * Note:</br>
	 * One Map of List can be assumed as one row of table. <br>
	 * Whole List can be assumed as whole table.
	 * @param lstExpectedRecords - Fetched from recordSet which have all the records element and column element
	 *            name and their expected values.
	 * @param lstActualRecords - This will contains the result rows in list manner after the execution of SQL
	 *            Query.
	 * @return - true if validation successful false otherwise
	 */
	public boolean verifyRecords(List<Map<String, String>> lstExpectedRecords,
		List<Map<String, String>> lstActualRecords)
	{
		try
		{
			// This is record loop, Will iterate till number of records
			for (Map<String, String> mapExpectedRecordAsRow : lstExpectedRecords)
			{
				if (!matchRecords(lstActualRecords, mapExpectedRecordAsRow))
				{
					return false;
				}
			}
		}
		catch (Exception ex)
		{
			LogManager.getRootLogger().error(ExceptionUtil.stackTraceToString(ex));
		}
		return true;
	}

	private boolean matchRecords(List<Map<String, String>> lstActualRecords,
		Map<String, String> mapExpectedRecordAsRow)
	{
		/*
		 * Through this bool, will decide that, searching of column need to be done on "current Map" of iteration
		 * of List
		 * 
		 * or
		 * 
		 * from previously saved map as replica.
		 * 
		 */
		boolean bPreviousColumnMatched = false;
		boolean bResult = true;

		/*
		 * This is maintain the index of last ignored Map, while matching the column.
		 * 
		 * Here can be the case that matching the column values with replica map may fail at end. so that time need
		 * to restart the matching from first column but here need to know from which map index of List we have to
		 * start this, means will not start from zero index Map of List.
		 * 
		 * So this integer var will maintain the index from which we start ignoring the <code>rowMap</code> and
		 * start using the <code>replicaOfRowMap</code>
		 */
		int nlastIgnoreIndexOfMap = 0;
		Set<String> columnNames = mapExpectedRecordAsRow.keySet();

		// This is column loop
		for (int nColumnIndex = 0; nColumnIndex < columnNames.size(); nColumnIndex++)
		{
			String strExpectedColumnName = new ArrayList<String>(columnNames).get(nColumnIndex);
			String strExpectedColumnValue = mapExpectedRecordAsRow.get(strExpectedColumnName);
			int nMapIndex = nlastIgnoreIndexOfMap;
			// This is List loop
			for (; nMapIndex < lstActualRecords.size() && !bPreviousColumnMatched; nMapIndex++)
			{
				Map<String, String> mapActualRecordAsRow = lstActualRecords.get(nMapIndex);
				// Means column name not found
				if (!mapActualRecordAsRow.containsKey(strExpectedColumnName))
				{
					break;
				}

				if (mapActualRecordAsRow.get(strExpectedColumnName).equals(strExpectedColumnValue))
				{
					nlastIgnoreIndexOfMap = nMapIndex + 1;
					bPreviousColumnMatched = true;

					/**
					 * Exit from loop, we got one Map which have the expected value for Column, So further check
					 * will be done on that Map till failure Or column ends
					 */
					break;
				}
			}

			// Means column not found, have searched in all Maps
			if (!bPreviousColumnMatched && nMapIndex == lstActualRecords.size())
			{
				bResult = false;
				// Exit from column loop, because no need to match for next column
				break;
			}

			// At first match in above loop, this will do again same matching
			if (bPreviousColumnMatched)
			{
				Map<String, String> mapMathedActualRow = lstActualRecords.get(nlastIgnoreIndexOfMap - 1);
				// Means column name not found
				if (!mapMathedActualRow.containsKey(strExpectedColumnName))
				{
					break;
				}
				if (!mapMathedActualRow.get(strExpectedColumnName).equals(strExpectedColumnValue))
				{
					/**
					 * ohh.. we need to start matching from first column again from lastIgnoreIndexOfMap of List.
					 * :(
					 * 
					 * Please exit from List loop as well as column loop and start column loop from fresh and List
					 * loop from <code>lastIgnoreIndexOfMap</code>.
					 */
					// This will again start the loop
					bPreviousColumnMatched = false;
				}
			}
		}
		return bResult;
	}

	/**
	 * This method will verify the expected records with, actual records after execution of query
	 * @param strQuery - select query, this will be run first.
	 * @param lstExpectedRecords - List of Map<String, String>, which will be matched with actual results,
	 *            <p>
	 *            <ul>
	 *            <li>In this List each <code>Map<String, String></code> will define a row
	 *            <li>In the <code>Map</code> each <code>Key</code> of Map will define the column name and value of
	 *            that key will define column's actual value.
	 *            </ul>
	 * @return true if matched each row of expected as whole with Actual Records
	 * @throws SQLException
	 * @see #verifyRecords(List, List)
	 * @see #executeSqlWithResults(String)
	 */
	public boolean verifyRecords(String strQuery, List<Map<String, String>> lstExpectedRecords) throws SQLException
	{
		List<Map<String, String>> actualRecords = executeSqlWithResults(strQuery);
		return verifyRecords(lstExpectedRecords, actualRecords);
	}

	public void closeConnection()
	{
		try
		{
			if (connection != null && !connection.isClosed())
			{
				connection.close();
				connection = null;
			}
		}
		catch (Exception ex)
		{
			LogManager.getRootLogger()
				.error("Issue when closing connection: " + ExceptionUtil.stackTraceToString(ex));
		}
	}
}
