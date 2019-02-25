package com.hangyjx.controller;


import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import util.ESUtil;
import util.UUIDUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author liuyuejie
 * @create 2018-12-24 20:21
 */
@Controller
@RequestMapping("/doc")
public class AddDocInterController {

    @RequestMapping("/adddoc")
    @ResponseBody
    public String addDoc(HttpServletRequest request){
        String paramJson = request.getParameter("paramJson");
        String index = request.getParameter("index");
        //将json字符串转化Java对象
        List<HashMap> paramMapList =JSON.parseArray(paramJson,HashMap.class);
        TransportClient client= ESUtil.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        try {
            if(paramMapList!=null && paramMapList.size()>0){
                for (HashMap paramMap : paramMapList){
                    bulkRequest.add(client.prepareIndex(index, index+"doc", UUIDUtil.get32UUId())
                            .setSource(paramMap)
                    );
                }

                BulkResponse bulkResponse = bulkRequest.execute().actionGet();
                bulkRequest.request().requests().clear();
            }
        }catch (Exception e){
            return "false";
        }
        return "true";
    }


    @RequestMapping("/querydoc")
    @ResponseBody
    public String querydoc(HttpServletRequest request){
        String index = request.getParameter("index");
        String standid = request.getParameter("standid");
        String paramJson = request.getParameter("paramJson");
        //将json字符串转化Java对象
        HashMap paramMapMap =JSON.parseObject(paramJson,HashMap.class);
        //获得es客户端
        TransportClient client= ESUtil.getClient();

        //通过索引名称获得查询字段的组合关系
        List<Map<String,Object>> queryFieldresultlist=null;
        //查询全部数据
        SearchRequestBuilder srb=client.prepareSearch("standardqueryfield").setTypes("querydoc");
        SearchResponse sr=srb.setQuery(QueryBuilders.matchQuery("standid",standid)).execute().actionGet();
        //获得返回结果
        SearchHits quryFieldhits=sr.getHits();
        //将返回结果添加到list集合中
        queryFieldresultlist = new ArrayList<Map<String,Object>>();
        for(SearchHit hit:quryFieldhits){
            queryFieldresultlist.add( hit.getSourceAsMap());
        }




        SearchResponse response=null;
        List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
        try {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            QueryBuilder queryBuilder = null;
            if(queryFieldresultlist!=null && queryFieldresultlist.size()>0){
                for (Map paramMap : queryFieldresultlist){
                    String  queryfieldname = paramMap.get("queryfieldname").toString();
                    String  querybool = paramMap.get("querybool").toString();
                    String querykeywork = paramMapMap.get(queryfieldname).toString();
                    if("must".equals(querybool)){
                        queryBuilder=boolQueryBuilder.must(QueryBuilders.termQuery(queryfieldname,querykeywork));
                    }

                    if("must_not".equals(querybool)){
                        queryBuilder = boolQueryBuilder.mustNot(QueryBuilders.termQuery(queryfieldname,querykeywork));
                    }

                    if("should".equals(querybool)){
                        queryBuilder = boolQueryBuilder.should(QueryBuilders.matchQuery(queryfieldname,querykeywork));
                    }
                }

            }

            response = client.prepareSearch(index)
                    .setQuery(queryBuilder)
                    .get();
            //获得返回结果
            SearchHits hits=response.getHits();
            //将返回结果添加到list集合中

            for(SearchHit hit:hits){
                resultlist.add(hit.getSourceAsMap());
            }
        }catch (Exception  e){
            e.printStackTrace();
        }
        System.out.println(resultlist);
        return JSON.toJSONString(resultlist);
    }



