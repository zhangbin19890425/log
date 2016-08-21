package cn.sh.ideal.web.nginx.entity;

public class NginxAccessLogEntity {
	private String body_bytes_sent;
	private String host;
	private String http_referer;
	private String http_true_client_ip;
	private String http_user_agent;
	private String remote_user;
	private String request;
	private String source_ip;
	private String status;
	private String tags;
	private String timestamp;
	private String upstream_addr;
	private String upstream_response_time;
	private String upstreap_request_time;
	private String x_forword;
	public String getBody_bytes_sent() {
		return body_bytes_sent;
	}
	public void setBody_bytes_sent(String body_bytes_sent) {
		this.body_bytes_sent = body_bytes_sent;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getHttp_referer() {
		return http_referer;
	}
	public void setHttp_referer(String http_referer) {
		this.http_referer = http_referer;
	}
	public String getHttp_true_client_ip() {
		return http_true_client_ip;
	}
	public void setHttp_true_client_ip(String http_true_client_ip) {
		this.http_true_client_ip = http_true_client_ip;
	}
	public String getHttp_user_agent() {
		return http_user_agent;
	}
	public void setHttp_user_agent(String http_user_agent) {
		this.http_user_agent = http_user_agent;
	}
	public String getRemote_user() {
		return remote_user;
	}
	public void setRemote_user(String remote_user) {
		this.remote_user = remote_user;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getSource_ip() {
		return source_ip;
	}
	public void setSource_ip(String source_ip) {
		this.source_ip = source_ip;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getUpstream_addr() {
		return upstream_addr;
	}
	public void setUpstream_addr(String upstream_addr) {
		this.upstream_addr = upstream_addr;
	}
	public String getUpstream_response_time() {
		return upstream_response_time;
	}
	public void setUpstream_response_time(String upstream_response_time) {
		this.upstream_response_time = upstream_response_time;
	}
	public String getUpstreap_request_time() {
		return upstreap_request_time;
	}
	public void setUpstreap_request_time(String upstreap_request_time) {
		this.upstreap_request_time = upstreap_request_time;
	}
	public String getX_forword() {
		return x_forword;
	}
	public void setX_forword(String x_forword) {
		this.x_forword = x_forword;
	}
	@Override
	public String toString() {
		return "NginxAccessLogEntity [body_bytes_sent=" + body_bytes_sent + ", host=" + host + ", http_referer=" + http_referer
				+ ", http_true_client_ip=" + http_true_client_ip + ", http_user_agent=" + http_user_agent + ", remote_user="
				+ remote_user + ", request=" + request + ", source_ip=" + source_ip + ", status=" + status + ", tags=" + tags
				+ ", timestamp=" + timestamp + ", upstream_addr=" + upstream_addr + ", upstream_response_time="
				+ upstream_response_time + ", upstreap_request_time=" + upstreap_request_time + ", x_forword=" + x_forword
				+ "]";
	}
	
	
}
