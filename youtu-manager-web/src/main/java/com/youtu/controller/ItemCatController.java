package com.youtu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youtu.common.pojo.EUTreeNode;
import com.youtu.service.ItemCatService;

/**
 * 商品类目控制器
 *@author:王贤锐
 *@date:2018年1月2日  下午5:07:17
**/
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 * 查询商品类目数据，传参为id,但是我们需要使用RequestParam注解来进行一个默认值的设置
	 * 为了确保一开始加载出第一层数据，默认parentId为0
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<EUTreeNode> getCatList(@RequestParam(value="id",defaultValue="0") long parentId){
		return itemCatService.getCatList(parentId);
	}
}
