package com.yang.sell.service.impl;

import com.yang.sell.dataobject.SellerInfo;
import com.yang.sell.repository.SellerInfoRepository;
import com.yang.sell.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService{

    @Autowired
    SellerInfoRepository repository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }
}

