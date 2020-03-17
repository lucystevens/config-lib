UPDATE config.application_config set value='updated' WHERE key='string.property';

INSERT INTO config.application_config VALUES
(default, 'new.property', 'newproperty',  'test', 10);