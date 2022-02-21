package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TermTupleScanner extends AbstractTermTupleScanner {

    protected List<String> list = new ArrayList<>();
    private int pos = 0;

    public TermTupleScanner(){}

    public TermTupleScanner(BufferedReader input){
        super(input);
        String str;
        StringSplitter split = new StringSplitter();
        split.setSplitRegex(Config.STRING_SPLITTER_REGEX);
        try {
            while((str = this.input.readLine()) != null){
                list.addAll(split.splitByRegex(str));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasNext(){
        return pos < list.size();
    }

    /**
     过滤不符合正则表达式的三元组*/
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple tt = null;
        if (this.hasNext()){
            tt = new TermTuple(new Term(list.get(pos)), pos++);
            if (Config.IGNORE_CASE) {
                tt.term.setContent((tt.term.getContent()).toLowerCase());
            }
        }
            return tt;
    }

}
