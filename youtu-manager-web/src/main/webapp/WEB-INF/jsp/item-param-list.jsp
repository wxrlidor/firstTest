<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="easyui-panel" title="Nested Panel" data-options="width:'100%',minHeight:500,noheader:true,border:false" style="padding:10px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'west',split:false" style="width:250px;padding:5px">
            <ul id="itemCatTree" class="easyui-tree" data-options="url:'/item/cat/list',animate: true,method : 'GET'">
            </ul>
        </div>
        <div data-options="region:'center'" style="padding:5px">
            <table class="easyui-datagrid" id="itemParamList" title="规格参数模版列表" 
		       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/item/param/list',method:'get',pageSize:30,toolbar:itemParamListToolbar,queryParams:{itemCatId:0}">
		    <thead>
		        <tr>
		        	<th data-options="field:'ck',checkbox:true"></th>
		        	<th data-options="field:'id',width:60">ID</th>
		        	<th data-options="field:'itemCatId',width:80">商品类目ID</th>
		        	<th data-options="field:'itemCatName',width:100">商品类目</th>
		            <th data-options="field:'paramData',width:300,formatter:formatItemParamData">规格(只显示分组名称)</th>
		            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
		            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
		        </tr>
		    </thead>
		</table> 
        </div>
    </div>
</div>
<!-- <table class="easyui-datagrid" id="itemParamList" title="规格参数模版列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/item/param/list',method:'get',pageSize:30,toolbar:itemParamListToolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">ID</th>
        	<th data-options="field:'itemCatId',width:80">商品类目ID</th>
        	<th data-options="field:'itemCatName',width:100">商品类目</th>
            <th data-options="field:'paramData',width:300,formatter:formatItemParamData">规格(只显示分组名称)</th>
            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table> -->
<div id="itemParamEditWindow" class="easyui-window" title="编辑规格参数模板" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/item-param-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<script>
$(function(){
	//获取左边的类目树，对点击事件进行绑定
	//使得点击时改变传递的过滤条件
	var tree = $("#itemCatTree");
	var datagrid = $("#itemParamList");
	tree.tree({
		onClick : function(node){
			if(tree.tree("isLeaf",node.target)){
				datagrid.datagrid('reload', {
					itemCatId :node.id
		        });
			}
		}
	});
});
	function formatItemParamData(value , index){
		var json = JSON.parse(value);
		var array = [];
		$.each(json,function(i,e){
			array.push(e.group);
		});
		return array.join(",");
	}

    function getSelectionsIds(){
    	var itemList = $("#itemParamList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var itemParamListToolbar = [{
        text:'新增',
        iconCls:'icon-add',
        handler:function(){
        	TAOTAO.createWindow({
        		url : "/item-param-add",
        	});
        }
    },{
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个分类才能编辑!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个分类!');
        		return ;
        	}
        	$("#itemParamEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#itemParamList").datagrid("getSelections")[0];
        			//把类目名称、类目id、id补充完整
        			$('#catname').html(data.itemCatName);
        			$('#cid').val(data.itemCatId);
        			$('#id').val(data.id);
        			//显示添加分组按钮
        			$(".addGroupTr").show();
        			// 加载商品描述
        			$.getJSON('/item/param/query/itemcatid/'+data.itemCatId,function(_data){
        				if(_data.status == 200){
        					//把对象转换成json数据
        					var json = JSON.parse(_data.data.paramData);
        					//遍历json中的每一个数据，一个｛｝为一个数据
        					//i为索引，e为每个数据的对象,e.group表示取key为group的值
        					 $.each(json,function(i,e){
        						 //取出表单的第一份li，克隆一份，每点击一次按钮就追加一个表单
        						  var temple = $(".itemParamAddTemplate li").eq(0).clone();
        						  
        						  //把组的名称添加进去
        						  temple.find("li").eq(0).find(".textbox-prompt").val(e.group);
        						  temple.find("li").eq(0).find(".textbox-value").val(e.group);
        						  //取出params
        						  var params=e.params;
        						  //遍历params,往html里面追加
								  $.each(params,function(i,e){
									  //如果是第一个参数，直接在原有的html中修改值
									  if(i==0){
										  temple.find("li").eq(1).find(".textbox-prompt").val(e);
		        						  temple.find("li").eq(1).find(".textbox-value").val(e);
									  }else{//如果不是第一个参数，就要新增并往里面写入值
										  var li = $(".itemParamAddTemplate li").eq(2).clone();
									  	  li.find(".textbox-prompt").val(e);
									  	  li.find(".textbox-value").val(e);
	        							  li.find(".delParam").click(function(){
	        								  $(this).parent().remove();
	        							  });
	        							  li.appendTo($(temple.find(".addParam")).parentsUntil("ul").parent());
									  }
								  });       
        						  $(".addGroup").parent().parent().append(temple);
        						  
        						  temple.find(".addParam").click(function(){
        							  var li = $(".itemParamAddTemplate li").eq(2).clone();
        							  li.find(".delParam").click(function(){
        								  $(this).parent().remove();
        							  });
        							  li.appendTo($(this).parentsUntil("ul").parent());
        						  });
        						  temple.find(".delParam").click(function(){
        							  $(this).parent().remove();
        						  });
        						  //删除组的按钮
        						  temple.find(".delGroup").click(function(){
        							  //把整个ul标签都删除掉
        							  $(this).parent().parent().parent().remove();
        						  });
        					}); 
        				}
        			});
        		}
        	}).window("open");
        }
    },{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品规格!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为 '+ids+' 的商品规格吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/item/param/delete",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','删除商品规格成功!',undefined,function(){
            					$("#itemParamList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    }];
</script>