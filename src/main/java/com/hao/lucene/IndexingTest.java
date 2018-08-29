package com.hao.lucene;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;

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



}
