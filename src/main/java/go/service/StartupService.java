package go.service;

import go.Spirit;
import go.support.SpiritAccessor;
import go.model.*;
import go.repo.*;
import eco.m1.M1;
import eco.m1.Q;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.jdbc.BasicDataSource;
import org.h2.tools.RunScript;
import xyz.goioc.Parakeet;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Map;

@Service
public class StartupService {

    @Inject
    Q q;

    @Inject
    UserRepo userRepo;

    @Inject
    RoleRepo roleRepo;

    @Inject
    StateRepo stateRepo;

    @Inject
    NationRepo nationRepo;

    @Inject
    SpiritAccessor spiritAccessor;

    public void init() throws Exception {

        runCreateScript();

        Parakeet.perch(spiritAccessor);

        Role superRole = roleRepo.find(Spirit.SUPER_ROLE);
        Role donorRole = roleRepo.find(Spirit.DONOR_ROLE);
        Role charityRole = roleRepo.find(Spirit.CHARITY_ROLE);

        if(superRole == null){
            superRole = new Role();
            superRole.setName(Spirit.SUPER_ROLE);
            roleRepo.save(superRole);
        }

        if(donorRole == null){
            donorRole = new Role();
            donorRole.setName(Spirit.DONOR_ROLE);
            roleRepo.save(donorRole);
        }

        if(charityRole == null){
            charityRole = new Role();
            charityRole.setName(Spirit.CHARITY_ROLE);
            roleRepo.save(charityRole);
        }

        User existing = userRepo.getByUsername(Spirit.SUPER_USERNAME);
        String password = Parakeet.dirty(Spirit.SUPER_PASSWORD);

        if(existing == null){
            User superUser = new User();
            superUser.setPhone("+19079878652");
            superUser.setUsername(Spirit.SUPER_USERNAME);
            superUser.setPassword(password);
            userRepo.saveAdministrator(superUser);
        }

        Long nationCount = nationRepo.getCount();
        Map<String, String[][]> nations = Spirit.getNations();
        if(nationCount == 0){
            for(Map.Entry<String, String[][]> nationEntry : nations.entrySet()){
                Nation nation = new Nation();
                nation.setName(nationEntry.getKey());
                Nation savedNation = nationRepo.save(nation);

                for(String[] stateEntry : nationEntry.getValue()){
                    State state = new State();
                    state.setName(stateEntry[0]);
                    state.setAbbreviation(stateEntry[1]);
                    state.setNationId(savedNation.getId());
                    stateRepo.save(state);
                }
            }
        }

        System.out.println("Users : " + userRepo.getCount());
        System.out.println("Roles : " + roleRepo.count());
        System.out.println("Nations : " + nationRepo.getCount());
        System.out.println("States : " + stateRepo.getCount());

    }

    private void runCreateScript() throws Exception {
        BasicDataSource dataSource = (BasicDataSource)q.getBean("datasource");
        Connection conn = dataSource.getConnection();
        String classPath = new File(M1.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
        if(classPath.endsWith(".jar")){
            classPath = Paths.get("src", "main", "resources", "sql")
                    .toAbsolutePath()
                    .toString();
        }
        String createSql = classPath.concat(File.separator).concat("create-db.sql");

        RunScript.execute(conn, new FileReader(createSql));
    }

    public void shutdown() throws Exception{
        System.out.println("shutdown...");
        runDropScript();
    }

    private void runDropScript() throws Exception {
        BasicDataSource dataSource = (BasicDataSource)Q.z.get("datasource").getBean();
        Connection conn = dataSource.getConnection();
        String classPath = new File(M1.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
        if(classPath.endsWith(".jar")){
            classPath = Paths.get("src", "main", "resources", "sql")
                    .toAbsolutePath()
                    .toString();
        }
        String dropDb = classPath.concat(File.separator).concat("drop-db.sql");

        RunScript.execute(conn, new FileReader(dropDb));
    }

}
