package com.qualtech.keywordframework.core;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {

	
	public Workbook bookObj;

	public static void main(String[] args) throws IOException {
		new ReadExcel().getDataFromRowNum("C:\\Users\\qualtech\\Desktop\\keyworddriven\\QualtechKeywordDriven\\TestData\\keywordTestData.xlsx", "login");
	}

	public Workbook getWorkbook(String Filepath) throws IOException {
		FileInputStream fis=new FileInputStream(Filepath);
		String[] filesplit=Filepath.split("\\.");
		if(filesplit[1].equalsIgnoreCase("xlsx")) {
			bookObj=new XSSFWorkbook(fis);
		}else {
			bookObj=new HSSFWorkbook();
		}
		return bookObj;
	}
	public Sheet getSheet(String Filepath,String sheetName) throws IOException {
		Workbook book=getWorkbook(Filepath);
		Sheet sheetObj=book.getSheet(sheetName);
		return sheetObj;
	}
	
	public void getDataFromRowNum(String Filepath,String sheetName) throws IOException {
		
		Sheet sheetObj=getSheet(Filepath,sheetName);
		int rownum=sheetObj.getLastRowNum();
		System.out.println(rownum);
		
		   int lastRowNum=sheetObj.getLastRowNum();
		   for(int i=1;i<=lastRowNum-1;i++) {
			   Row getRowNum=sheetObj.getRow(i);
	      int lastCellNum=getRowNum.getLastCellNum();
		for(int j=0;j<lastCellNum;j=j+2) {
		Cell getCellObj=getRowNum.getCell(j,MissingCellPolicy.CREATE_NULL_AS_BLANK);
		String KeyName=getCellObj.getStringCellValue();
		
		System.out.println(KeyName);
		Cell cellobjval=getRowNum.getCell(j+1);
		//CellType cellTypeObj=getCellObj.getCellType();
		String KayValue=cellobjval.getStringCellValue();
		System.out.println(KayValue);
		}
	}
	}
	
	
	
	
}
