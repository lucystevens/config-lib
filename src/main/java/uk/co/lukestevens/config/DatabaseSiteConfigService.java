package uk.co.lukestevens.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DatabaseSiteConfigService implements SiteConfigService{
	
	private final Map<String, Config> configs = new HashMap<>();
	private final Function<String, Config> siteConfigProvider;
	
	public DatabaseSiteConfigService(Function<String, Config> siteConfigProvider) {
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
