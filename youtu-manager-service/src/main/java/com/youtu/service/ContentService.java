package com.youtu.service;
/**
 *@author:王贤锐
 *@date:2018年1月17日  下午1:34:28
**/

import com.youtu.common.pojo.EUDataGridResult;
import com.youtu.common.pojo.YouTuResult;
import com.youtu.pojo.TbContent;

public interface ContentService {
	EUDataGridResult getContentListByCategoryId(long categryId,int page, int rows);
	
	YouTuResult insertContent(TbContent tbContent);
	
	YouTuResult updateContent(TbContent tbContent);
	
	YouTuResult deleteContentsByIds(String ids);
}
