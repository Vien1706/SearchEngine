package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IndexSearcher extends AbstractIndexSearcher {
    @Override
    public void open(String indexFile) {
        try {
            this.index.load(new File(indexFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractPostingList postingList = this.index.search(queryTerm);
        if (postingList == null)
            return null;
        List<AbstractHit> hits = new ArrayList<>();
        for (AbstractPosting posting : postingList.getList()) {
            int flag = 0;
            int docId = posting.getDocId();
            String docPath = this.index.getDocIdToDocPathMapping().get(posting.getDocId());
            for (AbstractHit hit : hits) {
                if (docId == hit.getDocId() && docPath.equals(hit.getDocPath())) {
                    hit.addTermPostingMapping(queryTerm, posting);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                Map<AbstractTerm, AbstractPosting> termPostingMapping = new TreeMap<AbstractTerm, AbstractPosting>();
                termPostingMapping.put(queryTerm, posting);
                hits.add(new Hit(docId, docPath, termPostingMapping));
            }
        }
        sorter.sort(hits);
        return hits.toArray(new Hit[hits.size()]);
    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        AbstractHit[] hits1, hits2, hits3;
        hits1 = this.search(queryTerm1, sorter);
        hits2 = this.search(queryTerm2, sorter);
        if (hits1 == null && hits2 == null)
            return null;
        if (hits1 == null)
            return hits2;
        if (hits2 == null)
            return hits1;
        hits3 = new AbstractHit[hits1.length + hits2.length];
        List<AbstractHit> list1, list2, list3;
        list1 = Arrays.asList(hits1);
        list2 = Arrays.asList(hits2);
        list3 = new ArrayList<>();
        int flag = 1;
        for (AbstractHit hit1 : list1) {
            for (AbstractHit hit2 : list2) {
                if (hit2.getDocId() == hit1.getDocId()) {
                    //合并同时在hit1和hit2中出现的文档
                    AbstractHit hit = new Hit(hit2.getDocId(), hit2.getDocPath());
                    hit.setScore(hit1.getScore() + hit2.getScore());
                    hit.getTermPostingMapping().putAll(hit1.getTermPostingMapping());
                    hit.getTermPostingMapping().putAll(hit2.getTermPostingMapping());
                    list3.add(hit);
                }
            }
        }
        if (combine == LogicalCombination.AND) {
            sorter.sort(list3);
            return list3.toArray(new Hit[list3.size()]);
        }
        if (combine == LogicalCombination.OR) {
            List<AbstractHit> list4 = new ArrayList<>(list3);
            for (AbstractHit hit1 : list1) {
                //在list1中出现未在list3中出现，加入list4
                flag = 1;
                for (AbstractHit hit4 : list4) {
                    if (hit1.equals(hit4))
                        flag = 0;
                }
                if (flag == 1)
                    list4.add(hit1);
            }
            for (AbstractHit hit2 : list2) {
                //在list2中出现未在list3中出现，加入list4
                flag = 1;
                for (AbstractHit hit4 : list4) {
                    if (hit2.equals(hit4))
                        flag = 0;
                }
                if (flag == 1)
                    list4.add(hit2);
            }
            sorter.sort(list4);
            return list4.toArray(new Hit[list4.size()]);
        }
        return null;
    }

}
