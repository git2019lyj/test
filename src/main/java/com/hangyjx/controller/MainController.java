package com.hangyjx.controller;

import com.alibaba.fastjson.JSON;
import com.hangyjx.mode.FieldIndexMode;
import com.hangyjx.mode.IndexMode;
import com.hangyjx.mode.QueryFieldIndexMode;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryShardContext;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanksAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import util.ESUtil;
import util.UUIDUtil;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author liuyuejie
 * @create 2018-12-24 20:21
 */
@Controller
@RequestMapping("/main")
public class MainController {

    /**
     * Es索引管理页面
     * @return modelAndView
     */
    @RequestMapping(value = "/index")
    public ModelAndView goindex() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    /**
     * 进入页面默认查询全部数据
     * @return
     */
    @RequestMapping(value = "/goindexlist")
    public ModelAndView goIndexList() {
        ModelAndView modelAndView = new ModelAndView("goindexlist");
        try {
            //获得es客户端
            TransportClient client = ESUtil.getClient();
            //查询全部数据
            SearchRequestBuilder srb=client.prepareSearch("standardlayout").setTypes("standdoc");
            SearchResponse sr=srb.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
            //获得返回结果
            SearchHits hits=sr.getHits();
            //将返回结果添加到list集合中
            List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
            for(SearchHit hit:hits){
                resultlist.add(hit.getSourceAsMap());
            }
            modelAndView.addObject("resultlist",resultlist);
        }catch (Exception  e){
            e.printStackTrace();
        }

        return modelAndView;
    }


    /**
     * 查看设置配置的索引详细
     * @param standid
     * @return
     */
    @RequestMapping("/lookindexinfo")
    public ModelAndView lookindexinfo(String standid){
        ModelAndView modelAndView = new ModelAndView("lookindexinfo");
        //获得es客户端
        TransportClient client = ESUtil.getClient();
        //获得索引信息
        GetResponse response =client.prepareGet("standardlayout","standdoc",standid).get();
        Map<String,Object> standardMap = response.getSourceAsMap();
        String resultJson= JSON.toJSONString(standardMap);
        IndexMode indexMode= JSON.parseObject(resultJson,IndexMode.class);
        //添加索引的字段
        List<Map<String,Object>> fieldList = getIndexInfo(client,"standardfield","fielddoc",standid);
        //查询索引的字段
        List<Map<String,Object>> queryfieldList = getIndexInfo(client,"standardqueryfield","querydoc",standid);
        //添加到作用域中
        modelAndView.addObject("indexMode",indexMode);
        modelAndView.addObject("fieldList",fieldList);
        modelAndView.addObject("queryfieldList",queryfieldList);
        return modelAndView;
    }

    /**
     * 删除索引配置
     * @param standid
     * @return
     */
    @RequestMapping("/deleteindexinfo")
    @ResponseBody
    public String deleteindexinfo(String standid){
        //获得es客户端
        TransportClient client=ESUtil.getClient();
        //删除配置数据
        client.prepareDelete("standardlayout","standdoc",standid).get();
        //根据查询条件删除数据

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("standid", standid));
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                        .filter(queryBuilder)
                        .source("standardfield","standardqueryfield")
                        .get();

