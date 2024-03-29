package cn.ictt.zhanghui.springboot_test.base.util.cipher;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * @author ZhangHui
 * @version 1.0
 * @className StringUtil
 * @description 字符串工具
 * @date 2020/3/18
 */
public class StringUtil {

    public static boolean isNumber(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    public static boolean isBlank(CharSequence c) {
        return StringUtils.isEmpty(c);
    }

    public static boolean equals(CharSequence c1, CharSequence c2) {
        return StringUtils.equals(c1, c2);
    }

    public static boolean isEmpty(Object o) {
        return org.springframework.util.StringUtils.isEmpty(o);
    }

    public static boolean isExistsIn(Object charSequence ,Object... charSequences) {
        for(Object charSeq :charSequences ) {
            if(Objects.equals(charSequence,charSeq)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExistsIn(String charSequence , Object[] arr) {
        for(Object obj : arr) {
            if(charSequence.equals(String.valueOf(obj))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 得到 全拼
     *
     * @param src
     * @return
     */
    public static String getPingYin(String src) {
        char[] t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (Character.toString(t1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else {
                    t4 += Character.toString(t1[i]);
                }
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }

    /**
     * 得到中文首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param cnStr
     * @return
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            // System.out.println(Integer.toHexString(bGBK[i]&0xff));
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    public static int get9RandomNumber() {
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 9; i++) {
            int randNum = rand.nextInt(9) + 1;
            String num = randNum + "";
            sb = sb.append(num);
        }
        return Integer.valueOf(String.valueOf(sb));
    }
}
