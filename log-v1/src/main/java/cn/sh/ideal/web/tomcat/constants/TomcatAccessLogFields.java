package cn.sh.ideal.web.tomcat.constants;
 
public interface TomcatAccessLogFields {
	//访问状态码
	String http_status = "http_status";
	//访问页面地址
	String visit_url = "visit_url";
	//页面响应时间
	String visit_time = "visit_time";
	//页面响应时间毫秒
	String visit_contined_time_mill = "visit_contined_time_mill";
	//本地服务器的IP
	String localhost_ip = "localhost_ip";
	//本地服务器host
	String host = "host";
}
