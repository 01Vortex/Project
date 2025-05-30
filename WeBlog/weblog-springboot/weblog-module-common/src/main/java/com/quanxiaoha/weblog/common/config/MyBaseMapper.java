package com.quanxiaoha.weblog.common.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: 01Vortex
 * @url: weblog.v4x.xyz
 * @date: 2023-06-13 22:50
 * @description: TODO
 **/
public interface MyBaseMapper<T> extends BaseMapper<T> {

    // 批量插入
    int insertBatchSomeColumn(@Param("list") List<T> batchList);

}
