package org.grails.datstore.gorm.cassandra

import org.grails.datastore.gorm.events.AutoTimestampEventListener
import org.grails.datastore.gorm.events.DomainEventListener
import org.grails.datastore.mapping.cassandra.CassandraDatastore
import org.grails.datastore.mapping.model.MappingContext
import org.springframework.beans.BeansException
import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

/**
 * Created by jeff.beck on 2/13/14.
 */
class CassandraDatastoreFactoryBean implements FactoryBean<CassandraDatastore>, ApplicationContextAware {

	ConfigObject config = [:]
	MappingContext mappingContext
	ApplicationContext applicationContext

	@Override
	CassandraDatastore getObject() throws Exception {
		CassandraDatastore datastore = new CassandraDatastore(mappingContext, applicationContext, config)

		applicationContext.addApplicationListener new DomainEventListener(datastore)
		applicationContext.addApplicationListener new AutoTimestampEventListener(datastore)

		return datastore
	}

	@Override
	Class<?> getObjectType() {
		return CassandraDatastore
	}

	@Override
	boolean isSingleton() {
		return true
	}
}
