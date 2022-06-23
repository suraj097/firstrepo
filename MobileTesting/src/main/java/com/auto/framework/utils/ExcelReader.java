package com.auto.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utility to interact with excel files
 * 
 * @author naini.ghai
 * 
 */
public class ExcelReader
{

	private static final String COLUMN_NAME = "Column Name: ";
	private static final String ROW_AT_INDEX = "Row at Index: ";
	private static final String	NOT_FOUND	= ", not found";
	private XSSFSheet			xssfSheet;
	private HSSFSheet			hssfSheet;
	private List<String>		headerList;
	private XSSFWorkbook		workBook;
	private HSSFWorkbook		workBookXls;
	private boolean				bXlsx		= false;
	
	public ExcelReader()
	{

	}

	/**
	 * Creates a new ExcelReader. Loads the excel File given by the path "workBookPath" and then loads the
	 * workSheet of given name "workSheetName" Takes the relative path of workBook For example if the excel file is
	 * in resources folder of your code directory then the relative path will be "resources" + File.separator +
	 * "ExcelName"
	 * 
	 * @param String path of the workBook
	 * @param String Name of WorkSheet
	 * @throws MalformedFileException
	 * @throws Exception IOExceptions
	 */
	public ExcelReader(String workBookPath, String workSheetName) throws IOException
	{
		try (FileInputStream fileInputStream = new FileInputStream(workBookPath))
		{
			if (workBookPath.endsWith("xlsx"))
			{
				bXlsx = true;
				workBook = new XSSFWorkbook(fileInputStream);
				xssfSheet = workBook.getSheet(workSheetName);
				if (xssfSheet == null)
				{
					LogManager.getLogManager()
						.getLogger(this.getClass().getName() + " Worksheet: " + workSheetName + NOT_FOUND);
				}
			}
			else
			{
				workBookXls = new HSSFWorkbook(fileInputStream);
				hssfSheet = workBookXls.getSheet(workSheetName);
				if (hssfSheet == null)
				{
					LogManager.getLogManager()
						.getLogger(this.getClass().getName() + " Worksheet: " + workSheetName + NOT_FOUND);
				}
			}
		}
		this.headerList = getHeader();
	}

	/**
	 * Creates a new ExcelReader. Loads the excel File given by the path "workBookPath" and then loads the very
	 * first workSheet of workBook. Takes the relative path of workBook For example if the excel file is in
	 * resources folder of your code directory then the relative path will be "resources" + File.separator +
	 * "ExcelName"
	 * 
	 * @param String path of the workBook
	 * @throws MalformedFileException
	 * @throws Exception IOExceptions
	 */
	public ExcelReader(String workBookPath) throws IOException
	{
		readExcelSheet(workBookPath, 0);
	}

	/**
	 * Creates a new ExcelReader. Loads the excel File given by the path "workBookPath" and then loads the
	 * workSheet at index. Takes the relative path of workBook For example if the excel file is in resources folder
	 * of your code directory then the relative path will be "resources" + File.separator + "ExcelName"
	 * 
	 * @param String path of the workBook
	 * @param int Index of WorkSheet
	 * @throws MalformedFileException
	 * @throws Exception IOExceptions
	 */
	public ExcelReader(String workBookPath, int index) throws IOException
	{
		readExcelSheet(workBookPath, index);
	}

	private void readExcelSheet(String workBookPath, int index) throws IOException
	{
		FileInputStream fileInputStream = new FileInputStream(workBookPath);
		if (workBookPath.endsWith("xlsx"))
		{
			bXlsx = true;
			workBook = new XSSFWorkbook(fileInputStream);
			xssfSheet = workBook.getSheetAt(index);
		}
		else
		{
			workBookXls = new HSSFWorkbook(fileInputStream);
			hssfSheet = workBookXls.getSheetAt(index);
		}
		this.headerList = getHeader();
	}

	/**
	 * Returns the number of Rows in the workSheet
	 */
	public int getRowCount()
	{
		if (bXlsx)
		{
			return (xssfSheet.getLastRowNum() - xssfSheet.getFirstRowNum());
		}
		else
		{
			return (hssfSheet.getLastRowNum() - hssfSheet.getFirstRowNum());
		}
	}

	/**
	 * Returns the number of Columns in the workSheet
	 */

	public int getColumnCount()
	{
		int columnCount = 0;
		if (bXlsx)
		{
			if (xssfSheet.getRow(0) != null)
			{
				columnCount = xssfSheet.getRow(0).getLastCellNum() - xssfSheet.getRow(0).getFirstCellNum();
			}
		}
		else
		{
			if (hssfSheet.getRow(0) != null)
			{
				columnCount = hssfSheet.getRow(0).getLastCellNum() - hssfSheet.getRow(0).getFirstCellNum();
			}
		}
		return columnCount;
	}

