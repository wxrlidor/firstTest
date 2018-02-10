package com.youtu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youtu.common.utils.JsonUtils;
import com.youtu.mapper.TbItemParamItemMapper;
import com.youtu.pojo.TbItemParamItem;
import com.youtu.pojo.TbItemParamItemExample;
import com.youtu.pojo.TbItemParamItemExample.Criteria;
import com.youtu.service.ItemParamItemService;

/**
 * @author:王贤锐
 * @date:2018年1月12日 上午10:33:01
 **/
@Service
public class ItemParamItemServiceImpl implements ItemParamItemService {
	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String getItemParamByItemId(long itemId) {
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
		if (list == null || list.size() == 0) {// 不存在则返回空
			return "";
		}
		// 取规格参数信息
		TbItemParamItem itemParamItem = list.get(0);
		//取出json字符串
		String paramData = itemParamItem.getParamData();
		// 把规格参数json数据转换成java对象，便于根据键值对取数据
		List<Map> jsonList = JsonUtils.jsonToList(paramData, Map.class);
		StringBuffer sb = new StringBuffer();
		sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
		sb.append(" <tbody>\n");
		//通过双重遍历来拼接html
		for (Map m1 : jsonList) {
			sb.append("        <tr>\n");
			sb.append("            <th class=\"tdTitle\" colspan=\"2\">" + m1.get("group") + "</th>\n");
			sb.append("        </tr>\n");
			List<Map> list2 = (List<Map>) m1.get("params");
			for (Map m2 : list2) {
				sb.append("        <tr>\n");
				sb.append("            <td class=\"tdTitle\">" + m2.get("k") + "</td>\n");
				sb.append("            <td>" + m2.get("v") + "</td>\n");
				sb.append("        </tr>\n");
			}
		}
		sb.append("    </tbody>\n");
		sb.append("</table>");
		return sb.toString();
	}

}
