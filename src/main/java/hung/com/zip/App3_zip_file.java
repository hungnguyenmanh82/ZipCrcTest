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

	public static void main(String[] args) throws IOException{

		String fileName = "testZip.txt";
		FileInputStream fileInputStream = new FileInputStream(fileName); // xem testZip.txt ở project folder
		BufferedInputStream bufInputStream = new BufferedInputStream(fileInputStream, 8192); //DEFAULT_BUFFER_SIZE = 8192
		
		/**
		 * FileSystem sector size: là đơn vị nhỏ nhất trên disk chứa dữ liệu khối.
		 * Tùy vào loại ổ cứng mà 1 sector là 512, 1k, 4k, 8k.
		 * CPU L cache cũng tổ chức data theo sector Page.
		 * Vì thế các buffer nên lấy đơn vị tròn theo bội số của sector bắt đầu là 512byte.
		 */
		byte[] buf = new byte[4096];
		int lengthBuf = 0;
		
		//zip data sẽ ghi vào file "hello-world.zip"
		FileOutputStream fos = new FileOutputStream("hello-world.zip");
		BufferedOutputStream bos = new BufferedOutputStream(fos, 8192); //DEFAULT_BUFFER_SIZE = 8192

		// try() statement là ở Java 8
		// try() sẽ tự động close FileInputStream sau khi hoàn thành
		// https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html 
		try(ZipOutputStream zipOutputStream = new ZipOutputStream(bos)) {
			//==================== zip configuration
			zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
			zipOutputStream.setLevel(9); //level for ZipOutputStream.DEFLATED
			
			//====================== bắt đầu zip 1	
			/**
			 *  Mỗi entry sẽ tương ứng với 1 file và 1 checksum CRC32
			 *  Nhiều entries là nhiều file
			 */
			ZipEntry entry = new ZipEntry(fileName); //tên của Entry trong hello-world.zip file

			zipOutputStream.putNextEntry(entry);
			
			lengthBuf = bufInputStream.read(buf);
			
			/**
			 * Nếu file đang đc ghi vào thì end of file sẽ bị sai
			 * end of File ở thời điểm ta read thôi
			 */
			while(lengthBuf >= 0 ) {
				zipOutputStream.write(buf, 0, lengthBuf);
			}
			
			//entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
            //entry.setComment("Created by TheCodersCorner");
			zipOutputStream.closeEntry();   //close 1 entry (= 1 file)
			//mỗi entry Close thì sẽ tạo ra 1 check sum CRC

			/* use more Entries to add more files
		     and use closeEntry() to close each file entry */	  
			System.out.println("zip successfull");


		} catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}

	/**
	 * cái này dùng thư viện NIO để đọc file ra 1 byte array
		import java.nio.file.Files;
		import java.nio.file.Paths;
		import java.nio.file.Path;
	 */
	public static byte[] readFile2ByteArray(String filename) throws IOException {

		Path path = Paths.get(filename);
		byte[] data = Files.readAllBytes(path);

		return data;

	}

}
