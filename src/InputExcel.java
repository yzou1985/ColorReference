import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

public class InputExcel {

	private static final String KEY_FIRST = "COLOR_NAME";
	private static final String KEY_SECOND = "COLOR_VALUE";
	private static final String KEY_THIRD = "COLOR_REFERENCE_FROM_JAVA";
	private static final String KEY_FOUR = "COLOR_REFERENCE_FROM_XML";

	private static CellStyle mCellCommonStyle;
	private static HSSFCellStyle mColumnHeadStyle;
	
	private static CellStyle mCellWarningStyle;

	private static ArrayList<RowData> mRowDatas = new ArrayList<RowData>();
	
	private static ArrayList<String> mValues = new ArrayList<String>();
	
	private static ArrayList<Integer> mFormatValues = new ArrayList<Integer>();
	
	private static HSSFWorkbook mWorkbook = new HSSFWorkbook();
	
	private static RowDataComparator mComparator = new RowDataComparator();
	
	enum FileType {
		JAVA, XML
	}

	public static void main(String args[]) {
		
		initData();

		//setRowHighLight();
		
		createExcel();

	}
	
	private static void initData() {
		
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader("/home/yzou/Dahuo/Dahuo/res/values/colors.xml"));
			//reader = new BufferedReader(new FileReader("/home/yzou/back/colors.xml"));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("name=")) {
					String name = line.substring(line.indexOf("name=") + 6, line.indexOf(">") - 1);
					String value = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<") - 1);
					
					StringBuilder sb1 = new StringBuilder();
					StringBuilder sb2 = new StringBuilder();
					sb1.append("fgrep -rnw R.color.").append(name).append(" /home/yzou/Dahuo/Dahuo/src/*");
					sb2.append("fgrep -rnw @color/").append(name).append(" /home/yzou/Dahuo/Dahuo/res/*");
					
//					String fileDir = "/home/yzou/color_references" + File.separator + name;
//					FileUtil.createDir(fileDir);
//
//					String filePath = fileDir + File.separator + "fromJavaFiles";
//					String filePath2 = fileDir + File.separator + "fromXmlFiles";
//					saveOutputToFile(filePath, sb1.toString());
//					saveOutputToFile(filePath2, sb2.toString());
					
					//mRowDatas.add(new RowData(name, value, filePath, filePath2));
					
					//int xxx = Integer.parseInt(value.substring(1, value.length()));
					
					RowData rowData = new RowData(name, value, 
							getOutputContent(sb1.toString(), FileType.JAVA), 
							getOutputContent(sb2.toString(), FileType.XML));
					
					if (mValues.contains(value)) {
						rowData.setHighLight(true);
					} else {
						rowData.setHighLight(false);
					}
					
					mValues.add(value);
					
