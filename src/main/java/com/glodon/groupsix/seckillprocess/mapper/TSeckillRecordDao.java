package com.glodon.groupsix.seckillprocess.mapper;

import com.glodon.groupsix.seckillprocess.models.vo.TSeckillRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TSeckillRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TSeckillRecord record);

    int insertSelective(TSeckillRecord record);

    TSeckillRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSeckillRecord record);

    int updateByPrimaryKey(TSeckillRecord record);
}
