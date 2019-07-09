package com.util.commons.service;

import com.util.commons.dao.easyReporsitoy;
import com.util.commons.entity.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EasyServiceImpl implements EasyService {

    @Autowired
    private easyReporsitoy  easyReporsitoy;

    @Override
    public List<TbUser> findAll() {
        List<TbUser> all = easyReporsitoy.findAll();
        return all;
    }
}
