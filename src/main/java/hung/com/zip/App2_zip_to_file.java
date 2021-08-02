package hung.com.zip;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
public class App2_zip_to_file {
	private static Logger log = LogManager.getLogger(); 
	
	public static void main(String[] args) throws IOException{
		String stringNeedZip1 = "dữ liệu cần zip chuyển sang dạng bytes[] và ghi vào trong zipOutputStream";
		String stringNeedZip2 = "dữ liệu cần zip chuyển sang dạng bytes[] và ghi vào trong zipOutputStream";

		//zip data sẽ ghi vào file "hello-world.zip"
		FileOutputStream fos = new FileOutputStream("hello-world.zip");  // xem project folder
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		// try(){} sẽ tự động close tream sau khi thực hiện xong lệnh
		try(ZipOutputStream zipOutPutStream = new ZipOutputStream(bos)) {
			//==================== zip configuration
			zipOutPutStream.setMethod(ZipOutputStream.DEFLATED);
			zipOutPutStream.setLevel(9); //level for ZipOutputStream.DEFLATED

			//================================== bắt đầu của file thứ 1 đc zip=====
			/**
			 *  Mỗi entry sẽ tương ứng với 1 file và 1 checksum CRC32
			 *  Nhiều entries là nhiều file
			 */
			ZipEntry entry = new ZipEntry("test1.txt"); 

			zipOutPutStream.putNextEntry(entry);
			//data will be zipped here
			zipOutPutStream.write(stringNeedZip1.getBytes());
			//entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
            //entry.setComment("Created by TheCodersCorner");
			
			//mỗi entry Close thì sẽ tạo ra 1 check sum CRC
			zipOutPutStream.closeEntry();   //close 1 entry (= 1 file)

			//================================== bắt đầu của file thứ 2 đc zip=====
			entry = new ZipEntry("test2.txt"); 
			zipOutPutStream.putNextEntry(entry);
			//data will be zipped here
			zipOutPutStream.write(stringNeedZip2.getBytes());
			zipOutPutStream.closeEntry();   //close 1 entry (= 1 file)
			
			zipOutPutStream.close();//kết thúc Zip file

		} catch(IOException e) {
			log.error("faile", e);
		}

	}

}
