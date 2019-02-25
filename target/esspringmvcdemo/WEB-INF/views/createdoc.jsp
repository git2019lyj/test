<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" >
    <!-- easyUI 源文件 begin-->
    <title>Build CRUD DataGrid with jQuery EasyUI - jQuery EasyUI Demo</title>
    <link rel="stylesheet" type="text/css" href="https://www.jeasyui.com/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="https://www.jeasyui.com/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="https://www.jeasyui.com/easyui/demo/demo.css">
    <script type="text/javascript" src="https://code.jquery.com/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="https://www.jeasyui.com/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="https://www.jeasyui.com/easyui/jquery.edatagrid.js"></script>
    <title>索引添加页面</title>
    <style type="text/css">
        body{font-family:'Microsoft Yahei','宋体',Tahoma,Arial;font-size:12px;color:#333;margin:0;padding:20px; }
        .title-icon{
            color: #333;
            font-size: 16px;
            padding-left: 10px;
            border-left: 4px solid #126497;
            height: 24px;
            line-height: 24px;
            font-weight: normal;
            margin: 20px 0 10px 0;
        }
        .btn {
            padding: 0px 35px;
            border-radius: 2px;
            box-shadow: none;
            font-weight: normal;
            font-size: 14px;
        }
        .btn-blank {
            background: none;
            border: 1px solid #0e97ee;
            color: #0e97ee;
        }
    </style>
    <script type="text/javascript">
        var fieldJSON=[
            {
                "id": "text",
                "name":"text"
            },
            {
                "id": "integer",
                "name":"integer"
            },
            {
                "id": "long",
                "name":"long"
            },
            {
                "id": "float",
                "name":"float"
            },
            {
                "id": "double",
                "name":"double"
            },
            {
                "id": "date",
                "name":"date"
            },
            {
                "id": "object",
                "name":"object"
            },
            {
                "id": "array",
                "name":"array"
            }
        ]

        var fieldJSON=[
            {
                "id": "text",
                "name":"text"
            },
            {
                "id": "integer",
                "name":"integer"
            },
            {
                "id": "long",
                "name":"long"
            },
            {
                "id": "float",
                "name":"float"
            },
            {
                "id": "double",
                "name":"double"
            },
            {
                "id": "date",
                "name":"date"
            },
            {
                "id": "object",
                "name":"object"
            },
            {
                "id": "array",
                "name":"array"
            }
        ];
        var  analyzerJSON =[
            {"id": "standard", "name":"standard"},
            {"id": "ik_max_word", "name":"ik_max_word"},
        ];

        var queryanalyzerJSON =[
            {"id": "standard", "name":"standard"},
            {"id": "ik_max_word", "name":"ik_max_word"},
        ];

        var queryboolJSON=[
            {"id": "must", "name":"must"},
            {"id": "must_not", "name":"must_not"},
            {"id": "should", "name":"should"}
        ];

    </script>
</head>
<body style="background: #f0f0f0;">
<h1 class="title-icon">设置文档内容</h1>
<div class="easyui-tabs" style="width:100%;height:450px">
    <p style="text-align: right;padding-right: 20px"><input type="button" class="btn btn-blank" onclick="returnForm()" value="关　闭" style="height: 30px;line-height: 30px;"  /></p>
    <div title="添加索引的字段" style="padding:10px">
        <table id="dg" style="width:98%;height:400px"
               toolbar="#toolbar" pagination="false"
               rownumbers="true" fitColumns="true" singleSelect="true" idField="fid">
            <thead>
            <tr>
                <th field="fid"  hidden="true" class="easyui-s" editor="{type:'validatebox'}">></th>
                <th field="fieldname" width="25%" class="easyui-s" editor="{type:'validatebox',options:{required:true}}">字段名称</th>
                <th field="fieldtype" width="25%"  editor="{type:'combobox',options:{data:fieldJSON,valueField:'id',textField:'name',required:true}}">字段类型</th>
                <th field="analyzer" width="25%" editor="{type:'combobox',options:{data:analyzerJSON,valueField:'id',textField:'name',required:true}}">分词器</th>
                <th field="boots" width="25%"  editor="{type:'numberbox',options:{required:true}}">权重</th>

            </tr>
            </thead>
        </table>
        <div id="toolbar">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"  onclick="javascript:$('#dg').edatagrid('addRow')">添加</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyUser()">删除</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:$('#dg').edatagrid('saveRow')">保存</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:$('#dg').edatagrid('cancelRow')">撤销</a>
        </div>
    </div>
    <div title="添加查询索引字段">

        <table id="adddg" style="width:98%;height:350px"
               toolbar="#addtoolbar" pagination="false"
               rownumbers="true" fitColumns="true" singleSelect="true" idField="aid">
            <thead>
            <tr>
                <th field="aid"  hidden="true" class="easyui-s" editor="{type:'validatebox'}">></th>
                <th field="queryfieldname" width="25%" class="easyui-s" editor="{type:'validatebox',options:{required:true}}">字段名称</th>
                <th field="querybool" width="25%"  editor="{type:'combobox',options:{data:queryboolJSON,valueField:'id',textField:'name',required:true}}">组合关系</th>
            </tr>
            </thead>
        </table>
        <div id="addtoolbar">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"  onclick="javascript:$('#adddg').edatagrid('addRow')">添加</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyQuery()">删除</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:$('#adddg').edatagrid('saveRow')">保存</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:$('#adddg').edatagrid('cancelRow')">撤销</a>
        </div>

    </div>
</div>



<script type="text/javascript">
    $(function(){
        $('#dg').edatagrid({
            url: "<c:out value='${pageContext.request.contextPath}'/>/main/fieldlist?standid=<c:out value='${standid}'/>",
            saveUrl: "<c:out value='${pageContext.request.contextPath}'/>/main/savefield?standid=<c:out value='${standid}'/>",
            updateUrl: "<c:out value='${pageContext.request.contextPath}'/>/main/updatefield?standid=<c:out value='${standid}'/>"
        });

        $('#adddg').edatagrid({
            url: "<c:out value='${pageContext.request.contextPath}'/>/main/queryfieldlist?standid=<c:out value='${standid}'/>",
            saveUrl: "<c:out value='${pageContext.request.contextPath}'/>/main/savequeryfield?standid=<c:out value='${standid}'/>",
            updateUrl: "<c:out value='${pageContext.request.contextPath}'/>/main/updatequeryfield?standid=<c:out value='${standid}'/>"
        });
    });

    <!-- 删除用户  -->
    function destroyUser() {
        var row = $("#dg").datagrid("getSelected");
        if(row) {
            $.messager.confirm("提示","你确定要删除吗?",function(r) {
                if(r) {
                    var url = "<c:out value='${pageContext.request.contextPath}'/>/main/deletefield?fid="+row.fid;
                    $.ajax({
                        type: "GET",
                        url: url,
                        dataType: "text",
                        success: function(result){
                            setTimeout(function(){  $('#dg').datagrid('reload') }, 1000);
                        }
                    });
                }
            });
        }
    }


    <!-- 删除用户  -->
    function destroyQuery() {
        var row = $("#adddg").datagrid("getSelected");
        if(row) {
            $.messager.confirm("提示","你确定要删除吗?",function(r) {
                if(r) {
                    var url = "<c:out value='${pageContext.request.contextPath}'/>/main/deletequeryfield?aid="+row.aid;
                    $.ajax({
                        type: "GET",
                        url: url,
                        dataType: "text",
                        success: function(result){
                            setTimeout(function(){  $('#adddg').datagrid('reload') }, 1000);
                        }
                    });
                }
            });
        }
    }

    function returnForm(){
        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/main/goindexlist";
    }
</script>

</body>
</html>