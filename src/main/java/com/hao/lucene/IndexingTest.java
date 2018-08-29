package com.hao.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class IndexingTest {
    private String ids[] = {"1", "2", "3"};
    private String citys[] = {"qingdao", "nanjing", "shanghai"};
    private String descs[] = {
            "Qingdao is a beautiful city.",
            "Nanjing is a city of culture.",
            "Shanghai is a bustling city."
    };

    private Directory dir;
    private IndexReader reader;
    private IndexWriter writer;

    @Before
    public void setUp() throws Exception {
        dir = FSDirectory.open(Paths.get("D:\\lucene"));
        getWriter();

        for (int i = 0; i < ids.length; i++) {
            Document doc = new Document();
            doc.add(new TextField("id", ids[i], Field.Store.YES));
            doc.add(new TextField("city", citys[i], Field.Store.YES));
            doc.add(new TextField("desc", descs[i], Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();
    }

    @After
    public void after() throws Exception {
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
    }

    private void getWriter() throws Exception {
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(dir, conf);
    }

    /**
     * 测试写了几个文档
     *
     * @throws Exception
     */
    @Test
    public void testIndexWriter() throws Exception {
        getWriter();
        System.out.println("写入了" + writer.numDocs() + "个文档");
        writer.close();
    }
}
