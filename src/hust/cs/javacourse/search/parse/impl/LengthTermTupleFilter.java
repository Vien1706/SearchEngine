package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

public class LengthTermTupleFilter extends AbstractTermTupleFilter {
    public LengthTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    /**
     * 过滤单词长度不合适的三元组
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple tt = this.input.next();
        if(tt == null)
            return null;
        while(tt.term.getContent().length() < Config.TERM_FILTER_MINLENGTH
                || tt.term.getContent().length() > Config.TERM_FILTER_MAXLENGTH) {
            tt = this.input.next();
            if(tt == null)
                return null;
        }
        return tt;
    }
}
