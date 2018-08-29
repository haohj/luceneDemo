package com.hao.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
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
    }

    /**
     * 测试读取文档
     *
     * @throws Exception
     */
    @Test
    public void testIndexReader() throws Exception {
        reader = DirectoryReader.open(dir);
        System.out.println("最大文档数：" + reader.maxDoc());
        System.out.println("实际文档数：" + reader.numDocs());
    }

    /**
     * 测试删除 在合并前
     *
     * @throws Exception
     */
    @Test
    public void testDeleteBeforeMerge() throws Exception {
        getWriter();
        System.out.println("删除前：" + writer.numDocs());
        writer.deleteDocuments(new Term("id", "1"));
        writer.commit();
        System.out.println("writer.maxDoc()：" + writer.maxDoc());
        System.out.println("writer.numDocs()：" + writer.numDocs());
    }

    /**
     * 测试删除 在合并后
     *
     * @throws Exception
     */
    @Test
    public void testDeleteAfterMerge() throws Exception {
        getWriter();
        System.out.println("删除前：" + writer.numDocs());
        writer.deleteDocuments(new Term("id", "1"));
        writer.forceMergeDeletes(); // 强制删除
        writer.commit();
        System.out.println("writer.maxDoc()：" + writer.maxDoc());
        System.out.println("writer.numDocs()：" + writer.numDocs());
    }

    /**
     * 测试更新
     *
     * @throws Exception
     */
    @Test
    public void testUpdate() throws Exception {
        getWriter();
        Document doc = new Document();
        doc.add(new StringField("id", "1", Field.Store.YES));
        doc.add(new StringField("city", "qingdao", Field.Store.YES));
        doc.add(new TextField("desc", "dsss is a city.", Field.Store.NO));
        writer.updateDocument(new Term("id", "1"), doc);
    }
}
