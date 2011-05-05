package org.resthub.test.dbunit.annotation;

import org.dbunit.operation.DatabaseOperation;

/**
 * Defines what DBUnit operation must be performed.
 * 
 * <p>
 * Each corresponding DBUnit operation is surrounded by a
 * {@link DatabaseOperation#CLOSE_CONNECTION(DatabaseOperation)} and a
 * {@link DatabaseOperation#TRANSACTION(DatabaseOperation)} operation.
 * </p>
 * 
 * @author a131199
 * @see DatabaseOperation
 * 
 */
public enum DBOperation {

	/**
	 * @see DatabaseOperation#NONE
	 */
	NONE(DatabaseOperation.NONE),

	/**
	 * @see DatabaseOperation#UPDATE
	 */
	UPDATE(DatabaseOperation.UPDATE),

	/**
	 * @see DatabaseOperation#INSERT
	 */
	INSERT(DatabaseOperation.INSERT),

	/**
	 * @see DatabaseOperation#REFRESH
	 */
	REFRESH(DatabaseOperation.REFRESH),

	/**
	 * @see DatabaseOperation#DELETE
	 */
	DELETE(DatabaseOperation.DELETE),

	/**
	 * @see DatabaseOperation#DELETE_ALL
	 */
	DELETE_ALL(DatabaseOperation.DELETE_ALL),

	/**
	 * @see DatabaseOperation#TRUNCATE_TABLE
	 */
	TRUNCATE_TABLE(DatabaseOperation.TRUNCATE_TABLE),

	/**
	 * @see DatabaseOperation#CLEAN_INSERT
	 */
	CLEAN_INSERT(DatabaseOperation.CLEAN_INSERT);

	private DatabaseOperation databaseOperation;

	private DBOperation(DatabaseOperation databaseOperation) {
		this.databaseOperation = DatabaseOperation
				.CLOSE_CONNECTION(DatabaseOperation
						.TRANSACTION(databaseOperation));
	}

	public DatabaseOperation getDbunitOperation() {
		return databaseOperation;
	}

}
