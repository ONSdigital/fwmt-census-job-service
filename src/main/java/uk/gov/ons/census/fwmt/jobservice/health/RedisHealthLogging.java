package uk.gov.ons.census.fwmt.jobservice.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;

import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.REDIS_SERVICE_UP;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.REDIS_SERVICE_DOWN;

@Component
public class RedisHealthLogging extends AbstractHealthIndicator {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  private RedisConnectionFactory redisConnectionFactory;

  public RedisHealthLogging(@Qualifier("jedisConnectionFactory") RedisConnectionFactory connectionFactory) {
    super("Redis health check failed");
    Assert.notNull(connectionFactory, "ConnectionFactory must not be null");
    this.redisConnectionFactory = connectionFactory;
  }

  @Override
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    RedisConnection connection = RedisConnectionUtils.getConnection(this.redisConnectionFactory);
    try {
      if (connection instanceof RedisClusterConnection) {
        builder.up();
        gatewayEventManager.triggerEvent("<N/A>", REDIS_SERVICE_UP);
      } else {
        builder.down();
        gatewayEventManager.triggerErrorEvent(this.getClass(), "Cannot reach Redis", "<NA>", REDIS_SERVICE_DOWN);
      }
    }
    finally {
      RedisConnectionUtils.releaseConnection(connection, this.redisConnectionFactory);
    }
  }
}
