package uk.co.lukestevens.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.inject.Singleton;

/**
 * A SiteConfigService implementation that loads configs using a provider function
 * 
 * @author Luke Stevens
 */
@Singleton
public class GenericSiteConfigService implements SiteConfigService{
	
	private final Map<String, Config> configs = new HashMap<>();
	private final Function<String, Config> siteConfigProvider;
	
	/**
	 * Creates a new GenericSiteConfigService
	 * @param siteConfigProvider A provider function that,
	 * when supplied with a site name, returns a config for that site
	 */
	public GenericSiteConfigService(Function<String, Config> siteConfigProvider) {
		this.siteConfigProvider = siteConfigProvider;
	}

	@Override
	public Config get(String site) {
		Config conf = configs.get(site);
		if(conf == null) {
			conf = this.siteConfigProvider.apply(site);
			if(conf != null) {
				this.configs.put(site, conf);
			}
		}
		return conf;
	}

}
