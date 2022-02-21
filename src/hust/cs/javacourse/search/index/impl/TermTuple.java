package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;

public class TermTuple extends AbstractTermTuple {
    public TermTuple() {
    }

    /**
     * 构造函数
     * @parma term:单词
     * @parma curpos:当前位置
     */
    public TermTuple(AbstractTerm term, int curPos){
        this.term = term;
        this.curPos = curPos;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TermTuple)
            return this.term.equals(((TermTuple)obj).term) && this.curPos == ((TermTuple)obj).curPos;
        else
            return false;
    }

    @Override
    public String toString() {
        return "TermTuple{" +
                "term=" + term +
                ", freq=" + freq +
                ", curPos=" + curPos +
                '}' + '\n';
    }
}
