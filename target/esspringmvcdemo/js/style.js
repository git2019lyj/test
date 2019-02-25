
$(document).ready(function() {

    $('.unsynergetic-tb').hide();
    $('.proj-num-content').hide();
    // 单选
    $('.select-ul').find('li').on('click', function(event) {
        $(this).addClass('active').siblings('li').removeClass('active');
    });
    // 单选
    $('.radio-list').find('li').on('click', function(event) {
        $(this).addClass('active').siblings('li').removeClass('active');
    });
    // 选择切换
    $('.unsynergetic-ul').find('li').on('click', function(event) {
        $(this).toggleClass('active');
    });
    // 单选多选不选以及弹出框
    // $(document).on('click','.select-ul2 li', function(event) {
    //     if ( $(this).hasClass('active')) {
    //         $(this).removeClass('active').siblings('li').removeClass('unactive');
    //     } else if( $(this).hasClass('unactive')){
    //         return;
    //     } else{
    //         $(this).addClass('active').siblings('li').addClass('unactive');
    //     }
    // }).on('click','.select-ul2 li i', function(e) {
    //     e.stopPropagation();
    //     $(this).parent('li').siblings('li').find('.layer-help').fadeOut('slow');
    //     $(this).next('.layer-help').fadeIn('slow');
    // });
    // 弹出框
    $('.table-query').on('click', '.with-help i', function(e) {
        e.stopPropagation();
        $(this).parents('.table-query').find('.layer-help').fadeOut('slow');
        $(this).next('.layer-help').fadeIn('slow');
    });
    $(document).on('click', function() {
        $('.layer-help').fadeOut('slow');
    }).on('click', '.layer-help', function(e) {
        e.stopPropagation();
    });

    // 是否协同
    $('.title-icon').find('.select-header').on('click', function(event) {
        if ( $(this).hasClass('active') ) {
            $('.synergetic-div').hide();
            $('.synergetic-default').show();
            $('.synergetic-tb').hide();
        }else{
            $('.synergetic-div').show();
        };
        $(this).toggleClass('active');
    });
    // 1.不协同：
    // 下拉框
    $('.unsynergetic').find('.pull-select').on('click', function(event) {
        $(this).parent('.unsynergetic').addClass('active');
    });
    // 下拉框收起
    $('.unsynergetic').find('.btn-group').find('.btn').on('click', function(event) {
        $(this).parent('.btn-group').parent('.unsynergetic').removeClass('active');
    });
    // 点下拉框取消按钮，选项消失
    $('.unsynergetic').find('.btn-group').find('.btn-blank').on('click', function(event) {
        $(this).parent('.btn-group').siblings('.scroll-wrapper').find('.unsynergetic-ul').find('li').removeClass('active');
    });
    // 点项目按钮，弹出框easyui表格出现
    $('.unsynergetic').find('.proj-num').on('click', function(event) {
        $('.proj-num-content').show();
    });
    // 点弹出框easyui表格按钮，弹出框easyui表格消失
    $('.proj-num-content').find('.btn').on('click', function(event) {
        $('.proj-num-content').hide();
    });
    // 2.协同
    // 协同弹出框中下拉展开
    $('.synergetic-div').find('.synergetic-ul').find('.synergetic-li').find('.check-mould').on('click', function(event) {
        $(this).parent('.synergetic-li').addClass('active').siblings('.synergetic-li').removeClass('active');
    });
    // 协同弹出框中下拉框中选中收起
    $('.synergetic-div').find('.synergetic-ul').find('.check-mould-ul').find('li').on('click', function(event) {
        $(this).parent('.check-mould-ul').parent('.synergetic-li').removeClass('active').addClass('on');
    });
    // 协同弹出框消失
    $('.synergetic-div').find('.btn-group').find('.btn').on('click', function(event) {
        $('.synergetic-div').hide();
        $(this).parents('.synergetic-div').siblings('.synergetic-tb').show();
        $(this).parents('.synergetic').siblings('.table1').hide();
        console.log($(this).parents('.synergetic-div').siblings('.synergetic-tb'));
    });
    // 协同修改
    $('.synergetic').find('.edit').on('click', function(event) {
        $('.synergetic-div').show();
    });
    //导航分类-弹出层里的tab页
    $(".tabs-nav li").each(function(i){
        $(this).click(function (){
            showTabs("tabs_",i+1,10);
        })
    });
    function showTabs(name,num,n){
        for(i=1;i<=n;i++){
            var menu=document.getElementById(name+"nav"+i);
            var con=document.getElementById(name+"con"+i);
            if(i == num){
                $(menu).addClass("active");
                $(con).show();
            }else{
                $(con).css("display", "none");
                $(menu).removeClass("active");
            }                   
        }
    };
    // 查询服务也右侧查询列表展开和隐藏
    $(".btn-hide").on('click', function(event) {
        $(".right-wrapper").toggleClass('right-wrapper-active');
    });
    //点击查询条件
    var timer = null;
    $(document).on('click', '.query-list>li span', function(e) {
        e.stopPropagation();
        $(this).parent().toggleClass('active').siblings().removeClass('active');
    }).on('mouseleave', '.query-list > li', function() {
        timer = setTimeout(function(){
            $('.query-list > li').removeClass('active');
        }, 200);
    }).on('mouseover', '.query-list > li', function() {
        clearTimeout(timer);
    });
    // 点击确定取消按钮弹出框消失
    $('.query-list').on('click', '.checkbox-wrapper .btn', function() {
        $('.query-list > li').removeClass('active');
    });
    //点击切换地图/热力图
    $('.tab-title-list').on('click', 'li', function() {
        var _index = $(this).index();
        if (!$('.tab').is(':animated')) {
            $(this).addClass('active').siblings().removeClass('active');
            $('.tab').eq(_index).fadeIn('slow').siblings('.tab').fadeOut('fast');                
        }
    });
    // 点击切换工商所/街乡镇
    $('.tab-title').on('click', 'li', function() {
        var _index = $(this).index();
        $(this).addClass('active').siblings().removeClass('active');
        $('.tabs-body .tab-con').eq(_index).show().siblings().hide();
    });
    // 点击翻页
    $('.nav-hook').on('click', 'li', function() {
        var _index = $(this).index();
        $(this).addClass('active').siblings().removeClass('active');
        $('.inner-tab').find('ul').eq(_index).show().siblings().hide();
    });
    // 点击显示信息
    $(document).on('click', '.tab', function(e) {
        e.stopPropagation();
        $('.info-mask-wrapper').fadeIn('slow');
    }).on('mouseleave', '.info-mask-wrapper', function() {
        $('.info-mask-wrapper').fadeOut('fast');
    });
    // 单选多选不选以及弹出框
    $(document).on('click','.select-ul2 li', function(event) {
        $(this).addClass('active').siblings('li').removeClass('active');
        $(this).removeClass('unactive').siblings('li').addClass('unactive');
    }).on('click','.select-ul2 li i', function(e) {
        e.stopPropagation();
        $(this).parent('li').siblings('li').find('.layer-help').fadeOut('slow');
        $(this).next('.layer-help').fadeIn('slow');
    });

    // $(document).on('click','.select-ul2 li', function(event) {
    //     if ( $(this).hasClass('active')) {
    //         $(this).removeClass('active').siblings('li').removeClass('unactive');
    //     } else if( $(this).hasClass('unactive')){
    //         return;
    //     } else{
    //         $(this).addClass('active').siblings('li').addClass('unactive');
    //     }
    // }).on('click','.select-ul2 li i', function(e) {
    //     e.stopPropagation();
    //     $(this).parent('li').siblings('li').find('.layer-help').fadeOut('slow');
    //     $(this).next('.layer-help').fadeIn('slow');
    // });
})