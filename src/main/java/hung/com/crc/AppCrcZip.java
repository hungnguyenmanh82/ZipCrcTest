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
/**
CRC là giải thuật check sum để check toàn vẹn dữ liệu
 *
 */
public class AppCrcZip {

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
			System.out.format("the initial CRC32 1 =%X\n", entry.getCrc()); 
			
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
			System.out.format("zip CRC32 1 =%X\n", entry.getCrc());
			
			zos.close();//kết thúc Zip file
			//baos.toByteArray()

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		//================================= CRC 32 to compare with the above CRC32 of zip
    	//CRC32 giá trị ban đầu = FFFFFFFFFFFFFFFF = long = 8bytes = 64bit => 
		//tuy nhiên CRC32 chỉ dùng 4byte = 32 bit
		//giá trị tính toán này giống hệt với Zip ở trên
    	CRC32 crc1 = new CRC32();  //32bit check sum
    	crc1.update(byteArr1);
    	System.out.format("****** CRC32 1 =%X\n", crc1.getValue());
    }
    

}
