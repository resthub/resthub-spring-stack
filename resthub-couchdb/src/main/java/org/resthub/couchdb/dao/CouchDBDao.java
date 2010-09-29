package org.resthub.couchdb.dao;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.support.CouchDbRepositorySupport;

/**
 * CouchDB implementation of SynchroDAO
 */
public abstract class CouchDBDao<T> {

	// ----------------------------------------------------------------------------------------------------------------
	// Properties
	
	/**
	 * Used for property injections
	 * 
	 * @param host Host of the CouchDB instance.
	 */
	public void setHost(String host) {
		this.host = host;
		// Creates another helper.
		createHelper();
	} // setHost().

	/**
	 * Used for property injections
	 * 
	 * @param port Port of the CouchDB instance.
	 */
	public void setPort(Integer port) {
		this.port = port;
		// Creates another helper.
		createHelper();
	} // setPort().

	/**
	 * Used for property injections
	 * 
	 * @param database Port of the CouchDB instance.
	 */
	public void setDatabase(String database) {
		this.database = database;
		// Creates another helper.
		createHelper();
	} // setDatabase().

	// ----------------------------------------------------------------------------------------------------------------
	// Protected attributes

	/**
	 * The class of manipulated documents.
	 */
	protected Class<T> cls;
	
	/**
	 * Host of the CouchDB instance.
	 */
	protected String host;
	
	/**
	 * Port of the CouchDB instance.
	 */
	protected Integer port;
	
	/**
	 * Database name.
	 */
	protected String database;
	
	/**
	 * Helper to manipulate instances of the specified class into CouchDB.
	 */
	protected InnerDao helper;
	
	/**
	 * Connector to the CouchDB instance.
	 */
	protected CouchDbConnector connector;
	
    // ----------------------------------------------------------------------------------------------------------------
	// Inner classes

	/**
	 * Wrapper class of erktop facilities to easily manipulate documents of specified class.
	 */
	protected class InnerDao extends CouchDbRepositorySupport<T> {
		
		/**
		 * Helper consructor.
		 * 
		 * @param connector Connector to CounchDB instance.
		 */
		InnerDao(CouchDbConnector connector) {
	        super(cls, connector);
		}
	} // class InnerDao().
	
    // ----------------------------------------------------------------------------------------------------------------
	// Protected methods
	
	/**
	 * Creates the CouchDB connector and helper, if all necessary properties have been set.
	 */
	protected void createHelper() {
		cls = getManagedClass();
		if (cls != null && database != null && port != null && host != null) {
			// Instanciate the tested DAO.
			HttpClient httpClient = new StdHttpClient.Builder()
		        .host(host)
		        .port(port)
		        .build();
			CouchDbInstance db = new StdCouchDbInstance(httpClient);
			// Supprime la base de données de test si besoin est.
			List<String> existingDB = db.getAllDatabases();
			if (!existingDB.contains(database)) {
				// Cré une nouvelle base.
				db.createDatabase(database);
			}
			connector = new StdCouchDbConnector(database, db);
			helper = new InnerDao(connector);
		}
	} // createHelper().

	/**
	 * Abstract method to be implemented in subclasses, to retrieve id from a document class.
	 * 
	 * @param document The document (may be null) to retrieve its id.
	 * @return The related id, may null for documents which do not have id yet.
	 */
	protected abstract String getId(T document);

	/**
	 * Used for property injections
	 * 
	 * @param cls The managed document's class.
	 */
	protected abstract Class<T> getManagedClass();

	// ----------------------------------------------------------------------------------------------------------------
	// Public methods
	
	public T save (T document) {
		if (getId(document) == null) {
			// New document : add it.
			helper.add(document);
		} else {
			// Existing document, update it.
			helper.update(document);
		}
		return document;
	}
	
	public void remove(String id) {
		T document = helper.get(id);
		helper.remove(document);
	}
	
	public T get(String id) {
		T document = null;
		try {
			document = helper.get(id);
		} catch (DocumentNotFoundException exc) {
			document = null;
		}
		return document;
	}
	
	public List<T> getAll() {
		return helper.getAll();
	}
	
} // CouchDBDAO
