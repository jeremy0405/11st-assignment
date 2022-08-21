package com.example.elevenstreet.config;

import java.util.Objects;
import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public EhCacheManagerFactoryBean cacheManagerFactoryBean() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setShared(true);
		return ehCacheManagerFactoryBean;
	}

	@Bean
	public EhCacheCacheManager ehCacheCacheManager() {
		CacheConfiguration productsCacheConfiguration = new CacheConfiguration()
			.name("getProducts")
			.maxEntriesLocalHeap(1000)
			.eternal(false)
			.timeToIdleSeconds(0)
			.timeToLiveSeconds(60)
			.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU);

		Cache productsCache = new Cache(productsCacheConfiguration);

		if (!Objects.requireNonNull(cacheManagerFactoryBean().getObject()).cacheExists("getProducts")) {
			Objects.requireNonNull(cacheManagerFactoryBean().getObject()).addCache(productsCache);
		}

		return new EhCacheCacheManager(Objects.requireNonNull(cacheManagerFactoryBean().getObject()));
	}
}
