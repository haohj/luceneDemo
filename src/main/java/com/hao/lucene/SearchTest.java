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
}
