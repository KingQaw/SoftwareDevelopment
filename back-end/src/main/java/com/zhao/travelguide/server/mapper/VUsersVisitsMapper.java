package com.zhao.travelguide.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhao.travelguide.pojo.entity.VUsersVisits;
import com.zhao.travelguide.pojo.vo.admin.RegionCount;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface VUsersVisitsMapper extends BaseMapper<VUsersVisits> {
    @Select("SELECT REGION AS name, COUNT(*) AS value FROM v_users_visits GROUP BY REGION")
    public List<RegionCount> countByRegion();
}
