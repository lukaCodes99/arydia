package hr.tvz.arydia.server.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenerateValueUtils {

    public static List<Integer> generateBinaryList() {

        List<Integer> list = Arrays.asList(0,0,0,0,0,0,1,1,1,1,1,1);
        Collections.shuffle(list);
        return list;
    }

}
