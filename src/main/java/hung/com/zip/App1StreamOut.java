package hung.com.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class App1StreamOut {

	public static void main(String[] args) {
		String s = "hello world";

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
			//==== entry option
			//entry.setCrc(crc); //zip sẽ tạo ra giá trị này
//			entry.setCreationTime(time)
			zos.putNextEntry(entry); //add 1 file vao trong zip
			
			//===============data will be zipped here
			zos.write(s.getBytes()); //sẽ ghi vào ByteArrayOutputStream baos
			
			//entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
            //entry.setComment("Created by TheCodersCorner");
			zos.closeEntry();   //close 1 entry (= 1 file)
			//mỗi entry Close thì sẽ tạo ra 1 check sum CRC

			/* use more Entries to add more files
		     and use closeEntry() to close each file entry */

			//================================== bắt đầu của file zip 2=====
			entry = new ZipEntry("test2.txt");
			zos.putNextEntry(entry); //add 1 file vao trong zip
			//data will be zipped here
			zos.write(s.getBytes());
			zos.closeEntry();   //close 1 entry (= 1 file)
			
			
			//mỗi entry Close thì sẽ tạo ra 1 check sum
			
			zos.close();//kết thúc Zip file
			//baos.toByteArray()


		} catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}

}