					mRowDatas.add(rowData);
					
				}
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
//	private static void setRowHighLight() {
//		
//		for (RowData rowData : mRowDatas) {
//			if (mValues.contains(rowData.getValue())) {
//				rowData.setHighLight(true);
//			}
//		}
//		
//	}

	private static void createExcel() {

		HSSFSheet sheet = mWorkbook.createSheet("TableSheet");

		mCellCommonStyle = mWorkbook.createCellStyle();
		
		mCellWarningStyle = mWorkbook.createCellStyle();

		// 一般样式
		mCellCommonStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		mCellCommonStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		mCellCommonStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		mCellCommonStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		mCellCommonStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		mCellCommonStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		mCellCommonStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		mCellCommonStyle.setLocked(true);
		mCellCommonStyle.setWrapText(true);
		mCellCommonStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		
		
		// warning
		mCellWarningStyle.setFillForegroundColor(HSSFColor.ROSE.index);
		mCellWarningStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		mCellWarningStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		mCellWarningStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		mCellWarningStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		mCellWarningStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		mCellWarningStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		mCellWarningStyle.setLocked(true);
		mCellWarningStyle.setWrapText(true);
		mCellWarningStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		
		HSSFFont columnHeadFont = mWorkbook.createFont();
		columnHeadFont.setFontName("宋体");
		columnHeadFont.setFontHeightInPoints((short) 10);
		columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// 列头的样式
		mColumnHeadStyle = mWorkbook.createCellStyle();
		mColumnHeadStyle.setFont(columnHeadFont);
		mColumnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		mColumnHeadStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		mColumnHeadStyle.setLocked(true);
		mColumnHeadStyle.setWrapText(true);
		mColumnHeadStyle.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色
		mColumnHeadStyle.setBorderLeft((short) 1);// 边框的大小
		mColumnHeadStyle.setRightBorderColor(HSSFColor.BLACK.index);// 右边框的颜色
		mColumnHeadStyle.setBorderRight((short) 1);// 边框的大小
		mColumnHeadStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		mColumnHeadStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色
		mColumnHeadStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		mColumnHeadStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// 设置sheet名称和单元格内容
		mWorkbook.setSheetName(0, "Dahuo_ColorRerenceMark");

		// 设置单元格内容
		inputDataInExcel(sheet);

		try {
			FileOutputStream out = new FileOutputStream(new File("Excel_from_java.xls"));
			mWorkbook.write(out);
			out.close();
			System.out.println("Excel written successfully...");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void inputDataInExcel(HSSFSheet sheet) {
		
		ArrayList<RowData> rowDatas = new ArrayList<RowData>();
		rowDatas.add(new RowData(KEY_FIRST, KEY_SECOND, KEY_THIRD, KEY_FOUR));
		Collections.sort(mRowDatas, mComparator);
		rowDatas.addAll(mRowDatas);
		
		for (int i = 0; i < rowDatas.size(); i++) {
			Row row = sheet.createRow(i);
			row.setHeight((short) 2000);
			RowData data = rowDatas.get(i);

			System.out.println("excel_row = " + i + ", name = " + data.getName() + ", value = " + data.getValue());

			CellStyle style = i == 0 ? mColumnHeadStyle : mCellCommonStyle;
			for (int j = 0; j < 4; j++) {
				String cellValue = getCellValue(j, data);
				if (!cellValue.isEmpty()) {
					Cell cell = row.createCell(j);
					cell.setCellValue(cellValue);
					if (data.isHighLight()) {
						cell.setCellStyle(mCellWarningStyle);
					} else {
						cell.setCellStyle(style);
					}
				}
			}
		}

	}

	private static String getCellValue(int index, RowData data) {
		String cellValue = "";
		if (index == 0) {
			cellValue = data.getName();
		} else if (index == 1) {
			cellValue = data.getValue();
		} else if (index == 2) {
			cellValue = data.getContentFromJavaFile();
		} else if (index == 3) {
			cellValue = data.getContentFromXmlFile();
		}

		return cellValue;
	}
	
	private static String getOutputContent(String command, FileType type) {

		try {
			Runtime rt = Runtime.getRuntime();
			String[] cmd = { "/bin/sh", "-c", command };
			Process proc = rt.exec(cmd);
			InputStream stderr = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;

			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				line = formatContent(line, type);
				sb.append(line + "\n");
			}
			
			if (!sb.toString().isEmpty()) {
				return sb.toString();
			}

			proc.waitFor();
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		}
		
		return "";

	}
	
	private static String formatContent(String content, FileType type) {
		if (content.isEmpty()) {
			return "";
		}
		
		int index = 0;
		if (type == FileType.JAVA) {
			index = content.indexOf("/src/");
		} else if (type == FileType.XML) {
			index = content.indexOf("/res/");
		}
		
		if (index == 0) {
			return "";
		}
		
		String temp = content.substring(index + 5);
		String[] array = temp.split(":");
		String rowNum = array[1];
		int endIndex = temp.indexOf(rowNum);
		String temp2 = temp.substring(0, endIndex + rowNum.length());
		
		return temp2;
		
	}

	private static void saveOutputToFile(String filePath, String command) {

		try {
			Runtime rt = Runtime.getRuntime();
			String[] cmd = { "/bin/sh", "-c", command };
			Process proc = rt.exec(cmd);
			InputStream stderr = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;

			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			if (!sb.toString().isEmpty()) {
				File file = FileUtil.createFile(filePath);
				FileUtils.writeStringToFile(file, sb.toString());
			}

			proc.waitFor();
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		}

	}

//	private static void inputExcelContent() {
//		
//		HSSFSheet sheet = mWorkbook.getSheet("TableSheet");
//		
//		for (int i = 0; i < mRowDatas.size(); i++) {
//			
//			if (i == 0) {
//				continue;
//			}
//			
//			Row row = sheet.getRow(i);
//			RowData data = mRowDatas.get(i);
//			for (int j = 2; j < 4; j++) {
//				String cellValue = getCellValue(j, data);
//				if (!cellValue.isEmpty()) {
//					Cell cell = row.createCell(j);
//					cell.setCellValue(cellValue);
//					cell.setCellStyle(mCellCommonStyle);
//				}
//			}
//		}
//		
//	}
	
	private static String getCellValue(int index, RowData data, boolean isFirstRow) {
		String cellValue = "";
		if (index == 0) {
			cellValue = data.getName();
		} else if (index == 1) {
			cellValue = data.getValue();
		} else if (index == 2) {
			cellValue = data.getContentFromJavaFile();
			if (!isFirstRow) {
				try {
					cellValue = FileUtils.readFileToString(new File(cellValue));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (index == 3) {
			cellValue = data.getContentFromXmlFile();
			if (!isFirstRow) {
				try {
					cellValue = FileUtils.readFileToString(new File(cellValue));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return cellValue;
	}
	
}