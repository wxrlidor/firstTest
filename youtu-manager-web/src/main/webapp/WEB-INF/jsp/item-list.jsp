<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="js/jquery-easyui-1.4.1/datagrid-filter.js"></script>
<!-- 查询条件 start -->
<form id="searchForm" style="padding: 0;margin: 0"> 
    <table border="0" width="100%">
        <tr style="height:30px">
            <td width="5%" style="text-align:right;border:0px">
               商品ID：
            </td>
            <td width="12%" style="border:0px">
                <input class="easyui-textbox" name="id" id="id" style="width:100%"> 
            </td>   

            <td width="5%" style="text-align:right;border:0px">
                商品标题：
            </td>
            <td width="12%" style="border:0px">
                <input class="easyui-textbox" name="title" id="title" style="width:100%"> 

            </td>   
           </tr>
        <tr style="height:30px">
            <td colspan="8" style="text-align:center;border:0px" >
                <a href="javascript:void(0)"  class="easyui-linkbutton" iconCls="icon-search" onclick="searchCase()" style="margin-right:20px"><strong>查询</strong></a>          
                <a href="javascript:void(0)"  class="easyui-linkbutton" iconCls="icon-redo" onclick="resetSearch()" ><strong>重置</strong></a>
            </td>

        </tr>                                               
    </table>   
</form>
<!-- 查询条件 end -->
<table class="easyui-datagrid" id="itemList" title="商品列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/item/list',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">商品ID</th>
            <th data-options="field:'title',width:360">商品标题</th>
            <!-- <th data-options="field:'cid',width:60">叶子类目id</th> -->
            <th data-options="field:'catName',width:60">类目名称</th>
            <th data-options="field:'sellPoint',width:300">卖点</th>
            <th data-options="field:'price',width:70,align:'right',formatter:TAOTAO.formatPrice">价格</th>
            <th data-options="field:'num',width:60,align:'right'">库存数量</th>
            <th data-options="field:'barcode',width:50">条形码</th>
            <th data-options="field:'status',width:60,align:'center',formatter:TAOTAO.formatItemStatus">状态</th>
            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>
