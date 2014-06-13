package org.grails.datastore.mapping.cassandra.engine;

import org.grails.datastore.mapping.cassandra.CassandraSession;
import org.grails.datastore.mapping.engine.AssociationIndexer;
import org.grails.datastore.mapping.keyvalue.engine.KeyValueEntry;
import org.grails.datastore.mapping.model.PersistentEntity;
import org.grails.datastore.mapping.model.types.Association;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CassandraAssociationIndexer implements AssociationIndexer {

	private KeyValueEntry nativeEntry;
	private Association association;
	private CassandraSession session;
	private static Logger log = LoggerFactory.getLogger(CassandraAssociationIndexer.class);

	public CassandraAssociationIndexer(KeyValueEntry nativeEntry, Association association, CassandraSession cassandraSession) {
		this.nativeEntry = nativeEntry;
		this.association = association;
		this.session = cassandraSession;
	}

	@Override
	public void preIndex(Object primaryKey, List foreignKeys) {
		if (!association.isBidirectional()) {
			//			List keys = new ArrayList();
			//			for (Object foreignKey : foreignKeys) {
			//					keys.add(foreignKey);
			//			}
			// update the native entry directly.
			nativeEntry.put(association.getName(), foreignKeys);
		}
	}

	@Override
	public void index(Object primaryKey, List foreignKeys) {

	}

	@Override
	public void index(Object primaryKey, Object foreignKey) {

	}

	@Override
	public List query(Object primaryKey) {
		// for a unidirectional one-to-many we use the embedded keys
		if (!association.isBidirectional()) {
			final Object keys = nativeEntry.get(association.getName());
			if (!(keys instanceof Collection)) {
				return Collections.emptyList();
			}
			List indexedList = (keys instanceof List) ? (List)keys : new ArrayList(((Collection)keys));

			return indexedList;
		}

		//TODO Support Bidirectional
		log.warn("Bidirectional query not supported at this time returning null");
		return null;
	}

	@Override
	public PersistentEntity getIndexedEntity() {
		return association.getAssociatedEntity();
	}

}
