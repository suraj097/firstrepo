package com.qualtech.keywordframework.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import bsh.ParseException;

public class Repayment {
	
	static HSSFWorkbook workbook;
	static int Interest=70000;
	 static double principalAmount;
	static int OpeningPrincipal=2500000;
	static int POS=280000;
	
	
	
	public static void main(String[] args) throws Exception {
		Repayment repaymentObj=new Repayment();
 		//repaymentSchedule
 		repaymentObj.repaymentScheduleForFlexiPay("3","300000","");
	}
	
	public static   String readProspectNo() throws Exception{
		String fileName = "..\\Ambit\\src\\main\\resources\\prospectNo";
		File file = new File(fileName);
		FileReader fr = new FileReader(file);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(fr);
		String prospectnum=br.readLine();
		return prospectnum;
	}


	public static  Workbook getDataBook(String filePath) throws IOException{
		Workbook workbook = null;
	      
		  FileInputStream fis=new FileInputStream(filePath);
		  String[] splitformet=	filePath.split("\\.");
				String fileExt = splitformet[1];
				if ("xlsx".equalsIgnoreCase(fileExt)) {
					workbook = new XSSFWorkbook(fis);
				
				} else {
					workbook = new HSSFWorkbook(fis);
					
				}
				return workbook;
			} 		
	
		

public void repaymentScheduleForFlexiPay( String Tenure, String totalLoanAmount,String prospectnum) throws Exception {
	
    
   //int tenure = 24;
   int k=0;
	String Interest="70000";
	
    
	
		FileInputStream fis = new FileInputStream("..\\QualtechKeywordDriven\\TestData\\RepaymentSheet\\Flexipay\\Repayment.xls");
		  workbook = new HSSFWorkbook(fis);
		 Sheet sheet =  workbook.getSheet("Repayment");
		 //String Tenure=dataUtil.getTestCaseDataMap().get("NewLoan_LoanTenure_ED");
		    //String totalLoanAmount=dataUtil.getTestCaseDataMap().get("NewLoan_LoanAmt_ED");
		     int totalLoanAmt = Integer.parseInt(totalLoanAmount);
			  int Tenurem = Integer.parseInt(Tenure);	
			  principalAmount = totalLoanAmt/Tenurem;
		 DeleteRow(sheet);
		 Cell cell = null ;
		 
		 for(int i=1;i<=Tenurem;i++) {
			
			 Row row = sheet.createRow(i);
			 for(int j=0;j<=0;j++) {
				  cell = row.createCell(j);
					 cell.setCellValue(prospectnum);
					 cell = row.createCell(j+1);
					 cell.setCellValue(k=k+1);
					 cell = row.createCell(j+2);
					String dateobj= addMonths("07-DEC-2034", i);
					 cell.setCellValue(dateobj);
					 cell = row.createCell(j+3);
					double totalRepayment= addPrincipalAndInterest();
					 cell.setCellValue(totalRepayment);
					 cell = row.createCell(j+4);
					 cell.setCellValue(principalAmount);
					 cell = row.createCell(j+5);
					 cell.setCellValue(Interest);
					 cell = row.createCell(j+6);
					 cell.setCellValue(OpeningPrincipal);
					 cell = row.createCell(j+7);
					 cell.setCellValue(POS);
					 
					 FileOutputStream fos = new FileOutputStream("..\\QualtechKeywordDriven\\TestData\\RepaymentSheet\\Flexipay\\Repayment.xls");
					 workbook.write(fos);
					
					
			 }
			
		 }
		
		 System.out.println("done");
		 
	
		
}
	
	
	
public void repaymentSchedule() throws Exception {
	String path = System.getProperty("user.dir")+"\\src\\test\\resources\\TestData\\RepaymentSheet\\Flexipay\\Repayment.xls";
   /* String Tenure=dataUtil.getTestCaseDataMap().get("NewLoan_LoanTenure_ED");
    String totalLoanAmount=dataUtil.getTestCaseDataMap().get("NewLoan_LoanAmt_ED");
     int totalLoanAmt = Integer.parseInt(totalLoanAmount);
	  int Tenurem = Integer.parseInt(Tenure);	
	  principalAmount = totalLoanAmt/Tenurem*/;
	 
	  String prospectNum  ="";
	String Interest="4000";
	
		//FileInputStream fis = new FileInputStream("..\\Ambit\\src\\test\\resources\\TestData\\RepaymentSheet\\Flexipay\\Repayment.xls");
		  //workbook = new HSSFWorkbook(fis);
	        Workbook workbook=getDataBook(path);
		 Sheet sheet =  workbook.getSheet("Repayment");
		 DeleteRow(sheet);
		// Cell cell = null ;
		 
		    Font font=workbook.createFont();
			CellStyle style=workbook.createCellStyle();
			
			Row row=sheet.createRow(10);
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("###,##0"));



			font.setBold(true);
			style.setFont(font);
			
			Cell cell=row.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("Prospect no.");
			sheet.autoSizeColumn(0);
			cell.setCellStyle(style);

			cell=row.createCell(1);
			cell.setCellValue("S.No.");
			sheet.autoSizeColumn(1);
			cell.setCellStyle(style);


			cell=row.createCell(2);
			cell.setCellValue("Due Date");
			sheet.autoSizeColumn(2);
			cell.setCellStyle(style);

			cell=row.createCell(3);
			cell.setCellValue("Installment");
			sheet.autoSizeColumn(3);
			cell.setCellStyle(style);

