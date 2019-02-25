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
        <h1 class="title-icon">索引列表</h1>
        <div class="content block-content" style="margin-top: 10px;">
            <!-- 查询结果列表 begin-->
            <div class="result-list">
                <table id="tb1" class="easyui-datagrid" data-options="pagination:false,singleSelect:false,rownumbers:false">
                    <thead>
                        <tr>
                            <th data-options="field:'name1',width:50">序号</th>
                            <th data-options="field:'name2',width:330">索引名称</th>
                            <th data-options="field:'name3',width:180">索引分片</th>
                            <th data-options="field:'name5',width:100">索引副本</th>
                            <th data-options="field:'name6',width:310">索引状态</th>
                            <th data-options="field:'name9',width:290">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="data" items="${resultlist}" varStatus="sta" >
                        <tr>
                            <td><c:out value="${sta.index+1 }"/></td>
                            <td><c:out value="${data.indexname}"/></td>
                            <td><c:out value="${data.shardsnum}"/></td>
                            <td><c:out value="${data.replicasnum}"/></td>
                            <td>
                                <c:if test="${data.indexstatus=='1'}">
                                   开启
                                </c:if>
                                <c:if test="${data.indexstatus=='2'}">
                                    关闭
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${data.indexstatus=='2'}">
                                    <span  onclick=openinfo("<c:out value='${data.indexname}'/>","<c:out value='${data.standid}'/>")>开启</span>
                                </c:if>
                                <c:if test="${data.indexstatus=='1'}">
                                     <span  onclick=closeinfo("<c:out value='${data.indexname}'/>","<c:out value='${data.standid}'/>") >关闭</span>
                                </c:if>
                                <span  onclick=deleteinfo("<c:out value='${data.indexname}'/>","<c:out value='${data.standid}'/>") >删除</span>
                                <span  onclick=lookIndexUrlInfo("<c:out value='${data.indexname}'/>","<c:out value='${data.standid}'/>") >数据添加接口</span>
                                <span  onclick=queryIndexInfo("<c:out value='${data.indexname}'/>","<c:out value='${data.standid}'/>") >数据查询接口</span>
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
    function openinfo(indexname,standid) {
        $.messager.confirm("提示","你确定要开启索引吗?",function(r) {
            if(r){
                var url="<c:out value='${pageContext.request.contextPath}'/>/manager/openinfo?indexname="+indexname+"&standid="+standid;
                $.ajax({
                    type: "GET",
                    url: url,
                    dataType: "text",
                    success: function(result){
                        setTimeout('locationurl()', 1000)
                    }
                });
            }
        });
    }


    function closeinfo(indexname,standid) {
        $.messager.confirm("提示","你确定要关闭索引吗?",function(r) {
            if(r){
                var url="<c:out value='${pageContext.request.contextPath}'/>/manager/closeinfo?indexname="+indexname+"&standid="+standid;
                $.ajax({
                    type: "GET",
                    url: url,
                    dataType: "text",
                    success: function(result){
                        setTimeout('locationurl()', 1000)

                    }
                });
            }
        });
    }


    function deleteinfo(indexname,standid) {
        $.messager.confirm("提示","你确定要删除索引吗?",function(r) {
            if(r){
                var url="<c:out value='${pageContext.request.contextPath}'/>/manager/deleteinfo?indexname="+indexname+"&standid="+standid;
                $.ajax({
                    type: "GET",
                    url: url,
                    dataType: "text",
                    success: function(result){
                        setTimeout('locationurl()', 1000)

                    }
                });
            }
        });
    }

    function  locationurl() {
        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/manager/indexmanger";
    }


    function lookIndexUrlInfo(indexname,standid){
        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/manager/lookurlinfo?indexname="+indexname+"&standid="+standid;
    }

    function queryIndexInfo(indexname,standid){
        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/manager/queryurlinfo?indexname="+indexname+"&standid="+standid;
    }
    
</script>