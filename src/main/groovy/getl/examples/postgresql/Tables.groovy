/**
 * Define PostgreSQL tables
 */
package getl.examples.postgresql

import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Load configuration file
runGroovyClass getl.examples.utils.Config, true

forGroup 'postgresql'

// PostgreSQL database connection
usePostgresqlConnection postgresqlConnection('con', true) {
    useConfig 'postgresql'
    schemaName = 'getl_demo'
    sqlHistoryFile = "${configContent.workPath}/postgresql.{date}.sql"
}

// History table for incremental load
historypoint('history', true) {
    schemaName = connection.schemaName
    tableName = 'history_point'
    saveMethod = mergeSave
}

// Price table
postgresqlTable('prices', true) {
    tableName = 'prices'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { type = stringFieldType; isNull = false; length = 50 }
    field('create_date') { type = datetimeFieldType; isNull = false }
    field('price') { type = numericFieldType; isNull = false; length = 9; precision = 2 }
    field('is_active') { type = booleanFieldType; isNull = false }
    field('description') { type = textFieldType }
}

// Customers table
postgresqlTable('customers', true) { table ->
    tableName = 'customers'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { length = 50 }
    field('customer_type') { length = 10 }
}

// Customer phones table
postgresqlTable('customers.phones', true) { table ->
    tableName = 'customer_phones'
    field('customer_id') { type = integerFieldType; isKey = true }
    field('phone') { length = 50; isKey = true }
}

// Sales table
postgresqlTable('sales', true) {
    tableName = 'sales'
    field('id') { type = bigintFieldType; isKey = true }
    field('price_id') { type = integerFieldType; isNull = false }
    field('customer_id') { type = integerFieldType; isNull = false }
    field('sale_date') { type = datetimeFieldType; isNull = false }
    field('sale_count') { type = bigintFieldType; isNull = false }
    field('sale_sum') { type = numericFieldType; isNull = false; length = 12; precision = 2 }
}

// Query to get a list of months of sales
query('sales.part', true) {
    setQuery """SELECT DISTINCT Date_Trunc('month', sale_date) as month 
                FROM ${postgresqlTable('sales').fullTableName}
                ORDER BY month"""
}