	/**
	 * Returns the value at rowIndex and ColumnIndex in the workSheet
	 * 
	 * @param int index of the row starting with 1. First Row means index 1 .
	 * @param int Index of column starting with 1 . First Column means index 1.
	 * @return String value at rowIndex and columnIndex
	 */
	public String getValue(int rowIndex, int columnIndex) throws IOException
	{
		String strCellValue = "";
		if (bXlsx)
		{
			if (xssfSheet.getRow(rowIndex) != null && xssfSheet.getRow(rowIndex).getCell(columnIndex - 1) != null)
			{
				strCellValue = xssfSheet.getRow(rowIndex).getCell(columnIndex - 1).toString();
			}
		}
		else
		{
			if (hssfSheet.getRow(rowIndex) != null && hssfSheet.getRow(rowIndex).getCell(columnIndex - 1) != null)
			{
				strCellValue = hssfSheet.getRow(rowIndex).getCell(columnIndex - 1).toString();
			}
		}

		if (columnIndex > getColumnCount())
		{
			throw new IOException("Column Index: " + columnIndex + NOT_FOUND);
		}
		if (rowIndex > getRowCount())
		{
			throw new IOException("Row Index: " + rowIndex + NOT_FOUND);
		}
		return strCellValue.trim();
	}

	/**
	 * Returns the List of headers of the workSheet
	 * 
	 * @return List list of headers
	 * @throws MalformedFileException
	 */
	public List<String> getHeader() throws IOException
	{
		XSSFRow xssfRow = null;
		HSSFRow hssfRow = null;
		List<String> lstheaderList = new ArrayList<String>();
		if (bXlsx)
		{
			xssfRow = xssfSheet.getRow(0);
			getXSSFSheetHeaderList(xssfRow, lstheaderList);
		}
		else
		{
			hssfRow = hssfSheet.getRow(0);
			getHSSFSheetHeaderList(hssfRow, lstheaderList);
		}

		return lstheaderList;
	}

	private void getHSSFSheetHeaderList(HSSFRow hssfRow, List<String> lstheaderList) throws IOException
	{
		if (hssfRow != null)
		{
			Iterator<Cell> iterator = hssfRow.cellIterator();
			while (iterator.hasNext())
			{
				HSSFCell hssfCell = (HSSFCell) iterator.next();
				if (hssfCell != null)
				{
					if (lstheaderList.contains(hssfCell.toString()))
					{
						throw new IOException("Duplicate header name found: " + hssfCell.toString());
					}
					else
						lstheaderList.add(hssfCell.toString());
				}
				else
				{
					lstheaderList.add("");
				}
			}
		}
	}

	private void getXSSFSheetHeaderList(XSSFRow xssfRow, List<String> lstheaderList) throws IOException
	{
		if (xssfRow != null)
		{
			Iterator<Cell> iterator = xssfRow.cellIterator();
			while (iterator.hasNext())
			{
				XSSFCell xssfCell = (XSSFCell) iterator.next();
				if (xssfCell != null)
				{
					if (lstheaderList.contains(xssfCell.toString()))
					{
						throw new IOException("Duplicate header name found: " + xssfCell.toString());
					}
					else
						lstheaderList.add(xssfCell.toString());
				}
				else
				{
					lstheaderList.add("");
				}
			}
		}
	}

	/**
	 * Returns the value at rowIndex and ColumnName in the workSheet
	 * 
	 * @param int index of the row starting with 1. First Row means index 1 .
	 * @param String Name of the Column
	 * @return String value at rowIndex and columnName
	 */
	public String getValue(int rowIndex, String columnName) throws IOException
	{
		String returnValue = "";
		int columnIndex = -1;
		List<String> lstheaderList = getHeader();
		Iterator<String> iterator = lstheaderList.iterator();
		while (iterator.hasNext())
		{
			String value = iterator.next();
			if (value.equals(columnName))
			{
				columnIndex = lstheaderList.indexOf(columnName);
				break;
			}
		}
		if (rowIndex > getRowCount())
		{
			throw new IOException(ROW_AT_INDEX + rowIndex + NOT_FOUND);
		}

		if (columnIndex != -1)
		{
			if (bXlsx)
			{
				if (xssfSheet.getRow(rowIndex) != null && xssfSheet.getRow(rowIndex).getCell(columnIndex) != null)
					returnValue = xssfSheet.getRow(rowIndex).getCell(columnIndex).getStringCellValue();
			}
			else
			{
				if (hssfSheet.getRow(rowIndex) != null && hssfSheet.getRow(rowIndex).getCell(columnIndex) != null)
					returnValue = hssfSheet.getRow(rowIndex).getCell(columnIndex).getStringCellValue();
			}
		}
		else if (columnIndex == -1)
		{
			throw new IOException(COLUMN_NAME + columnName + NOT_FOUND);
		}
		return returnValue;
	}

