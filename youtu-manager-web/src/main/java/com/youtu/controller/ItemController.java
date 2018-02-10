package com.youtu.controller;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youtu.common.pojo.EUDataGridResult;
import com.youtu.common.pojo.YouTuResult;
import com.youtu.pojo.TbItem;
import com.youtu.service.ItemService;

/**
 * 商品相关控制器类
 *@author:王贤锐
 *@date:2017年12月31日  下午2:16:00
**/
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	/**
	 * 传入一个商品id，返回一个json格式的商品pojo对象，使用PathVariable注解来获得url参数
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	/**
	 * 查询出所有商品信息，展示商品列表
	 * id和title是过滤条件
	 * @param page
	 * @param rows
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EUDataGridResult getItemList(int page,int rows,Long id,String title) throws UnsupportedEncodingException{
		//解决乱码问题，tomcat默认是iso8859-1的编码
		if(!StringUtils.isBlank(title)){
			title = new String(title.getBytes("iso8859-1"), "utf-8");
		}
		EUDataGridResult result = itemService.getItemList(page, rows,id,title);
		return result;
	}
	/**
	 * 根据ids一次性删除多个商品  商品状态，1-正常，2-下架，3-删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public YouTuResult deleteItemByIds(String ids){
		YouTuResult result = itemService.modifyItemsByIds(ids,(byte) 3);
		return result;
	}
	/**
	 * 根据ids一次性下架多个商品  商品状态，1-正常，2-下架，3-删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public YouTuResult instockItemByIds(String ids){
		YouTuResult result = itemService.modifyItemsByIds(ids,(byte) 2);
		return result;
	}
	/**
	 * 根据ids一次性上架多个商品  商品状态，1-正常，2-下架，3-删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public YouTuResult reshelfItemByIds(String ids){
		YouTuResult result = itemService.modifyItemsByIds(ids,(byte) 1);
		return result;
	}
	/**
	 * 新增商品,同时保存规格参数的数据
	 * @param item
	 * @param desc
	 * @param itemParams
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	private YouTuResult createItem(TbItem item,String desc,String itemParams)  throws Exception {
		YouTuResult result = itemService.createItem(item,desc,itemParams);
		return result;
	}
	/**
	 * 修改商品信息
	 * @param item   商品实体类
	 * @param desc   商品描述
	 * @param itemParams  商品规格参数json数据
	 * @param itemParamId  规格参数主键id
	 * @param id   商品id
	 * @param status  商品状态
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/rest/item/update", method=RequestMethod.POST)
	@ResponseBody
	private YouTuResult updateItem(TbItem item,String desc,String itemParams,String itemParamId,String id,String status)  throws Exception {
		//先补全商品的id和状态，这里用到类型转换
		item.setId(Long.valueOf(id));
		item.setStatus((byte)Integer.valueOf(status).intValue());
		YouTuResult result = itemService.updateItem(item,desc,itemParams,itemParamId);
		return result;
	}
	/**
	 * 根据商品id加载商品描述信息
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	private YouTuResult getDescByItem(@PathVariable long itemId){
		return itemService.getDescByItemId(itemId);
	}
	/**
	 * 加载规格参数
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/rest/item/param/item/query/{itemId}")
	@ResponseBody
	private YouTuResult getParmaItemByItemId(@PathVariable long itemId){
		return itemService.getParmaItemByItemId(itemId);
	}
}
