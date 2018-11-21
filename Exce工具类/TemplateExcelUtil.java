package com.leimingtech.core.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * excel工具类 
 */
public class TemplateExcelUtil {
		
 
    private Workbook wb ;
    private POIFSFileSystem fs = null;
    
	public TemplateExcelUtil(){
		 wb = new HSSFWorkbook();
	}

	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";
	
	public void openModal(InputStream in){
	     try {
	            fs = new POIFSFileSystem(in);
	            wb = new HSSFWorkbook(fs);
	        } catch (IOException e) {
	            e.printStackTrace( );
	        }
	}
	
	
    public void openModal(String modlePath)  {
		File file =new File(modlePath);
		String fileName=file.getName();
		String prefix=fileName.substring(fileName.lastIndexOf(".")+1);  //后缀名
		InputStream in = FileUtil.getResourceAsStream(modlePath);
        try {
			if(OFFICE_EXCEL_2003_POSTFIX.equals(prefix)){
				 wb = new HSSFWorkbook(in);
			}else if(OFFICE_EXCEL_2010_POSTFIX.equals(prefix)){
				 wb = new XSSFWorkbook(in);
			}
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

	public Workbook openModals(String modlePath)  {
		File file =new File(modlePath);
		String fileName=file.getName();
		String prefix=fileName.substring(fileName.lastIndexOf(".")+1);  //后缀名
		InputStream in = FileUtil.getResourceAsStream(modlePath);
		try {
			if(OFFICE_EXCEL_2003_POSTFIX.equals(prefix)){
				wb = new HSSFWorkbook(in);
			}else if(OFFICE_EXCEL_2010_POSTFIX.equals(prefix)){
				wb = new XSSFWorkbook(in);
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return  wb;
	}
    
    public void writeToFile(String targetFile) {
    	FileOutputStream fileOut = null; 
        try {
            fileOut = new FileOutputStream(targetFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            wb.write(fileOut);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            wb = null;
            try {
                fileOut.flush();
                fileOut.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
    
     
    
    public void setCellMoney(int numRow, int numCol){
    	try{
    		if (wb.getSheetAt(0)!=null){
    			Sheet aSheet = wb.getSheetAt(0);
    			DataFormat format = wb.createDataFormat();
    	        Row row =aSheet.getRow((short) numRow);
    	        Cell csCell = row.getCell((short)numCol);
                CellStyle style = wb.createCellStyle();
                style.setDataFormat(format.getFormat("0.00"));
                style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
          	    style.setBottomBorderColor(HSSFColor.BLACK.index);
          	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
          	    style.setLeftBorderColor(HSSFColor.BLACK.index);
          	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
          	    style.setRightBorderColor(HSSFColor.BLACK.index);
          	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
          	    style.setTopBorderColor(HSSFColor.BLACK.index);
          	    
          	    csCell.setCellStyle(style);
                
    		}
    	}catch (Exception e) {
    		System.out.println("insertDataToExcel" + e);
       }
    }


	//不加样式
	public void writeStringToCell(int numRow, int numCol, String strval){
		try{
			strval = StringUtils.isEmpty(strval)?"":strval;
			if (wb.getSheetAt(0)!=null){
				Sheet aSheet = wb.getSheetAt(0);
				Row row =aSheet.getRow((short) numRow);
				if(row==null)
					row = aSheet.createRow(numRow);

				Cell csCell = row.getCell((short)numCol);
				if(csCell==null)
					csCell = row.createCell(numCol);
				//csCell.setEncoding(HSSFCell.ENCODING_UTF_16);
				csCell.setCellValue(strval);
			}
		}catch (Exception e) {
			e.printStackTrace();
			//   System.out.println("insertDataToExcel" + e);

		}
	}


    
    public void insertStringToCell(int numRow, int numCol, String strval){
    	try{
    		if (wb.getSheetAt(0)!=null){
    			Sheet aSheet = wb.getSheetAt(0);
    	        Row row =aSheet.createRow((short) numRow);
    	        Cell csCell = row.createCell((short)numCol);
              //  csCell.setEncoding(HSSFCell.ENCODING_UTF_16);
                csCell.setCellValue(strval);
               
    		}
    	}catch (Exception e) {

            System.out.println("insertDataToExcel" + e);

       }
    }
    
    public void writeFormula(int numRow, int numCol, String formula){
    	try{
    		if (wb.getSheetAt(0)!=null){
    			Sheet aSheet = wb.getSheetAt(0);
    	        Row row =aSheet.getRow((short) numRow);
    	        Cell csCell = row.getCell((short)numCol);
                csCell.setCellFormula(formula);
    		}
    	}catch (Exception e) {
    		System.out.println("insertDataToExcel" + e);
       }
    }
    
    public void insertFormula(int numRow, int numCol, String formula){
    	try{
    		if (wb.getSheetAt(0)!=null){
    			Sheet aSheet = wb.getSheetAt(0);
    	        Row row =aSheet.createRow((short) numRow);
    	        Cell csCell = row.createCell((short)numCol);
                csCell.setCellFormula(formula);
    		}
    	}catch (Exception e) {
    		System.out.println("insertDataToExcel" + e);
       }
    }
    
    public void writeNumToCell(int numRow, int numCol, Double num){
    	try{
    		if (wb.getSheetAt(0)!=null){
    			Sheet aSheet = wb.getSheetAt(0);
    	        Row row =aSheet.getRow((short) numRow);
    	        Cell csCell = row.getCell((short)numCol);              
                csCell.setCellValue(num.doubleValue());
    		}
    	}catch (Exception e) {

            System.out.println("insertDataToExcel" + e);

       }
    }
    
    public void insertNumToCell(int numRow, int numCol, Double num){
    	try{
    		if (wb.getSheetAt(0)!=null){
    			Sheet aSheet = wb.getSheetAt(0);
    	        Row row =aSheet.createRow((short) numRow);
    	        Cell csCell = row.createCell((short)numCol);
                csCell.setCellValue(num.doubleValue());
    		}
    	}catch (Exception e) {

            System.out.println("insertDataToExcel" + e);

       }
    }
    
       
    public void replaceDataToCell(int numRow, int numCol, String temstr, String strval){
    	try{
    		if (wb.getSheetAt(0)!=null){
    			Sheet aSheet = wb.getSheetAt(0);
    	        Row row =aSheet.getRow((short) numRow);
    	        Cell csCell = row.getCell((short)numCol);
                String temp = "";
                temp = csCell.getStringCellValue();
                temp = temp.replaceAll(temstr, strval);
                csCell.setCellValue(temp);
    		}
    	}catch (Exception e) {

            System.out.println("insertDataToExcel" + e);

       }
    }
    
    
    public void insertDataToExcel(int numRow,Object[] object) {

        try {
          
          if (null != wb.getSheetAt(0)) {
            Sheet aSheet = wb.getSheetAt(0);
            Row row =aSheet.getRow((short) numRow); 
           
           
            if (row ==null)             	
               row = aSheet.createRow((short)numRow);
            
         
             for(int i = 0;i < object.length ; i++){
              Cell csCell = row.createCell((short)i);
              
      	    CellStyle style = wb.createCellStyle();
      	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      	    style.setBottomBorderColor(HSSFColor.BLACK.index);
      	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      	    style.setLeftBorderColor(HSSFColor.BLACK.index);
      	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
      	    style.setRightBorderColor(HSSFColor.BLACK.index);
      	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
      	    style.setTopBorderColor(HSSFColor.BLACK.index);
      	    
      	    csCell.setCellStyle(style);
              
              if(object[i]!=null)
              csCell.setCellValue(object[i].toString());
              else
              csCell.setCellValue("0");
             }
            
          }

         } catch (Exception e) {

         System.out.println("insertDataToExcel" + e);

        }


       }
       
    public int getExcelLastCellNum()
    {
        
        int count = 0;
            if (null != wb.getSheetAt(0)) {
                Sheet aSheet = wb.getSheetAt(0); 
                Row aRow = aSheet.getRow(0);
                count = aRow.getLastCellNum();
            }
        return count;
    }
 
    public String getOutputPath(String str){
        String temp = "";
       if(str.lastIndexOf("/") != -1) 
       {
         temp = str.substring(0,str.lastIndexOf("/"));   
       }else if(str.lastIndexOf("\\") != -1)
       {
           temp = str.substring(0,str.lastIndexOf("\\"));
       }else if(str.lastIndexOf("\\\\") != -1)
       {
           temp = str.substring(0,str.lastIndexOf("\\\\"));
       }else
       {
           temp = str;
       }
        
        return temp;
        
    }
    
    public Row getRow(int sheetIndex,int rowIndex){
    	Sheet sheet  = wb.getSheetAt(sheetIndex);
    	return   sheet.getRow(rowIndex);
    	
    }
    
    public Sheet getSheet(int sheetIndex){
    	return  wb.getSheetAt(sheetIndex);
    }

     

	public static Object getCellValue(Cell cell){
		 if(cell==null) return null;
		int celltype = cell.getCellType();
		
		if(celltype ==  Cell.CELL_TYPE_NUMERIC)
			return cell.getNumericCellValue();
		
		if(celltype == Cell.CELL_TYPE_STRING){
			String value =cell.getStringCellValue();
			if("null".equals(value)){
				value="";
			}
			
			if(!StringUtils.isEmpty(value)){
				
				value = value.replaceAll(" ", "");
				value = value.replaceAll("  ", "");
				
			}
			return value;
		}
		 
	
		if(celltype == Cell.CELL_TYPE_FORMULA){
			String value =cell.getStringCellValue();
			if("null".equals(value)){
				value="";
			}
			
			return value;
		}
		
		if(celltype  == cell.CELL_TYPE_BLANK)
			return "";
		
		if(celltype ==  cell.CELL_TYPE_ERROR)
			return "";
		
		
		
		return "";
	}
	
	public static void main(String[] args){
//		   try {
//	        	POIFSFileSystem  fs = new POIFSFileSystem(new FileInputStream("D:/goodsimport/brands.xls")); 
//	        	Workbook wb = new HSSFWorkbook(fs);
//	        	System.out.println(wb.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
//	        } catch (IOException e) {
//	        	e.printStackTrace();
//	        	 
//	        }
		
		int a = Double.valueOf("3.0") .intValue();
		
		System.out.println( a);
			 
	}
	
	public void writeStringToCellNum(int numRow, int numCol, Double strval) {
		try {
			if (wb.getSheetAt(0) != null) {
				Sheet aSheet = wb.getSheetAt(0);
				Row row = aSheet.getRow((short) numRow);
				if (row == null)
					row = aSheet.createRow(numRow);

				Cell csCell = row.getCell((short) numCol);
				if (csCell == null)
					csCell = row.createCell(numCol);
				// csCell.setEncoding(HSSFCell.ENCODING_UTF_16);
				csCell.setCellValue(strval);

			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("insertDataToExcel" + e);

		}
	}
}