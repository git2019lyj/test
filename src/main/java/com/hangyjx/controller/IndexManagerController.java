package com.hangyjx.controller;

import com.alibaba.fastjson.JSON;
import com.hangyjx.mode.FieldIndexMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import util.ESUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author liuyuejie
 * @create 2018-12-24 20:21
 */
@Controller
@RequestMapping("/manager")
public class IndexManagerController {

    /**
     * 获得集群的信息
     * @return
     */
    @RequestMapping("/indexmanger")
    public ModelAndView gotoIndexManager(){
        ModelAndView modelAndView = new ModelAndView("indexmanger");
        try {
            //获得es客户端
            TransportClient client = ESUtil.getClient();
            //查询全部数据
            SearchRequestBuilder srb=client.prepareSearch("standardlayout").setTypes("standdoc");
            SearchResponse sr=srb.setQuery(QueryBuilders.matchQuery("iscreat","是")).execute().actionGet();
            //获得返回结果
            SearchHits hits=sr.getHits();
            //将返回结果添加到list集合中
            List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
            for(SearchHit hit:hits){
                Map<String,Object> hitMap = hit.getSourceAsMap();
                String indexname = (String)hitMap.get("indexname");
                if("standardqueryfield".equals(indexname) || "standardlayout".equals(indexname)
                    || "standardfield".equals(indexname) || ".kibana_1".equals(indexname)){
                   continue;
                }
                resultlist.add(hitMap);
            }
            modelAndView.addObject("resultlist",resultlist);
        }catch (Exception  e){
            e.printStackTrace();
        }
        return modelAndView;
    }

    /**
     * 关闭索引
     * @param indexname
     * @param standid
     * @return
     */
    @RequestMapping("closeinfo")
    @ResponseBody
    public String closeinfo(String indexname,String standid){
        ESUtil.getAdminClient().prepareClose(indexname).execute().actionGet();
        //如果创建成功，更新数据
        if(ESUtil.isExists(indexname)){
            UpdateRequest updateRequest=new UpdateRequest();
            try {
                updateRequest.index("standardlayout")
                        .type("standdoc")
                        .id(standid)
                        .doc(
                                jsonBuilder().startObject()
                                        .field("indexstatus","2")
                                        .endObject()
                        );
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ESUtil.getClient().update(updateRequest).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return "true";
    }

    /**
     *开启索引
     * @param indexname
     * @param standid
     * @return
     */
    @RequestMapping("openinfo")
    @ResponseBody
    public String openinfo(String indexname,String standid){
        ESUtil.getAdminClient().prepareOpen(indexname).execute().actionGet();
        //如果创建成功，更新数据
        if(ESUtil.isExists(indexname)){
            UpdateRequest updateRequest=new UpdateRequest();
            try {
                updateRequest.index("standardlayout")
                        .type("standdoc")
                        .id(standid)
                        .doc(
                                jsonBuilder().startObject()
                                        .field("indexstatus","1")
                                        .endObject()
                        );
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ESUtil.getClient().update(updateRequest).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return "true";
    }

    /**
     *删除索引
     * @param indexname
     * @param standid
     * @return
     */
    @RequestMapping("deleteinfo")
    @ResponseBody
    public String deleteinfo(String indexname,String standid){
        ESUtil.getAdminClient().prepareDelete(indexname).execute().actionGet();
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
     * 查看添加接口
     * @param indexname
     * @param standid
     * @return
     */
    @RequestMapping("/lookurlinfo")
    public ModelAndView lookindexurlInfo(String indexname,String standid){

        ModelAndView modelAndView = new ModelAndView("lookurlinfo");
        List<Map<String,Object>> resultlist=null;
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
            resultlist = new ArrayList<Map<String,Object>>();
            for(SearchHit hit:hits){
                resultlist.add( hit.getSourceAsMap());
            }
        }catch (Exception  e){
            e.printStackTrace();
        }

        modelAndView.addObject("resultlist",resultlist);
        modelAndView.addObject("indexname",indexname);
        return modelAndView;
    }


    /**
     * 查看查询接口
     * @param indexname
     * @param standid
     * @return
     */
    @RequestMapping("/queryurlinfo")
    public ModelAndView queryindexurlInfo(String indexname,String standid){

        ModelAndView modelAndView = new ModelAndView("queryurlinfo");
        List<Map<String,Object>> resultlist=null;
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
            resultlist = new ArrayList<Map<String,Object>>();
            for(SearchHit hit:hits){
                resultlist.add( hit.getSourceAsMap());
            }
        }catch (Exception  e){
            e.printStackTrace();
        }

        modelAndView.addObject("resultlist",resultlist);
        modelAndView.addObject("indexname",indexname);
        modelAndView.addObject("standid",standid);
        return modelAndView;
    }


}