package core;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * @author Ahmed Yusuf
 * used for pooling connection on Redis server
 */
public class MyJedisPool {
    private static MyJedisPool instance;
    private static JedisPool jedisPool;

    public static MyJedisPool getInstance() {
        if(instance == null)
            instance = new MyJedisPool();
        return instance;
    }

    public MyJedisPool() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(10);
        genericObjectPoolConfig.setMinIdle(10);
        genericObjectPoolConfig.setMaxIdle(10);
        jedisPool = new JedisPool(genericObjectPoolConfig,
                "localhost", 6379, 30000);
    }

    public void put(String key, String value){
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
        System.out.println("inserted key: "+key+" value: "+value);
        jedis.close();
    }

    public void putInt(String key, int value){
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, String.valueOf(value));
        jedis.close();
    }

    public String get(String key){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    public int getInt(String key){
        int result = 0;
        try {
            Jedis jedis = jedisPool.getResource();
            result = Integer.parseInt(jedis.get(key));
            jedis.close();
        }catch (Exception e){}
        return result;
    }

    public long getAndIncrement(String key){
        long result = 0;
        Jedis jedis = jedisPool.getResource();
        try {
            result = jedis.incr(key);
        }catch (JedisDataException e){
            jedis.set(key, "1");
            result = 1;
        }
        jedis.close();
        return result-1;
    }

    public long incrementAndGet(String key){
        long result = 0;
        Jedis jedis = jedisPool.getResource();
        try {
            result = jedis.incr(key);
        }catch (JedisDataException e){
            jedis.set(key, "1");
            result = 1;
        }
        jedis.close();
        return result;
    }

    public static JedisPool getJedisPool() {
        return jedisPool;
    }

}
