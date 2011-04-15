package org.resthub.test.dbunit.annotation;

import org.dbunit.operation.DatabaseOperation;

public enum DBOperation {

	NONE(DatabaseOperation.NONE),
    UPDATE(DatabaseOperation.UPDATE),
    INSERT(DatabaseOperation.INSERT),
    REFRESH(DatabaseOperation.REFRESH),
    DELETE(DatabaseOperation.DELETE),
    DELETE_ALL(DatabaseOperation.DELETE_ALL),
    TRUNCATE_TABLE(DatabaseOperation.TRUNCATE_TABLE),
    CLEAN_INSERT(DatabaseOperation.CLEAN_INSERT);

	private DatabaseOperation databaseOperation;

	private DBOperation(DatabaseOperation databaseOperation) {
		this.databaseOperation = DatabaseOperation.CLOSE_CONNECTION(DatabaseOperation.TRANSACTION(databaseOperation));
	}

	public DatabaseOperation getDbunitOperation() {
		return databaseOperation;
	}
	
}
