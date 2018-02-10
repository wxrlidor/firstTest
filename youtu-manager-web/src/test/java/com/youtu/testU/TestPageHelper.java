package com.youtu.testU;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youtu.mapper.TbItemMapper;
import com.youtu.pojo.TbItem;
import com.youtu.pojo.TbItemExample;

/**
 * 分页插件测试
 *@author:王贤锐
 *@date:2018年1月1日  下午3:07:57
**/
public class TestPageHelper {
	@SuppressWarnings("resource")
	@Test
	public void testPageHelper(){
		//初始化一个spring容器,后面参数要写spring配置文件的路径
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		//取得TbItemMapper对象
		TbItemMapper mapper = applicationContext.getBean(TbItemMapper.class);
		TbItemExample tbItemExample = new TbItemExample();
		//在执行SQL语句之前使用分页插件加入分页语句
		PageHelper.startPage(1, 7);
		//查询数据,这里已经加入了分页查询，全部的查询语句相当于
		//select * from tb_item limit 0,7
		List<TbItem> list = mapper.selectByExample(tbItemExample);
		//遍历list,输出查询结果
		for (TbItem tbItem : list) {
			System.out.println(tbItem.getTitle());
		}
		//然后再把list格式化成PageInfo对象
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//取得所有的数据总数（数据库中的数据总数）
		long sum = pageInfo.getTotal();
		System.out.println("总数："+sum);
	}
}
