package com.qualtech.framework.demo.genricmethod.login;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadFile {

	
	public static void main(String[] args) throws IOException {
		FileInputStream fis=new FileInputStream("..\\TestData\\keywordTestData.xlsx");
	Workbook bookObj=new XSSFWorkbook(fis);
	Sheet sheetobj=bookObj.getSheetAt(0);
	int rowcnt=sheetobj.getLastRowNum();
	System.out.println(rowcnt);
	for(int i=0;i<=rowcnt-1;i++) {
		Row rowobj=sheetobj.getRow(i);
		int cellnum=rowobj.getLastCellNum();
		for(int j=0;j<=cellnum;j++) {
			Cell cellobj=rowobj.getCell(j,MissingCellPolicy.CREATE_NULL_AS_BLANK);
			String val=cellobj.getStringCellValue();
			System.out.println(val);
		}
	}
	
	}
	
	
	
}
