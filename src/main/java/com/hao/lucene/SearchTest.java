package com.hao.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class SearchTest {
    private Directory directory;
    private IndexReader reader;
    private IndexSearcher searcher;

    @Before
    public void setUp() throws Exception {
        directory = FSDirectory.open(Paths.get("D:\\lucene"));
        reader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(reader);
    }

    @After
    public void tearDown() throws Exception {
        reader.close();
    }

    /**
     * 对特定项搜索
     * TODO 与Indexer配合使用
     *
     * @throws Exception
     */
    @Test
    public void testTermQuery() throws Exception {
        String searchField = "contents";
        String q = "particular";
        Term term = new Term(searchField, q);
        Query query = new TermQuery(term);
        TopDocs topDocs = searcher.search(query, 10);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("fullPath"));
        }
    }

    /**
     * 解析查询表达式
     * TODO 与Indexer配合使用
     *
     * @throws Exception
     */
    @Test
    public void testQueryParser() throws Exception {
        Analyzer analyzer = new StandardAnalyzer();
        String searchField = "contents";
        String q = "abc~";
        QueryParser parser = new QueryParser(searchField, analyzer);
        Query query = parser.parse(q);
        TopDocs topDocs = searcher.search(query, 100);
        System.out.println("匹配 " + q + "查询到" + topDocs.totalHits + "个记录");
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("fullPath"));
        }
    }

    /**
     * 指定项范围搜索
     * TODO 与IndexingTest配合使用
     *
     * @throws Exception
     */
    @Test
    public void testTermRangeQuery() throws Exception {
        //
        String field = "desc";
        //查询范围下限
        BytesRef lowerTerm = new BytesRef("b".getBytes());
        //查询范围上限
        BytesRef upperTerm = new BytesRef("c".getBytes());
        //是否包含下限
        boolean includeLower = true;
        //是否包含上限
        boolean includeUpper = true;
        TermRangeQuery termRangeQuery = new TermRangeQuery(field, lowerTerm, upperTerm, includeLower, includeUpper);
        TopDocs topDocs = searcher.search(termRangeQuery, 10);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
        }

    }

    /**
     * 指定数字范围
     * TODO 与IndexingTest配合使用
     *
     * @throws Exception
     */
    @Test
    public void testNumericRangeQuery() throws Exception {
        //
        String field = "id";
        //查询的最小数字
        Integer min = 1;
        //查询的最大数字
        Integer max = 2;
        //是否包含最小数字
        boolean minInclusive = true;
        //是否包含最大数字
        boolean maxInclusive = true;
        NumericRangeQuery<Integer> query = NumericRangeQuery.newIntRange(field, min, max, minInclusive, maxInclusive);
        TopDocs topDocs = searcher.search(query, 10);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
        }
    }

    /**
     * 指定字符串开头搜索
     * TODO 与IndexingTest配合使用
     *
     * @throws Exception
     */
    @Test
    public void testPrefixQuery() throws Exception {
        PrefixQuery query = new PrefixQuery(new Term("city", "a"));
        TopDocs hits = searcher.search(query, 10);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
        }
    }

    /**
     * 多条件查询
     * TODO 与IndexingTest配合使用
     *
     * @throws Exception
     */
    @Test
    public void testBooleanQuery() throws Exception {
        NumericRangeQuery<Integer> query1 = NumericRangeQuery.newIntRange("id", 1, 2, true, true);
        PrefixQuery query2 = new PrefixQuery(new Term("city", "a"));
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        booleanQuery.add(query1, BooleanClause.Occur.MUST);
        booleanQuery.add(query2, BooleanClause.Occur.MUST);
        TopDocs hits = searcher.search(booleanQuery.build(), 10);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("id"));
            System.out.println(doc.get("city"));
            System.out.println(doc.get("desc"));
        }
    }
}
