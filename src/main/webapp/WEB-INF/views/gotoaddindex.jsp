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
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/easyui/themes/icon.css"/>
    <script src="<c:out value='${pageContext.request.contextPath}'/>/easyui/jquery.min.js"></script>
    <script src="<c:out value='${pageContext.request.contextPath}'/>/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/css/blue1_layout.css"/>
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/css/style.css">
    <title>索引添加页面</title>
</head>
<body style="background: #f0f0f0;">
<div class="container">
    <h1 class="title-icon">索引添加页面</h1>
    <div class="content block-content">
        <!-- 查询条件列表 -->
        <form  id="formpost" method="post" action="<c:out value='${pageContext.request.contextPath}'/>/main/addindex">
            <input type="hidden" name="standid" value="<c:out value='${indexMode.standid}'/>" />
            <table class="table-query">
                <tbody>
                <tr>
                    <th width="15%">索引名称</th>
                    <td width="35%">
                        <input class="easyui-textbox"  name="indexname" value="<c:out value='${indexMode.indexname}'/>" style="width:100%" data-options="required:true">
                    </td>
                    <th width="15%">索引分片</th>
                    <td width="35%">
                        <input class="easyui-numberbox"  value="5" name="shardsnum" value="<c:out value='${indexMode.shardsnum}'/>" style="width:100%" data-options="required:true">
                    </td>
                </tr>
                <tr>
                    <th width="15%">索引副本</th>
                    <td width="35%">
                        <input class="easyui-numberbox" value="3" name="replicasnum" value="<c:out value='${indexMode.replicasnum}'/>" style="width:100%" data-options="required:true">
                    </td>
                    <th width="15%">创建时间</th>
                    <td width="35%">
                        <input class="easyui-datebox" name="createdate" value="<c:out value='${indexMode.createdate}'/>" data-options="formatter:myformatter,parser:myparser,required:true" style="width:93%" data-options="required:true">
                    </td>
                </tr>
                <tr>
                    <th width="15%">索引备注</th>
                    <td width="35%" colspan="3">
                        <input class="easyui-textbox" name="indexremark"  value="<c:out value='${indexMode.indexremark}'/>"  multiline="true" style="width:80%" style="height: 60px;" data-options="required:true">
                    </td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="6">
                        <input type="button" class="btn btn-info" onclick="submitForm()"  value="下一步" style="padding: 0px 25px;" />
                        <input type="button" class="btn btn-blank" onclick="clearForm()" value="清　空" style="padding: 0px 25px;"  />
                        <input type="button" class="btn btn-blank" onclick="returnIndex()" value="返　回" style="padding: 0px 25px;"  />
                    </td>
                </tr>
                </tfoot>
            </table>
        </form>
    </div>
</div>

<script type="text/javascript">


    function submitForm() {
        if($("#formpost").form('validate')){
            $("#formpost").submit();
        }
    }

    function clearForm(){
        $('#formpost').form('clear');
    }
    
    function  returnIndex() {
        window.location.href="<c:out value='${pageContext.request.contextPath}'/>/main/goindexlist"
    }
    
    function myformatter(date){
        var y = date.getFullYear();
        var m = date.getMonth()+1;
        var d = date.getDate();
        return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
    }
    function myparser(s){
        if (!s) return new Date();
        var ss = (s.split('-'));
        var y = parseInt(ss[0],10);
        var m = parseInt(ss[1],10);
        var d = parseInt(ss[2],10);
        if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
            return new Date(y,m-1,d);
        } else {
            return new Date();
        }
    }
</script>

</body>
</html>