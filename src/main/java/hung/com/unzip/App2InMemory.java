package hung.com.unzip;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class App2InMemory {

    public static void main(String[] args) throws IOException {
    	//================== Lưu thông tin vào memory
		byte[] byteArr = readFile2ByteArray("hello-world.zip");

    	ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArr);
        byte[] buffer = new byte[1024];
        
        //unzip socket tương tự
        ZipInputStream zis = new ZipInputStream(byteArrayInputStream);
        
        //lấy file đầu tiên trong zip file
        ZipEntry zipEntry = zis.getNextEntry();
        
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            File newFile = new File("unzipTest/" + fileName);
            //tạo file để chứa nội dung unzip của file trong zip file
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            //đọc cho đến hết file
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
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
