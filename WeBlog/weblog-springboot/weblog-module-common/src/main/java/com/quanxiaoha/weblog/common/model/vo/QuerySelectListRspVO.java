package com.quanxiaoha.weblog.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 01Vortex
 * @url: weblog.v4x.xyz
 * @date: 2023-04-19 16:06
 * @description: TODO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuerySelectListRspVO {
    private String label;
    private Long value;
}
