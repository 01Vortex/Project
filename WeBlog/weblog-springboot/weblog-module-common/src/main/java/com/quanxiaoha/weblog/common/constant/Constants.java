package com.quanxiaoha.weblog.common.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * @author: 01Vortex
 * @url: weblog.v4x.xyz
 * @date: 2023-07-01 16:37
 * @description: 全局静态变量
 **/
public interface Constants {

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    DateFormat MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");
}
