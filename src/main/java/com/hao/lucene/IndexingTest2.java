package com.hao.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.nio.file.Paths;

public class IndexingTest2 {
    private String ids[] = {"1", "2", "3", "4"};
    private String authors[] = {"Jack", "Marry", "John", "Json"};
    private String positions[] = {"accounting", "technician", "salesperson", "boss"};
    private String titles[] = {"Java is a good language.", "Java is a cross platform language", "Java powerful", "You should learn java"};
    private String contents[] = {
            "If possible, use the same JRE major version at both index and search time.",
            "When upgrading to a different JRE major version, consider re-indexing. ",
            "Different JRE major versions may implement different versions of Unicode,",
            "For example: with Java 1.4, `LetterTokenizer` will split around the character U+02C6,"
    };

    private Directory dir;

    /**
     * 获取IndexWriter实例
     *
     * @param indexDir
     * @return
     * @throws Exception
     */
    public IndexWriter getWriter(String indexDir) throws Exception {
        dir = FSDirectory.open(Paths.get(indexDir));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, conf);
        return writer;
    }


    /**
     * 生成索引
     *
     * @throws Exception
     */
    @Test
    public void index() throws Exception {
        IndexWriter writer = getWriter("D:\\lucene");
        for (int i = 0; i < ids.length; i++) {
            Document doc = new Document();
            doc.add(new TextField("id", ids[i], Field.Store.YES));
            doc.add(new TextField("author", authors[i], Field.Store.YES));
            doc.add(new TextField("position", positions[i], Field.Store.YES));
            // 加权操作
            TextField field = new TextField("title", titles[i], Field.Store.YES);
            if ("boss".equals(positions[i])) {
                field.setBoost(1.5f);
            }
            doc.add(field);
            doc.add(new TextField("content", contents[i], Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();
    }

    /**
     * 查询
     *
     * @throws Exception
     */
    @Test
    public void search() throws Exception {
        dir = FSDirectory.open(Paths.get("D:\\lucene"));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        String searchField = "title";
        String q = "java";
        Term term = new Term(searchField, q);
        Query query = new TermQuery(term);

        TopDocs topDocs = searcher.search(query, 10);
        System.out.println("匹配 '" + q + "'，总共查询到" + topDocs.totalHits + "个文档");
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println("#####################################################################");
            System.out.println("author:"+doc.get("author"));
            System.out.println("position:"+doc.get("position"));
            System.out.println("title:"+doc.get("title"));
            System.out.println("content:"+doc.get("content"));
            System.out.println("#####################################################################");
        }
        reader.close();
    }

}
