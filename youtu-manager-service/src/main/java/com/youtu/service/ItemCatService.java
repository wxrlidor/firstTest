package com.youtu.service;
/**
 * 商品类目相关业务类
 *@author:王贤锐
 *@date:2018年1月2日  下午4:50:48
**/

import java.util.List;

import com.youtu.common.pojo.EUTreeNode;

public interface ItemCatService {
	List<EUTreeNode> getCatList(long parentId);
}
