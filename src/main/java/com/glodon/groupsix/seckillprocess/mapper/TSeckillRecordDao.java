package com.glodon.groupsix.seckillprocess.mapper;

import com.glodon.groupsix.seckillprocess.models.vo.TSeckillRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TSeckillRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TSeckillRecord record);

    int insertSelective(TSeckillRecord record);

    TSeckillRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSeckillRecord record);

    int updateByPrimaryKey(TSeckillRecord record);

    @Select("select * from t_seckill_record where phone = #{phone}")
    List<TSeckillRecord> getRecordByPhone(@Param("phone")String phone);

    @Select("select phone from t_seckill_record where commodity_id = #{commodityId}")
    List<String> getRecordByCommodityId(@Param("commodityId") String commodityId);

    @Select("select count(*) from t_seckill_record where commodity_id = #{commodityId}")
    int getRecordCountByCommodityId(@Param("commodityId")String commodityId);
}
