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
        <div class="btn-group" style="text-align: right;">
            <input type="button" class="btn btn-blank" onclick="returnForm()" value="返  回" />
        </div>
        <h1 class="title-icon">索引详细</h1>
        <div class="content block-content">
            <!-- 查询条件列表 -->
                <table class="table-query blue-th" cellspacing="0" cellpadding="0">
                    <tbody>
                        <tr>
                            <th width="120">索引名称</th>
                            <td>
                                <c:out value="${indexMode.indexname}"/>
                            </td>
                            <th width="120">索引分片</th>
                            <td>
                                <c:out value="${indexMode.shardsnum}"/>
                            </td>
                        </tr>
                        <tr>
                            <th width="120">索引副本</th>
                            <td>
                                <c:out value="${indexMode.replicasnum}"/>
                            </td>
                            <th width="120">创建时间</th>
                            <td>
                                <c:out value="${indexMode.createdate}"/>
                            </td>
                        </tr>
                        <tr>
                            <th width="120">是否已创建</th>
                            <td>
                                <c:out value="${indexMode.iscreat}"/>
                            </td>
                            <th width="120">索引备注</th>
                            <td>
                                <c:out value="${indexMode.indexremark}"/>
                            </td>
                        </tr>
                    </tbody>
                </table>
        </div>
        <c:if test="${fieldList.size()>0}">
            <h1 class="title-icon">索引文档详细列表</h1>
            <div class="content block-content" style="margin-top: 18px;">
                <!-- 查询结果列表 begin-->
                <div class="result-list">
                    <!-- 按钮 -->
                    <table id="tb1" class="easyui-datagrid" data-options="pagination:false,singleSelect:false,rownumbers:false">
                        <thead>
                            <tr>
                                <th data-options="field:'name1',width:50">序号</th>
                                <th data-options="field:'name2',width:330">字段名称</th>
                                <th data-options="field:'name3',width:180">字段类型</th>
                                <th data-options="field:'name5',width:100">分词器</th>
                                <th data-options="field:'name6',width:310">权重</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="data" items="${fieldList }" varStatus="sta" >
                            <tr>
                                <td><c:out value="${sta.index+1 }"/></td>
                                <td><c:out value="${data.fieldname}"/></td>
                                <td><c:out value="${data.fieldtype}"/></td>
                                <td><c:out value="${data.analyzer}"/></td>
                                <td><c:out value="${data.boots}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <!-- 查询结果列表 end -->
            </div>
        </c:if>
        <c:if test="${queryfieldList.size()>0}">
            <h1 class="title-icon">索引查询字段详细列表</h1>
            <div class="content block-content" style="margin-top: 18px;">
                <!-- 查询结果列表 begin-->
                <div class="result-list">
                    <!-- 按钮 -->
                    <table id="tb2" class="easyui-datagrid" data-options="pagination:false,singleSelect:false,rownumbers:false">
                        <thead>
                        <tr>
                            <th data-options="field:'name1',width:50">序号</th>
                            <th data-options="field:'name2',width:330">字段名称</th>
                            <th data-options="field:'name3',width:180">组合关系</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="data" items="${queryfieldList }" varStatus="sta" >
                            <tr>
                                <td><c:out value="${sta.index+1 }"/></td>
                                <td><c:out value="${data.queryfieldname}"/></td>
                                <td><c:out value="${data.querybool}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <!-- 查询结果列表 end -->
            </div>
        </c:if>
    </div>
</body>
</html>

<script type="text/javascript">

    function returnForm(){
        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/main/goindexlist";
    }
    
</script>