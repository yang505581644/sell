package com.yang.sell.controller;


import com.yang.sell.config.ProjectUrlConfig;
import com.yang.sell.enums.ResultEnum;
import com.yang.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {
        //1.配置
        //2.调用方法
        String url = projectUrlConfig.getWechatMpAuthorize()+"/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url,WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode(returnUrl));
        log.info("【微信网页授权】获取code,result={}",redirectUrl);

        return "redirect:"+redirectUrl;
    }

    @GetMapping("userInfo")
    public String userInfo(@RequestParam("code") String code,
                         @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
            try {
                wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            }catch (WxErrorException e) {
                log.error("【微信网页授权】{}",e);
                throw new SellException(ResultEnum.WX_MP_ERROR.getCode(),e.getError().getErrorMsg());
            }
            String openId = wxMpOAuth2AccessToken.getOpenId();

            return "redirect:"+returnUrl+"?openid="+openId;
    }

    @GetMapping("/qrAuthorize")
    public String qrAuthorize(@RequestParam("returnUrl") String returnUrl) {
        String url = projectUrlConfig.getWechatOpenAuthorize()+"/wechat/qrUserInfo";
        String redirectUrl = wxOpenService.buildQrConnectUrl(url,WxConsts.QrConnectScope.SNSAPI_LOGIN,URLEncoder.encode(returnUrl));
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/qrUserInfo")
    public String qrUserInfo (@RequestParam("code") String code,
                              @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        }catch (WxErrorException e) {
            log.error("【微信网页授权】{}",e);
            throw new SellException(ResultEnum.WX_MP_ERROR.getCode(),e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();

        return "redirect:"+returnUrl+"?openid="+openId;
    }
}

