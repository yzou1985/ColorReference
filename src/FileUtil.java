
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class FileUtil {

	public static boolean isExist(String filePath) {
		File file = null;
		boolean boo = false;
		try {
			file = new File(filePath);
			boo = file.exists();
		} catch (Exception e) {
			e.printStackTrace();
			boo = false;
		}
		return boo;
	}

	public static File clearFile(String folderPath, String fileName) {
		File file = new File(folderPath + "/" + fileName);
		String prefix = fileName.substring(0, fileName.lastIndexOf("."));
		String postfix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		String newFilePrefix = prefix;
		int temp = 0;
		while (file.exists()) { // 避免新上传目录重复
			temp++;
			newFilePrefix = prefix + temp;

			file = new File(folderPath + "/" + newFilePrefix + postfix);
		}

		return file;
	}

	public static File createDir(String folderPath) {
		File dirFile = null;
		try {
			dirFile = new File(folderPath);
			// 当前不存在，且路径是文件夹(目录)时创建
			if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
				dirFile.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dirFile;
	}

	public static File createFile(String filePath) {
		File file = new File(filePath);
		if (file != null && file.exists()) {
			return file;
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void createFile(String filePath, String fileContent) {

		try {
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);
			resultFile.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}