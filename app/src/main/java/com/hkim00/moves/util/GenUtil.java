package com.hkim00.moves.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenUtil {

    public static List<String> union(List<String> list1, List<String> list2) {
        Set<String> set = new HashSet<String>();

        if (list1 == null) {
            return list2;
        }
        else if (list2 == null) {
            return list1;
        }
        else {
            set.addAll(list1);
            set.addAll(list2);
            return new ArrayList<String>(set);
        }
    }
}
