package cn.ictt.zhanghui.springboot_test.util.system;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * excel相关操作
 * @author ZhangHui
 * @date 2020/8/6
 * TODO（使用阿里的easyexcel替换）
 */
public class ExcelUtil {

	/**
	 * 导出excel
	 * @author ZhangHui
	 * @date 2020/8/6
	 * @param titles 导出excel的标题
	 * @param varList excel内容
	 * @param fileName 文件名称
	 * @param filePath 文件路径
	 * @return boolean 是否导出成功
	 */
	public static boolean exportExcel(List<String> titles, List<Map<String, Object>> varList, String fileName, String filePath) {
		try {
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("titles", titles);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet;
			HSSFCell cell;
			sheet = workbook.createSheet("sheet1");
			int len = titles.size();
			// 标题样式
			HSSFCellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 标题字体
			HSSFFont headerFont = workbook.createFont();
			headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerFont.setFontHeightInPoints((short) 11);
			headerStyle.setFont(headerFont);
			short width = 20, height = 25 * 20;
			sheet.setDefaultColumnWidth(width);
			HSSFRow row0 = sheet.createRow(0);
			// 设置标题
			for (int i = 0; i < len; i++) {
				String title = titles.get(i);
				row0.setHeight((short) 900);
				// 创建第一列
				HSSFCell cell0 = row0.createCell(i);
				cell = sheet.getRow(0).getCell(i);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(title);
			}
			sheet.getRow(0).setHeight(height);

			// 内容样式
			HSSFCellStyle contentStyle = workbook.createCellStyle();
			contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			int varCount = varList.size();
			for (int i = 0; i < varCount; i++) {
				HSSFRow row1 = sheet.createRow(i + 1);
				Map<String, Object> vpd = varList.get(i);
				for (int j = 0; j < len; j++) {
					row1.createCell(j);
					String varstr = vpd.get("var" + (j + 1)) != null ? vpd.get("var" + (j + 1)).toString() : "";
					// cell = getCell(sheet, i+1, j);
					cell = sheet.getRow(i + 1).getCell(j);
					cell.setCellStyle(contentStyle);
					// setText(cell,varstr);
					cell.setCellValue(varstr);
				}
			}
			// 项目路径
			String projectPath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";
			projectPath = projectPath.replaceAll("file:/", "");
			projectPath = projectPath.replaceAll("%20", " ");
			projectPath = projectPath.trim() + filePath;
			OutputStream out = new FileOutputStream(projectPath);
			workbook.write(out);
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
