package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.PatternTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;

import java.io.*;

public class DocumentBuilder extends AbstractDocumentBuilder {
    @Override
    public AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream) {
        Document a = new Document(docId, docPath    );
        AbstractTermTuple tt;
        while(true){
            tt = termTupleStream.next();
            if(tt==null)
                break;
            a.addTuple(tt);
        }
        return a;
    }

    @Override
    public AbstractDocument build(int docId, String docPath, File file) throws FileNotFoundException {
        Document a = new Document(docId,docPath);
        BufferedReader  reader = new BufferedReader(
                new InputStreamReader( new FileInputStream( new File(String.valueOf(file)))));
        AbstractTermTupleStream termTupleStream = new LengthTermTupleFilter(new StopWordTermTupleFilter(
                new PatternTermTupleFilter(new TermTupleScanner(reader))));
        AbstractTermTuple tt;
        while(true){
            tt = termTupleStream.next();
            if(tt==null)
                break;
            a.addTuple(tt);
        }
        return a;
    }
}