			cell=row.createCell(4);
			cell.setCellValue("Principal Component");
			sheet.autoSizeColumn(4);
			cell.setCellStyle(style);

			cell=row.createCell(5);
			cell.setCellValue("Interest Component");
			sheet.autoSizeColumn(5);
			cell.setCellStyle(style);

			cell=row.createCell(6);
			cell.setCellValue("Opening Principal");
			sheet.autoSizeColumn(6);
			cell.setCellStyle(style);

			cell=row.createCell(7);
			cell.setCellValue("Closing Principal");
			sheet.autoSizeColumn(7);
			cell.setCellStyle(style);
					
						
			/////////////////////////////////////////////////////////////////////     
			
			Row Rowobj=sheet.createRow(11);
			Cell cellobj= Rowobj.createCell(6);
			cellobj.setCellFormula("B1");
			
			cell.setCellStyle(cellStyle);
			cellobj= Rowobj.createCell(5);
			cellobj.setCellFormula("G12*$B$2*30/360");
			
			
			cell.setCellStyle(cellStyle);
			cellobj= Rowobj.createCell(3);
			cellobj.setCellFormula("$B$7");
			
			cellobj= Rowobj.createCell(4);
			cellobj.setCellFormula("D12-F12");
			
			
			
			cellobj= Rowobj.createCell(7);
			cellobj.setCellFormula("G12-E12");
			
			
			cell.setCellStyle(cellStyle);
			cellobj= Rowobj.createCell(0);
			cellobj.setCellFormula(prospectNum);
			
			
			///G12-E12
			//////////////////////////row 1/////////////////////

			for (int i=11;i<=34;i++) {
				//String date=new Repayment().addMonths("05/02/2019",1);

				Rowobj=sheet.createRow(i+1);
				
				DecimalFormat dFormat = new DecimalFormat("##.#");
				
				
				//cellobj= Rowobj.createCell(8);
				
				cellobj= Rowobj.createCell(6);
				cell.setCellStyle(cellStyle);
				cellobj.setCellFormula("H"+(i+1));
			
				cellobj= Rowobj.createCell(5);
				cell.setCellStyle(cellStyle);
				cellobj.setCellFormula("G"+(i+2)+"*$B$2*30/360");


				cellobj= Rowobj.createCell(3);
				cell.setCellStyle(cellStyle);
				cellobj.setCellFormula("$B$8");

				cellobj= Rowobj.createCell(4);
				cell.setCellStyle(cellStyle);
				cellobj.setCellFormula("D"+(i+2)+"-F"+(i+2));

				cellobj= Rowobj.createCell(7);
				cell.setCellStyle(cellStyle);
				cellobj.setCellFormula("G"+(i+2)+"-E"+(i+2));
				
			
				
				cell.setCellStyle(cellStyle);
				cellobj= Rowobj.createCell(0);
				cellobj.setCellFormula(prospectNum);

			}
			
			
			
			
			
			
			System.out.println("its done");
			
			 FileOutputStream fos = new FileOutputStream( System.getProperty("user.dir")+"\\src\\test\\resources\\TestData\\RepaymentSheet\\Flexipay\\Repayment.xls");
			 workbook.write(fos);
			/////////////////////////////////////////////////////////////////////  
		 
		 
		 
		 
		 
		/* 
		 for(int i=11;i<=24;i++) {
			 Row row = sheet.createRow(i);
			 for(int j=0;j<=0;j++) {
				  cell = row.createCell(j);
				  String prospectnum= readProspectNo() ;
					 cell.setCellValue(prospectnum);
					 cell = row.createCell(j+1);
					 
					 
					 
					 
					 
					 cell.setCellValue(i+"");
					 cell = row.createCell(j+2);
					String dateobj= addMonths("02-NOV-2021", i);
					 cell.setCellValue(dateobj);
					 cell = row.createCell(j+3);
					String totalRepayment= addPrincipalAndInterest();
					 cell.setCellValue(totalRepayment);
					 cell = row.createCell(j+4);
					 cell.setCellValue(principalAmount);
					 cell = row.createCell(j+5);
					 cell.setCellValue(Interest);
					 cell = row.createCell(j+6);
					 cell.setCellValue(OpeningPrincipal);
					 cell = row.createCell(j+7);
					 cell.setCellValue(POS);
					 
					
					
			 }
		 }*/
		
		
		 
	
		
}
	
	public  String  addMonths(String dateAsString, int nbMonths) throws ParseException, java.text.ParseException {
        String format = "dd-MMM-yyyy" ;
        SimpleDateFormat sdf = new SimpleDateFormat(format) ;
        Date dateAsObj = sdf.parse(dateAsString) ;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        cal.add(Calendar.MONTH, nbMonths);
        Date dateAsObjAfterAMonth = cal.getTime() ;
    System.out.println(sdf.format(dateAsObjAfterAMonth));
    return sdf.format(dateAsObjAfterAMonth) ;
}
	
public  double addPrincipalAndInterest() {
	
	double totalRepayment=Interest+principalAmount;
	//String str=totalRepayment+"";
	//12000000
	return totalRepayment;
}

public static void DeleteRow(Sheet  sheet)
{


    int lastRowNum = sheet.getLastRowNum();
for(int i=11;i<=lastRowNum;i++) {
	Row rowObj=sheet.getRow(i);
	sheet.removeRow(rowObj);
	
}
    
}
}
