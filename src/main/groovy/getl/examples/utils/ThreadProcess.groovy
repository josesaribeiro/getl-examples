package getl.examples.utils

import groovy.transform.BaseScript

@BaseScript getl.lang.Getl getl

runGroovyClass getl.examples.h2.H2Init

thread {
    useList listDatasets('samples:*', [EMBEDDEDTABLE])
    run(2) { table ->
        runGroovyClass ThreadProcessTable, { tableName = table }
    }
}