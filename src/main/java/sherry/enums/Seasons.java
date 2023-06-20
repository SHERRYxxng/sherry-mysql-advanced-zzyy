package sherry.enums;


import lombok.Getter;

/**
 * @Description: 举值,构造,遍历
 * @Author: SHERRY
 * @email: <a href="mailto:SherryTh743779@gmail.com">TianHai</a>
 * @Date: 2023/6/16 20:36
 */
public enum Seasons {
//    spring,summer,fall,winter
    SPRINMG("spring","春天的竹笋炒肉","2023"),
    SUMMER("summer","夏天荔枝炒肉","2023"),
    FALL("fall","秋天给你一板栗","2023"),
    WINTER("winter","冬天的第一个起暖气","2023");
    @Getter
    String key;
    @Getter
    String message;
    @Getter
    String year;
    //构造
    Seasons(String code,String message,String year){
        this.key=code;
        this.message=message;
        this.year=year;
    }
    //遍历
    //枚举的遍历先声明方法的类型为当前创建的枚举类

    public static Seasons getSeaSons (String code){
        Seasons [] seasons=Seasons.values();
        for (Seasons element : seasons) {
            if(element.getKey().equalsIgnoreCase(code)){
                return element;
            }
        }
        return null;
    }
    public static void main(String[] args) {
        Seasons spring = getSeaSons("winter");
        System.out.println(spring.getKey());
        System.out.println(spring.getMessage());
        System.out.println(spring.year);
        System.out.println(spring);
    }

}
