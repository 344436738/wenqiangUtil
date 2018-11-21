package test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class TestList {
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/*
	 * 右压栈 : 后添加的元素排在后边
	 */
	@Test
	public void testSetValue1(){
		redisTemplate.boundListOps("namelist1").rightPush("刘备");
		redisTemplate.boundListOps("namelist1").rightPush("关羽");
		redisTemplate.boundListOps("namelist1").rightPush("张飞");
	}
	
	/**
	 * 显示右压栈的值
	 */
	@Test
	public void testGetValue1(){
		List list = redisTemplate.boundListOps("namelist1").range(0, 10);
		System.out.println(list);
	}
	
	@Test
	public void delete(){
		redisTemplate.delete("namelist1");
	}
	
	/**
	 * 左压栈
	 */
	@Test
	public void testSetValue2(){
		redisTemplate.boundListOps("namelist2").leftPush("刘备");
		redisTemplate.boundListOps("namelist2").leftPush("关羽");
		redisTemplate.boundListOps("namelist2").leftPush("张飞");
	}
	
	/**
	 * 显示左压栈的值
	 */
	@Test
	public void testGetValue2(){
		List list = redisTemplate.boundListOps("namelist2").range(0, 10);
		System.out.println(list);
	}
	
	/**
	 * 删除值
	 */
	@Test
	public void removeValue(){
		redisTemplate.boundListOps("namelist1").remove(0, "刘备");
	}

}
