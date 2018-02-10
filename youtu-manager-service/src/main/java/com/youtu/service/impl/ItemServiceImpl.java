package com.youtu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youtu.common.pojo.EUDataGridResult;
import com.youtu.common.pojo.YouTuResult;
import com.youtu.common.utils.IDUtils;
import com.youtu.mapper.TbItemCatMapper;
import com.youtu.mapper.TbItemDescMapper;
import com.youtu.mapper.TbItemMapper;
import com.youtu.mapper.TbItemParamItemMapper;
import com.youtu.pojo.TbItem;
import com.youtu.pojo.TbItemCat;
import com.youtu.pojo.TbItemCatExample;
import com.youtu.pojo.TbItemDesc;
import com.youtu.pojo.TbItemDescExample;
import com.youtu.pojo.TbItemExample;
import com.youtu.pojo.TbItemExample.Criteria;
import com.youtu.pojo.TbItemParamItem;
import com.youtu.pojo.TbItemParamItemExample;
import com.youtu.service.ItemService;

/**
 * 商品相关业务实现类
 * 
 * @author 王贤锐
 * @date :2017年12月31日 下午2:15:27
 */
@Service
public class ItemServiceImpl implements ItemService {
	// 注入mapper
	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbItemDescMapper tbItemDescMapper;

	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;

	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	@Override
	public TbItem getItemById(long itemId) {
		// 可以使用mapper提供的方法直接查询
		// TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		// 也可以使用自定义查询条件，需要先生成example对象，再生成cire
		TbItemExample tbItemExample = new TbItemExample();
		Criteria criteria = tbItemExample.createCriteria();
		criteria.andIdEqualTo(itemId);
		// 返回的是一个list
		List<TbItem> list = tbItemMapper.selectByExample(tbItemExample);
		// 如果list的长度大于0，说明查询有结果,返回查询结果pojo对象
		if (list != null && list.size() > 0) {
			TbItem item = list.get(0);
			return item;
		} else {
			return null;
		}
	}

	/**
	 * 查询商品列表
	 */
	@Override
	public EUDataGridResult getItemList(int page, int rows,Long id,String title) {
		// 使用分页查询商品列表
		TbItemExample tbItemExample = new TbItemExample();
		Criteria criteriaItem = tbItemExample.createCriteria(); //这里过滤掉已经删除的
		criteriaItem.andStatusNotEqualTo((byte) 3);
		//判断id和title过滤条件是否为空
		if(id !=null){
			//添加id过滤条件
			criteriaItem.andIdEqualTo(id);
		}
		if(!StringUtils.isBlank(title)){
			//添加商品标题模糊查询,字符串两边加上%代表模糊查询
			title="%" + title +"%";
			criteriaItem.andTitleLike(title);
		}
		PageHelper.startPage(page, rows);
		List<TbItem> list = tbItemMapper.selectByExample(tbItemExample);
		for (TbItem tbItem : list) {
			// 根据catId去查询出商品分类的信息，取出名称在加入到原本的数据中，最后展示在页面上
			TbItemCatExample example = new TbItemCatExample();
			com.youtu.pojo.TbItemCatExample.Criteria criteria = example.createCriteria();
			criteria.andIdEqualTo(tbItem.getCid());
			List<TbItemCat> catlist = tbItemCatMapper.selectByExample(example);
			if (catlist != null && catlist.size() > 0) {
				tbItem.setCatName(catlist.get(0).getName());
			}
		}
		// 取出总数
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long sum = pageInfo.getTotal();
		// 将查询到的信息保存到返回的pojo对象中
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		result.setTotal(sum);
		return result;
	}

	/**
	 * 修改商品状态 参数status表示要修改的状态 1-正常，2-下架，3-删除
	 */
	@Override
	public YouTuResult modifyItemsByIds(String ids, byte status) {
		YouTuResult youTuResult = new YouTuResult(null);
		// 传进来的ids格式为"12323,3434,3232",有多个id时以","隔开
		// 这里使用split方法分割成字符串数组
		String[] idArray = ids.split(",");
		List<Long> list = new ArrayList<>();
		// 遍历字符串数组，拷贝到list中去，因为criteria的查询条件只支持list参数
		for (int i = 0; i < idArray.length; i++) {
			// 这里需要用Long.parseLong(String)方法把字符串变量转换Long变量
			list.add(Long.parseLong(idArray[i]));
		}
		// 创建查询条件
		TbItemExample tbItemExample = new TbItemExample();
		Criteria criteria = tbItemExample.createCriteria();
		// 这里相当于添加查询语句 id in ('123232','232323','121212'),可以根据多个id查询出多个结果
		criteria.andIdIn(list);
		List<TbItem> itemList = tbItemMapper.selectByExample(tbItemExample);
		// 遍历查询出来的结果，修改为删除
		for (TbItem tbItem : itemList) {
			// 设置status属性为3，即删除
			tbItem.setStatus(status);
			// 同时设置更新时间
			tbItem.setUpdated(new Date());
			// 然后再进行保存操作,更新方法会返回值,成功时返回受影响的行数
			int result = tbItemMapper.updateByPrimaryKey(tbItem);
			if (result < 1) {// 说明更新失败,返回状态设置为非200
				youTuResult.setStatus(100);
			}
		}
		return youTuResult;
	}

