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
    <script src="<c:out value='${pageContext.request.contextPath}'/>/json/jquery.format.js"></script>
</head>
<body style="background: #f0f0f0;">
    <div class="container">
        <div class="btn-group" style="text-align: right;">
            <input type="button" class="btn btn-blank" onclick="returnForm()" value="返  回" />
        </div>
        <h1 class="title-icon">查看查询数据地址</h1>
        <div class="content block-content">
            <!-- 查询条件列表 -->
                <table class="table-query blue-th" cellspacing="0" cellpadding="0">
                    <tbody>
                        <tr>
                            <th width="20%">url地址</th>
                            <td width="80%" style="text-align: left">
                                http://localhost:8080/doc/querydoc?index=<c:out value="${indexname}"/>&standid=<c:out value="${standid}"/>
                            </td>
                        </tr>
                    </tbody>
                </table>
        </div>

        <h1 class="title-icon">索引添加数据项列表</h1>
        <div class="content block-content" style="margin-top: 18px;">
            <!-- 查询结果列表 begin-->
            <div class="result-list">
                <!-- 按钮 -->
                <table id="tb2" class="easyui-datagrid" data-options="pagination:false,singleSelect:false,rownumbers:false">
                    <thead>
                    <tr>
                        <th data-options="field:'name1',width:50">序号</th>
                        <th data-options="field:'name2',width:330">字段名称</th>
                        <th data-options="field:'name3',width:180">筛选关系</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="data" items="${resultlist }" varStatus="sta" >
                        <tr>
                            <td><c:out value="${sta.index+1 }"/></td>
                            <td><c:out value="${data.queryfieldname}"/></td>
                            <td><c:out value="${data.querybool}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
         </div>


        <!-- 查询结果列表 end -->

        <h1 class="title-icon">测试接口</h1>
        <div class="content block-content" style="margin-top: 18px;">
            <!-- 查询结果列表 begin-->
            <div class="result-list">
                <!-- 按钮 -->
                    <table id="tb3" class="easyui-datagrid" data-options="pagination:false,singleSelect:false,rownumbers:false">
                        <thead>
                        <tr>
                            <th data-options="field:'name4'" style="width: 30%">字段名称</th>
                            <th data-options="field:'name5'" style="width: 70%">值</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="data" items="${resultlist }" varStatus="sta" >
                            <tr>
                                <td><c:out value="${data.queryfieldname}"/></td>
                                <td>
                                    <input class="text" id="<c:out value='${data.queryfieldname}'/>" name="<c:out value='${data.queryfieldname}'/>"  style="width:50%;height: 25px;line-height: 25px;">
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <table id="tb4" class="easyui-datagrid" data-options="pagination:false,singleSelect:false,rownumbers:false">
                        <thead>
                        <tr>
                            <th data-options="field:'name6'" style="width: 20%">测试接口</th>
                            <th data-options="field:'name7'" style="width: 80%">返回结果</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                                <input type="button" class="btn btn-info" onclick="resultTestJson()"  value="接口测试" style="padding: 0px 25px;" />
                            </td>
                            <td>
                                <textarea style="height: 300px;width: 90%" id="resultJson"></textarea>
                            </td>
                        </tr>
                        </tbody>
                    </table>
            </div>
            <!-- 查询结果列表 end -->
        </div>

    </div>
</body>
</html>

<script type="text/javascript">

    function returnForm(){
        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/manager/indexmanger";
    }

    function resultTestJson(){
        var paramJsonstr ={}
        <c:forEach var="data" items="${resultlist }" varStatus="sta" >
             paramJsonstr["<c:out value='${data.queryfieldname}'/>"]=$("#<c:out value='${data.queryfieldname}'/>").val();
        </c:forEach>
        var paramJson={
            "paramJson":JSON.stringify(paramJsonstr),
            "standid":"<c:out value='${standid}'/>",
            "index":"<c:out value='${indexname}'/>"
        };
        var url="<c:out value='${pageContext.request.contextPath}'/>/doc/querytestdoc";
        $.ajax({
            type: "POST",
            url: url,
            dataType: "JSON",
            data:paramJson,
            success: function(data){
                $('#resultJson').val( JSON.stringify(data));
                $('#resultJson').format({method: 'json'});

            }
        });

    }

</script>