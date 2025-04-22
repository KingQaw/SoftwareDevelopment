package com.zhao.travelguide.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhao.travelguide.pojo.entity.Guides;
import com.zhao.travelguide.pojo.vo.me.GetFavoriteGuidesByUserIdVO;
import org.apache.ibatis.annotations.Param;

public interface GuidesMapper extends BaseMapper<Guides> {
    Page<GetFavoriteGuidesByUserIdVO> getFavoriteGuidesByUserId(Page<?> page, @Param("userId") Long userId);
}
