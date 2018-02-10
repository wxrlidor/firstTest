package com.youtu.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youtu.common.pojo.EUDataGridResult;
import com.youtu.common.pojo.YouTuResult;
import com.youtu.pojo.TbItemParam;
import com.youtu.service.ItemParamService;

/**
 * 商品规格参数控制器
 *@author:王贤锐
 *@date:2018年1月11日  下午8:28:29
**/
@Controller
public class ItemParamController {

	@Autowired
	private ItemParamService itemParamService;
	
	/**
	 * 查询出规格参数模版列表,传入itemCatId作为过滤条件，默认为0，既没有查询条件
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/item/param/list")
	@ResponseBody
	public EUDataGridResult getItemParamList(int page,int rows,long itemCatId){
		EUDataGridResult result = itemParamService.getItemParamList(page, rows,itemCatId);
		return result;
	}
	
	/**
	 * 查询该类目是否已经有规格参数模版
	 * @param itemCatId
	 * @return
	 */
	@RequestMapping("/item/param/query/itemcatid/{itemCatId}")
	@ResponseBody
	public YouTuResult getItemParamByCid(@PathVariable long itemCatId){
		YouTuResult result = itemParamService.getItemParamByItemCatId(itemCatId);
		return result;
	}
	/**
	 * 规格参数的保存，参数为字符串格式的json数据
	 * @param catId
	 * @param paramData
	 * @return
	 */
	@RequestMapping("/item/param/save/{catId}")
	@ResponseBody
	public YouTuResult insertItemParam(@PathVariable long catId,String paramData){
		//生成一个pojo对象
		TbItemParam tbItemParam = new TbItemParam();
		tbItemParam.setItemCatId(catId);
		tbItemParam.setParamData(paramData);
		YouTuResult result = itemParamService.insertItemParam(tbItemParam);
		return result;
	}
	/**
	 * 一次性删除多个规格参数模版
	 * @param ids
	 * @return
	 */
	@RequestMapping("/item/param/delete")
	@ResponseBody
	public YouTuResult deleteItmParamByIds(String ids){
		return itemParamService.deleteItemParamByIds(ids);
	}
	/**
	 * 更新规格参数模板，接收参数，补全pojo，调用service层
	 * @param catId
	 * @param paramData
	 * @return
	 */
	@RequestMapping("/item/param/update/{id}/{catId}")
	@ResponseBody
	public YouTuResult updateItemParam(@PathVariable long catId,@PathVariable Long id,String paramData){
		//生成一个pojo对象
		TbItemParam tbItemParam = new TbItemParam();
		tbItemParam.setId(id);
		tbItemParam.setItemCatId(catId);
		tbItemParam.setParamData(paramData);
		tbItemParam.setUpdated(new Date());
		YouTuResult result = itemParamService.updateItemParam(tbItemParam);
		return result;
	}
}
