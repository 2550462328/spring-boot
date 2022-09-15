package cn.ictt.zhanghui.springboot_test.base.util.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtil {
	/**
	 * 文件复制
	 * 
	 * @param srcPath
	 *            源文件路径
	 * @param newFilePath
	 *            复制后存放路径
	 * @param fileName
	 *            复制后文件名称
	 */
	public static void copyFile(String srcPath, String newFilePath, String fileName) {
		File srcFile = new File(srcPath);
		File target = new File(newFilePath);
		if (!target.exists()) {
			target.mkdir();
		}

		File targetFile = new File(newFilePath + fileName);
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(targetFile);
			// 从in中批量读取字节，放入到buf这个字节数组中，
			// 从第0个位置开始放，最多放buf.length个 返回的是读到的字节的个数
			byte[] buf = new byte[8 * 1024];
			int len = 0;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
				out.flush();
			}
		} catch (Exception e) {
			throw new RuntimeException("获取生育保险核定单预览图失败");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
			}
		}

	}
	
	public static boolean delFile(File file) {
		if (!file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				delFile(f);
			}
		}
		return file.delete();
	}
}