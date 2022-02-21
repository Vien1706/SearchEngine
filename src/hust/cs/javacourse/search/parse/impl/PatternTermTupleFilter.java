package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;


public class PatternTermTupleFilter extends AbstractTermTupleFilter {
    public PatternTermTupleFilter() {
        super();
    }

    public PatternTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    /**
     * 过滤单词不符合规则的三元组*/
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple tt = this.input.next();
        if(tt == null)
            return null;
        if(!tt.term.getContent().matches(Config.TERM_FILTER_PATTERN))
            return this.next();
        return tt;
    }
}
