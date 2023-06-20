package sherry.enums;


import lombok.Getter;

import java.util.Arrays;

/**
 * @Description: 静态数据枚举: 将常用的数据存储在枚举类型中，在程序运行时直接从枚举值中获取数据，而不需要每次都查询数据库
 * 1.举值定义
 * 2.枚举构造
 * 3.遍历查询
 * 举值->构造->遍历
 * @Author: SHERRY
 * @email: <a href="mailto:SherryTh743779@gmail.com">TianHai</a>
 * @Date: 2023/6/16 19:49
 */
public enum BankEnum
{
    //1.举值定义
    CCB("ccb","中国建设银行"),
    ABC("abc","中国农业银行");
    //2.枚举构造
    @Getter
    String code;
    @Getter
    String message;
    BankEnum(String code,String message){
        this.code=code;
        this.message=message;
    }
    //3.遍历查询
    public static BankEnum getBankEnum(String code){
        BankEnum [] bankEnums=BankEnum.values();
        for (BankEnum element : bankEnums) {
            if(element.getCode().equalsIgnoreCase(code)){
                return element;
            }
        }
        return null;
    }
    //3.流式遍历查询
    public static BankEnum getBankEnumSteam(String code){
        return Arrays.stream(BankEnum.values()).filter(x->x.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
    }
    public static void main(String[] args) {
        BankEnum ccb = getBankEnumSteam("ccb");
//        System.out.println(ccb.getCode());
//        System.out.println(ccb.getMessage());
        System.out.println(ccb);
    }
}

