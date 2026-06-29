package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentListener implements ISuiteListener, ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // One parent node per test class; methods are nested under their class.
    private static final Map<String, ExtentTest> classNodes = new ConcurrentHashMap<>();

    // --- Suite level: runs ONCE per whole run, regardless of how many <test> tags exist ---

    @Override
    public void onStart(ISuite suite) {

        // Create reports directory if it doesn't exist
        new File("reports").mkdirs();

        // Unique report name for every run
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                .format(new Date());

        String reportPath = "reports/ExtentReport_" + timestamp + ".html";

        System.out.println("Creating report: " + reportPath);

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

        spark.config().setDocumentTitle("Automation Test Report");
        spark.config().setReportName("Selenium Test Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Tester", "Mohamed Islam");
        extent.setSystemInfo("Framework", "Selenium + TestNG");
        extent.setSystemInfo("Language", "Java");
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));

        System.out.println("Extent Report Initialized");
    }

    @Override
    public void onFinish(ISuite suite) {
        if (extent != null) {
            extent.flush();
        }

        System.out.println("Extent Report Generated Successfully");
    }

    // --- Test method level: one parent node per class, each method nested under it ---

    @Override
    public void onTestStart(ITestResult result) {
        // Parent node = the test class (created once, reused for every method in it).
        String className = result.getTestClass().getRealClass().getSimpleName();
        ExtentTest classNode =
                classNodes.computeIfAbsent(className, name -> extent.createTest(name));

        // Child node = the individual test method, nested under its class.
        ExtentTest methodNode =
                classNode.createNode(result.getMethod().getMethodName());

        test.set(methodNode);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test Skipped");
    }
}