    @RequestMapping(value="/querytestdoc",method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String queryTestdoc(HttpServletRequest request){
        String index = request.getParameter("index");
        String standid = request.getParameter("standid");
        String paramJson = request.getParameter("paramJson");
        //将json字符串转化Java对象
        HashMap paramMapMap =JSON.parseObject(paramJson,HashMap.class);
        //获得es客户端
        TransportClient client= ESUtil.getClient();

        //通过索引名称获得查询字段的组合关系
        List<Map<String,Object>> queryFieldresultlist=null;
        //查询全部数据
        SearchRequestBuilder srb=client.prepareSearch("standardqueryfield").setTypes("querydoc");
        SearchResponse sr=srb.setQuery(QueryBuilders.matchQuery("standid",standid)).execute().actionGet();
        //获得返回结果
        SearchHits quryFieldhits=sr.getHits();
        //将返回结果添加到list集合中
        queryFieldresultlist = new ArrayList<Map<String,Object>>();
        for(SearchHit hit:quryFieldhits){
            queryFieldresultlist.add( hit.getSourceAsMap());
        }

        SearchResponse response=null;
        List<Map<String,Object>> resultlist = new ArrayList<Map<String,Object>>();
        try {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            QueryBuilder queryBuilder = null;
            if(queryFieldresultlist!=null && queryFieldresultlist.size()>0){
                for (Map paramMap : queryFieldresultlist){
                    String  queryfieldname = paramMap.get("queryfieldname").toString();
                    String  querybool = paramMap.get("querybool").toString();
                    System.out.println(paramMapMap.get(queryfieldname));
                    String querykeywork = paramMapMap.get(queryfieldname)==null?"":paramMapMap.get(queryfieldname).toString();
                    if("must".equals(querybool)){
                        queryBuilder=boolQueryBuilder.must(QueryBuilders.termQuery(queryfieldname,querykeywork));
                    }

                    if("must_not".equals(querybool)){
                        queryBuilder = boolQueryBuilder.mustNot(QueryBuilders.termQuery(queryfieldname,querykeywork));
                    }

                    if("should".equals(querybool)){
                        queryBuilder = boolQueryBuilder.should(QueryBuilders.matchQuery(queryfieldname,querykeywork));
                    }
                }

            }

            System.out.println(queryBuilder);
            response = client.prepareSearch(index)
                    .setQuery(queryBuilder).setSize(100)
                    .get();
            //获得返回结果
            SearchHits hits=response.getHits();
            //将返回结果添加到list集合中
            for(SearchHit hit:hits){
                resultlist.add(hit.getSourceAsMap());
            }
        }catch (Exception  e){
            e.printStackTrace();
        }
        return JSON.toJSONString(resultlist);
    }





    @RequestMapping(value = "/addtestdoc",method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String addTestDoc(HttpServletRequest request){
        String paramJson = request.getParameter("paramJson");
        String index = request.getParameter("index");
        //将json字符串转化Java对象
        List<HashMap> paramMapList =JSON.parseArray(paramJson,HashMap.class);
        TransportClient client= ESUtil.getClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("index",index);
        resultMap.put("paramJson",paramJson);
        try {
            if(paramMapList!=null && paramMapList.size()>0){
                for (HashMap paramMap : paramMapList){
                    bulkRequest.add(client.prepareIndex(index, index+"doc", UUIDUtil.get32UUId())
                            .setSource(paramMap)
                    );
                }

                BulkResponse bulkResponse = bulkRequest.execute().actionGet();
                bulkRequest.request().requests().clear();
            }
        }catch (Exception e){
            resultMap.put("result","false");
            return JSON.toJSONString(resultMap);
        }
        resultMap.put("result","true");
        return JSON.toJSONString(resultMap);
    }



    public static void main(String[] args) {
//        HttpPost httpPost = new HttpPost("http://localhost:8080/doc/adddoc?index=entidnex");
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpResponse response = null;
//
//        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
//        for(int i=0;i<100;i++){
//            Map<String,Object> resultMap = new HashMap<String,Object>();
//            resultMap.put("username","张三"+i);
//            resultMap.put("age",i);
//            resultMap.put("height",i);
//            resultMap.put("lovethings","游泳、打篮球");
//            resultList.add(resultMap);
//        }
//
//        String resultStr = JSON.toJSONString(resultList);
//
//        // 创建请求参数
//        List<BasicNameValuePair> list = new LinkedList<BasicNameValuePair>();
//        BasicNameValuePair param = new BasicNameValuePair("paramJson",resultStr);
//        list.add(param);
//        // 使用URL实体转换工具
//        UrlEncodedFormEntity entityParam = null;
//        try {
//            entityParam = new UrlEncodedFormEntity(list, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        httpPost.setEntity(entityParam);
//
//        // 浏览器表示
//        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
//        // 传输的类型
//        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
//
//        // 执行请求
//        try {
//            response = httpClient.execute(httpPost);
//            httpClient.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        HttpPost httpPost = new HttpPost("http://localhost:8080/doc/querydoc?index=entidnex&standid=d525891c422d4420ae7d00bfd55b532c");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("username","张三");
        resultMap.put("age","30");
        String resultStr = JSON.toJSONString(resultMap);

        // 创建请求参数
        List<BasicNameValuePair> list = new LinkedList<BasicNameValuePair>();
        BasicNameValuePair param = new BasicNameValuePair("paramJson",resultStr);
        list.add(param);
        // 使用URL实体转换工具
        UrlEncodedFormEntity entityParam = null;
        try {
            entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(entityParam);

        // 浏览器表示
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        // 传输的类型
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

        // 执行请求
        try {
            response = httpClient.execute(httpPost);
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}