<div id="itemEditWindow" class="easyui-window" title="编辑商品" data-options="modal:true,closed:true,iconCls:'icon-save',href:'item-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<script>
	//重置查询
	function resetSearch() {
	    $("#searchForm").form("clear");
	}
	//查询
	function searchCase() {
	    var param = new Object();   
	    //获取查询条件
	    var searchFormData = $("#searchForm").serializeArray();                
	    $.each(searchFormData,function(i,v){
	        param[v.name] = v.value;
	    });
		console.info(param);
	    //根据查询条件重新加载datagrid，这里会将查询的条件信息发送到后端，
	    //url是在datagrid定义时的url，只需在后端根据查询的添加获取相应信息返回即可。
	    $('#itemList').datagrid("reload", param); 

	}
		//延迟一秒开启过滤，防止报错
		/*setTimeout(function(){
			//取得商品列表对象
			var dg = $('#itemList').datagrid();
			//开启过滤
			//1) field：自定义规则的字段名。
			//2) type：过滤类型，可用值有：label,text,textarea,checkbox,numberbox,validatebox,datebox,combobox,combotree。
			//3) options：过滤类型参数。
			//4) op：过滤条件：可用值有：contains,equal,notequal,beginwith,endwith,less,lessorequal,greater,greaterorequal。
		     dg.datagrid('enableFilter', [{
		        field:'price',
		        type:'numberbox',
		        options:{precision:1},
		        op:['equal','notequal','less','greater']
		    },{
		        field:'num',
		        type:'numberbox',
		        options:{precision:1},
		        op:['equal','notequal','less','greater']
		    },{
		        field:'created',
		        type:'datebox',
		        options:{precision:1},
		        op:['equal','notequal','less','greater']
		    },{
		        field:'updated',
		        type:'datebox',
		        options:{precision:1},
		        op:['equal','notequal','less','greater']
		    },{
		        field:'status',
		        type:'combobox',
		        options:{
		            panelHeight:'auto',
		            data:[{value:'',text:'全部'},{value:'上架',text:'上架'},{value:'<span style="color:red;">下架</span>',text:'下架'}],
		            onChange:function(value){
		                if (value == ''){
		                    dg.datagrid('removeFilterRule', 'status');
		                } else {
		                    dg.datagrid('addFilterRule', {
		                        field: 'status',
		                        op: 'equal',
		                        value: value
		                    });
		                }
		                dg.datagrid('doFilter');
		            }
		        }
		    }]); 
		},1000);*/
    function getSelectionsIds(){
    	var itemList = $("#itemList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    var toolbar = [{
        text:'新增',
        iconCls:'icon-add',
        handler:function(){
        	$(".tree-title:contains('新增商品')").parent().click();
        }
    },{
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个商品才能编辑!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个商品!');
        		return ;
        	}
        	
        	$("#itemEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#itemList").datagrid("getSelections")[0];
        			data.priceView = TAOTAO.formatPrice(data.price);
        			$("#itemeEditForm").form("load",data);
        			
        			// 加载商品描述
        			$.getJSON('/rest/item/query/item/desc/'+data.id,function(_data){
        				if(_data.status == 200){
        					//UM.getEditor('itemeEditDescEditor').setContent(_data.data.itemDesc, false);
        					itemEditEditor.html(_data.data.itemDesc);
        				}
        			});
        			
        			//加载商品规格
        			$.getJSON('/rest/item/param/item/query/'+data.id,function(_data){
        				if(_data && _data.status == 200 && _data.data && _data.data.paramData){
        					$("#itemeEditForm .params").show();
        					$("#itemeEditForm [name=itemParams]").val(_data.data.paramData);
        					$("#itemeEditForm [name=itemParamId]").val(_data.data.id);
        					
        					//回显商品规格
        					 var paramData = JSON.parse(_data.data.paramData);
        					
        					 var html = "<ul>";
        					 for(var i in paramData){
        						 var pd = paramData[i];
        						 html+="<li><table>";
        						 html+="<tr><td colspan=\"2\" class=\"group\">"+pd.group+"</td></tr>";
        						 
        						 for(var j in pd.params){
        							 var ps = pd.params[j];
        							 html+="<tr><td class=\"param\"><span>"+ps.k+"</span>: </td><td><input autocomplete=\"off\" type=\"text\" value='"+ps.v+"'/></td></tr>";
        						 }
        						 
        						 html+="</li></table>";
        					 }
        					 html+= "</ul>";
        					 $("#itemeEditForm .params td").eq(1).html(html);
        				}
        			});
        			
        			TAOTAO.init({
        				"pics" : data.image,
        				"cid" : data.cid,
        				fun:function(node){
        					TAOTAO.changeItemParam(node, "itemeEditForm");
        				}
        			});
        		}
        	}).window("open");
        }
    },{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsIds();//先获取以选择商品的ids字符串
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品!');
        		return ;
        	}
        	//弹出提示对话框
        	$.messager.confirm('确认','确定删除ID为 '+ids+' 的商品吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
        	    	//发出请求url /rest/item/delete 参数为ids，data是返回的结果数据
                	$.post("/rest/item/delete",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','删除商品成功!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				});
            			}else{
            				$.messager.alert('提示','删除失败，请联系管理员!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				})
            			}
            		});
        	    }
        	});
        }
    },'-',{
        text:'下架',
        iconCls:'icon-remove',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品!');
        		return ;
        	}
        	$.messager.confirm('确认','确定下架ID为 '+ids+' 的商品吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/instock",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','下架商品成功!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				});
            			}else{
            				$.messager.alert('提示','下架商品失败，请联系管理员!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				})
            			}
            		});
        	    }
        	});
        }
    },{
        text:'上架',
        iconCls:'icon-remove',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品!');
        		return ;
        	}
        	$.messager.confirm('确认','确定上架ID为 '+ids+' 的商品吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/reshelf",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','上架商品成功!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				});
            			}else{
            				$.messager.alert('提示','上架商品失败，请联系管理员!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				})
            			}
            		});
        	    }
        	});
        }
    }];
</script>