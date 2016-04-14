package com.projecta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yutao on 4/14/16.
 */
public class Utils {
    public static List<String> getMovieList() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("movie-list.txt"));
            String s = in.readLine();
            List<String> list = Arrays.asList(s.split(","));
//			logger.info("list: {}", list);
            return list;
        } catch (Exception e) {

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
