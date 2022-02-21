package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.Hit;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;

import java.util.*;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s,s1,s2;
        int n;
        char condition = 'Y';
        Sort hitsSorter = new SimpleSorter();
        AbstractHit[] hits;
        List<Integer> p1, p2;
        String indexFile = Config.INDEX_DIR + "index.dat";
        AbstractIndexSearcher searcher = new IndexSearcher();
        searcher.open(indexFile);
        while(condition == 'Y') {
            System.out.println("Search mode[1-3]:");
            s = in.nextLine();
            n = (int)(s.charAt(0) - '0');
            switch(n){
                case 1:
                    System.out.print("Enter your word:");   break;
                case 2:
                    System.out.print("Enter your first word:"); break;
                case 3:
                    System.out.print("Enter two words in a row:"); break;
                default:
                    System.out.println("Error in your choice");
                    continue;
            }
            s = in.nextLine();
            String[] terms = s.split(" ");
            s1 = terms[0];
            Term termSearch1 = new Term(s1);
            if(n == 2) {
                System.out.print("Enter your second word:");
                s2 = in.nextLine();
                Term termSearch2 = new Term(s2);
                hits = searcher.search(termSearch1, termSearch2, hitsSorter, AbstractIndexSearcher.LogicalCombination.OR);
            }
            else if(n == 3) {
                s2 = terms[1];
                Term termSearch2 = new Term(s2);
                hits = searcher.search(termSearch1, termSearch2, hitsSorter, AbstractIndexSearcher.LogicalCombination.AND);
                List<AbstractHit> resHits = new ArrayList<>();
                if (hits != null) {
                    for (AbstractHit hit : hits)
                        resHits.add(hit);
                    List<AbstractHit> disHits = new ArrayList<>();
                    for (AbstractHit hit : hits) {
                        int flag = 0;
                        p1 = hit.getTermPostingMapping().get(termSearch1).getPositions();
                        p2 = hit.getTermPostingMapping().get(termSearch2).getPositions();
                        for (int pos1 : p1) {
                            for (int pos2 : p2) {
                                if (pos1 == pos2 + 1 || pos1 == pos2 - 1) {
                                    flag = 1;
                                    break;
                                }
                            }
                        }
                        if (flag == 0)
                            disHits.add(hit);
                    }
                    boolean ret = resHits.removeAll(disHits);
                    hits = resHits.toArray(new Hit[resHits.size()]);
                } else
                    hits = searcher.search(termSearch1, hitsSorter);
                //Term termSearch = new Term("coronavirus");
                if (hits == null || hits.length == 0)
                    System.out.println("Found nothing");
                else
                    for (AbstractHit hit : hits) {
                        System.out.println(hit.toString());
                    }
                System.out.println("Continue or not?(Y/F)");
                condition = in.nextLine().charAt(0);
                System.out.println("");
            }
        }
    }
}
