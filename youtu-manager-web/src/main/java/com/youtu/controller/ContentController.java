package com.youtu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youtu.common.pojo.EUDataGridResult;
import com.youtu.common.pojo.YouTuResult;
import com.youtu.pojo.TbContent;
import com.youtu.service.ContentService;

/**
 * 首页分类管理
 *@author:王贤锐
 *@date:2018年1月17日  下午1:43:23
**/
@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;
	
	/**
	 * 根据categoryid取得内容列表
	 * @param page
	 * @param rows
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EUDataGridResult getContenListByCId(int page,int rows,long categoryId){
		return contentService.getContentListByCategoryId(categoryId, page, rows);
	}
	/**
	 * 新增内容
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public YouTuResult insertContent(TbContent tbContent){
		return contentService.insertContent(tbContent);
	}
	/**
	 * 更新内容
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public YouTuResult updateContent(TbContent tbContent){
		return contentService.updateContent(tbContent);
	}
	/**
	 * 删除内容
	 * @param ids
	 * @return
	 */
	@RequestMapping("/content/delete")
	@ResponseBody
	public YouTuResult deleteContentsByIds(String ids){
		return contentService.deleteContentsByIds(ids);
	}
}
