package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.*;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.*;
import java.util.List;

public class IndexBuilder extends AbstractIndexBuilder {

    public IndexBuilder(AbstractDocumentBuilder docBuilder){
        super(docBuilder);
    }

    @Override
    public AbstractIndex buildIndex(String rootDirectory) {
        AbstractIndex index = new Index();
        List<String> filePaths = FileUtil.list(rootDirectory);
        for(String filePath:filePaths){
            BufferedReader  reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(new File(filePath))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            AbstractTermTupleStream termTupleStream = new LengthTermTupleFilter(new StopWordTermTupleFilter(
                    new PatternTermTupleFilter(new TermTupleScanner(reader))));
            AbstractDocument d = docBuilder.build(docId++,filePath,termTupleStream);
            index.addDocument(d);
        }
        return index;
    }
}
