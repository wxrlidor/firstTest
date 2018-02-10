package com.youtu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youtu.common.pojo.EUTreeNode;
import com.youtu.mapper.TbItemCatMapper;
import com.youtu.pojo.TbItemCat;
import com.youtu.pojo.TbItemCatExample;
import com.youtu.pojo.TbItemCatExample.Criteria;
import com.youtu.service.ItemCatService;

/**
 * @author:王贤锐
 * @date:2018年1月2日 下午4:52:38
 **/
@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	/**
	 * 商品类目展示：根据parentId，返回所有子节点数据
	 */
	@Override
	public List<EUTreeNode> getCatList(long parentId) {
		//创建查询条件
		TbItemCatExample tbItemCatExample = new TbItemCatExample();
		Criteria criteria = tbItemCatExample.createCriteria();
		// 添加查询条件parennId=?
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = tbItemCatMapper.selectByExample(tbItemCatExample);
		// 接着遍历结果，把数据转换成异步tree EUTreeNode格式数据并返回
		List<EUTreeNode> resultList = new ArrayList<>();// 返回的结果
		for (TbItemCat tbItemCat : list) {
			EUTreeNode node = new EUTreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			//数据中isParent 1代表是父节点  0不是父节点
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		return resultList;
	}

}
