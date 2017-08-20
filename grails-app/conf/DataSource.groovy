dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    //hibernate.show_sql = true
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    test {
        
        //        dataSource {
        //            driverClassName = "org.postgresql.Driver"
        //            dialect = org.hibernate.dialect.PostgreSQLDialect
        //            username = "openpg"
        //            password = "openpgpwd"
        //            dbCreate = "create-drop"
        //            url = "jdbc:postgresql://localhost:5432/gestiflotteprod2"
        //        }
        logSql = true
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
       
            url = "jdbc:hsqldb:mem:devDB"
            
        
        }
    }
    development {
        dataSource {
            driverClassName = "org.postgresql.Driver"
            dialect = org.hibernate.dialect.PostgreSQLDialect
            username = "root"
            password = "root"
            dbCreate = "validate"
            //url = "jdbc:postgresql://localhost:5432/gestiflotteprodbackup"
            url = "jdbc:postgresql://localhost/gestiflotteouarid"
        }
    }
    production {
        dataSource {
            driverClassName = "org.postgresql.Driver"
            dialect = org.hibernate.dialect.PostgreSQLDialect
            username = "root"
            password = "root"
            dbCreate = "validate"
            //dbCreate = "create"
            jndiName = "jdbc/gestiflotte"
            //            url = "jdbc:postgresql://localhost:5432/gestiflotteouariddb4"
        }
    }
}
