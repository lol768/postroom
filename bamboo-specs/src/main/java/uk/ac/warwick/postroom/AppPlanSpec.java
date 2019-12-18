package uk.ac.warwick.postroom;

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.deployment.Deployment;
import com.atlassian.bamboo.specs.api.builders.deployment.Environment;
import com.atlassian.bamboo.specs.api.builders.notification.Notification;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.api.builders.requirement.Requirement;
import com.atlassian.bamboo.specs.api.util.EntityPropertiesBuilders;
import com.atlassian.bamboo.specs.builders.notification.DeploymentFailedNotification;
import com.atlassian.bamboo.specs.builders.task.*;
import com.atlassian.bamboo.specs.builders.trigger.AfterSuccessfulBuildPlanTrigger;
import com.atlassian.bamboo.specs.model.task.ScriptTaskProperties;
import com.atlassian.bamboo.specs.model.task.TestParserTaskProperties;
import uk.ac.warwick.bamboo.specs.AbstractWarwickBuildSpec;

import java.util.Collection;
import java.util.Collections;

/**
 * Plan configuration for Bamboo.
 * Learn more on: <a href="https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs">https://confluence.atlassian.com/display/BAMBOO/Bamboo+Specs</a>
 */
@BambooSpec
public class AppPlanSpec extends AbstractWarwickBuildSpec {

  private static final Project PROJECT =
    new Project()
      .key("POST")
      .name("Postroom");

  private static final String LINKED_REPOSITORY = "Postroom";

  private static final String SLACK_CHANNEL = "#postroom";

  public static void main(String[] args) throws Exception {
    new AppPlanSpec().publish();
  }

  private static Stage buildStage() {
    Job job =
      new Job("Build and check", "BUILD")
        .tasks(
          new VcsCheckoutTask()
            .description("Checkout source from default repository")
            .checkoutItems(new CheckoutItem().defaultRepository()),
          new ScriptTask()
            .description("gradlew clean check bootJar")
            .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
            .location(ScriptTaskProperties.Location.FILE)
            .fileFromPath("gradlew")
            .argument("clean check bootJar --no-daemon")
            .environmentVariables("JAVA_OPTS=\"-Xmx256m -Xms128m\""),
          new ScriptTask()
            .description("Touch test files so Bamboo doesn't ignore them")
            .interpreter(ScriptTaskProperties.Interpreter.BINSH_OR_CMDEXE)
            .location(ScriptTaskProperties.Location.INLINE)
            .inlineBody("find . -type f -name 'TEST-*.xml' -exec touch {} +")
        )
        .requirements(
          new Requirement("system.jdk.JDK 1.8")
        );

    job.finalTasks(
      new TestParserTask(TestParserTaskProperties.TestType.JUNIT)
        .description("Parse test results")
        .resultDirectories("**/test-results/**/*.xml")
    );

    job.artifacts(
      new Artifact()
        .name("app.jar")
        .copyPattern("app.jar")
        .location("build/libs")
        .shared(true)
    );

    return new Stage("Build Stage").jobs(job);
  }

  @Override
  protected Collection<Plan> builds() {
    return Collections.singletonList(
      build(PROJECT, "ALL", "Postroom")
        .linkedRepository(LINKED_REPOSITORY)
        .description("Run checks and build executable jar")
        .stage(buildStage())
        .slackNotifications(SLACK_CHANNEL, false)
        .build()
    );
  }

  @Override
  protected Collection<Deployment> deployments() {
    return Collections.singleton(
      deployment(PROJECT, "ALL", "Post room")
        .autoSpringBootEnvironment("Development", "postroom-dev.warwick.ac.uk", "postroom", "dev", SLACK_CHANNEL)
        .autoSpringBootEnvironment("Test", "postroom-test.warwick.ac.uk", "postroom", "test", SLACK_CHANNEL)
        .productionSpringBootEnvironment("Production", "postroom.warwick.ac.uk", "postroom", SLACK_CHANNEL)
        .build()
    );
  }

}