	/**
	 * 修改商品信息
	 */
	@Override
	public YouTuResult updateItem(TbItem tbItem, String desc, String itemParams, String itemParamId) throws Exception {
		// 修改更新时间
		tbItem.setUpdated(new Date());
		tbItem.setCreated(new Date());
		// 根据主键id，更新商品信息
		tbItemMapper.updateByPrimaryKey(tbItem);

		// 更新商品描述
		TbItemDesc tbItemDesc = new TbItemDesc();
		// 补全描述实体类
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setItemId(tbItem.getId());
		tbItemDesc.setUpdated(new Date());
		// 根据主键id更新
		tbItemDescMapper.updateByPrimaryKeyWithBLOBs(tbItemDesc);

		// 更新商品规格参数
		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		// 补全规格参数实体类
		tbItemParamItem.setParamData(itemParams);
		tbItemParamItem.setId(Long.valueOf(itemParamId));
		tbItemParamItem.setItemId(tbItem.getId());
		tbItemParamItem.setUpdated(new Date());
		// 根据主键id更新
		tbItemParamItemMapper.updateByPrimaryKeyWithBLOBs(tbItemParamItem);

		return YouTuResult.ok();
	}

	/**
	 * 新增商品
	 * 
	 * @throws Exception
	 */
	@Override
	public YouTuResult createItem(TbItem tbItem, String desc, String itemParams) throws Exception {
		// 先补全tbItem
		tbItem.setId(IDUtils.genItemId());
		tbItem.setStatus((byte) 1);
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		// 保存到数据库中
		tbItemMapper.insert(tbItem);
		// 添加商品描述
		YouTuResult result = addDesc(tbItem.getId(), desc);
		if (result.getStatus() != 200) {
			// 这里抛出异常，spring就会知道出错，进行事务回滚
			throw new Exception();
		}
		// 保存规格参数
		result = insertItemParamItem(tbItem.getId(), itemParams);
		if (result.getStatus() != 200) {
			// 这里抛出异常，spring就会知道出错，进行事务回滚
			throw new Exception();
		}
		// 返回结果
		return YouTuResult.ok();
	}

	/**
	 * 添加商品描述
	 * 
	 * @param tbitem
	 * @param desc
	 * @return
	 */
	private YouTuResult addDesc(long itemId, String desc) {
		// 创建pojo对象，补全信息
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setUpdated(new Date());
		tbItemDescMapper.insert(tbItemDesc);
		return YouTuResult.ok();
	}

	/**
	 * 保存规格参数
	 * 
	 * @param itemId
	 * @param itemParams
	 * @return
	 */
	private YouTuResult insertItemParamItem(long itemId, String itemParams) {
		// 创建pojo对象，补全信息
		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		tbItemParamItem.setItemId(itemId);
		tbItemParamItem.setParamData(itemParams);
		tbItemParamItem.setCreated(new Date());
		tbItemParamItem.setUpdated(new Date());
		tbItemParamItemMapper.insert(tbItemParamItem);

		return YouTuResult.ok();

	}

	/**
	 * 查询商品描述
	 */
	@Override
	public YouTuResult getDescByItemId(long itemId) {
		TbItemDescExample example = new TbItemDescExample();
		com.youtu.pojo.TbItemDescExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemDesc> list = tbItemDescMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			// 把查询到的描述文本保存到返回结果中
			return YouTuResult.ok(list.get(0));
		}
		return YouTuResult.ok();
	}

	/**
	 * 加载商品规格参数
	 */
	@Override
	public YouTuResult getParmaItemByItemId(long itemId) {
		TbItemParamItemExample example = new TbItemParamItemExample();
		com.youtu.pojo.TbItemParamItemExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			// 把查询到的描述文本保存到返回结果中
			return YouTuResult.ok(list.get(0));
		}
		return YouTuResult.ok();
	}

}
