package com.zbjdl.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

	public static final String EMPTY = "";
	public static final String POINT = ".";
	public static final String LIB_PATH = "lib";
	public static final String STUDENT_INFO_XLS_PATH = LIB_PATH
			+ "/student_info" + POINT + OFFICE_EXCEL_2003_POSTFIX;
	public static final String STUDENT_INFO_XLSX_PATH = LIB_PATH
			+ "/student_info" + POINT + OFFICE_EXCEL_2010_POSTFIX;
	public static final String NOT_EXCEL_FILE = " : Not the Excel file!";
	public static final String PROCESSING = "Processing...";

	/**
	 * get postfix of the path
	 * 
	 * @param path
	 * @return
	 */
	public static String getPostfix(String path) {
		if (path == null || EMPTY.equals(path.trim())) {
			return EMPTY;
		}
		if (path.contains(POINT)) {
			return path.substring(path.lastIndexOf(POINT) + 1, path.length());
		}
		return EMPTY;
	}

	/**
	 * Read the Excel 2010
	 * 
	 * @param path
	 *            the path of the excel file
	 * @return
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public <T> List<T> readXlsx(InputStream stream,
			Map<Integer, String> header, Class<T> classz) throws IOException,
			InstantiationException, IllegalAccessException, SecurityException,
			NoSuchFieldException {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(stream);
		T t = null;
		List<T> list = new ArrayList<T>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					t = classz.newInstance();
					for (Map.Entry<Integer, String> entry : header.entrySet()) {
						XSSFCell cell = xssfRow.getCell(entry.getKey());
						Field field = classz.getField(entry.getValue());
						field.setAccessible(true);
						field.set(t, getValue(cell));
					}

					list.add(t);
				}
			}
		}
		return list;
	}

	/**
	 * Read the Excel 2003-2007
	 * 
	 * @param path
	 *            the path of the Excel
	 * @return
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public <T> List<T> readXls(InputStream stream, Map<Integer, String> header,
			Class<T> classz) throws IOException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchFieldException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(stream);
		T t = null;
		List<T> list = new ArrayList<T>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					t = classz.newInstance();
					for (Map.Entry<Integer, String> entry : header.entrySet()) {
						HSSFCell cell = hssfRow.getCell(entry.getKey());
						Field field = classz.getField(entry.getValue());
						field.setAccessible(true);
						field.set(t, getValue(cell));
					}
					list.add(t);
				}
			}
		}
		return list;
	}

	@SuppressWarnings("static-access")
	private String getValue(XSSFCell xssfCell) {
		if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfCell.getBooleanCellValue());
		} else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfCell.getNumericCellValue());
		} else {
			return String.valueOf(xssfCell.getStringCellValue());
		}
	}

	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	/**
	 * 写excel
	 * 
	 * @param dataList
	 *            外层list是行的数据， 内层list是列的数据
	 * @return
	 */
	public static HSSFWorkbook wirteExcel(List<List<String>> dataList) {
		// 创建excel文件对象
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个张表
		Sheet sheet = wb.createSheet();
		// 创建第一行
		Font font0 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false,
				(short) 200);
		Font font1 = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "宋体", false,
				(short) 200);
		for (int i = 0; i < dataList.size(); i++) {
			List<String> temp = dataList.get(i);
			Row rowData = sheet.createRow(i + 1);
			for (int j = 0; j < temp.size(); j++) {
				createCell(wb, rowData, j, temp.get(j), i == 0 ? font0 : font1);
			}
		}
		return wb;
	}
	/**
	 * 写excel
	 * 
	 * @param dataList 
	 *           外层是sheet页数据 中层list是行的数据， 内层list是列的数据
	 * @return
	 */
	public static HSSFWorkbook wirteExcelMulitSheet(List< List<List<String>> > dataLists,List<String> sheetName) {
		// 创建excel文件对象
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个张表
		for(int index=0;index<dataLists.size();index++)
		{
			Sheet sheet = wb.createSheet();
			List<List<String>> dataList= dataLists.get(index);
			wb.setSheetName(index, sheetName.get(index));//TODO  有可能异常
			// 创建第一行
			Font font0 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false,
					(short) 200);
			Font font1 = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "宋体", false,
					(short) 200);
			for (int i = 0; i < dataList.size(); i++) {
				List<String> temp = dataList.get(i);
				Row rowData = sheet.createRow(i + 1);
				for (int j = 0; j < temp.size(); j++) {
					createCell(wb, rowData, j, temp.get(j), i == 0 ? font0 : font1);
				}
			}
		}
		
		return wb;
	}

	 public  static void main(String args[]){
		 List< List<List<String>> > dataLists = new ArrayList< List<List<String>> >();
		 List<List<String>> e  =new ArrayList <List<String>> ();
		 List<String> sheetName = new ArrayList<String>();
		 sheetName.add("1");
		 sheetName.add("2");
		 e.add(sheetName);
		 
		 dataLists.add(e);
		 dataLists.add(e);
		
		ExcelUtil.wirteExcelMulitSheet(dataLists, sheetName );
	 }
	/**
	 * 写excel
	 * 
	 * @param dataList
	 *            外层list是行的数据， 内层list是列的数据
	 * @return
	 */
	public static HSSFWorkbook wirteExcelForBackBusi(List<List<String>> dataList,String title) {
		// 创建excel文件对象
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个张表
		Sheet sheet = wb.createSheet();

		// 设置列宽
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 6500);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 3500);
		sheet.setColumnWidth(4, 6500);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);

		// 创建第一列
		HSSFRow row0 = (HSSFRow) sheet.createRow(0);

		// 设置字体    - 标题
	    HSSFFont headfont = wb.createFont();    
	    headfont.setFontName("黑体");    
	    headfont.setFontHeightInPoints((short) 22);// 字体大小    
	    headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗    
		
		// 样式 -标题
		HSSFCellStyle headstyle = wb.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		headstyle.setLocked(true);
		headstyle.setWrapText(true);// 自动换行

		// 设置行高
		row0.setHeight((short) 900);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue(new HSSFRichTextString(title));
		cell0.setCellStyle(headstyle);

		/**
		 * 合并单元格 第一个参数：第一个单元格的行数（从0开始） 第二个参数：第二个单元格的行数（从0开始）
		 * 第三个参数：第一个单元格的列数（从0开始） 第四个参数：第二个单元格的列数（从0开始）
		 */
		CellRangeAddress range = new CellRangeAddress(0, 0, 0, 9);
		sheet.addMergedRegion(range);

		// 创建第一行
		Font font0 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false,
				(short) 200);
		Font font1 = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "宋体", false,
				(short) 200);
		for (int i = 0; i < dataList.size(); i++) {
			List<String> temp = dataList.get(i);
			Row rowData = sheet.createRow(i + 1);
			for (int j = 0; j < temp.size(); j++) {
				createCell(wb, rowData, j, temp.get(j), i == 0 ? font0 : font1);
			}
		}
		return wb;
	}
	
	/**
	 * 写excel
	 * 
	 * @param dataList
	 *            外层list是行的数据， 内层list是列的数据
	 * @return
	 */
	public static HSSFWorkbook wirteExcelForZD(List<List<String>> dataList,String title) {
		// 创建excel文件对象
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个张表
		Sheet sheet = wb.createSheet();

		// 设置列宽
		sheet.setColumnWidth(0, 6500);
		sheet.setColumnWidth(1, 6500);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 3500);
		sheet.setColumnWidth(4, 4500);
		sheet.setColumnWidth(5, 4500);
		sheet.setColumnWidth(6, 4500);
		sheet.setColumnWidth(7, 5000);
		sheet.setColumnWidth(8, 6500);
		sheet.setColumnWidth(9, 6500);

		// 创建第一列
		HSSFRow row0 = (HSSFRow) sheet.createRow(0);

		// 设置字体    - 标题
	    HSSFFont headfont = wb.createFont();    
	    headfont.setFontName("黑体");    
	    headfont.setFontHeightInPoints((short) 22);// 字体大小    
	    headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗    
		
		// 样式 -标题
		HSSFCellStyle headstyle = wb.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		headstyle.setLocked(true);
		headstyle.setWrapText(true);// 自动换行

		// 设置行高
		row0.setHeight((short) 900);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue(new HSSFRichTextString(title));
		cell0.setCellStyle(headstyle);

		/**
		 * 合并单元格 第一个参数：第一个单元格的行数（从0开始） 第二个参数：第二个单元格的行数（从0开始）
		 * 第三个参数：第一个单元格的列数（从0开始） 第四个参数：第二个单元格的列数（从0开始）
		 */
		CellRangeAddress range = new CellRangeAddress(0, 0, 0, 7);
		sheet.addMergedRegion(range);

		// 创建第一行
		Font font0 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false,
				(short) 200);
		Font font1 = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "宋体", false,
				(short) 200);
		for (int i = 0; i < dataList.size(); i++) {
			List<String> temp = dataList.get(i);
			Row rowData = sheet.createRow(i + 1);
			for (int j = 0; j < temp.size(); j++) {
				createCell(wb, rowData, j, temp.get(j), i == 0 ? font0 : font1);
			}
		}
		return wb;
	}
	
	/**
	 * 导出转账模版
	 * @param title
	 * @return
	 */
	public static HSSFWorkbook wirteExcelForDemo(List<List<String>> dataList,String title) {
		// 创建excel文件对象
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个张表
		Sheet sheet = wb.createSheet();

		// 设置列宽
		sheet.setColumnWidth(0, 6500);
		sheet.setColumnWidth(1, 6500);
		sheet.setColumnWidth(2, 6500);
		sheet.setColumnWidth(3, 3200);
		sheet.setColumnWidth(4, 3200);
		sheet.setColumnWidth(5, 6500);
		sheet.setColumnWidth(6, 6500);
		sheet.setColumnWidth(7, 6500);

		// 创建第一列
		HSSFRow row0 = (HSSFRow) sheet.createRow(0);

		// 设置字体    - 标题
	    HSSFFont headfont = wb.createFont();    
	    headfont.setFontName("黑体");    
	    headfont.setFontHeightInPoints((short) 22);// 字体大小    
	    headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗    
		
		// 样式 -标题
		HSSFCellStyle headstyle = wb.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		headstyle.setLocked(true);
		headstyle.setWrapText(true);// 自动换行

		// 设置行高
		row0.setHeight((short) 900);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue(new HSSFRichTextString(title));
		cell0.setCellStyle(headstyle);

		/**
		 * 合并单元格 第一个参数：第一个单元格的行数（从0开始） 第二个参数：第二个单元格的行数（从0开始）
		 * 第三个参数：第一个单元格的列数（从0开始） 第四个参数：第二个单元格的列数（从0开始）
		 */
		CellRangeAddress range = new CellRangeAddress(0, 0, 0, 7);
		sheet.addMergedRegion(range);

		// 创建第一行
		Font font0 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false,
				(short) 200);
		Font font1 = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "宋体", false,
				(short) 200);
		for (int i = 0; i < dataList.size(); i++) {
			List<String> temp = dataList.get(i);
			Row rowData = sheet.createRow(i + 1);
			for (int j = 0; j < temp.size(); j++) {
				createCell(wb, rowData, j, temp.get(j), i == 0 ? font0 : font1);
			}
		}
		return wb;
	}


	/**
	 * 创建单元格并设置样式,值
	 * 
	 * @param wb
	 * @param row
	 * @param column
	 * @param
	 * @param
	 * @param value
	 */
	private static void createCell(Workbook wb, Row row, int column,
			String value, Font font) {
		Cell cell = row.createCell(column);
		cell.setCellValue(value);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * 设置字体
	 * 
	 * @param wb
	 * @return
	 */
	private static Font createFonts(Workbook wb, short bold, String fontName,
			boolean isItalic, short hight) {
		Font font = wb.createFont();
		font.setFontName(fontName);
		font.setBoldweight(bold);
		font.setItalic(isItalic);
		font.setFontHeight(hight);
		return font;
	}

}
