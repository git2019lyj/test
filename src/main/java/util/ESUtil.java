package util;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author liuyuejie
 * @create 2018-12-22 15:41
 */
public class ESUtil {
     //集群名,默认值elasticsearch
     private  static final String CLUSTER_NAME  = PropertyUtil.getProperty("cluster.name");
    //ES集群中某个节点
    private static final String HOSTNAMES = PropertyUtil.getProperty("host.name");
    //连接端口号
    private static final String TCP_PORTS  =  PropertyUtil.getProperty("tcp.port");
    //构建Settings对象
    private static  Settings settings = Settings.builder().put("cluster.name",CLUSTER_NAME).build();
    //TransportClient对象，用于连接ES集群
    private static volatile TransportClient client;

    /**
     * 同步synchronized(*.class)代码块的作用和synchronized static方法作用一样,
     * 对当前对应的*.class进行持锁,static方法和.class一样都是锁的该类本身,同一个监听器
     * 单节点和集群获得客户端
     * @return
     * @throws UnknownHostException
     */
    public static TransportClient getClient(){
        if (client==null){
            synchronized (TransportClient.class){
                try {
                    if(HOSTNAMES!=null&&!HOSTNAMES.isEmpty() && TCP_PORTS!=null && !TCP_PORTS.isEmpty()){
                        String[] hostNameArr = HOSTNAMES.split(",");
                        String[] tcpPortArr = TCP_PORTS.split(",");
                        client = new PreBuiltTransportClient(settings);
                        //单机模式
                        if(hostNameArr.length==1 && tcpPortArr.length==1){
                            client.addTransportAddress(new TransportAddress(InetAddress.getByName(hostNameArr[0]),Integer.parseInt(tcpPortArr[0])));
                        }else{
                            //集群模式
                            for(int i=0;i<hostNameArr.length;i++){
                                String hostname=hostNameArr[i];
                                int tcp_port = Integer.parseInt(tcpPortArr[i]);
                                client.addTransportAddress(new TransportAddress(InetAddress.getByName(hostname),tcp_port));
                            }
                        }

                    }

                }catch (UnknownHostException e){
                    e.printStackTrace();
                }

            }
        }
        return client;
    }


    /**
     * 获取索引管理的IndicesAdminClient
     */
    public  static IndicesAdminClient getAdminClient(){
        return getClient().admin().indices();
    }






    /**
     * 判定索引是否存在
     * @param indexName
     * @return
     */
    public static boolean isExists(String indexName){
       IndicesExistsResponse indicesExistsResponse = getAdminClient().prepareExists(indexName).get();
        return indicesExistsResponse.isExists()?true:false;
    }

    /**
     * 创建索引
     * @param indexName
     * @return
     */
    public static boolean createIndex(String indexName){
        CreateIndexResponse createIndexResponse = getAdminClient().prepareCreate(indexName.toLowerCase()).get();
        return createIndexResponse.isAcknowledged()?true:false;
    }

    /**
     * 创建索引
     * @param indexName 索引名
     * @param typeName 类型名
     * @param settings   分片数
     * @param mapping 设置类型
     * @return
     */
    public static void createIndex(String indexName,String typeName, Settings settings, XContentBuilder mapping ){
        PutMappingRequest putMappingRequest = Requests.putMappingRequest(indexName).type(typeName).source(mapping);
        CreateIndexResponse createIndexResponse = getAdminClient().prepareCreate(indexName.toLowerCase())
                .setSettings(settings)
                .execute().actionGet();
        getAdminClient().putMapping(putMappingRequest).actionGet();
    }


    /**
     * 创建索引
     * @param indexName 索引名
     * @param typeName 类型名
     * @param shards   分片数
     * @param replicas  副本数
     * @return
     */
    public static void createIndex(String indexName,String typeName, int shards, int replicas){
        Settings settings = Settings.builder()
                .put("index.number_of_shards",shards)
                .put("index.number_of_replicas",replicas)
                .build();
        CreateIndexResponse createIndexResponse = getAdminClient().prepareCreate(indexName.toLowerCase())
                .setSettings(settings)
                .execute().actionGet();
    }

    /**
     * 创建索引
     * @param indexName 索引名
     * @param typeName 类型名
     * @return
     */
    public static void createIndex(String indexName,String typeName){
        getAdminClient().prepareCreate(indexName.toLowerCase()).execute().actionGet();
    }

    /**
     * 删除索引
     * @param indexName
     * @return
     */
    public static boolean deleteIndex(String indexName){
           boolean isAcknowledged= getAdminClient()
                    .prepareDelete(indexName.toLowerCase())
                    .execute()
                    .actionGet().isAcknowledged();
        return isAcknowledged?true:false;
    }
}

