package com.lanshan.web.admin.redis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
	
	@Autowired
    protected RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 对map数据类型进行操作
	 */
	@Resource
    protected HashOperations<String, String, Object> hashOperations;
	
	/**
	 * key-value操作
	 */
	@Resource
	protected ValueOperations<String, Object> valueOperations;
	
	public void setObj(String key,Object value){
		valueOperations.set(key, value);
	}
	
	public void setObj(String key,Object value, long expire){
		if(expire > 0){
			valueOperations.set(key, value, expire, TimeUnit.SECONDS);
		}else{
			setObj(key, value);
		}
	}
	
	public Object getObj(String key){
		return valueOperations.get(key);
	}
	
	public void delObj(String key){
		valueOperations.getOperations().delete(key);
	}
	
	public Boolean existsObj(String key){
		return valueOperations.getOperations().hasKey(key);
	}
	
	public Boolean exists(String key, String hashKey){
		return hashOperations.hasKey(key, hashKey);
	}
	
	public void set(String key, String hashKey, Object value){
		hashOperations.put(key, hashKey, value);
	}
	
	public void set(String key, String hashKey, Object value, long expire){
		hashOperations.put(key, hashKey, value);
		if (expire != -1) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
	    }
	}
	
	public Object get(String key, String hashKey){
		return hashOperations.get(key, hashKey);
	}
	
	public void del(String key, String hashKey){
		hashOperations.delete(key, hashKey);
	}
	
	public void empty(String key){
		Set<String> set = hashOperations.keys(key);
        set.stream().forEach(k -> hashOperations.delete(key, k));
	}
}
