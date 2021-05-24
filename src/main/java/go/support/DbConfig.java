package go.support;

import eco.m1.annotate.Configuration;
import eco.m1.annotate.Dependency;
import eco.m1.annotate.Prop;
import eco.m1.jdbc.BasicDataSource;

@Configuration
public class DbConfig {

    @Prop("db.url")
    String dbUrl;

    @Prop("db.user")
    String dbUser;

    @Prop("db.pass")
    String dbPass;

    @Prop("db.driver")
    String dbDriver;

    @Dependency
    public BasicDataSource dataSource(){
        return new BasicDataSource.Builder()
                            .driver(dbDriver)
                            .url(dbUrl)
                            .username(dbUser)
                            .password(dbPass)
                            .build();
    }

}
