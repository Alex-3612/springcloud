package com.atguigu.springcloud.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//注在类上，提供类的get、set、equals、hashCode、canEqual、toString⽅法
@AllArgsConstructor//注在类上，提供类的全参构造
@NoArgsConstructor//： 注在类上，提供类的⽆参构造
public class CommonResult<T> {

    private Integer code;
    private String message;
    private T data;

    public CommonResult(Integer code,String message){
        this(code,message,null);
    }
}
