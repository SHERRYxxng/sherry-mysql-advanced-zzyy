package sherry.redis;

import com.google.common.primitives.Ints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: SHERRY
 * @email: <a href="mailto:SherryTh743779@gmail.com">TianHai</a>
 * @Date: 2023/6/19 19:41
 */
@RestController

public class RedPackageController {

    public static String annotationStudy01(String[] args) {
    return "什么是RestController,与普通的@Controller有什么区别";
}
    public static final String RED_PACKGE_KEY="redpackage";
    public static final String RED_PACKAGE_CONSUME_KEY="redpackage:consume";
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * @Description 发送红包
     * @Date 2023/6/19 20:39
     * @Param [totalMoney, redPackageNumber]
     * @return java.lang.String
     * @Author SHERRY
     */
    @RequestMapping(value="/send")
    String sendRedPackage(int totalMoney,int redPackageNumber){
    //1.拆红包,将总金额totalMoney拆分为redPackageNumber个子红包
        //拆分红包算法通过后获得的多个子红包数组
        Integer [] spliRedPackages= splitRedPackageAlgorithm(totalMoney,redPackageNumber);
        //发红包并保存进list结构里面,IdUtil.simpleUUid()是使用自定义的IdUtil类生成一个简单的UUID字符串，然后拼接在RED_PACKGE_KEY常量上
        //生成唯一标识符,这个id可以用雪花算法生成
        String key=RED_PACKGE_KEY;
        //调用 leftPushAll() 方法将 spliRedPackages 列表中的所有元素从左侧插入到 key 对应的 Redis 列表中。
        redisTemplate.opsForList().leftPushAll(key,spliRedPackages);
        //设置key过期时间
        redisTemplate.expire(key,1, TimeUnit.DAYS);
        //发送红包OK,返回前台显示
        //这段代码的意思是将一个整数数组转换为字符串，并使用制表符分隔键（key）和该整数数组，然后将其作为结果返回。
        return key+"\t"+ Ints.asList(Arrays.stream(spliRedPackages).mapToInt(Integer::valueOf).toArray());
    }

    /**
     * @Description 拆红包的算法->二倍均值算法
     * @Date  2023/6/19 20:02
     * @Param [totalMoney, redPackageNumber]
     * @return java.lang.Integer[]
     * @Author SHERRY
     **/
    private Integer [] splitRedPackageAlgorithm(int totalMoney,int redPackageNumber) {
        //每次抢到的钱
        Integer[] redPackageNumbers = new Integer[redPackageNumber];
        //创建一个变量统计红包被抢走的金额
        int useMoney = 0;
        for (int i = 0; i < redPackageNumber; i++) {
            if (i == redPackageNumber - 1) {
                redPackageNumbers[i] = totalMoney - useMoney;
            } else {
                //二倍均值算法,每次拆分后塞进子红包的金额
                //金额=随机区间(0,(剩余红包/未被抢的剩余红包的个数N)*2)
                int avgMoney = ((totalMoney - useMoney) / (redPackageNumber - i)) * 2;
                redPackageNumbers[i] = 1 + new Random().nextInt(avgMoney - 1);
            }
            useMoney=useMoney+redPackageNumbers[i];
        }
        return redPackageNumbers;
    }
    @RequestMapping(value="/rob")
    public String robRedPackage(String redPackageKey,String userId){
        //验证某个用户是否抢过红包,不可以多抢
        Object redPackage=redisTemplate.opsForHash().get(RED_PACKAGE_CONSUME_KEY+redPackageKey,
                userId);
        //如果没有抢过可以抢红包,否则返回-2表示该用户抢过红包了
        if(null==redPackage){
            //2.1从大红包List里面出队一个作为该客户抢的红包,抢到了一个红包
            Object partRedPackage = redisTemplate.opsForList().leftPop(RED_PACKGE_KEY + redPackageKey);
            if(partRedPackage!=null){
                redisTemplate.opsForHash().put(RED_PACKAGE_CONSUME_KEY+redPackageKey,userId,partRedPackage);
                System.out.println("用户"+userId+"\t 抢到了多少钱的红包"+partRedPackage);
                //TODO后续异步进Mysql或者MQ进一步做统计处理,每一年你发出了多少红包,抢到了多少红包,年度总结
                return String.valueOf(partRedPackage);
            }
            return "errorCode:-1,红包抢完了";
        }
        //3.某个用户抢过了不可以作弊抢多次
        return "errorCode:-2,  message"+userId+"\t"+"你已经抢过红包了,不能重新抢";
    }

    public static void main(String[] args) {
    }
}
