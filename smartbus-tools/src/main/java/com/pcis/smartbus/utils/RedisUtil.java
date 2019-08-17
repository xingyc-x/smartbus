package com.pcis.smartbus.utils;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /*
     *	 通用的删除，存在测试
     */
    public void Delete(String key)
    {
    	redisTemplate.delete(key);
    }
    public boolean Has(String key)
    {
    	return redisTemplate.hasKey(key);
    }
    /*
     *	 对普通键值对缓存的增删查改
     */
    public void ComSet(String key,Object item)
    {
    	redisTemplate.opsForValue().set(key, item);
    }
    
    public Object ComGet(String key)
    {
    	return redisTemplate.opsForValue().get(key);
    }
    
    public void ComUpdate(String key,Object obj)
    {
    	redisTemplate.opsForValue().getAndSet(key, obj);
    }
    
    /*
     * hash的增删查改
     */
    public void HashSet(String key,String itemKey,Object obj)
    {
    	redisTemplate.opsForHash().put(key, itemKey, obj);
    }
    
    public void HashSetAll(String key,Map<String, Object> m)
    {
    	redisTemplate.opsForHash().putAll(key, m);
    }
    
    public Object HashGet(String key,String itemKey)
    {
    	return redisTemplate.opsForHash().get(key, itemKey);
    }
    
    public Map<Object,Object> HashGetAll(String key)
    {
    	return redisTemplate.opsForHash().entries(key);
    }
    public void HashDel(String key,String itemKey)
    {
    	redisTemplate.opsForHash().delete(key, itemKey);
    }
    /*
     * list的增删查改
     */
    public List<Object> ListGet(String key, long start, long end) {
            return redisTemplate.opsForList().range(key, start, end);
        }
    public void ListSet(String key, Object value) {
    	redisTemplate.opsForList().rightPush(key, value);
    }
    public void ListSetL(String key, Object value) {
    	redisTemplate.opsForList().leftPush(key, value);
    }
    public Object ListGetOne(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }
}
