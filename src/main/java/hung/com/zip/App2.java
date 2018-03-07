package hung.com.zip;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class App2 {

	public static void main(String[] args) throws IOException{
		String s = "hello world";

		//zip data sẽ ghi vào file "hello-world.zip"
		FileOutputStream fos = new FileOutputStream("hello-world.zip");
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		try(ZipOutputStream zos = new ZipOutputStream(bos)) {
			//==================== zip configuration
			zos.setMethod(ZipOutputStream.DEFLATED);
			zos.setLevel(9); //level for ZipOutputStream.DEFLATED

			//============================	
			/* File is not on the disk, test.txt indicates
		     only the file name to be put into the zip */
			ZipEntry entry = new ZipEntry("test.txt"); 

			zos.putNextEntry(entry);
			//data will be zipped here
			zos.write(s.getBytes());
			//entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
            //entry.setComment("Created by TheCodersCorner");
			zos.closeEntry();   //close 1 entry (= 1 file)
			//mỗi entry Close thì sẽ tạo ra 1 check sum CRC
			
			/* use more Entries to add more files
		     and use closeEntry() to close each file entry */

			//================================== bắt đầu của file thứ 2 đc zip=====
			entry = new ZipEntry("test2.txt"); 
			zos.putNextEntry(entry);
			//data will be zipped here
			zos.write(s.getBytes());
			zos.closeEntry();   //close 1 entry (= 1 file)
			//mỗi entry Close thì sẽ tạo ra 1 check sum CRC
			
			zos.close();//kết thúc Zip file

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}

}
