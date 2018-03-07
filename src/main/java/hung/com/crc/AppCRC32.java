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
public class AppCRC32 {

    public static void main(String[] args) throws IOException {
    	byte[] byteArr1 = new String("ko gi ngheo bang thieu tai").getBytes();
    	byte[] byteArr2 = new String("ko gi hen bang thieu chi").getBytes();
    	
    	//CRC32 giá trị ban đầu = FFFFFFFFFFFFFFFF = long = 8bytes = 64bit => 
		//tuy nhiên CRC32 chỉ dùng 4byte = 32 bit
    	CRC32 crc1 = new CRC32();  //32bit check sum
    	crc1.update(byteArr1);
    	System.out.format("CRC32 1 =%X\n", crc1.getValue());
    	//cộng thêm vào check sum cũ
    	crc1.update(byteArr2);
    	System.out.format("CRC32 1+2 =%X\n", crc1.getValue());
    	
    	crc1.reset(); //xóa bỏ Checksum cũ đi => tính lại checksum từ đầu
    	crc1.update(byteArr2);
    	System.out.format("CRC32 2 =%X\n", crc1.getValue());
    	//======================================
    	CRC32 crc2 = new CRC32();
    	System.out.format("***CRC32 2 =%X\n", crc1.getValue());

    }

}
