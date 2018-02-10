package com.youtu.service;
/**
 *@author:王贤锐
 *@date:2018年1月3日  下午8:51:46
**/

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface PicService {
	Map uploadPicture(MultipartFile uploadFile);
}
