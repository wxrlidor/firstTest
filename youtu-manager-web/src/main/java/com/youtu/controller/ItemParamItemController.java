package com.youtu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.youtu.service.ItemParamItemService;

/**
 * 展示商品规格参数
 *@author:王贤锐
 *@date:2018年1月12日  上午10:39:11
**/
@Controller
public class ItemParamItemController {
	@Autowired
	private ItemParamItemService itemParamItemService;
	
	/**
	 * 展示商品规格参数，把html片段写入model对象中，页面中使用${itemParam}获取数据
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping("/item/queryParam/{itemId}")
	public String getItemParamByItemId(@PathVariable long itemId,Model model){
		String result = itemParamItemService.getItemParamByItemId(itemId);
		model.addAttribute("itemParam", result);
		return "itemParamTest";
	}
}
