package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.Sort;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SimpleSorter implements Sort {
    @Override
    public void sort(List<AbstractHit> hits) {
        for(AbstractHit hit: hits)
            score(hit);
        hits.sort(Comparator.comparing(AbstractHit::getScore));
    }

    @Override
    public double score(AbstractHit hit) {
        double score = 0;
        AbstractPosting posting;
        Iterator iterator = hit.getTermPostingMapping().values().iterator();
        while(iterator.hasNext()){
            posting = (AbstractPosting) iterator.next();
            score += posting.getFreq();
        }
        hit.setScore(score*(-1));
        return hit.getScore();
    }
}
