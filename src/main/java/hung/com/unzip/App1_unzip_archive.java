package hung.com.unzip;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * http://www.baeldung.com/java-compress-and-uncompress
 * 
 * Hỗ trợ unzip 1 file chứa nhiều items và folder recursively
 */
/**
 * code này đã test chạy tốt. Lúc nào dùng thì bỏ các dòng log.debug() đi là ok
 */
public class App1_unzip_archive {
	private static Logger log = LogManager.getLogger(); 

	public static void main(String[] args) throws IOException {
		String fileZip = "dirCompressed.zip";
		File destDir = new File("unzipTest"); // sẽ unzip vào folder này
		byte[] buffer = new byte[1024];
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(fileZip)); //unzip
		
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		while (zipEntry != null) {
			log.debug("=====newFile zipEntry: " + zipEntry.getName()); // = fullPath name
			File newFile = newFile(destDir, zipEntry);
			if (zipEntry.isDirectory()) {
				log.debug("zipEntry = folder");
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					throw new IOException("Failed to create directory " + newFile);
				}
			} else {
				log.debug("zipEntry = file");
				// fix for Windows-created archives
				File parent = newFile.getParentFile(); // get parent folder's path  
				log.debug("create parents' folder of file");
				
				/**
				 * Nếu các parent directory của file ko tồn tại nó sẽ báo lỗi
				 */
				if (!parent.isDirectory() && !parent.mkdirs()) {
					throw new IOException("Failed to create directory " + parent);
				}

				// write file content
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zipInputStream.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.closeEntry();
		zipInputStream.close();
	}

	/**
	 * Mục đích của function này là loại bỏ lỗi unzip outside folder khi khi zipEntryName = absolute path 
	 * lỗi: ghi đè nên 1 file nào đó ở bên ngoài tạo thành virus
	 */
	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		
		/**
		 * zipEntry.getName(): gồm cả directory path
		 * relative path is ok
		 * absolute path is wrong ( cách tạo virus)
		 */
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();  //CanonicalPath trả về absolute path
		log.debug("destDirPath: " + destDirPath);
		String destFilePath = destFile.getCanonicalPath();  //CanonicalPath trả về absolute path
		log.debug("destFilePath: " + destFilePath);

		/**
		 * 
		 */
		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}
}
