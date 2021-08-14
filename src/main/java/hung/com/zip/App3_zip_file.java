package hung.com.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * lúc compile sẽ gộp "main/resources/" và "main/java/" vào 1 folder chung
 App81_https_Server.class.getResource("/") = root = main/resources/ = main/java/
 App81_https_Server.class.getResource("/abc") = main/resource/abc  = main/java/abc  
 //
 App81_https_Server.class.getResource(".") = root/pakage_name/     => package_name của class này
 App81_https_Server.class.getResource("abc") = root/pakage_name/abc
 App81_https_Server.class.getResource("abc").getPath()
 //
   App81_https_Server.class.getResource("..") = parent folder of root/pakage_name/
   App81_https_Server.class.getResource("../..") = parent of parent of root/pakage_name/  
  //===========================
  + Run or Debug mode trên Eclipse lấy ./ = project folder 

  + run thực tế:  ./ = folder run "java -jar *.jar"
 //========= 
 File("loginTest.json"):   file ở ./ folder    (tùy run thực tế hay trên eclipse)
 File("./abc/test.json"):   
 File("/abc"): root folder on linux (not window)
 */

/**
 * InputStream/OutputStream: là 2 interface xử lý chuỗi bytes
 * có nhiều class Implement 2 interface này
 */
/**
 * Zip file là chuẩn rồi. Cấu trúc zip qui định thành chuẩn. Phương pháp mã hóa cũng thành chuẩn.
 * https://en.wikipedia.org/wiki/Zip_(file_format)
 * “compression methods”
 * http://www.baeldung.com/java-compress-and-uncompress
 */
public class App3_zip_file {
	private static Logger log = LogManager.getLogger(); 
	
	public static void main(String[] args){

		File file = new File("./testZip.txt");
		try {
			zipFile(file);
		} catch (Exception e) {
			log.error("fail", e);
		}
	}
	
	/**
	 *  chỉ zip từn file 1, vì zip tới đâu thì gửi về local tới đó
	 *  zipEntry name = fileName (ko kèm path). FileName đã đầy đủ thông tin về AppName, Date rồi 
	 */
	public static void zipFile(File file) throws Exception {
		
		
		// STEP 1:  read file logs
		FileInputStream fileInputStream = new FileInputStream(file); // xem testZip.txt ở project folder
		BufferedInputStream bufInputStream = new BufferedInputStream(fileInputStream, 8192); //DEFAULT_BUFFER_SIZE = 8192
		
		/**
		 * FileSystem sector size: là đơn vị nhỏ nhất trên disk chứa dữ liệu khối.
		 * Tùy vào loại ổ cứng mà 1 sector là 512, 1k, 4k, 8k.
		 * CPU L cache cũng tổ chức data theo sector Page.
		 * Vì thế các buffer nên lấy đơn vị tròn theo bội số của sector bắt đầu là 512byte.
		 */
		byte[] buf = new byte[4096];
		int lengthBuf = 0;
		
		// STEP 2: zipFileName = logFilePath + ".zip"
		String zipName = file.getPath() + ".zip";
		FileOutputStream fos = new FileOutputStream(zipName);
		BufferedOutputStream bufOutStream = new BufferedOutputStream(fos, 8192); //DEFAULT_BUFFER_SIZE = 8192

		// try() statement là ở Java 8
		// try() sẽ tự động close ZipOutputStream sau khi hoàn thành
		// https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html 
		try {
			ZipOutputStream zipOutputStream = new ZipOutputStream(bufOutStream);
			
			//==================== zip configuration
			zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
			zipOutputStream.setLevel(9); //level for ZipOutputStream.DEFLATED
			
			//====================== bắt đầu zip 1	
			/**
			 *  Mỗi entry sẽ tương ứng với 1 file và 1 checksum CRC32
			 *  Nhiều entries là nhiều file
			 */
			ZipEntry entry = new ZipEntry(file.getName()); //tên của Entry trong hello-world.zip file

			zipOutputStream.putNextEntry(entry);
			
			
			
			/**
			 * Nếu file đang đc ghi vào thì end of file sẽ bị sai
			 * end of File ở thời điểm ta read thôi
			 */
			while((lengthBuf = bufInputStream.read(buf)) >= 0 ) {
				zipOutputStream.write(buf, 0, lengthBuf);
			}
			
			//entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
            //entry.setComment("Created by TheCodersCorner");
			zipOutputStream.closeEntry();   //close 1 entry (= 1 file)
			//mỗi entry Close thì sẽ tạo ra 1 check sum CRC

			/* use more Entries to add more files
		     and use closeEntry() to close each file entry */	  
			System.out.println("zip successfull");
			
			//
			zipOutputStream.flush();
			zipOutputStream.close();
			bufOutStream.close();
			
			bufInputStream.close();
		} catch(IOException e) {
			throw e; // xử lý sau
		}
	}


}
