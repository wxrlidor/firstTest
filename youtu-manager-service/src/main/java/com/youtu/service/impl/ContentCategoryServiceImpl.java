package com.youtu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.youtu.common.pojo.EUTreeNode;
import com.youtu.common.pojo.YouTuResult;
import com.youtu.common.utils.HttpClientUtil;
import com.youtu.mapper.TbContentCategoryMapper;
import com.youtu.pojo.TbContentCategory;
import com.youtu.pojo.TbContentCategoryExample;
import com.youtu.pojo.TbContentCategoryExample.Criteria;
import com.youtu.service.ContentCategoryService;

/**
 * 内容分类管理
 *@author:王贤锐
 *@date:2018年1月15日  下午9:53:01
**/
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper; 
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_CONTENT_SYNC_URL}")
	private String REST_CONTENT_SYNC_URL;
	/**
	 * 获取内容分类管理的树形列表
	 * @param parentId
	 * @return
	 */
	@Override
	public List<EUTreeNode> getContentCategoryList(long parentId) {
		//根据parentId查询
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//状态。可选值:1(正常),2(删除)
		//过滤掉删除的信息
		criteria.andStatusNotEqualTo(2);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		List<EUTreeNode> result = new ArrayList<>();
		//遍历查询结果，拼装成树形结构数据返回
		for (TbContentCategory tbContentCategory : list) {
			EUTreeNode node =new EUTreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			
			result.add(node);
		}
		return result;
	}
	/**
	 * 新增内容分类,并返回新增的pojo对象
	 */
	@Override
	public YouTuResult insertCategory(long parentId, String name) {
		//创建pojo
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setIsParent(false);
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		//状态。可选值:1(正常),2(删除)
		tbContentCategory.setStatus(1);
		//添加分类,同时取得返回的id更新到pojo中,这个是自动返回添加到pojo对象中
		tbContentCategoryMapper.insert(tbContentCategory);
		//更新父节点的isparent属性，先判断是否是false
		TbContentCategory parentCateory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parentCateory.getIsParent()){
			//更新父节点
			parentCateory.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(parentCateory);
		}
	
		return YouTuResult.ok(tbContentCategory);
	}
	/**
	 * 删除结点
	 */
	@Override
	public YouTuResult deleteCategory( long id) {
		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
		long parentId = category.getParentId();
		if(category.getIsParent()){
			//如果是父节点，就不能删除
			YouTuResult result = new YouTuResult();
			result.setStatus(111);
			return result;
		}
		// 根据id删除结点
		tbContentCategoryMapper.deleteByPrimaryKey(id);
		//判断父节点下面是否还有子节点，没有的话就把父节点的isparent改为false
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		if(list == null || list.size() ==0){
			//不存在子节点，更新父节点
			TbContentCategory parentCat = tbContentCategoryMapper.selectByPrimaryKey(parentId);
			parentCat.setIsParent(false);
			tbContentCategoryMapper.updateByPrimaryKey(parentCat);
		}
		
		//redis同步缓存
		try {
			HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return YouTuResult.ok();
	}
	/**
	 * 更新结点
	 */
	@Override
	public YouTuResult updateCateory(long id, String name) {
		// 根据id查询出pojo对象
		TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
		tbContentCategory.setName(name);
		//更新结点名称
		tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
		return YouTuResult.ok();
	}

}
