package com.hao.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class Searcher {
    public static void search(String indexDir, String q) throws Exception {
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        // 标准分词器
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("contents", analyzer);
        Query query = parser.parse(q);

        //计算输出查询耗时
        long start = System.currentTimeMillis();
        TopDocs topDocs = searcher.search(query, 10);
        long end = System.currentTimeMillis();

        System.out.println("匹配 " + q + " ，总共花费" + (end - start) + "毫秒" + "查询到" + topDocs.totalHits + "个记录");

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            //输出文件路径
            System.out.println(doc.get("fullPath"));

            //输出文件名称
            System.out.println("文件名称：" + doc.get("fileName"));

            //输出文件内容
            //因为文件内容并没有保存到内存中，所有为null
            System.out.println("文件全文如下：");
            System.out.println(doc.get("contents"));

        }
        reader.close();
    }

    public static void main(String[] args) {
        String indexDir = "D:\\lucene";
        String q = "Zygmunt Saloni";
        try {
            search(indexDir, q);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
