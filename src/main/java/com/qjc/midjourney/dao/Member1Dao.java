package com.qjc.midjourney.dao;

import com.qjc.midjourney.dto.Member1;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Member1Dao {

    int insert(List<Member1> resources);
}
