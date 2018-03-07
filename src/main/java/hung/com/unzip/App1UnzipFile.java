package hung.com.unzip;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class App1UnzipFile {

    public static void main(String[] args) throws IOException {
    	//file cần unzip
        String fileZip = "compressed.zip";
        byte[] buffer = new byte[1024];
        
        //unzip socket tương tự
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        
        //lấy file đầu tiên trong zip file
        ZipEntry zipEntry = zis.getNextEntry();
         
        
        //trong file zip chứa nhiều files
        while(zipEntry != null){
        	//get CRC32 to check
        	long crc32 = zipEntry.getCrc();
        	System.out.format("CRC32=%X\n",crc32);
        	//
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
}
