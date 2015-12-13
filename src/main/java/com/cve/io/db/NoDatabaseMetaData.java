package com.cve.io.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

/**
 * This is not a database meta data, but you could extend it ot make one.
 * @author curt
 */
class NoDatabaseMetaData implements DatabaseMetaData {

    @Override
    public boolean allProceduresAreCallable() throws SQLException {
        throw no();
    }

    @Override
    public boolean allTablesAreSelectable() throws SQLException {
        throw no();
    }

    @Override
    public String getURL() throws SQLException {
        throw no();
    }

    @Override
    public String getUserName() throws SQLException {
        throw no();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        throw no();
    }

    @Override
    public boolean nullsAreSortedHigh() throws SQLException {
        throw no();
    }

    @Override
    public boolean nullsAreSortedLow() throws SQLException {
        throw no();
    }

    @Override
    public boolean nullsAreSortedAtStart() throws SQLException {
        throw no();
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws SQLException {
        throw no();
    }

    @Override
    public String getDatabaseProductName() throws SQLException {
        throw no();
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        throw no();
    }

    @Override
    public String getDriverName() throws SQLException {
        throw no();
    }

    @Override
    public String getDriverVersion() throws SQLException {
        throw no();
    }

    @Override
    public int getDriverMajorVersion() {
        throw no();
    }

    @Override
    public int getDriverMinorVersion() {
        throw no();
    }

    @Override
    public boolean usesLocalFiles() throws SQLException {
        throw no();
    }

    @Override
    public boolean usesLocalFilePerTable() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        throw no();
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        throw no();
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        throw no();
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        throw no();
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        throw no();
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        throw no();
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        throw no();
    }

    @Override
    public String getIdentifierQuoteString() throws SQLException {
        throw no();
    }

    @Override
    public String getSQLKeywords() throws SQLException {
        throw no();
    }

    @Override
    public String getNumericFunctions() throws SQLException {
        throw no();
    }

    @Override
    public String getStringFunctions() throws SQLException {
        throw no();
    }

    @Override
    public String getSystemFunctions() throws SQLException {
        throw no();
    }

    @Override
    public String getTimeDateFunctions() throws SQLException {
        throw no();
    }

    @Override
    public String getSearchStringEscape() throws SQLException {
        throw no();
    }

    @Override
    public String getExtraNameCharacters() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsColumnAliasing() throws SQLException {
        throw no();
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsConvert() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsConvert(int arg0, int arg1) throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsTableCorrelationNames() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsOrderByUnrelated() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsGroupBy() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsGroupByUnrelated() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsLikeEscapeClause() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsMultipleResultSets() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsMultipleTransactions() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsNonNullableColumns() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsANSI92FullSQL() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsOuterJoins() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws SQLException {
        throw no();
    }

    @Override
    public String getSchemaTerm() throws SQLException {
        throw no();
    }

    @Override
    public String getProcedureTerm() throws SQLException {
        throw no();
    }

    @Override
    public String getCatalogTerm() throws SQLException {
        throw no();
    }

    @Override
    public boolean isCatalogAtStart() throws SQLException {
        throw no();
    }

    @Override
    public String getCatalogSeparator() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsPositionedDelete() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsPositionedUpdate() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSelectForUpdate() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsStoredProcedures() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSubqueriesInExists() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSubqueriesInIns() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsUnion() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsUnionAll() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxBinaryLiteralLength() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxCharLiteralLength() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxColumnNameLength() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxColumnsInGroupBy() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxColumnsInIndex() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxColumnsInOrderBy() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxColumnsInSelect() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxColumnsInTable() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxConnections() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxCursorNameLength() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxIndexLength() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxSchemaNameLength() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxProcedureNameLength() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxCatalogNameLength() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxRowSize() throws SQLException {
        throw no();
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxStatementLength() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxStatements() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxTableNameLength() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxTablesInSelect() throws SQLException {
        throw no();
    }

    @Override
    public int getMaxUserNameLength() throws SQLException {
        throw no();
    }

    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsTransactions() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        throw no();
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        throw no();
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getProcedures(String arg0, String arg1, String arg2) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getProcedureColumns(String arg0, String arg1, String arg2, String arg3) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getTables(String arg0, String arg1, String arg2, String[] arg3) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getColumns(String arg0, String arg1, String arg2, String arg3) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getColumnPrivileges(String arg0, String arg1, String arg2, String arg3) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getTablePrivileges(String arg0, String arg1, String arg2) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getBestRowIdentifier(String arg0, String arg1, String arg2, int arg3, boolean arg4) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getVersionColumns(String arg0, String arg1, String arg2) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getPrimaryKeys(String arg0, String arg1, String arg2) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getImportedKeys(String arg0, String arg1, String arg2) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getExportedKeys(String arg0, String arg1, String arg2) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getCrossReference(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getIndexInfo(String arg0, String arg1, String arg2, boolean arg3, boolean arg4) throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsResultSetType(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsResultSetConcurrency(int arg0, int arg1) throws SQLException {
        throw no();
    }

    @Override
    public boolean ownUpdatesAreVisible(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean ownDeletesAreVisible(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean ownInsertsAreVisible(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean othersUpdatesAreVisible(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean othersDeletesAreVisible(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean othersInsertsAreVisible(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean updatesAreDetected(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean deletesAreDetected(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean insertsAreDetected(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsBatchUpdates() throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getUDTs(String arg0, String arg1, String arg2, int[] arg3) throws SQLException {
        throw no();
    }

    @Override
    public Connection getConnection() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsSavepoints() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsNamedParameters() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsMultipleOpenResults() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getSuperTypes(String arg0, String arg1, String arg2) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getSuperTables(String arg0, String arg1, String arg2) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getAttributes(String arg0, String arg1, String arg2, String arg3) throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsResultSetHoldability(int arg0) throws SQLException {
        throw no();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        throw no();
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
        throw no();
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
        throw no();
    }

    @Override
    public int getJDBCMajorVersion() throws SQLException {
        throw no();
    }

    @Override
    public int getJDBCMinorVersion() throws SQLException {
        throw no();
    }

    @Override
    public int getSQLStateType() throws SQLException {
        throw no();
    }

    @Override
    public boolean locatorsUpdateCopy() throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsStatementPooling() throws SQLException {
        throw no();
    }

    @Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getSchemas(String arg0, String arg1) throws SQLException {
        throw no();
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        throw no();
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getFunctions(String arg0, String arg1, String arg2) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getFunctionColumns(String arg0, String arg1, String arg2, String arg3) throws SQLException {
        throw no();
    }

    @Override
    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        throw no();
    }

    @Override
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        throw no();
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        throw no();
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        throw no();
    }

    private static UnsupportedOperationException no() {
        return new UnsupportedOperationException();
    }
}
