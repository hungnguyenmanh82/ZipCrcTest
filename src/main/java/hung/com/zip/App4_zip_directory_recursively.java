package hung.com.zip;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
http://www.baeldung.com/java-compress-and-uncompress
 *
 */

/**
 * code này đã test chạy tốt. Lúc nào dùng thì bỏ các dòng log.debug() đi là ok
 */
public class App4_zip_directory_recursively {
	private static Logger log = LogManager.getLogger(); 
	

    public static void main(String[] args) throws IOException {
    	//zipTestDirectory: là folder path chứa cả file và folder nhiều cấp
        String fileToZipPath = "zipTestDirectory";
        //
        FileOutputStream fos = new FileOutputStream("dirCompressed.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
		//zip configuration
        zipOut.setMethod(ZipOutputStream.DEFLATED);
        zipOut.setLevel(9); //level for ZipOutputStream.DEFLATED

		//============================	
        File fileToZip = new File(fileToZipPath);
        
        zipFile(fileToZip, fileToZip.getName(), zipOut); // .getName() = name ko gồm path
        zipOut.close();
        fos.close();
        
        log.debug("zip folder successfully");
    }
 
    /**
     * @param fileToZip: nếu là folder thì nó sẽ gọi hàm này recursively
     */
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        
        // if it is a folder then execute recursively
        if (fileToZip.isDirectory()) {
        	log.debug("zip folder: " + fileName);
        	
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
            	//recursively
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        
        // if it is a file
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName); //fileName = relative path = "directory/name"
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
        log.debug("zip file: " + fileName);
    }


}
