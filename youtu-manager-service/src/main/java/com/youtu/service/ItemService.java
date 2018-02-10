package com.youtu.service;

import com.youtu.common.pojo.EUDataGridResult;
import com.youtu.common.pojo.YouTuResult;

/**
 * 商品相关业务接口
 * @author 王贤锐
 * @date :2017年12月31日  下午2:12:33
 */

import com.youtu.pojo.TbItem;

public interface ItemService {
	TbItem getItemById(long ItemId);

	EUDataGridResult getItemList(int page, int rows,Long id,String title);
	
	YouTuResult modifyItemsByIds(String ids,byte status);
	
	YouTuResult createItem(TbItem tbItem,String desc,String itemParams)  throws Exception ;
	
	YouTuResult updateItem(TbItem tbItem,String desc,String itemParams,String itemParamId)  throws Exception ;
	
	YouTuResult getDescByItemId(long itemId);
	
	YouTuResult getParmaItemByItemId(long itemId);
}
