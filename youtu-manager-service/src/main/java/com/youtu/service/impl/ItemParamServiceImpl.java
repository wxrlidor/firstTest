package com.youtu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youtu.common.pojo.EUDataGridResult;
import com.youtu.common.pojo.YouTuResult;
import com.youtu.mapper.TbItemCatMapper;
import com.youtu.mapper.TbItemParamMapper;
import com.youtu.pojo.TbItemCat;
import com.youtu.pojo.TbItemCatExample;
import com.youtu.pojo.TbItemParam;
import com.youtu.pojo.TbItemParamExample;
import com.youtu.pojo.TbItemParamExample.Criteria;
import com.youtu.service.ItemParamService;

/**
 * 商品规格参数业务层
 * 
 * @author:王贤锐
 * @date:2018年1月11日 下午8:22:44
 **/
@Service
public class ItemParamServiceImpl implements ItemParamService {
	@Autowired
	private TbItemParamMapper tbItemParamMapper;

	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	/**
	 * 取得商品规格参数模板的列表
	 */
	@Override
	public EUDataGridResult getItemParamList(int page, int rows,Long itemCatId) {
		// 使用分页查询商品列表
		TbItemParamExample tbItemParamExample = new TbItemParamExample();
		//检查是否有传入catId作为查询条件
		if(itemCatId != 0){
			Criteria criteria = tbItemParamExample.createCriteria();
			criteria.andItemCatIdEqualTo(itemCatId);
		}
		PageHelper.startPage(page, rows);
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
		// 遍历每一个规格参数，把商品分类的名称添加进去
		for (TbItemParam tbItemParam : list) {
			// 根据catId去查询出商品分类的信息，取出名称在加入到原本的数据中，最后展示在页面上
			TbItemCatExample example = new TbItemCatExample();
			com.youtu.pojo.TbItemCatExample.Criteria criteria = example.createCriteria();
			criteria.andIdEqualTo(tbItemParam.getItemCatId());
			List<TbItemCat> catlist = tbItemCatMapper.selectByExample(example);
			if (catlist != null && catlist.size() > 0) {
				tbItemParam.setItemCatName(catlist.get(0).getName());
			}
		}
		// 取出总数
		PageInfo<TbItemParam> pageInfo = new PageInfo<>(list);
		long sum = pageInfo.getTotal();
		// 将查询到的信息保存到返回的pojo对象中
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		result.setTotal(sum);
		return result;
	}

	/**
	 * 根据商品分类id查询是否存在规格参数模版
	 */
	@Override
	public YouTuResult getItemParamByItemCatId(long itemCatId) {
		TbItemParamExample tbItemParamExample = new TbItemParamExample();
		Criteria criteria = tbItemParamExample.createCriteria();
		// 根据catId查询规格参数模版
		criteria.andItemCatIdEqualTo(itemCatId);
		// 因为取的数据中有大文本格式，所以需要使用selectByExampleWithBLOBs方法取才能取到大文本数据
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
		// 判断是否查询到数据,查询到则返回数据，否则返回无数据的结果
		if (list.size() > 0 && list != null) {
			return YouTuResult.ok(list.get(0));
		}
		return YouTuResult.ok();
	}

	/**
	 * 保存规格参数模板
	 */
	@Override
	public YouTuResult insertItemParam(TbItemParam tbItemParam) {
		// 补全信息，id是自动增长的
		tbItemParam.setCreated(new Date());
		tbItemParam.setUpdated(new Date());
		// 插入到规格参数模板表，返回结果
		int isSu = tbItemParamMapper.insert(tbItemParam);
		// 判断是否插入成功
		if (isSu > 0) {
			return YouTuResult.ok();
		}
		return null;
	}

	/**
	 * 根据id一次性删除多个规格参数模版
	 */
	@Override
	public YouTuResult deleteItemParamByIds(String ids) {
		// 传进来的ids格式为"12323,3434,3232",有多个id时以","隔开
		// 这里使用split方法分割成字符串数组
		String[] idArray = ids.split(",");
		List<Long> list = new ArrayList<>();
		// 遍历字符串数组，拷贝到list中去，因为criteria的查询条件只支持list参数
		for (int i = 0; i < idArray.length; i++) {
			// 这里需要用Long.parseLong(String)方法把字符串变量转换Long变量
			list.add(Long.parseLong(idArray[i]));
		}
		TbItemParamExample example = new TbItemParamExample();
		// 这里相当于添加查询语句 id in ('123232','232323','121212'),可以根据多个id删除多条数据
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(list);
		tbItemParamMapper.deleteByExample(example);
		return YouTuResult.ok();
	}
	/**
	 * 更新规格参数模板
	 */
	@Override
	public YouTuResult updateItemParam(TbItemParam tbItemParam) {
		tbItemParamMapper.updateByPrimaryKeyWithBLOBs(tbItemParam);
		
		return YouTuResult.ok();
	}

}
