package com.coder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("normal")
public class alphaDaoImp implements alphaDao {
    @Override
    public String select() {
        return "Dao";
    }
}
