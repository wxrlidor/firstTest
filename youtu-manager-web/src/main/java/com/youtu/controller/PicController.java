package com.youtu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.youtu.common.utils.JsonUtils;
import com.youtu.service.PicService;

/**
 * 图片上传控制器
 *@author:王贤锐
 *@date:2018年1月3日  下午9:20:48
**/
@Controller
public class PicController {
	@Autowired
	private PicService picService;
	
	/**
	 * 接收图片对象参数，参数名需与common.js定义的一致
	 * @param uploadFile
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String pictureUpload(MultipartFile uploadFile){
		Map resultMap = picService.uploadPicture(uploadFile);
		//为了保证功能的兼容性，需要把Result转换成json格式的字符串。
		//JsonUtils工具也是在common工程中
		String json = JsonUtils.objectToJson(resultMap);
		return json;
	}
}
