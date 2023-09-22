package com.coder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class alphaDaoMybatis implements alphaDao {

    @Override
    public String select() {
        return "Mybatis";
    }
    
}
