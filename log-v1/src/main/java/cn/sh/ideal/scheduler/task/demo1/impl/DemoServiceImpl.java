package cn.sh.ideal.scheduler.task.demo1.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.sh.ideal.scheduler.task.demo1.DemoService;

@Service
public class DemoServiceImpl implements DemoService {
	//Scheduled(cron = "*/10 * * * * ?")
	@Override
	public void sayHello() throws Exception {
		System.out.println("hello ....");
	}

}
