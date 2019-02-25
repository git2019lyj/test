<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>查询导入</title>
    <!-- easyUI 源文件 begin-->
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/easyui/themes/icon.css"/>
    <script src="<c:out value='${pageContext.request.contextPath}'/>/easyui/jquery.min.js"></script>
    <script src="<c:out value='${pageContext.request.contextPath}'/>/easyui/jquery.easyui.min.js"></script>
    <!-- easyUI 源文件 end-->
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/css/blue1_layout.css"/>
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
</head>
<body style="background: #f0f0f0;">
    <div class="container">
        <h1 class="title-icon">Elasticsearch索引维护</h1>
        <div class="content block-content">
            <!-- 查询条件列表 -->
            <form class="query-list" id="formpost" method="post" action="<c:out value='${pageContext.request.contextPath}'/>/main/queryindex">
                <table class="table-query blue-th" cellspacing="0" cellpadding="0">
                    <tbody>
                        <tr>
                            <th width="15%">索引名称</th>
                            <td width="34%">
                                <input class="easyui-textbox" name="indexname" type="text" value="<c:out value='${indexname}'/>" />
                            </td>
                            <th width="15%">索引备注</th>
                            <td width="34%">
                                 <input class="easyui-textbox" name="indexremark" type="text" value="<c:out value='${indexremark}'/>"/>
                            </td>
                        </tr>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="6">
                                <input type="button" class="btn btn-info" onclick="submitForm()"  value="查　询" style="padding: 0px 25px;" />
                                <input type="button" class="btn btn-blank" onclick="clearForm()" value="清　空" style="padding: 0px 25px;"  />
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </form>
        </div>
        <h1 class="title-icon">查询列表</h1>
        <div class="content block-content" style="margin-top: 10px;">
            <!-- 查询结果列表 begin-->
            <div class="result-list">
                <!-- 按钮 -->
                <div class="btn-group" style="text-align: right;">
                    <input type="button" class="btn btn-info" onclick="gotoaddindex()" value="添加" />
                </div>
                <table id="tb1" class="easyui-datagrid" data-options="pagination:false,singleSelect:false,rownumbers:false">
                    <thead>
                        <tr>
                            <th data-options="field:'name1',width:50">序号</th>
                            <th data-options="field:'name2',width:330">索引名称</th>
                            <th data-options="field:'name3',width:180">索引分片</th>
                            <th data-options="field:'name5',width:100">索引副本</th>
                            <th data-options="field:'name6',width:310">索引备注</th>
                            <th data-options="field:'name7',width:310">是否创建</th>
                            <th data-options="field:'name8',width:150">创建时间</th>
                            <th data-options="field:'name9',width:290">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="data" items="${resultlist }" varStatus="sta" >
                        <tr>
                            <td><c:out value="${sta.index+1 }"/></td>
                            <td><c:out value="${data.indexname}"/></td>
                            <td><c:out value="${data.shardsnum}"/></td>
                            <td><c:out value="${data.replicasnum}"/></td>
                            <td><c:out value="${data.indexremark}"/></td>
                            <td><c:out value="${data.iscreat}"/></td>
                            <td><c:out value="${data.createdate}"/></td>
                            <td>
                                <span  onclick=lookinfo("<c:out value='${data.standid}'/>") >查看</span>
                                <c:if test='${data.iscreat=="否"}'>
                                    <span  onclick=updateinfo("<c:out value='${data.standid}'/>") >编辑</span>
                                    <span  onclick=deleteinfo("<c:out value='${data.standid}'/>")>删除</span>
                                    <span  onclick=createIndex("<c:out value='${data.standid}'/>")>创建索引</span>
                                </c:if>

                            </td>
                        </tr>
                    </c:forEach>
                    </tbody> 
                </table>
            </div>
            <!-- 查询结果列表 end -->
        </div>
    </div>
</body>
</html>

<script type="text/javascript">

    function submitForm() {
        $("#formpost").submit();
    }

    function clearForm(){
        $('#formpost').form('clear');
    }

    function lookinfo(standid) {
        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/main/lookindexinfo?standid="+standid;
    }

    function updateinfo(standid) {
        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/main/updateindexinfo?standid="+standid;
    }
    function deleteinfo(standid){
        $.messager.confirm("提示","你确定要删除吗?",function(r) {
            if(r){
                var url="<c:out value='${pageContext.request.contextPath}'/>/main/deleteindexinfo?standid="+standid;
                $.ajax({
                    type: "GET",
                    url: url,
                    dataType: "text",
                    success: function(result){
                        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/main/goindexlist";
                    }
                });
            }
        });
    }
    function gotoaddindex(){
        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/main/gotoaddindex";
    }
    
    function  createIndex(standid) {
        var url = "<c:out value='${pageContext.request.contextPath}'/>/main/createIndex?standid="+standid;
        $.ajax({
            type: "GET",
            url: url,
            dataType: "text",
            success: function(result){
                if(result){
                    alert("创建成功");
                    window.location.href="<c:out value='${pageContext.request.contextPath}'/>/main/goindexlist";
                }
            }
        });
    }
    
</script>