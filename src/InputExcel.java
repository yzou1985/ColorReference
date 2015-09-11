import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

public class InputExcel {

	private static Map<String, String[]> mExcelDataMap = new HashMap<String, String[]>();

	private static final String KEY_FIRST = "COLOR_NAME";
	private static final String KEY_SECOND = "COLOR_VALUE";
	private static final String KEY_THIRD = "COLOR_REFERENCE_FROM_JAVA";
	private static final String KEY_FOUR = "COLOR_REFERENCE_FROM_XML";

	private static CellStyle mCellStyle;

	public static void main(String args[]) {

		initData();

		createExcel();

	}

	private static void createExcel() {

		// 创建Excel工作薄对象
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 创建Excel工作表对象
		HSSFSheet sheet = workbook.createSheet("TableSheet");

		// 创建单元格样式
		mCellStyle = workbook.createCellStyle();

		// 设置这些样式
		mCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		mCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		mCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		mCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		mCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		mCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		mCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 设置sheet名称和单元格内容
		workbook.setSheetName(0, "Dahuo_ColorRerenceMark");

		// 设置单元格内容 cell.setCellValue("单元格内容");
		inputDataInExcel(sheet);

		try {
			FileOutputStream out = new FileOutputStream(new File("Excel_from_java.xls"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully...");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void inputDataInExcel(HSSFSheet sheet) {

		// 创建第一行
		Row row0 = sheet.createRow(0);
		String[] strArray0 = mExcelDataMap.get(KEY_FIRST);
		for (int i = 0; i < strArray0.length; i++) {
			Cell cell = row0.createCell(i);
			cell.setCellValue(strArray0[i]);
			cell.setCellStyle(mCellStyle);
		}

		// 创建第二行
		Row row1 = sheet.createRow(1);
		String[] strArray1 = mExcelDataMap.get(KEY_SECOND);
		for (int i = 0; i < strArray1.length; i++) {
			Cell cell = row1.createCell(i);
			cell.setCellValue(strArray1[i]);
			cell.setCellStyle(mCellStyle);
		}

		// 创建第三行
		Row row2 = sheet.createRow(2);
		String[] strArray2 = mExcelDataMap.get(KEY_THIRD);
		for (int i = 0; i < strArray2.length; i++) {
			Cell cell = row2.createCell(i);
			cell.setCellValue(strArray2[i]);
			cell.setCellStyle(mCellStyle);
		}

		// 创建第四行
		Row row3 = sheet.createRow(3);
		String[] strArray3 = mExcelDataMap.get(KEY_FOUR);
		for (int i = 0; i < strArray3.length; i++) {
			Cell cell = row3.createCell(i);
			cell.setCellValue(strArray3[i]);
			cell.setCellStyle(mCellStyle);
		}

	}

	private static void initData() {

		BufferedReader reader = null;
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> contentsFromJavaFile = new ArrayList<String>();
		ArrayList<String> contensFromXmlFile = new ArrayList<String>();

		try {

			reader = new BufferedReader(new FileReader("/home/yzou/back/colors.xml"));
			String line;

			while ((line = reader.readLine()) != null) {

				if (line.contains("name=")) {
					String name = line.substring(line.indexOf("name=") + 6, line.indexOf(">") - 1);

					names.add(name);

					String value = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<") - 1);

					values.add(value);

					StringBuilder sb1 = new StringBuilder();
					StringBuilder sb2 = new StringBuilder();
					sb1.append("fgrep -rnw R.color.").append(name).append(" /home/yzou/Dahuo/Dahuo/*");
					sb2.append("fgrep -rnw @color/").append(name).append(" /home/yzou/Dahuo/Dahuo/*");

					String contentFromJavaFile = getOutputContent(sb1.toString());
					String contenFromXmlFile = getOutputContent(sb2.toString());

					contentsFromJavaFile.add(contentFromJavaFile);
					contensFromXmlFile.add(contenFromXmlFile);

				}

			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		String[] nameArray = new String[names.size()];
		mExcelDataMap.put(KEY_FIRST, names.toArray(nameArray));

		String[] valueArray = new String[values.size()];
		mExcelDataMap.put(KEY_SECOND, values.toArray(valueArray));

		String[] contentArray = new String[contentsFromJavaFile.size()];
		mExcelDataMap.put(KEY_THIRD, contentsFromJavaFile.toArray(contentArray));

		String[] contentArray2 = new String[contensFromXmlFile.size()];
		mExcelDataMap.put(KEY_FOUR, contensFromXmlFile.toArray(contentArray2));

	}

	private static String getOutputContent(String command) {

		StringBuilder sb = new StringBuilder();

		try {
			Runtime rt = Runtime.getRuntime();

			String[] cmd = { "/bin/sh", "-c", command };

			Process proc = rt.exec(cmd);

			InputStream stderr = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);

			BufferedReader br = new BufferedReader(isr);
			String line = null;
			System.out.println(command);

			while ((line = br.readLine()) != null) {
				int index = line.indexOf(":");
				sb.append(line.substring(0, index) + "\n");
			}
			proc.waitFor();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return sb.toString();

	}

}