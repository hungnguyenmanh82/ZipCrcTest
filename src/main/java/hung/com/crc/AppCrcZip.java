package hung.com.crc;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * CRC là giải thuật check sum để check toàn vẹn dữ liệu.
 * CRC là dạng đơn giản của giải thuật Hashing
 */
/**
 * Zip file là chuẩn rồi. Cấu trúc zip qui định thành chuẩn. Phương pháp mã hóa cũng thành chuẩn.
 * https://en.wikipedia.org/wiki/Zip_(file_format)
 * “compression methods”
   http://www.baeldung.com/java-compress-and-uncompress
   Java zip cũng  tính toán CRC32 (32 bits). Giá trị này là giá trị của data trước khi zip dữ liệu.
   CRC (Cyclic Redundancy Check): tính toán để kiểm tra toàn vẹn dữ liệu, giống checksum.
   CRC của Zip và CRC32 tính toán là giống nhau đều cho ra kết quả giống nhau (đã test)
    https://en.wikipedia.org/wiki/Cyclic_redundancy_check

 */
public class AppCrcZip {
	private static Logger log = LogManager.getLogger(); 
	
    public static void main(String[] args) throws IOException {
    	
    	//========================= check zip CRC 32 ========================
    	byte[] byteArr1 = new String("ko gi ngheo bang thieu tai").getBytes();
		//ByteArrayOutputStream => byte array
		//baos.toByteArray()
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		//@zos sẽ trả zip data cho @baos
		try(ZipOutputStream zos = new ZipOutputStream(baos)) {
			//==================== zip configuration
			zos.setMethod(ZipOutputStream.DEFLATED);
			zos.setLevel(9); //level for ZipOutputStream.DEFLATED

			//============================	
			/* File is not on the disk, test.txt indicates
		     only the file name to be put into the zip */
			ZipEntry entry = new ZipEntry("test.txt"); 
			
			//CRC32 giá trị ban đầu = FFFFFFFFFFFFFFFF = long = 8bytes = 64bit
			//tuy nhiên CRC32 chỉ dùng 4byte = 32 bit
			//CRC là giá trị của data trước khi zip
			log.debug("the initial CRC32 1 =%X\n", entry.getCrc()); 
			
			//==== entry option
			//entry.setCrc(crc); //zip sẽ tạo ra giá trị này
//			entry.setCreationTime(time)
			zos.putNextEntry(entry); //add 1 file vao trong zip
			
			//===============data will be zipped here
			zos.write(byteArr1); //sẽ ghi vào ByteArrayOutputStream baos
			
			//entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
            //entry.setComment("Created by TheCodersCorner");
			zos.closeEntry();   //close 1 entry (= 1 file)
			//mỗi entry Close thì sẽ tạo ra 1 check sum CRC
			log.debug("zip CRC32 1 =%X\n", entry.getCrc());
			
			zos.close();//kết thúc Zip file
			//baos.toByteArray()

		} catch(IOException e) {
			log.error("fail", e);
		}
		
		//================================= CRC 32 to compare with the above CRC32 of zip
    	//CRC32 giá trị ban đầu = FFFFFFFFFFFFFFFF = long = 8bytes = 64bit => 
		//tuy nhiên CRC32 chỉ dùng 4byte = 32 bit
		//giá trị tính toán này giống hệt với Zip ở trên
    	CRC32 crc1 = new CRC32();  //32bit check sum
    	crc1.update(byteArr1);
    	log.debug("****** CRC32 1 =%X\n", crc1.getValue());
    }
    

}
