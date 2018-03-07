package hung.com.zip;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class App3NioReadFile {

	public static void main(String[] args) throws IOException{
		String s = "hello world";

		//đoc "testZip.txt" và zip lại vào file "hello-world.zip"
		//tên file trong zip là "test.txt"
		byte[] byteArr = readFile2ByteArray("testZip.txt");


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
			//=========== data will be zipped here
			zos.write(byteArr);  //sẽ ghi vào "bos" BufferedOutputStream
			
			//entry.setCreationTime(FileTime.fromMillis(file.toFile().lastModified()));
            //entry.setComment("Created by TheCodersCorner");
			zos.closeEntry();   //close 1 entry (= 1 file)
			//mỗi entry Close thì sẽ tạo ra 1 check sum CRC

			/* use more Entries to add more files
		     and use closeEntry() to close each file entry */	  
			System.out.println("zip successfull");
			zos.close();

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
