package go.service;

import go.Spirit;
import go.support.SpiritAccessor;
import go.jobs.NotificationJob;
import go.model.*;
import go.repo.*;
import eco.m1.M1;
import eco.m1.Q;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.jdbc.BasicDataSource;
import org.h2.tools.RunScript;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import xyz.goioc.Parakeet;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;

@Service
public class StartupService {

    @Inject
    Q q;

    @Inject
    UserRepo userRepo;

    @Inject
    RoleRepo roleRepo;

    @Inject
    StatusRepo statusRepo;

    @Inject
    ActivityRepo activityRepo;

    @Inject
    ProspectRepo prospectRepo;

    @Inject
    EffortRepo effortRepo;

    @Inject
    SmsService smsService;

    @Inject
    SpiritAccessor spiritAccessor;


    public void init() throws Exception {

        Parakeet.perch(spiritAccessor);

        Role superRole = roleRepo.find(Spirit.SUPER_ROLE);
        Role userRole = roleRepo.find(Spirit.USER_ROLE);

        if(superRole == null){
            superRole = new Role();
            superRole.setName(Spirit.SUPER_ROLE);
            roleRepo.save(superRole);
        }

        if(userRole == null){
            userRole = new Role();
            userRole.setName(Spirit.USER_ROLE);
            roleRepo.save(userRole);
        }

        User existing = userRepo.getByUsername(Spirit.SUPER_USERNAME);
        String password = Parakeet.dirty(Spirit.SUPER_PASSWORD);

        if(existing == null){
            User superUser = new User();
            superUser.setPhone("+19076879557");
            superUser.setUsername(Spirit.SUPER_USERNAME);
            superUser.setPassword(password);
            userRepo.saveAdministrator(superUser);
        }

        Long statusCount = statusRepo.getCount();
        if(statusCount == 0) {
            String[] names = {Spirit.IDLE_STATUS,
                                Spirit.PROSPECT_STATUS,
                                Spirit.WORKING_STATUS,
                                Spirit.CUSTOMER_STATUS};

            for (String name : names) {
                Status status = new Status();
                status.setName(name);
                statusRepo.save(status);
            }
        }


        Long activityCount = activityRepo.getCount();
        if(activityCount == 0) {
            String[] activityNames = {"Call",
                                    "Email",
                                    "Mailer",
                                    "Meeting",
                                    "Demo",
                                    "Sale"};
            for (String name : activityNames) {
                Activity activity = new Activity();
                activity.setName(name);
                activityRepo.save(activity);
            }
        }

        System.out.println("Roles : " + roleRepo.count());
        System.out.println("Users : " + userRepo.getCount());
        System.out.println("Statuses : " + statusRepo.getCount());
        System.out.println("Activities : " + activityRepo.getCount());


        try {

            Class[] jobs = { NotificationJob.class };
            String[] jobNames = { Spirit.NOTIFICATION_JOB };
            String[] triggers = { Spirit.NOTIFICATION_TRIGGER };

            for(int n = 0; n < jobs.length; n++){

                JobDetail job = JobBuilder.newJob(jobs[n])
                        .withIdentity(jobNames[n], Spirit.NOTIFICATION_GROUP).build();

                job.getJobDataMap().put(Spirit.PROSPECT_REPO_KEY, prospectRepo);
                job.getJobDataMap().put(Spirit.SMS_SERVICE_KEY, smsService);

                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggers[n], Spirit.NOTIFICATION_GROUP)
                        .withSchedule(
                                SimpleScheduleBuilder.simpleSchedule()
                                        .withIntervalInSeconds(Spirit.NOTIFICATION_JOB_DURATION).repeatForever())
                        .build();

                Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                scheduler.startDelayed(0 );
                JobKey key = new JobKey(jobNames[n], Spirit.NOTIFICATION_GROUP);
                if(!scheduler.checkExists(key)) {
                    scheduler.scheduleJob(job, trigger);
                    System.out.println(jobs[n] + " repeated " + Spirit.NOTIFICATION_JOB_DURATION + " seconds");
                }
            }


        }catch (Exception ex){
            ex.printStackTrace();
        }
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

    protected void createMockData(){
        User superUser = userRepo.getByUsername(Spirit.SUPER_USERNAME);
        String password = Parakeet.dirty(Spirit.SUPER_PASSWORD);

        if(superUser == null){
            User user = new User();
            user.setUsername(Spirit.SUPER_USERNAME);
            user.setPassword(password);
            userRepo.save(user);
        }

        List<Status> statuses = statusRepo.getList();
        Long prospectCount = prospectRepo.getCount();
        if(prospectCount == 0) {
            String[] prospectNames = {"Blue Water Trucking Co.",
                                    "Love Hour Meditation",
                                    "Jeff's Silly Suds Brew House",
                                    "Dr. Suese's Chiropractor's Masseuse",
                                    "Tidy Tim's Bean Factory",
                                    "Grand Rapids Auto Park",
                                    "Dirken's Fluffanutters"};

            for (String name : prospectNames) {
                Status status = statuses.get(Spirit.getNumber(statuses.size()));
                Prospect prospect = new Prospect();
                prospect.setName(name);
                prospect.setEmail(Spirit.getString(13));
                prospect.setPhone("(808) 012-0910");
                prospect.setStatusId(status.getId());
                prospectRepo.save(prospect);
            }
        }
        System.out.println("Prospects : " + prospectRepo.getCount());


        Long activityCount = activityRepo.getCount();
        if(activityCount == 0) {
            String[] activityNames = {"Mailer",
                                    "Call",
                                    "Email",
                                    "Meeting",
                                    "Demo",
                                    "Sale"};
            for (String name : activityNames) {
                Activity activity = new Activity();
                activity.setName(name);
                activityRepo.save(activity);
            }
            System.out.println("Activities : " + activityRepo.getCount());

            List<Activity> activities = activityRepo.getList();

            List<Prospect> prospects = prospectRepo.getList();
            for (int z = 4; z < prospects.size(); z++) {
                Prospect prospect = prospects.get(z);
                int index = Spirit.getNumber(activityNames.length);
                Activity activity = activities.get(index);
                ProspectActivity prospectActivity = new ProspectActivity();
                prospectActivity.setActivityId(activity.getId());
                prospectActivity.setProspectId(prospect.getId());
                prospectRepo.saveActivity(prospectActivity);
            }
        }
        System.out.println("Prospect Activities : " + prospectRepo.getActivityCount());

    }


    public void shutdown() throws Exception{
        System.out.println("shutdown...");
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
