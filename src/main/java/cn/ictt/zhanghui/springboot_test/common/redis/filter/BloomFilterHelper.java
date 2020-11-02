package cn.ictt.zhanghui.springboot_test.common.redis.filter;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;

/**
 * @author ZhangHui
 * @version 1.0
 * @className BloomFilterHelper
 * @description 基于redis的布隆过滤器的核心算法，这里借用com.google.guava的BloomFilter的算法来计算位数组
 * 包括新增元素存放位数组的位置，位数组长度等
 * @date 2020/7/9
 */
public class BloomFilterHelper<T> {
    private int numHashFunctions;
    private int bitSize;
    private Funnel<T> funnel;

    public BloomFilterHelper(Funnel<T> funnel, int expectedInsertions, double fpp) {
        Preconditions.checkArgument(funnel != null, "funnel不能为空");
        this.funnel = funnel;
        bitSize = optimalNumOfBits(expectedInsertions, fpp);
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }

    /**
     * 返回元素valuez在多个hash算法下返回的位置数组
     * @author ZhangHui
     * @date 2020/7/9
     * @param value 新增泛型元素
     * @return int[]
     */
    public int[] murmurHashOffset(T value) {
        int[] offset = new int[numHashFunctions];
        long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);
        for (int i = 1; i <= numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % bitSize;
        }
        return offset;
    }

    /**
     * 计算bit数组的长度
     * @author ZhangHui
     * @date 2020/7/9
     * @param n 期待存放元素个数
     * @param p 最大容忍误判率
     * @return int 返回位数组长度
     */
    private int optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 计算hash方法执行次数
     * @author ZhangHui
     * @date 2020/7/9
     * @param n 期待存放元素个数
     * @param m 位数组长度
     * @return int 返回hash次数
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
}
