package cn.ictt.zhanghui.springboot_test.common.search;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;

import java.io.IOException;

/**
 * @author ZhangHui
 * @version 1.0
 * @className ElasticSearchUtils
 * @description ElasticSearch工具类
 * @date 2019/12/24
 */
public class ElasticSearchUtils {

    private static final String HOST = "localhost";
    private static final int PORT = 9200;
    private static final String INDEX_NAME = "post";

    public static void main(String[] args) {
//        try {
//            createIndex();
//            savePost(new Post(1L, "测试", "这是一个测试的文档", "/test", "test文档", new Date(), null));
//            savePost(new Post(2L,"测试", "这是一个测试的文档", "/test", "test文档", new Date(), null));
//            savePost(new Post(3L,"测试", "这是一个测试的文档", "/test", "test文档", new Date(), null));
//            getPost("适配器", 0, 2);
//            deletePost("1");
//            deletePost("2");
//            deletePost("3");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void createIndex() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(HOST, PORT, "http")));
        XContentBuilder builder = JsonXContent.contentBuilder()
                .startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("post-title")
                        .field("type", "keyword")
                        .endObject()
                        .startObject("post-content")
                        .field("type", "text")
                        .field("analyzer", "ik_max_word")
                        .endObject();
            }
            builder.endObject();
        }
        builder.endObject();

        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
        //索引设置
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
        );

        request.mapping(builder);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * 向ElasticSearch插入Post数据
     *
     * @param post
     * @return void
     * @author ZhangHui
     * @date 2019/12/24
     */
//    public static void savePost(Post post) throws IOException {
//        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(HOST, PORT, "http")));
//        IndexRequest indexRequest = new IndexRequest(INDEX_NAME, "_doc", post.getPostId().toString());
//        JSONObject obj = new JSONObject();
//        obj.put("post-id", post.getPostId());
//        obj.put("post-title", post.getPostTitle());
//        obj.put("post-content", StrUtil.cleanBlank(HtmlUtil.cleanHtmlTag(post.getPostContent())));
//        obj.put("post-url", post.getPostUrl());
//        obj.put("post-summary", post.getPostSummary());
//        obj.put("post-date", post.getPostDate().getTime());
//        obj.put("tags", JSONObject.toJSONString(post.getTags()));
//        indexRequest.source(obj.toJSONString(), XContentType.JSON);
//        //添加数据
//        client.index(indexRequest, RequestOptions.DEFAULT);
//    }

    /**
     * 从elasticsearch中分页查询
     *
     * @param keyword
     * @return cc.ryanc.halo.model.domain.Post
     * @author ZhangHui
     * @date 2019/12/24
     */
