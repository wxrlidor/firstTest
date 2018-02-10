package com.youtu.service;
/**
 *@author:王贤锐
 *@date:2018年1月15日  下午9:51:55
**/

import java.util.List;

import com.youtu.common.pojo.EUTreeNode;
import com.youtu.common.pojo.YouTuResult;

public interface ContentCategoryService {
	List<EUTreeNode> getContentCategoryList(long parentId);
	
	YouTuResult insertCategory(long parentId,String name);
	
	YouTuResult deleteCategory(long id);
	
	YouTuResult updateCateory(long id,String name);
}
