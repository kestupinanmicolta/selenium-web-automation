package com.karen.listeners;

import org.testng.*;
import org.testng.annotations.AfterSuite;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HtmlReportListener implements ITestListener {

    private static final List<TestMethodResult> results = Collections.synchronizedList(new ArrayList<>());
    private static final String TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    @Override
    public void onTestSuccess(ITestResult result) {
        results.add(new MethodResult(result, "PASSED"));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        results.add(new MethodResult(result, "FAILED", result.getThrowable()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        results.add(new MethodResult(result, "SKIPPED"));
    }

    @Override
    public void onTestStart(ITestResult result) {}

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    public static void generateReport() {
        int passed = (int) results.stream().filter(r -> r.status.equals("PASSED")).count();
        int failed = (int) results.stream().filter(r -> r.status.equals("FAILED")).count();
        int skipped = (int) results.stream().filter(r -> r.status.equals("SKIPPED")).count();
        int total = results.size();
        String overallStatus = failed == 0 ? "PASSED" : "FAILED";

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Selenium Test Report</title>");
        sb.append("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap' rel='stylesheet'>");
        sb.append("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css'>");
        sb.append("<style>*{margin:0;padding:0;box-sizing:border-box}body{font-family:Inter,sans-serif;background:#f1f5f9;color:#1e293b}");
        sb.append(".header{background:linear-gradient(135deg,#0f172a,#1e3a5f);color:white;padding:2rem;text-align:center}");
        sb.append(".header h1{font-size:1.5rem}.header p{color:#60a5fa}");
        sb.append(".status-badge{display:inline-block;padding:.3rem .8rem;border-radius:20px;font-weight:700;font-size:.85rem;margin-top:.5rem}");
        sb.append(".status-PASSED{background:#dcfce7;color:#166534}.status-FAILED{background:#fee2e2;color:#991b1b}");
        sb.append(".stats{display:flex;gap:1rem;justify-content:center;margin:1.5rem 0;flex-wrap:wrap}");
        sb.append(".stat{background:white;border-radius:12px;padding:1.2rem 1.5rem;box-shadow:0 2px 8px rgba(0,0,0,.08);text-align:center;min-width:120px}");
        sb.append(".stat h3{font-size:1.8rem;font-weight:700}.stat p{font-size:.75rem;color:#64748b;text-transform:uppercase;letter-spacing:.5px}");
        sb.append(".stat.passed h3{color:#16a34a}.stat.failed h3{color:#dc2626}.stat.skipped h3{color:#ca8a04}.stat.total h3{color:#0f172a}");
        sb.append(".container{max-width:900px;margin:2rem auto;padding:0 1rem}");
        sb.append(".card{background:white;border-radius:12px;padding:1.5rem;margin-bottom:1rem;box-shadow:0 2px 8px rgba(0,0,0,.08)}");
        sb.append(".card h2{font-size:1rem;margin-bottom:1rem;color:#0f172a;display:flex;align-items:center;gap:.5rem}");
        sb.append(".card h2 i{color:#3b82f6}");
        sb.append(".tags{display:flex;flex-wrap:wrap;gap:.4rem;margin-bottom:1rem}.tag{background:#eff6ff;color:#3b82f6;padding:.25rem .6rem;border-radius:6px;font-size:.75rem;font-weight:600}");
        sb.append("table{width:100%;border-collapse:collapse;font-size:.85rem}th{text-align:left;padding:.6rem .8rem;background:#f8fafc;color:#475569;font-weight:600;border-bottom:2px solid #e2e8f0}");
        sb.append("td{padding:.6rem .8rem;border-bottom:1px solid #f1f5f9}");
        sb.append(".badge{padding:.2rem .5rem;border-radius:4px;font-size:.7rem;font-weight:700;text-transform:uppercase}");
        sb.append(".badge-passed{background:#dcfce7;color:#166534}.badge-failed{background:#fee2e2;color:#991b1b}.badge-skipped{background:#fef3c7;color:#92400e}");
        sb.append(".trace{font-size:.75rem;color:#64748b;margin-top:.4rem;max-height:80px;overflow:auto;background:#f8fafc;padding:.4rem;border-radius:4px;font-family:monospace}");
        sb.append(".footer{text-align:center;padding:1.5rem;font-size:.75rem;color:#94a3b8}");
        sb.append("</style></head><body>");

        sb.append("<div class='header'><h1><i class='fas fa-check-double'></i> Selenium Automation</h1>");
        sb.append("<p>Test Execution Report</p>");
        sb.append("<div class='status-badge status-").append(overallStatus).append("'>").append(overallStatus).append("</div></div>");

        sb.append("<div class='container'>");
        sb.append("<div class='stats'>");
        sb.append("<div class='stat total'><h3>").append(total).append("</h3><p>Total</p></div>");
        sb.append("<div class='stat passed'><h3>").append(passed).append("</h3><p>Passed</p></div>");
        sb.append("<div class='stat failed'><h3>").append(failed).append("</h3><p>Failed</p></div>");
        sb.append("<div class='stat skipped'><h3>").append(skipped).append("</h3><p>Skipped</p></div>");
        sb.append("</div>");

        sb.append("<div class='card'><h2><i class='fas fa-info-circle'></i> Project Overview</h2>");
        sb.append("<p style='font-size:.85rem;color:#475569;margin-bottom:1rem'>Automated web testing with Selenium 4, TestNG, Page Object Model, and Allure Reports against practicesoftwaretesting.com</p>");
        sb.append("<div class='tags'><span class='tag'>Java</span><span class='tag'>Selenium 4</span><span class='tag'>TestNG</span><span class='tag'>POM</span><span class='tag'>Allure</span></div></div>");

        sb.append("<div class='card'><h2><i class='fas fa-list'></i> Test Results</h2>");
        sb.append("<table><thead><tr><th>Test</th><th>Class</th><th>Status</th><th>Time</th></tr></thead><tbody>");
        for (MethodResult r : results) {
            sb.append("<tr><td><strong>").append(r.methodName).append("</strong>");
            if (r.throwable != null) {
                String msg = r.throwable.getMessage();
                if (msg != null && msg.length() > 120) msg = msg.substring(0, 120) + "...";
                sb.append("<div class='trace'>").append(escapeHtml(msg != null ? msg : r.throwable.getClass().getSimpleName())).append("</div>");
            }
            sb.append("</td><td style='font-size:.8rem;color:#64748b'>").append(r.className).append("</td>");
            sb.append("<td><span class='badge badge-").append(r.status.toLowerCase()).append("'>").append(r.status).append("</span></td>");
            sb.append("<td style='font-size:.8rem;color:#64748b'>").append(r.duration).append("ms</td></tr>");
        }
        sb.append("</tbody></table></div>");

        sb.append("<a href='https://github.com/kestupinanmicolta/selenium-web-automation' class='card' style='display:inline-flex;align-items:center;gap:.5rem;background:#0f172a;color:white;padding:.8rem 1.5rem;border-radius:8px;text-decoration:none;font-weight:600;font-size:.85rem'><i class='fab fa-github'></i> View Source Code</a>");
        sb.append("</div><div class='footer'>Generated on ").append(TIMESTAMP).append(" | Karen Paola Estupinan Micolta</div></body></html>");

        try {
            File dir = new File("target/allure-results");
            dir.mkdirs();
            File outFile = new File(dir, "selenium-report.html");
            Writer writer = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
            writer.write(sb.toString());
            writer.close();
            System.out.println("HTML report generated: " + outFile.getAbsolutePath() + " (" + outFile.length() + " bytes)");
        } catch (IOException e) {
            System.err.println("Failed to generate HTML report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("\n", "<br>");
    }

    private static class MethodResult {
        String methodName, className, status, duration;
        Throwable throwable;

        MethodResult(ITestResult result, String status) {
            this(result, status, null);
        }

        MethodResult(ITestResult result, String status, Throwable t) {
            this.methodName = result.getMethod().getMethodName();
            this.className = result.getTestClass().getRealClass().getSimpleName();
            this.status = status;
            this.duration = String.valueOf(result.getEndMillis() - result.getStartMillis());
            this.throwable = t;
        }
    }
}
