package com.tu.mall;

import com.tu.mall.common.result.ResultCodeEnum;
import org.junit.Test;

/**
 * @author JiFeiYe
 * @since 2024/10/26
 */
public class TestAdd {

    @Test
    public void test1() {
        long a = 65484548856466L;
        long b = 456489498699L;

        {
            System.out.println(1);
            long c = 0;
            long start = System.currentTimeMillis();
            for (int i = 0; i < 999999999; i++) {
                c = a + b;
            }
            long end = System.currentTimeMillis();
            System.out.println(c);
            System.out.println("(end - start) = " + (end - start) + "ms");
        }
        {
            System.out.println(2);
            long c = 0;
            long start = System.currentTimeMillis();
            for (int i = 0; i < 999999999; i++) {
                c = a * b;
            }
            long end = System.currentTimeMillis();
            System.out.println(c);
            System.out.println("(end - start) = " + (end - start) + "ms");
        }
    }

    @Test
    public void test2() {
        String a = "abc";
        String b = "abc";
        System.out.println(a == b);
        System.out.println(a.equals(b));

        String c = new String("ABC");
        String d = new String("ABC");
        System.out.println(c == d);
        System.out.println(c.equals(d));

        ResultCodeEnum zxc = ResultCodeEnum.valueOf("FAIL");
        System.out.println(zxc);
    }
}
