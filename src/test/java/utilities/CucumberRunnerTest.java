package utilities;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "src/test/resources/features" }
        ,glue = { "stepdefinition", "generic"}
        ,plugin = {"html:target/cucumber-report.html",
        "rerun:target/rerun.txt" ,
        "json:target/cucumber.json"}
        ,monochrome = true
        ,dryRun = false
        ,tags= "@drinks")
public class CucumberRunnerTest {

}