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
    TownRepo townRepo;

    @Inject
    StateRepo stateRepo;

    @Inject
    NationRepo nationRepo;

    @Inject
    OrganizationRepo organizationRepo;

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

                String[] stateNames = nationEntry.getValue()[0];
                String[] abbreviations = nationEntry.getValue()[1];
                for(int z = 0; z < stateNames.length; z++){
                    State state = new State();
                    state.setName(stateNames[z]);
                    state.setAbbreviation(abbreviations[z]);
                    state.setNationId(savedNation.getId());
                    stateRepo.save(state);
                }
            }
        }

        System.out.println("Users : " + userRepo.getCount());
        System.out.println("Roles : " + roleRepo.count());
        System.out.println("Nations : " + nationRepo.getCount());
        System.out.println("States : " + stateRepo.getCount());

        createMockData();
    }


    private void createMockData(){
        String[][] towns = {
            {"Los Angeles", "Seattle", "Boston", "Miami", "New Durham"},
            {"California", "Washington", "Massachusetts", "Florida", "New Hampshire"},
        };

        String[] uris = { "losangeles", "seattle", "boston", "miami", "newdurham" };

        Long[] counts = { new Long(66436),
                        new Long(11752),
                        new Long(4021),
                        new Long(3472),
                        new Long( 142) };

        String[] townNames = towns[0];
        String[] stateNames = towns[1];
        for(int z = 0; z < townNames.length; z++){
            State state = stateRepo.get(stateNames[z]);
            Town town = new Town();
            town.setName(townNames[z]);
            town.setStateId(state.getId());
            town.setCount(counts[z]);
            town.setTownUri(uris[z]);
            townRepo.save(town);
        }

        System.out.println("towns : " + townRepo.getCount());

        String[] organizations = { "Miami Homeless Shelter", "New Durham Chapel", "Los Angeles Mission" };
        String[] organizationUris = { "miamihomelessshelter", "newdurhamchapel", "losangelesmission" };
        String[] organizationTowns = { "miami", "newdurham", "losangeles"};

        for(int n = 0; n < organizations.length; n++){
            String townUri = organizationTowns[n];
            Town town = townRepo.get(townUri);
            Organization organization = new Organization();
            organization.setName(organizations[n]);
            organization.setUri(organizationUris[n]);
            organization.setTownId(town.getId());
            organization.setDescription("Helping at-risk and homeless families with children in Clark County achieve sustainable housing and independence through a compassionate, community-based response.");
            Organization savedOrganization = organizationRepo.save(organization);
            savedOrganization.setStripeAccountId("acct_1IwHYyCGAv7rFLov");
            organizationRepo.update(savedOrganization);
        }

        System.out.println("Organizations : " + organizationRepo.getCount());


        String[] emails = {"croteau.mike+one@gmail.com",
                "croteau.mike+dos@gmail.com",
                "croteau.mike+three@gmail.com",};

        int idx = 0;
        for(String email : emails){
            OwnershipRequest ownershipRequest = new OwnershipRequest();
            ownershipRequest.setOrganizationId(new Long(1));
            ownershipRequest.setEmail(email);
            ownershipRequest.setPhone("9079878652");
            ownershipRequest.setName("Mike " + idx);
            idx++;
            organizationRepo.saveRequest(ownershipRequest);
        }

        System.out.println("Requests : " + organizationRepo.getCountRequests());
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
