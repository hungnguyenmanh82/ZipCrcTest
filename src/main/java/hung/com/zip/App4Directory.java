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

/**
http://www.baeldung.com/java-compress-and-uncompress
 *
 */
public class App4Directory {

    public static void main(String[] args) throws IOException {
    	//"zipTestDirectory" is a recursive folder
        String sourceFile = "zipTestDirectory";
        FileOutputStream fos = new FileOutputStream("dirCompressed.zip");
        //=========================
        ZipOutputStream zipOut = new ZipOutputStream(fos);
		//==================== zip configuration
        zipOut.setMethod(ZipOutputStream.DEFLATED);
        zipOut.setLevel(9); //level for ZipOutputStream.DEFLATED

		//============================	
        File fileToZip = new File(sourceFile);
        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }
 
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
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
