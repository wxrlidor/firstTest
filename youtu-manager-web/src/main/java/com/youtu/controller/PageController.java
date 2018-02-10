package com.youtu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 专门用于跳转页面的控制器
 *@author:王贤锐
 *@date:2018年1月1日  上午10:31:33
**/
@Controller
public class PageController {
	/**
	 * 跳转到首页，配置请求路径为 /，也就是说只要访问项目 http://localhost:8080/youtuManager 
	 * 就会触发这个控制器
	 * @return
	 */
	@RequestMapping("/")
	public String showIndex(){
		//当控制器的方法返回参数是String类型时，系统会认为是跳转到某个页面
		//怎么确定路径？就是根据springmvc.xml里面配置的视图解析器去确定的
		return "index";
	}
	/**
	 * 展示页面，根据参数请求对应的页面
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
}
