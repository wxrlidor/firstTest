package com.youtu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youtu.common.pojo.EUTreeNode;
import com.youtu.common.pojo.YouTuResult;
import com.youtu.service.ContentCategoryService;

/**
 * 内容分类管理
 *@author:王贤锐
 *@date:2018年1月15日  下午10:04:18
**/
@Controller
@RequestMapping("/content/category")
public class ContentCatoryController {
	@Autowired
	private ContentCategoryService categoryService;
	/**
	 * 获取商品分类树形列表，需要有默认值0
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<EUTreeNode> getCatory(@RequestParam(value="id",defaultValue="0")Long parentId){
		return categoryService.getContentCategoryList(parentId); 
	}
	/**
	 * 新增内容分类管理
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public YouTuResult insertCateory(Long parentId,String name){
		return categoryService.insertCategory(parentId, name);
	}
	/**
	 * 删除结点
	 * @param parentId
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public YouTuResult deleteCateory(Long id){
		return categoryService.deleteCategory(id);
	}
	/**
	 * 更新结点
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public YouTuResult updateCategory(Long id,String name){
		return categoryService.updateCateory(id, name);
	}
}
