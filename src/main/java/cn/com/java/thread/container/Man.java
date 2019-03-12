package cn.com.java.thread.container;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Man implements Delayed{

	@Override
	public int compareTo(Delayed o) {
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return 0;
	}

}
