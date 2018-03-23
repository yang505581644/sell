package com.yang.sell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    @GetMapping("auth")
    public void auth(@RequestParam("code") String code) {
      log.info("进入auth方法...");
      log.info("code={}",code);

      String url =  "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx8694b4447262dc80&secret=9fd033977ee509a8dd6674dcfc1840eb&code="+code+"&grant_type=authorization_code";
      RestTemplate restTemplate = new RestTemplate();
      String response = restTemplate.getForObject(url,String.class);
      log.info("response={}",response);
    }
}
