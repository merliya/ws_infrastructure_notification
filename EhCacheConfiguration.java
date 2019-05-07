package com.jbhunt.infrastructure.notification.configuration;

import java.net.MalformedURLException;

import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.UrlResource;

import com.jbhunt.infrastructure.notification.properties.EhcacheProperties;

@Configuration
public class EhCacheConfiguration {
	
	private EhcacheProperties  ehcacheProperties;
	
	public EhCacheConfiguration(EhcacheProperties  ehcacheProperties){
		this.ehcacheProperties=ehcacheProperties;
	}

	@Bean
	public EhCacheManagerFactoryBean getEhCacheFactory() throws MalformedURLException   {
		EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
		factoryBean.setConfigLocation(new UrlResource(ehcacheProperties.getUrl()));
		return factoryBean;
	}

	@Bean
	@Primary
	public CacheManager platformNotificationCacheManager() throws MalformedURLException  {
		return new EhCacheCacheManager(getEhCacheFactory().getObject());
	}
}
