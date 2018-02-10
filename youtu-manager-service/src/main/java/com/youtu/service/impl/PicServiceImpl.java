package com.youtu.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.youtu.common.utils.FtpUtil;
import com.youtu.common.utils.IDUtils;
import com.youtu.service.PicService;

/**
 * 图片上传服务
 * 
 * @author:王贤锐
 * @date:2018年1月3日 下午8:52:42
 **/
@Service
public class PicServiceImpl implements PicService {
	// 使用注解引入资源配置变量
	@Value("${FTP_ADDRESS}")
	private String FTP_ADDRESS;
	@Value("${FTP_PORT}")
	private Integer FTP_PORT;
	@Value("${FTP_USERNAME}")
	private String FTP_USERNAME;
	@Value("${FTP_PASSWORD}")
	private String FTP_PASSWORD;
	@Value("${FTP_BASE_PATH}")
	private String FTP_BASE_PATH;
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;

	/**
	 * 接收图片对象，通过ftp保存到图片服务器，并返回执行结果给controller
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map uploadPicture(MultipartFile uploadFile) {
		Map resultMap = new HashMap<>();//返回的结果信息
		//生成新的随机文件名，避免文件名重复
		//1.取得源文件名的后缀
		String oldName = uploadFile.getOriginalFilename();
		oldName = oldName.substring(oldName.lastIndexOf("."));
		//2.使用工具类生成新文件名
		String newName = IDUtils.genImageName();
		//3.合成新文件名
		newName = newName + oldName;
		//取得文件的存储路径，按照日期存/2017/09/03
		String filePath = new DateTime().toString("/yyyy/MM/dd");
		//上传文件 ,会返回一个result
		try {
			boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASE_PATH, filePath,
					newName, uploadFile.getInputStream());
			//根据结果返回信息
			if(!result){//失败
				resultMap.put("error", 1);
				resultMap.put("message", "文件上传失败，请联系管理员");
			}else{
				resultMap.put("error", 0);
				//返回图片的url，用于访问图片
				resultMap.put("url", IMAGE_BASE_URL+filePath+"/"+newName);
			}
		} catch (IOException e) {
			resultMap.put("error", 1);
			resultMap.put("message", "文件上传失败，请联系管理员");
			e.printStackTrace();
		}
		return resultMap;
	}

}