	/**
	 * Returns a Row at index
	 * 
	 * @param int index of the row starting with 1. First Row means index 1 .
	 * @return a Map with key as column header and value as row value
	 */
	public Map<String, String> getRow(int index) throws IOException
	{
		Map<String, String> hashMap = new LinkedHashMap<String, String>();
		if (bXlsx)
		{
			XSSFRow xssfRow = xssfSheet.getRow(index);
			if (xssfRow != null)
			{
				for (int i = 0; i < getColumnCount(); i++)
				{
					XSSFCell xssfCell = xssfRow.getCell(i);
					hashMap.put(headerList.get(i), xssfCell.toString());
				}
			}
			else
				throw new IOException(ROW_AT_INDEX + index + NOT_FOUND);
		}
		else
		{
			HSSFRow hssfRow = hssfSheet.getRow(index);
			if (hssfRow != null)
			{
				for (int i = 0; i < getColumnCount(); i++)
				{
					HSSFCell hssfCell = hssfRow.getCell(i);
					hashMap.put(headerList.get(i), hssfCell.toString());
				}
			}
			else
				throw new IOException(ROW_AT_INDEX + index + NOT_FOUND);
		}
		return hashMap;
	}

	/**
	 * Returns values in a column.
	 * 
	 * @param String name of the column header .
	 * @return a list of column values
	 */

	public List<String> getValues(String columnName) throws IOException
	{
		List<String> list = new ArrayList<String>();
		int columnIndex = -1;
		int nIndex = 0;
		Iterator<String> iterator = headerList.iterator();
		while (iterator.hasNext())
		{
			String value = iterator.next();
			if (value.equals(columnName))
			{
				columnIndex = headerList.indexOf(columnName);
				break;
			}
		}
		
		if (bXlsx)
		{
			Iterator<Row> rowIterator = xssfSheet.rowIterator();

			while (rowIterator.hasNext())
			{
				XSSFRow xssfRow = (XSSFRow) rowIterator.next();
				if (columnIndex != -1)
				{
					XSSFCell columnValue = xssfRow.getCell(columnIndex);
					if (nIndex != 0)
						list.add(columnValue.toString());
					nIndex++;
				}
				else
				{
					throw new IOException(COLUMN_NAME + columnName + NOT_FOUND);
				}
			}
		}
		else
		{
			Iterator<Row> rowIterator = hssfSheet.rowIterator();

			while (rowIterator.hasNext())
			{
				HSSFRow hssfRow = (HSSFRow) rowIterator.next();
				if (columnIndex != -1)
				{
					HSSFCell columnValue = hssfRow.getCell(columnIndex);
					if (nIndex != 0)
						list.add(columnValue.toString());
					nIndex++;
				}
				else
				{
					throw new IOException(COLUMN_NAME + columnName + NOT_FOUND);
				}
			}
		}
		return list;

	}

	/**
	 * Returns all the data stored in excel sheet in Object[][]
	 * 
	 * @param strFilePath
	 * @param strSheetName
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public Object[][] getTableArray(String strFilePath, String strSheetName) throws IOException
	{
		String[][] tabArray = null;

		try (FileInputStream fileExcel = new FileInputStream(strFilePath))
		{
			int totalRows = 0;
			int totalCols = 0;
			// Access the required test data sheet
			if (strFilePath.endsWith(".xlsx"))
			{
				workBook = new XSSFWorkbook(fileExcel);
				xssfSheet = workBook.getSheet(strSheetName);
				totalRows = xssfSheet.getLastRowNum();
			}
			else
			{
				workBookXls = new HSSFWorkbook(fileExcel);
				hssfSheet = workBookXls.getSheet(strSheetName);
				totalRows = hssfSheet.getLastRowNum();
			}

			// you can write a function as well to get Column count
			totalCols = getColumnCount();
			int nExecutableRows = 0;
			nExecutableRows = getRunnableRows(1, totalRows, nExecutableRows);
			tabArray = new String[nExecutableRows][totalCols];
			tabArray = populateTableArray(tabArray, 1, 1, totalRows, totalCols);
			// - To uncomment when debugging
			/*
			 * for (int i = 0; i < nExecutableRows; i++) { for (int j = 0; j < totalCols; j++) {
			 * System.out.println(tabArray[i][j]); } }
			 */
		}
		return (tabArray);
	}

	private String[][] populateTableArray(String[][] tabArray, int startRow, int startCol, int totalRows,
		int totalCols) throws IOException
	{
		int ci;
		int cj;
		ci = 0;
		for (int i = startRow; i <= totalRows; i++)
		{
			cj = 0;
			String strTestRunFlag = getValue(i, "TEST_RUN");
			if (strTestRunFlag != null && strTestRunFlag.equalsIgnoreCase("Y"))
			{
				for (int j = startCol; j <= totalCols; j++, cj++)
				{
					tabArray[ci][cj] = (getValue(i, j) == null) ? "" : getValue(i, j);
				}
				ci++;
			}
		}
		return tabArray;
	}

	private int getRunnableRows(int startRow, int totalRows, int nExecutableRows) throws IOException
	{
		int ci = 0;
		for (int i = startRow; i <= totalRows; i++)
		{
			String strTestRunFlag = getValue(i, "TEST_RUN");
			if (strTestRunFlag != null && strTestRunFlag.equalsIgnoreCase("Y"))
			{
				nExecutableRows = ++ci;
			}
		}
		return nExecutableRows;
	}

	public void closeWorkBook() throws IOException
	{
		if (bXlsx && workBook != null)
		{
			workBook.close();
		}
		else if (workBookXls != null)
		{
			workBookXls.close();
		}
	}
}
