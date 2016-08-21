package cn.sh.ideal.web.tomcat.entity;

public class TomcatAccessLogEntity {
	private String bytes_send;
	private String bytes_send_excluding_http_header;
	private String host;
	private String http_status;
	private String identd_user;
	private String identd_username;
	private String local_port;
	private String localhost_ip;
	private String localhost_name;
	private String server_name;
	private String session_id;
	private String visit_aggrement;
	private String visit_contined_time_mill;
	private String visit_contined_time_second;
	private String visit_ip;
	public String getBytes_send() {
		return bytes_send;
	}
	public void setBytes_send(String bytes_send) {
		this.bytes_send = bytes_send;
	}
	public String getBytes_send_excluding_http_header() {
		return bytes_send_excluding_http_header;
	}
	public void setBytes_send_excluding_http_header(String bytes_send_excluding_http_header) {
		this.bytes_send_excluding_http_header = bytes_send_excluding_http_header;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getHttp_status() {
		return http_status;
	}
	public void setHttp_status(String http_status) {
		this.http_status = http_status;
	}
	public String getIdentd_user() {
		return identd_user;
	}
	public void setIdentd_user(String identd_user) {
		this.identd_user = identd_user;
	}
	public String getIdentd_username() {
		return identd_username;
	}
	public void setIdentd_username(String identd_username) {
		this.identd_username = identd_username;
	}
	public String getLocal_port() {
		return local_port;
	}
	public void setLocal_port(String local_port) {
		this.local_port = local_port;
	}
	public String getLocalhost_ip() {
		return localhost_ip;
	}
	public void setLocalhost_ip(String localhost_ip) {
		this.localhost_ip = localhost_ip;
	}
	public String getLocalhost_name() {
		return localhost_name;
	}
	public void setLocalhost_name(String localhost_name) {
		this.localhost_name = localhost_name;
	}
	public String getServer_name() {
		return server_name;
	}
	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getVisit_aggrement() {
		return visit_aggrement;
	}
	public void setVisit_aggrement(String visit_aggrement) {
		this.visit_aggrement = visit_aggrement;
	}
	public String getVisit_contined_time_mill() {
		return visit_contined_time_mill;
	}
	public void setVisit_contined_time_mill(String visit_contined_time_mill) {
		this.visit_contined_time_mill = visit_contined_time_mill;
	}
	public String getVisit_contined_time_second() {
		return visit_contined_time_second;
	}
	public void setVisit_contined_time_second(String visit_contined_time_second) {
		this.visit_contined_time_second = visit_contined_time_second;
	}
	public String getVisit_ip() {
		return visit_ip;
	}
	public void setVisit_ip(String visit_ip) {
		this.visit_ip = visit_ip;
	}
	
	
}
