package org.grails.datastore.mapping.cassandra

import org.junit.Test
import org.grails.datastore.mapping.cassandra.uuid.UUIDUtil
import org.grails.datastore.mapping.core.Session
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.support.GenericApplicationContext

/**
 * @author Graeme Rocher
 * @since 1.1
 */
class CassandraEntityPersisterTest extends AbstractCassandraTest {

	@Test
	void testReadWrite() {
		def ctx = new GenericApplicationContext()
		ctx.refresh()

		ConfigObject config = new ConfigObject()
		config.put("contactPoints", "jeff-cassandra.dev.wh.reachlocal.com")
		def ds = new CassandraDatastore(new CassandraMappingContext(CassandraDatastore.DEFAULT_KEYSPACE), ctx, config)

		ds.mappingContext.addPersistentEntity(TestEntity)
		Session conn = ds.connect(null)
		conn.applicationEventPublisher = new ApplicationEventPublisher() {
			@Override
			void publishEvent(ApplicationEvent applicationEvent) {
				println applicationEvent
			}
		}


		def t = conn.retrieve(TestEntity, UUIDUtil.getRandomUUID())


		assert t == null

		t = new TestEntity(name: "Bob", age: 45)

		conn.persist(t)

		conn.flush()
		assert t.id != null

		def t2 = conn.retrieve(TestEntity, t.id)

		println t2.id.toString() + " - " + t2.name

		assert t2 != null
		assert "Bob" == t2.name
		assert 45 == t2.age
		assert t2.id != null


		t.age = 55
		conn.persist(t)
		conn.flush()
		t = conn.retrieve(TestEntity, t.id)

		assert 55 == t.age

		def uuid = t.id
		conn.delete(t)
		conn.flush()

		def deletedEntity = conn.retrieve(TestEntity, uuid)
		assert deletedEntity == null

	}
}

class TestEntity {
	UUID id
	String name
	int age
}
