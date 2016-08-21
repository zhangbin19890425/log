package cn.sh.ideal.web.nginx.constants;

public interface NginxAccessLogFields {
	     //状态码
         String status = "status";
         //客户端ip地址
         String source_ip = "source_ip";
         //访问用户
         String remote_user = "remote_user";
         //访问时间
         String timestamp = "timestamp";
         //发出请求 包含url和http协议
         String request = "request";
         //主体回复字节
         String body_bytes_sent = "body_bytes_sent";
         //来源页面
         String http_referer = "http_referer";
         //用户请求信息
         String http_user_agent = "http_user_agent";
         //页面处理时间
         String upstreap_request_time = "upstreap_request_time";
       //页面处理时间
         String upstreap_response_time = "upstreap_response_time";
         //服务器地址
         String upstream_addr = "upstream_addr";
         //机器名
         String host = "host";
}
