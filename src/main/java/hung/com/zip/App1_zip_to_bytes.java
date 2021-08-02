package hung.com.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.attribute.FileTime;
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
public class App1_zip_to_bytes {
	private static Logger log = LogManager.getLogger(); 
	
	public static void main(String[] args) {
		String stringNeedZip1 = "dữ liệu cần zip chuyển sang dạng bytes[] và ghi vào trong zipOutputStream";
		String stringNeedZip2 = "dữ liệu cần zip chuyển sang dạng bytes[] và ghi vào trong zipOutputStream";
		/**
		 * ByteArrayOutputStream để lấy dữ liệu byte[] sau khi zip
		 */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// try(){} sẽ tự động close tream sau khi thực hiện xong lệnh
		try(ZipOutputStream zipOutputStream = new ZipOutputStream(baos)) {
			//==================== zip configuration
			zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
			zipOutputStream.setLevel(9); //level for ZipOutputStream.DEFLATED

			//====================== bắt đầu zip 1	
			/**
			 *  Mỗi entry sẽ tương ứng với 1 file và 1 checksum CRC32
			 *  Nhiều entries là nhiều file
			 */
			ZipEntry entry = new ZipEntry("test.txt"); 
			//==== entry option
			//entry.setCrc(crc); //zip sẽ tạo ra giá trị này
//			entry.setCreationTime(time)
//			entry.setLastModifiedTime(time)    // nếu muốn đồng bộ file giữa server và local (ko cần)
			//entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
            //entry.setComment("Created by TheCodersCorner");
			zipOutputStream.putNextEntry(entry); //add 1 file vao trong zip
			
			//===============data will be zipped here
			zipOutputStream.write(stringNeedZip1.getBytes()); //sẽ ghi vào ByteArrayOutputStream baos
			zipOutputStream.write(stringNeedZip2.getBytes());
			
			//mỗi entry Close thì sẽ tạo ra 1 check sum CRC
			zipOutputStream.closeEntry();   //close 1 entry (= 1 file)

			//================================== bắt đầu của file zip 2=====
			/**
			 *  Mỗi entry sẽ tương ứng với 1 file và 1 checksum CRC32
			 *  Nhiều entries là nhiều file
			 */
			entry = new ZipEntry("test2.txt");
			zipOutputStream.putNextEntry(entry); //add 1 file vao trong zip
			//data will be zipped here
			zipOutputStream.write(stringNeedZip1.getBytes());
			zipOutputStream.closeEntry();   //close 1 entry (= 1 file)
			//mỗi entry Close thì sẽ tạo ra 1 check sum
			zipOutputStream.close();//kết thúc Zip file
			
			//baos.toByteArray()
		} catch(IOException e) {
			log.error("fail", e);
		}

	}

}
