package com.image.pagination;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        System.out.println(checkPermut("","737728787887387"));
    }

    private static String checkPermut(String prefix, String str){
        int n = str.length();
        if(n == 1 ){
            if(Double.parseDouble(prefix+str) % 8 == 0){
                return "YES";
            }
        } else{
            for(int i = 0 ;i<n;i++){
                checkPermut(prefix + str.charAt(i), str.substring(0,i) + str.substring(i+1, n));
            }
        }

        return "NO";
    }
}