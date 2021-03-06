/**
 * Define Hive tables
 */
package getl.examples.hive

import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Load configuration file
runGroovyClass getl.examples.utils.Config, true

forGroup 'hive'

// Hive database connection
useHiveConnection hiveConnection('con', true) {
    // Use parameters from [connections.hive] section
    useConfig 'hive'
    schemaName = 'getl_demo'
    sqlHistoryFile = "${configContent.workPath}/hive.{date}.sql"
}

// Price table
hiveTable('prices', true) {
    tableName = 'prices'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { type = stringFieldType; isNull = false; length = 50 }
    field('create_date') { type = datetimeFieldType; isNull = false }
    field('price') { type = numericFieldType; isNull = false; length = 9; precision = 2 }
    field('is_active') { type = booleanFieldType; isNull = false }
    field('description') { type = stringFieldType; length = 8000 }

    createOpts {
        storedAs = 'ORC'
        clustered {
            by = ['id']
            intoBuckets = 2
        }
        tblproperties.transactional = true
    }
}

// Customers table
hiveTable('customers', true) { table ->
    tableName = 'customers'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { length = 50 }
    field('customer_type') { length = 10 }

    createOpts {
        storedAs = 'ORC'
        clustered {
            by = ['id']
            intoBuckets = 2
        }
        tblproperties.transactional = true
    }
}

// Customer phones table
hiveTable('customers.phones', true) { table ->
    tableName = 'customer_phones'
    field('customer_id') { type = integerFieldType; isKey = true }
    field('phone') { length = 50; isKey = true }

    createOpts {
        storedAs = 'ORC'
        clustered {
            by = ['customer_id']
            intoBuckets = 2
        }
        tblproperties.transactional = true
    }
}

// Sales table
hiveTable('sales', true) {
    tableName = 'sales'
    field('id') { type = bigintFieldType; isKey = true }
    field('price_id') { type = integerFieldType; isNull = false }
    field('customer_id') { type = integerFieldType; isNull = false }
    field('sale_date') { type = datetimeFieldType; isNull = false }
    field('sale_count') { type = bigintFieldType; isNull = false }
    field('sale_sum') { type = numericFieldType; isNull = false; length = 12; precision = 2 }
    createOpts {
        storedAs = 'ORC'
        clustered {
            by = ['id']
            intoBuckets = 2
        }
        tblproperties.transactional = true
    }
}