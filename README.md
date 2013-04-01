
Extended Authentication plugin for Liferay
------------------------------------------

This plugin provides additional settings for user authentication mechanism in Liferay.

  - Maximum number of sessions per user, by default **3**
  > Can be configured in portal-ext.properties with key **auth.user.max.session.count=2**  
  > **0** value disables session count limit  
  > Also can be configured for each user separately through additional fields of user. Setting specified for user overrides global setting.

  - Restrict user access allowing to login only from specified IP addresses
  > Each user configured from Control panel through additional fields. Supported values are IPv4 and IPv6 single values, range values or CIDR notation, for example:  
  >> **192.168.31.1**  
  >> **192.168.31.1/27**  
  >> **192.168.31.1-192.168.31.30**  
  >> **::1**  
  >> **::1/128**  
  >> **fe80::226:2dff:fefa:0/112**  
  >> **fe80::226:2dff:fefa:cd1f-fe80::226:2dff:fefa:ffff**

  - Allow to user work with system only for specified period of time
  > This option also available in additional fields of user within Control panel. To apply this option to work, you should set attribute 'Access by date enabled' to **True**