        return "true";
    }


    /**
     * 修改索引配置
     * @param standid
     * @return
     */
    @RequestMapping("/updateindexinfo")
    public ModelAndView updateindexinfo(String standid){
        ModelAndView modelAndView = new ModelAndView("gotoaddindex");
        //获得es客户端
        TransportClient client=ESUtil.getClient();
        //获得索引信息
        GetResponse response =client.prepareGet("standardlayout","standdoc",standid).get();
        Map<String,Object> standardMap = response.getSourceAsMap();
        String resultJson= JSON.toJSONString(standardMap);
        IndexMode indexMode= JSON.parseObject(resultJson,IndexMode.class);
        modelAndView.addObject("indexMode",indexMode);
        return modelAndView;
    }






    /**
     * 按照查询条件进行查询
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryindex",method = RequestMethod.POST)
    public ModelAndView queryIndex(HttpServletRequest request) {
        //获得请求参数
        String indexname = request.getParameter("indexname")==null?"":request.getParameter("indexname");
        String indexremark = request.getParameter("indexremark")==null?"":request.getParameter("indexremark");

        ModelAndView modelAndView = new ModelAndView("goindexlist");
        try {
            //获得es客户端
            TransportClient client = ESUtil.getClient();
            SearchResponse response=null;
            if(indexname.isEmpty() && indexremark.isEmpty()) {
                //查询全部数据
                SearchRequestBuilder srb = client.prepareSearch("standardlayout").setTypes("standdoc");
                response = srb.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
            }else{
                //根据查询条件进行查询
                QueryBuilder builder = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("indexname",indexname))
                        .should(QueryBuilders.matchQuery("indexremark",indexremark));
                response = client.prepareSearch("standardlayout")
                        .setQuery(builder)
                        .get();



            }
            //获得返回结果
            SearchHits hits=response.getHits();
            //将返回结果添加到list集合中
            List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
            for(SearchHit hit:hits){
                resultlist.add(hit.getSourceAsMap());
            }
            modelAndView.addObject("indexname",indexname);
            modelAndView.addObject("indexremark",indexremark);
            modelAndView.addObject("resultlist",resultlist);
        }catch (Exception  e){
            e.printStackTrace();
        }

        return modelAndView;
    }


    /**
     * 进入索引添加页面
     * @return
     */
    @RequestMapping(value = "/gotoaddindex",method = RequestMethod.GET)
    public String gotoAdd(){
        return "gotoaddindex";
    }

    /**
     * 插入索引配置
     * @param indexMode
     * @param request
     * @return
     */
    @RequestMapping(value = "/addindex",method = RequestMethod.POST)
    public ModelAndView addindex(IndexMode indexMode,HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("createdoc");
        //主键id
        String standid ="";
        if(indexMode.getStandid().isEmpty()) {
            standid = UUIDUtil.get32UUId();
        }else{
            standid=indexMode.getStandid();
        }
        indexMode.setStandid(standid);
        indexMode.setIscreat("否");
        indexMode.setIndexstatus("1");
        //将索引对象转化为json字符串
        String indexModeJson=JSON.toJSONString(indexMode);

        //获得es客户端
        TransportClient client=ESUtil.getClient();
        //插入数据
        IndexResponse response =client.prepareIndex("standardlayout", "standdoc",standid)
                .setSource(indexModeJson, XContentType.JSON)
                .get();
        modelAndView.addObject("standid",standid);
        return modelAndView;
    }

    /*****添加索引字段******************************************************************/
    @RequestMapping("/fieldlist")
    @ResponseBody
    public String fieldIndexList(HttpServletRequest request){
        String standid = request.getParameter("standid");
        List<FieldIndexMode> resultlist=null;
        try {
            //获得es客户端
            TransportClient client = ESUtil.getClient();
            //查询全部数据
            SearchRequestBuilder srb=client.prepareSearch("standardfield").setTypes("fielddoc");
             SearchResponse sr=srb.setQuery(QueryBuilders.matchQuery("standid",standid))
                     .addSort(SortBuilders.fieldSort("createdate").unmappedType("date").order(SortOrder.ASC))
                     .execute().actionGet();
            //获得返回结果
            SearchHits hits=sr.getHits();
            //将返回结果添加到list集合中
            resultlist = new ArrayList<FieldIndexMode>();
            for(SearchHit hit:hits){
                String resultJson= JSON.toJSONString(hit.getSourceAsMap());
                FieldIndexMode fieldIndexMode= JSON.parseObject(resultJson,FieldIndexMode.class);
                resultlist.add(fieldIndexMode);
            }
        }catch (Exception  e){
            e.printStackTrace();
        }
        return JSON.toJSONString(resultlist);
    }


    @RequestMapping("/savefield")
    public void  savefield(FieldIndexMode fieldIndexMode){
        //主键id
        String fid = UUIDUtil.get32UUId();
        fieldIndexMode.setFid(fid);
        //获得时间
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        fieldIndexMode.setCreatedate(dateNowStr);
        //将索引对象转化为json字符串
        String indexModeJson=JSON.toJSONString(fieldIndexMode);
        //获得es客户端
        TransportClient client=ESUtil.getClient();
        //插入数据
        IndexResponse response =client.prepareIndex("standardfield", "fielddoc",fid)
                .setSource(indexModeJson, XContentType.JSON)
                .get();

    }

    @RequestMapping("/updatefield")
    public void  updatefield(FieldIndexMode fieldIndexMode,HttpServletRequest request){
        String fid = fieldIndexMode.getFid();
        //将索引对象转化为json字符串
        String indexModeJson=JSON.toJSONString(fieldIndexMode);
        //获得es客户端
        TransportClient client=ESUtil.getClient();
        //修改数据
        IndexResponse response =client.prepareIndex("standardfield", "fielddoc",fid)
                .setSource(indexModeJson, XContentType.JSON)
                .get();

    }

    @RequestMapping("/deletefield")
    @ResponseBody
    public String  deletefield(FieldIndexMode fieldIndexMode,HttpServletRequest request){
        String fid = fieldIndexMode.getFid();
        //获得es客户端
        TransportClient client=ESUtil.getClient();
        //删除数据
        client.prepareDelete("standardfield","fielddoc",fid).get();
        return  "true";

    }

    /*****添加索引字段******************************************************************/

    /*****添加索引查询字段******************************************************************/

    @RequestMapping("/queryfieldlist")
    @ResponseBody
    public String queryfieldlist(HttpServletRequest request){
        String standid = request.getParameter("standid");
        List<QueryFieldIndexMode> resultlist=null;
        try {
            //获得es客户端
            TransportClient client = ESUtil.getClient();
            //查询全部数据
            SearchRequestBuilder srb=client.prepareSearch("standardqueryfield").setTypes("querydoc");
            SearchResponse sr=srb.setQuery(QueryBuilders.matchQuery("standid",standid))
                    .addSort(SortBuilders.fieldSort("createdate").unmappedType("date").order(SortOrder.ASC))
                    .execute().actionGet();
            //获得返回结果
            SearchHits hits=sr.getHits();
            //将返回结果添加到list集合中
            resultlist = new ArrayList<QueryFieldIndexMode>();
            for(SearchHit hit:hits){
                String resultJson= JSON.toJSONString(hit.getSourceAsMap());
                QueryFieldIndexMode queryFieldIndexMode= JSON.parseObject(resultJson,QueryFieldIndexMode.class);
                resultlist.add(queryFieldIndexMode);
            }
        }catch (Exception  e){
            e.printStackTrace();
        }
        return JSON.toJSONString(resultlist);
    }


    @RequestMapping("/savequeryfield")
    public void  savequeryfield(QueryFieldIndexMode queryFieldIndexMode){
        //主键id
        String aid = UUIDUtil.get32UUId();
        queryFieldIndexMode.setAid(aid);
        //获得时间
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        queryFieldIndexMode.setCreatedate(dateNowStr);

        //将索引对象转化为json字符串
        String indexModeJson=JSON.toJSONString(queryFieldIndexMode);
        //获得es客户端
        TransportClient client=ESUtil.getClient();
        //插入数据
        IndexResponse response =client.prepareIndex("standardqueryfield", "querydoc",aid)
                .setSource(indexModeJson, XContentType.JSON)
                .get();

    }

    @RequestMapping("/updatequeryfield")
    public void  updatequeryfield(QueryFieldIndexMode queryFieldIndexMode,HttpServletRequest request){
        String aid = queryFieldIndexMode.getAid();
        //将索引对象转化为json字符串
        String indexModeJson=JSON.toJSONString(queryFieldIndexMode);
        //获得es客户端
        TransportClient client=ESUtil.getClient();
        //修改数据
        IndexResponse response =client.prepareIndex("standardqueryfield", "querydoc",aid)
                .setSource(indexModeJson, XContentType.JSON)
                .get();

    }

    @RequestMapping("/deletequeryfield")
    @ResponseBody
    public String  deletequeryfield(QueryFieldIndexMode queryFieldIndexMode,HttpServletRequest request){
        String aid = queryFieldIndexMode.getAid();
        //获得es客户端
        TransportClient client=ESUtil.getClient();
        //删除数据
        client.prepareDelete("standardqueryfield","querydoc",aid).get();
        return  "true";

    }

    /*****添加索引查询字段******************************************************************/

    /*****创建索引******************************************************************/
    /**
     * 创建索引
     * @param standid
     * @param request
     * @return
     */
    @RequestMapping("/createIndex")
    @ResponseBody
    public String  createIndex(String standid ,HttpServletRequest request){
        //获得es客户端
        TransportClient client=ESUtil.getClient();
        //获得索引信息
        GetResponse response =client.prepareGet("standardlayout","standdoc",standid).get();
        Map<String,Object> standardMap = response.getSourceAsMap();
        String resultJson= JSON.toJSONString(standardMap);
        IndexMode indexMode= JSON.parseObject(resultJson,IndexMode.class);

        //添加索引的字段
        List<Map<String,Object>> fieldList = getIndexInfo(client,"standardfield","fielddoc",standid);

        //创建索引
        String indexname = indexMode.getIndexname();
        ESUtil.createIndex(indexname,indexname+"doc",setSettings(indexMode),setMapping(fieldList));

        //如果创建成功，更新数据
        if(ESUtil.isExists(indexname)){
            UpdateRequest updateRequest=new UpdateRequest();
            try {
                updateRequest.index("standardlayout")
                        .type("standdoc")
                        .id(standid)
                        .doc(
                                jsonBuilder().startObject()
                                        .field("iscreat","是")
                                        .endObject()
                        );
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                client.update(updateRequest).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return  "true";
    }

    public List<Map<String,Object>> getIndexInfo(TransportClient client,String index,String type,String id){
        List<Map<String,Object>> resultlist=null;
        try {
            //查询全部数据
            SearchRequestBuilder srb=client.prepareSearch(index).setTypes(type);
            SearchResponse sr=srb.setQuery(QueryBuilders.matchQuery("standid",id))
                    .addSort(SortBuilders.fieldSort("createdate").unmappedType("date").order(SortOrder.ASC))
                    .execute().actionGet();
            //获得返回结果
            SearchHits hits=sr.getHits();
            //将返回结果添加到list集合中
            resultlist = new ArrayList<Map<String,Object>>();
            for(SearchHit hit:hits){
                resultlist.add(hit.getSourceAsMap());
            }
        }catch (Exception  e){
            e.printStackTrace();
        }

        return resultlist;
    }


    /**
     * 设置settings
     * @param indexMode
     * @return
     */
    public Settings setSettings(IndexMode indexMode){
        Settings settings = Settings.builder()
                .put("index.number_of_shards",Integer.parseInt(indexMode.getShardsnum()))
                .put("index.number_of_replicas",Integer.parseInt(indexMode.getReplicasnum()))
                .build();
        return settings;

    }

    /**
     * 设置Mapping
     * @param fieldList
     * @return
     */
    public XContentBuilder setMapping( List<Map<String,Object>> fieldList){
        XContentBuilder builder =null;
        try {
            builder = jsonBuilder().startObject().startObject("properties");
            if(fieldList!=null) {
                int size = fieldList.size();
                for (Map<String,Object> fieldMap:fieldList ) {
                    FieldIndexMode fieldIndexMode= JSON.parseObject(JSON.toJSONString(fieldMap),FieldIndexMode.class);
                    String type= fieldIndexMode.getFieldtype();
                    if("text".equals(type)){
                        builder.startObject(fieldIndexMode.getFieldname())
                                .field("type",type)
                                .field("analyzer",fieldIndexMode.getAnalyzer())
                                .field("search_analyzer",fieldIndexMode.getAnalyzer())
                                .field("boost",fieldIndexMode.getBoots())
                                .endObject();
                    }else if("keyword".equals(type)) {
                        builder.startObject(fieldIndexMode.getFieldname())
                                .field("type",type)
                                .field("analyzer",fieldIndexMode.getAnalyzer())
                                .field("boost",fieldIndexMode.getBoots())
                                .endObject();
                    }else if("date".equals(type)){
                        builder.startObject(fieldIndexMode.getFieldname())
                                .field("type",type)
                                .field("format","yyyy-MM-dd")
                                .field("boost",fieldIndexMode.getBoots())
                                .endObject();
                    }else{
                        builder.startObject(fieldIndexMode.getFieldname())
                                .field("type",type)
                                .field("boost",fieldIndexMode.getBoots())
                                .endObject();
                    }
                }
            }
            builder.endObject().endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder;
    }

    /*****创建索引******************************************************************/

}