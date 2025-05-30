package com.quanxiaoha.weblog.common.eventbus;

/**
 * @author: 01Vortex
 * @url: weblog.v4x.xyz
 * @date: 2023-07-02 9:20
 * @description: TODO
 **/
public interface EventListener {
    void handleEvent(ArticleEvent weblogEvent);
}
