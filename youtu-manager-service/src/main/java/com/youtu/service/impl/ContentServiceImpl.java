package com.youtu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youtu.common.pojo.EUDataGridResult;
import com.youtu.common.pojo.YouTuResult;
import com.youtu.common.utils.HttpClientUtil;
import com.youtu.mapper.TbContentMapper;
import com.youtu.pojo.TbContent;
import com.youtu.pojo.TbContentExample;
import com.youtu.pojo.TbContentExample.Criteria;
import com.youtu.service.ContentService;

/**
 * 内容管理
 * 
 * @author:王贤锐
 * @date:2018年1月17日 下午1:36:34
 **/
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper tbContentMapper;
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_CONTENT_SYNC_URL}")
	private String REST_CONTENT_SYNC_URL;

	/**
	 * 根据内容分类id获取内容列表
	 */
	@Override
	public EUDataGridResult getContentListByCategoryId(long categryId, int page, int rows) {
		// 根据categoryId查询出数据
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categryId);
		// 分页查询
		PageHelper.startPage(page, rows);
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
		// 取出总数
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		long sum = pageInfo.getTotal();
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		result.setTotal(sum);
		return result;
	}

	/**
	 * 添加内容数据
	 */
	@Override
	public YouTuResult insertContent(TbContent tbContent) {
		// 补全pojo对象信息,主键自增
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		tbContentMapper.insert(tbContent);
		
		//redis同步缓存
		try {
			HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + tbContent.getCategoryId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return YouTuResult.ok();
	}

	/**
	 * 更新内容数据
	 */
	@Override
	public YouTuResult updateContent(TbContent tbContent) {
		// 补全pojo
		tbContent.setUpdated(new Date());
		tbContent.setCreated(new Date());
		// 更新数据，把大文本数据也更新进去
		tbContentMapper.updateByPrimaryKeyWithBLOBs(tbContent);
		
		// redis同步缓存
		try {
			String doGet = HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + tbContent.getCategoryId());
			System.out.println(doGet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return YouTuResult.ok();
	}

	/**
	 * 一次性删除多个内容数据
	 */
	@Override
	public YouTuResult deleteContentsByIds(String ids) {
		// 这里使用split方法分割成字符串数组
		String[] idArray = ids.split(",");
		List<Long> list = new ArrayList<>();
		// 遍历字符串数组，拷贝到list中去，因为criteria的查询条件只支持list参数
		for (int i = 0; i < idArray.length; i++) {
			// 这里需要用Long.parseLong(String)方法把字符串变量转换Long变量
			list.add(Long.parseLong(idArray[i]));
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		// 这里相当于添加查询语句 id in ('123232','232323','121212'),可以根据多个id查询出多个结果
		criteria.andIdIn(list);
		//先查询出值然后再查出分类id用于同步缓存
		List<TbContent> contentList = tbContentMapper.selectByExample(example);
		//根据id，删除多个数据
		int num = tbContentMapper.deleteByExample(example);
		if(num > 0 ){
			
			//redis同步缓存
			try {
				HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + contentList.get(0).getCategoryId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return YouTuResult.ok();
		}
		return YouTuResult.build(500, "删除出错");
	}

}
