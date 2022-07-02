package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Alex
 * @create 2022/5/6 23:12
 */

/*@RestController 是@controller和@ResponseBody 的结合
@Controller 将当前修饰的类注入SpringBoot IOC容器，使得从该类所在的项目跑起来的过程中，这个类就被实例化。
@ResponseBody 它的作用简短截说就是指该类中所有的API接口返回的数据，甭管你对应的方法返回Map或是其他Object，
              它会以Json字符串的形式返回给客户端
*/
@RestController
/*
* @Slf4j是用作日志输出的，一般会在项目每个类的开头加入该注解，添加了该注释之后，
*       就可以在代码中直接饮用log.info( ) 打印日志了
* */
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;
    /*
    * 1.@Value(“${xxxx}”)注解从配置文件读取值的用法，也就是从application.yaml文件中获取值。
    * 2.常量注入@Value("xiaozhou")
    * 3.@Value(“#{}”)是获取bean属性，系统属性，表达式
    * */
    @Resource
    private DiscoveryClient discoveryClient;

    /*
    * @RequestBody主要用来接收前端传递给后端的请求体中的数据；
    *       GET方式无请求体，所以使用@RequestBody接收数据时，前端不能使用GET方式提交数据，
    *       而需要用POST方式进行提交。在后端的同一个接收方法里，@RequestBody与@RequestParam()可以同时使用，
    *       @RequestBody最多只能有一个，而@RequestParam()可以有多个。
    * */
    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("*******插入结果："+result);

        if (result > 0){
            return new CommonResult(200,"插入数据库成功,serverPort:"+serverPort,result);
        }else {
            return new CommonResult(444,"插入数据库失败",null);
        }
    }

    /*
    * 通过 @PathVariable 可以将 URL 中占位符参数绑定到控制器处理方法的入参中：
    *      URL 中的 {xxx} 占位符可以通过@PathVariable(“xxx“) 绑定到操作方法的入参中。
    * */
    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("*******插入结果："+payment);

        if (payment != null){
            return new CommonResult(200,"查询成功,serverPort:"+serverPort,payment);
        }else {
            return new CommonResult(444,"没有对应记录，查询ID："+id,null);
        }
    }

    @GetMapping("/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String service:services) {
            System.out.println("*********service:"+service);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance:instances) {
            System.out.println(instance.getServiceId()+ "\t" +instance.getHost()+"\t"+instance.getPort()
                                +"\t"+instance.getUri());
        }
        return discoveryClient;
    }
}