//    public static Page<Post> getPost(String keyword, int pagenum, int size) throws IOException {
//        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(HOST, PORT, "http")));
//        // 这个sourcebuilder就类似于查询语句中最外层的部分。包括查询分页的起始，
//        // 查询语句的核心，查询结果的排序，查询结果截取部分返回等一系列配置
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        // 结果开始处
//        sourceBuilder.from(pagenum);
//        // 查询个数
//        sourceBuilder.size(size);
//        // 查询的等待时间
//        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
//
//        /**
//         * 使用QueryBuilder
//         * termQuery("key", obj) 完全匹配
//         * termsQuery("key", obj1, obj2..)   一次匹配多个值
//         * matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
//         * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
//         * matchAllQuery();         匹配所有文件
//         */
//        MatchQueryBuilder postContentQuery = QueryBuilders.matchQuery("post-content", keyword).analyzer("ik_max_word");
//        MatchQueryBuilder postTitleQuery = QueryBuilders.matchQuery("post-title", keyword).analyzer("ik_max_word");
//
//        //分词精确查询
////            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tag", "体育");
//
////            // 查询在时间区间范围内的结果  范围查询
////            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime");
////            rangeQueryBuilder.gte("2018-01-26T08:00:00Z");
////            rangeQueryBuilder.lte("2018-01-26T20:00:00Z");
//
//        //模糊查询
////        QueryBuilders.wildcardQuery("name", "*" + term + "*").boost(10f);
////        QueryBuilders.fuzzyQuery("name",keyword).analyzer("ik").likeText(keyword).boost(0.1f);
//
//        // 等同于bool，合并多个查询
//        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
//        boolBuilder.should(postContentQuery);
//        boolBuilder.should(postTitleQuery);
////          boolBuilder.must(termQueryBuilder);
////          boolBuilder.must(rangeQueryBuilder);
//        sourceBuilder.query(boolBuilder);
//
//        // 排序
////          FieldSortBuilder fsb = SortBuilders.fieldSort("date");
////          fsb.order(SortOrder.DESC);
////          sourceBuilder.sort(fsb);
//
//        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
//        searchRequest.types("_doc");
//        searchRequest.source(sourceBuilder);
//        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//        SearchHits hits = response.getHits();
//
//        return assemblePage(pagenum, size, hits);
//    }

    /**
     * 根据postId删除索引
     *
     * @param postId
     * @return void
     * @author ZhangHui
     * @date 2019/12/24
     */
    public static void deletePost(String postId) throws IOException {
//        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(HOST, PORT, "http")));
//        //指定要删除的索引名称
//        DeleteRequest request = new DeleteRequest(INDEX_NAME, "_doc", postId);
//        //设置超时，等待所有节点确认索引删除（使用TimeValue形式）
//        request.timeout(TimeValue.timeValueMinutes(2));
//        client.delete(request, RequestOptions.DEFAULT);
        //异步执行删除索引请求需要将DeleteIndexRequest实例和ActionListener实例传递给异步方法：
        //DeleteIndexResponse的典型监听器如下所示：
        //异步方法不会阻塞并立即返回。
//        ActionListener<DeleteIndexResponse> listener = new ActionListener<DeleteIndexResponse>() {
//            @Override
//            public void onResponse(DeleteIndexResponse deleteIndexResponse) {
//                //如果执行成功，则调用onResponse方法;
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                //如果失败，则调用onFailure方法。
//            }
//        };
//        client.indices().deleteAsync(request, listener);
    }

    /**
     * 根据查询结果封装
     *
     * @param pagenum
     * @param size
     * @param hits
     * @return org.springframework.data.domain.Page<cc.ryanc.halo.model.domain.Post>
     * @author ZhangHui
     * @date 2019/12/25
     */
//    private static Page<Post> assemblePage(int pagenum, int size, SearchHits hits) {
//        PageSearch page = new PageSearch() {
//            @Override
//            public int getTotalPages() {
//                return this.getSize() == 0 ? 1 : (int) Math.ceil((double) this.getTotalElements() / (double) this.getSize());
//            }
//
//            @Override
//            public long getTotalElements() {
//                return hits.getTotalHits().value;
//            }
//
//            @Override
//            public int getNumber() {
//                return pagenum;
//            }
//
//            @Override
//            public int getSize() {
//                return size;
//            }
//
//            @Override
//            public List<Post> getContent() {
//                SearchHit[] hitSources = hits.getHits();
//                if (hitSources.length == 0) {
//                    return new ArrayList<>(0);
//                }
//                List<Post> contentList = new ArrayList<>();
//                for (SearchHit searchHit : hitSources) {
//                    Map<String, Object> sourceMap = searchHit.getSourceAsMap();
//                    Post post = new Post();
//                    post.setPostId(Long.valueOf((Integer) sourceMap.get("post-id")));
//                    post.setPostTitle((String) sourceMap.get("post-title"));
//                    post.setPostUrl((String) sourceMap.get("post-url"));
//                    post.setPostSummary((String) sourceMap.get("post-summary"));
//                    post.setPostContent((String) sourceMap.get("post-content"));
//                    post.setPostDate(new Date(((Long) sourceMap.get("post-date"))));
//                    post.setTags(JSONObject.parseObject((String) sourceMap.get("tags"), List.class));
//                    contentList.add(post);
//                }
//                return contentList;
//            }
//
//            @Override
//            public boolean hasNext() {
//                return this.getNumber() + 1 < this.getTotalPages();
//            }
//
//            @Override
//            public boolean hasPrevious() {
//                return this.getNumber() > 0;
//            }
//        };
//        return page;
//    }
}
