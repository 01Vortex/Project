package com.quanxiaoha.weblog.common.utils;

import com.quanxiaoha.weblog.common.enums.ResponseCodeEnum;
import com.quanxiaoha.weblog.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.lionsoul.ip2region.xdb.Searcher;
import java.io.InputStream;


/**
 * @author yjf
 * @description 访客IP归属地工具类
 */
@Slf4j
public class AgentRegionUtils {

    /**
     * 获取http请求ip
     * @param request 请求
     * @return ipAddress
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    assert inet != null;
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割 "***.***.***.***".length()
            if (ipAddress != null && ipAddress.length() > 15) {
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * 根据ip 获取归属地
     * @param ip 访问ip
     * @return 返回归属地结果
     */


    public static String getIpRegion(String ip, String xdbPath) {
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip) ||
                "127.0.0.1".equals(ip) || "::1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            return "局域网";
        }

        try (InputStream is = AgentRegionUtils.class.getClassLoader().getResourceAsStream("ip2region.xdb")) {
            if (is == null) {
                log.error("ip2region.xdb 文件未找到，请确认已放置在 resources 目录下");
                return "未知";
            }

            byte[] cBuff = readStream(is);
            Searcher searcher = Searcher.newWithBuffer(cBuff);
            return searcher.search(ip);
        } catch (Exception e) {
            log.error("IP归属地查询失败，IP地址：{}", ip, e);
            return "外太空";
        }
    }


// 辅助方法：将 InputStream 读为 byte[]
private static byte[] readStream(InputStream is) throws Exception {
    byte[] buffer = new byte[1024];
    int len;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while ((len = is.read(buffer)) != -1) {
        bos.write(buffer, 0, len);
    }
    return bos.toByteArray();
}

}
