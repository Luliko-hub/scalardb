package com.scalar.db.storage.cassandra;

import com.scalar.db.api.DistributedStorageColumnValueIntegrationTestBase;
import java.util.Properties;

public class CassandraColumnValueIntegrationTest
    extends DistributedStorageColumnValueIntegrationTestBase {
  @Override
  protected Properties getProperties() {
    return CassandraEnv.getProperties();
  }
}
