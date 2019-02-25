<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Elasticsearch</title>
    <!-- easyUI 源文件 begin-->
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/easyui/themes/default/easyui.css"/>
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/easyui/themes/icon.css"/>
    <script src="<c:out value='${pageContext.request.contextPath}'/>/easyui/jquery.min.js"></script>
    <script src="<c:out value='${pageContext.request.contextPath}'/>/easyui/jquery.easyui.min.js"></script>
    <!-- easyUI 源文件 end-->
    <link rel="stylesheet" href="<c:out value='${pageContext.request.contextPath}'/>/css/blue1_layout.css"/>
</head>
<body class="easyui-layout">
    <div data-options="region:'north',border:false" style="height:60px;">
        <!-- header begin -->
        <div class="public-header">
            <div class="center">
                <h1 class="title">Elasticsearch</h1>
                <!-- <img class="logo-title" src="images/green/logo_title.png" alt="系统名" /> -->
                <div class="right-links" style="float: left">
                    <a title="ES索引管理" class="active" onclick="indexmanager()" href="javascript:void(0);">ES索引管理</a>
                    <a title="ES索引管理" onclick="indexdp()" href="javascript:void(0);">ES索引维护</a>
                </div>
            </div>
        </div>
    </div>
    <div class="rightArea" data-options="region:'center',border:false">
        <iframe class="rightFrame" name="rightFrame" id="rightFrame"  frameborder="0"  allowtransparency="true"></iframe>
    </div>
    <script>
        $(function(){
            $("#rightFrame").attr("src","<c:out value='${pageContext.request.contextPath}'/>/manager/indexmanger");
            // 点击头部下拉箭头显示更多
            var hideTimer = null;
            $(document).on('click', '.public-header span', function(e) {
                e.stopPropagation();
                $(this).toggleClass('active').siblings().removeClass('active');
                if ($(this).hasClass('active')) {
                    $('.header-more').fadeIn('slow');
                }else {
                    $('.header-more').fadeOut('fast');
                }
            }).on('mouseleave', '.public-header span', function() {
                hideTimer = setTimeout('hideHM()', 200);
            }).on('mouseleave', '.header-more', function() {
                hideTimer = setTimeout('hideHM()', 200);
            }).on('mouseover', '.public-header span', function(e) {
                clearTimeout(hideTimer);
            }).on('mouseover', '.header-more', function() {
                clearTimeout(hideTimer);
            }).on('click', '.header-more li', function(e) {
                e.stopPropagation();
            });
            // 头部导航点击样式
            $('.right-links').on('click', 'a', function() {
                $(this).addClass('active').siblings().removeClass('active');
            });
        })
        function hideHM() {
            $('.public-header span').removeClass('active');
            $('.header-more').fadeOut('fast');
        }

        function indexdp(){
            $("#rightFrame").attr("src","<c:out value='${pageContext.request.contextPath}'/>/main/goindexlist");
        }

        function  indexmanager() {
            $("#rightFrame").attr("src","<c:out value='${pageContext.request.contextPath}'/>/manager/indexmanger");
        }
    </script>
</body>
</html>