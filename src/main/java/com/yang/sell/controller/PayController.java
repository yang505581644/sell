package com.yang.sell.controller;

import com.lly835.bestpay.model.PayResponse;
import com.yang.sell.dto.OrderDTO;
import com.yang.sell.enums.ResultEnum;
import com.yang.sell.exception.SellException;
import com.yang.sell.service.OrderService;
import com.yang.sell.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.util.Map;

@Controller
@Slf4j
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/pay")
    public ModelAndView index(@RequestParam("openid") String openid,
                              @RequestParam("orderId") String orderId,
                              @RequestParam("returnUrl") String returnUrl,
                        Map<String,Object> map) {

            log.info("openid={}",openid);
            //1.查询订单
            OrderDTO orderDTO = orderService.findOne(orderId);
            if (orderDTO==null) {
                throw new SellException(ResultEnum.ORDER_NOT_EXIST);
            }

            //发起支付
            PayResponse payResponse = payService.create(orderDTO);

            map.put("payResponse",payResponse);
            String decode = URLDecoder.decode(returnUrl);
            map.put("returnUrl",decode);
            return new ModelAndView("pay/create",map);

    }


    @RequestMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                                Map<String,Object> map) {
            //1.查询订单
            OrderDTO orderDTO = orderService.findOne(orderId);
            if (orderDTO==null) {
                throw new SellException(ResultEnum.ORDER_NOT_EXIST);
            }

            //发起支付
            PayResponse payResponse = payService.create(orderDTO);

            map.put("payResponse",payResponse);


            return new ModelAndView("pay/create",map);
    }

    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);

        //返回给微信处理结果
        return new ModelAndView("pay/success");
    }
}
