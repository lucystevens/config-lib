![Develop Status][workflow-badge-develop]
![Main Status][workflow-badge-main]
![Version][version-badge] 

# config-lib
**config-lib** is a configuration library for fetching properties from various sources

## Installation

Install the latest version of config-lib using Maven:

```	
<dependency>
	<groupId>uk.co.lukestevens</groupId>
	<artifactId>config-lib</artifactId>
	<version>2.0.0</version>
</dependency>
```

and the latest version of [base-lib][base-lib-repo]

### Github Packages Authentication
Currently public packages on Github require authentication to be installed by Maven. Add the following repository to your project's `.m2/settings.xml`

```
<repository>
	<id>github-lukecmstevens</id>
	<name>GitHub lukecmstevens Apache Maven Packages</name>
	<url>https://maven.pkg.github.com/lukecmstevens/packages</url>
	<snapshots><enabled>true</enabled></snapshots>
</repository>
```

For more information see here: [Authenticating with Github packages][gh-package-auth]

## Usage
This library contains several Config implementations for fetching properties from various sources:

### BaseConfig
`BaseConfig` is an abstract class providing most of the `Config` implementation logic. Extend this if you need to provide configuration from your own sources, defining the following:
 - `get(String)` - The base method to get a value for a key from the config source. This is expcted to return null if the property doesn't exist.
 - `entrySet()` - Returns a set of entries of properties currently in the config. Avoid implementing long fetches here and return cached values.
 - `load()` - This is called to load the initial cache of values from the config source and is where the heavy lifting should be done.

### PropertiesConfig
`PropertiesConfig` is a simple config implementation that uses a backing `Properties` object. Several other implementations extend this class.

```
Config propertiesConfig = new PropertiesConfig(properties);
```

### EnvironmentConfig
`EnvironmentConfig` as another simple config that wraps the System environment variables. Before being cached, they are normalised to the expected Java notation.
For example `APP_PORT` becomes `app.port`.

```
Config environmentConfig = new EnvironmentConfig();
```

The environment variables used by this can be mocked using the `EnvironmentVariableMocker` class from base-lib

### FileConfig
`FileConfig` loads properties using a file as source. This will also re-load properties from the file on every `load()` call

```
Config fileConfig = new FileConfig(file);
```

### DatabaseConfig
`DatabaseConfig` loads properties from a database using a `PropertyService` as an intermediary source. It requires the following table in the `core` schema:

| config |
| ------ |
| id SERIAL PRIMARY KEY |
| key VARCHAR NOT NULL |
| value VARCHAR NOT NULL |
| application_name VARCHAR NOT NULL |
| refresh_rate BIGINT NOT NULL |

This uses the application name from the `ApplicationProperties` to fetch properties which either match the application name or '*'.
The refresh rate is how long these properties are cached, before a new fetch from the database is forced.
As with the `FileConfig` a reload can be forced manually using `load()`.

```
PropertyService propertyService = new DatabasePropertyService(database, applicationProperties);
Config databaseConfig = new DatabaseConfig(propertyService);
```

### CompoundConfig
`CompoundConfig` uses a variable list of other configs as it's source, with the first in the list being highest priority in case of overridden keys.
This is useful in occasions where an application needs a base set of config to be able to be updated at runtime, or from several sources.

```
Config compoundConfig = new CompoundConfig(dbConfig, fileConfig, envConfig);
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

New features, fixes, and bugs should be branched off of develop.

Please make sure to update tests as appropriate.

## License
[MIT][mit-license]

[base-lib-repo]: https://github.com/lukecmstevens/base-lib
[gh-package-auth]: https://docs.github.com/en/free-pro-team@latest/packages/guides/configuring-apache-maven-for-use-with-github-packages#authenticating-to-github-packages
[workflow-badge-develop]: https://img.shields.io/github/workflow/status/lukecmstevens/config-lib/publish/develop?label=develop
[workflow-badge-main]: https://img.shields.io/github/workflow/status/lukecmstevens/config-lib/release/main?label=main
[version-badge]: https://img.shields.io/github/v/release/lukecmstevens/config-lib
[mit-license]: https://choosealicense.com/licenses/mit/