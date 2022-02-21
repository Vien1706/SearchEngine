package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.StopWords;

public class StopWordTermTupleFilter extends AbstractTermTupleFilter {
    public StopWordTermTupleFilter() {
        super();
    }

    public StopWordTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    /**
     * 过滤停用词的三元组
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple tt = this.input.next();
        if(tt == null)
            return null;
        for(String str: StopWords.STOP_WORDS){
            if(tt.term.getContent().equals(str))
                return this.next();
        }
        return tt;
    }
}
