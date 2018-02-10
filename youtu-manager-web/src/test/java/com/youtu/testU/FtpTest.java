package com.youtu.testU;
/**
 * ftp测试类
 *@author:王贤锐
 *@date:2018年1月3日  下午4:37:17
**/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import com.youtu.common.utils.FtpUtil;

public class FtpTest {
     @Test
     public void FtpTestC(){
    	 //创建FTP客户端对象
    	 FTPClient ftpClient = new FTPClient();
    	 try {
    		 //创建连接
			ftpClient.connect("192.168.168.121",21);
			//登录ftp服务器，使用用户名和密码
			ftpClient.login("ftpuser", "19951228");
			//读取本地文件
			FileInputStream fileInputStream = new FileInputStream(new File("G:\\商城图片\\TB1EBA9nrsTMeJjy1zbXXchlVXa_!!0-item_pic.jpg_430x430q90.jpg"));
			//设置ftp上传格式为二进制，默认为文本格式
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			//这是上传文件的存放路径
			ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");
			//上传文件 第一个参数:保存的文件名称
			//第二个参数：文件的IO流对象
			ftpClient.storeFile("hello2.jpg", fileInputStream);
			//关闭连接
			ftpClient.logout();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
     }
     @Test
 	public void testFtpUtil() throws Exception {
 		FileInputStream inputStream = new FileInputStream(new File("G:\\商城图片\\TB1EBA9nrsTMeJjy1zbXXchlVXa_!!0-item_pic.jpg_430x430q90.jpg"));
 		FtpUtil.uploadFile("192.168.168.121", 21, "ftpuser", "19951228", "/home/ftpuser/www/images", "/2015/09/04", "hello22.jpg", inputStream);
 		
 	}
}
