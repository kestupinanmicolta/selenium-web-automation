import xml.etree.ElementTree as ET
import glob, os, sys, html
from datetime import datetime

print(f"Current dir: {os.getcwd()}")
print(f"target exists: {os.path.exists('target')}")

surefire_dir = "target/surefire-reports"
if not os.path.exists(surefire_dir):
    print("surefire-reports does NOT exist - writing fallback")
    with open("public/index.html", "w") as f:
        f.write("""<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Selenium Test Report</title>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>*{margin:0;padding:0;box-sizing:border-box}body{font-family:Inter,sans-serif;background:#f1f5f9;color:#1e293b}
.header{background:linear-gradient(135deg,#0f172a,#1e3a5f);color:white;padding:2rem;text-align:center}
.header h1{font-size:1.5rem}.header p{color:#60a5fa}
.container{max-width:800px;margin:2rem auto;padding:0 1rem}
.card{background:white;border-radius:12px;padding:1.5rem;margin-bottom:1rem;box-shadow:0 2px 8px rgba(0,0,0,.08)}
.card h2{font-size:1rem;margin-bottom:.75rem;color:#0f172a;display:flex;align-items:center;gap:.5rem}
.card h2 i{color:#3b82f6}
.tags{display:flex;flex-wrap:wrap;gap:.4rem;margin-bottom:1rem}
.tag{background:#eff6ff;color:#3b82f6;padding:.25rem .6rem;border-radius:6px;font-size:.75rem;font-weight:600}
.btn{display:inline-flex;align-items:center;gap:.5rem;background:#0f172a;color:white;padding:.6rem 1.2rem;border-radius:8px;text-decoration:none;font-weight:600;font-size:.85rem;margin-top:.5rem}
.footer{text-align:center;padding:1.5rem;font-size:.75rem;color:#94a3b8}
</style></head><body>
<div class="header"><h1><i class="fas fa-check-double"></i> Selenium Automation</h1><p>Test Execution Report</p></div>
<div class="container">
<div class="card"><h2><i class="fas fa-info-circle"></i> Project Overview</h2>
<p style="font-size:.85rem;color:#475569;margin-bottom:1rem">Automated web testing with Selenium 4, TestNG, Page Object Model against practicesoftwaretesting.com</p>
<div class="tags"><span class="tag">Java</span><span class="tag">Selenium 4</span><span class="tag">TestNG</span><span class="tag">POM</span><span class="tag">Allure</span></div></div>
<div class="card"><h2><i class="fas fa-exclamation-triangle"></i> Test Status</h2>
<p style="font-size:.85rem;color:#475569">Tests executed but surefire reports were not generated. This typically happens when tests fail during browser initialization in CI environment.</p>
<p style="font-size:.85rem;color:#475569;margin-top:.5rem">The test suite includes: Login, Search, Cart, and Checkout tests using Page Object Model design pattern.</p></div>
<a href="https://github.com/kestupinanmicolta/selenium-web-automation" class="btn"><i class="fab fa-github"></i> View Source Code</a>
</div><div class="footer">2026 Karen Paola Estupinan Micolta</div></body></html>""")
    print("Fallback written")
    sys.exit(0)

files = os.listdir(surefire_dir)
print(f"surefire-reports has {len(files)} files: {files[:10]}")

total = passed = failed = skipped = 0
rows = []
xml_files = glob.glob("target/surefire-reports/TEST-*.xml")
print(f"Found {len(xml_files)} TEST XML files")

for f in xml_files:
    try:
        tree = ET.parse(f)
        root = tree.getroot()
        ts = root.attrib
        total += int(ts.get("tests", 0))
        t = int(ts.get("tests", 0))
        f_count = int(ts.get("failures", 0))
        e_count = int(ts.get("errors", 0))
        s_count = int(ts.get("skipped", 0))
        p = t - f_count - e_count - s_count
        passed += p
        failed += f_count + e_count
        skipped += s_count
        classname = os.path.basename(f).replace("TEST-", "").replace(".xml", "")
        for tc in root.findall("testcase"):
            name = tc.attrib.get("name", "?")
            time_ms = int(float(tc.attrib.get("time", "0")) * 1000)
            failure = tc.find("failure")
            skip = tc.find("skipped")
            if failure is not None:
                status, badge = "FAILED", "badge-failed"
                msg = failure.attrib.get("message", "")[:150]
            elif skip is not None:
                status, badge = "SKIPPED", "badge-skipped"
                msg = ""
            else:
                status, badge = "PASSED", "badge-passed"
                msg = ""
            trace = '<div class="trace">{}</div>'.format(html.escape(msg)) if msg else ""
            rows.append('<tr><td><strong>{}</strong>{}</td><td style="font-size:.8rem;color:#64748b">{}</td><td><span class="badge {}">{}</span></td><td style="font-size:.8rem;color:#64748b">{}ms</td></tr>'.format(html.escape(name), trace, html.escape(classname), badge, status, time_ms))
    except Exception as e:
        print("Error parsing {}: {}".format(f, e))

status_text = "FAILED" if failed > 0 else ("PASSED" if total > 0 else "NO RESULTS")
sclass = "status-FAILED" if failed > 0 or total == 0 else "status-PASSED"
ts_str = datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S UTC")
table_rows = "\n".join(rows)

html_out = '''<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Selenium Test Report</title>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>*{margin:0;padding:0;box-sizing:border-box}body{font-family:Inter,sans-serif;background:#f1f5f9;color:#1e293b}
.header{background:linear-gradient(135deg,#0f172a,#1e3a5f);color:white;padding:2rem;text-align:center}
.header h1{font-size:1.5rem}.header p{color:#60a5fa}
.status-badge{display:inline-block;padding:.3rem .8rem;border-radius:20px;font-weight:700;font-size:.85rem;margin-top:.5rem}
.status-PASSED{background:#dcfce7;color:#166534}.status-FAILED{background:#fee2e2;color:#991b1b}
.stats{display:flex;gap:1rem;justify-content:center;margin:1.5rem 0;flex-wrap:wrap}
.stat{background:white;border-radius:12px;padding:1.2rem 1.5rem;box-shadow:0 2px 8px rgba(0,0,0,.08);text-align:center;min-width:120px}
.stat h3{font-size:1.8rem;font-weight:700}.stat p{font-size:.75rem;color:#64748b;text-transform:uppercase}
.stat.passed h3{color:#16a34a}.stat.failed h3{color:#dc2626}.stat.skipped h3{color:#ca8a04}.stat.total h3{color:#0f172a}
.container{max-width:900px;margin:2rem auto;padding:0 1rem}
.card{background:white;border-radius:12px;padding:1.5rem;margin-bottom:1rem;box-shadow:0 2px 8px rgba(0,0,0,.08)}
.card h2{font-size:1rem;margin-bottom:1rem;color:#0f172a;display:flex;align-items:center;gap:.5rem}
.card h2 i{color:#3b82f6}
.tags{display:flex;flex-wrap:wrap;gap:.4rem;margin-bottom:1rem}.tag{background:#eff6ff;color:#3b82f6;padding:.25rem .6rem;border-radius:6px;font-size:.75rem;font-weight:600}
table{width:100%;border-collapse:collapse;font-size:.85rem}th{text-align:left;padding:.6rem .8rem;background:#f8fafc;color:#475569;font-weight:600;border-bottom:2px solid #e2e8f0}
td{padding:.6rem .8rem;border-bottom:1px solid #f1f5f9}
.badge{padding:.2rem .5rem;border-radius:4px;font-size:.7rem;font-weight:700;text-transform:uppercase}
.badge-passed{background:#dcfce7;color:#166534}.badge-failed{background:#fee2e2;color:#991b1b}.badge-skipped{background:#fef3c7;color:#92400e}
.trace{font-size:.75rem;color:#64748b;margin-top:.4rem;max-height:80px;overflow:auto;background:#f8fafc;padding:.4rem;border-radius:4px;font-family:monospace}
.footer{text-align:center;padding:1.5rem;font-size:.75rem;color:#94a3b8}
a.src{display:inline-flex;align-items:center;gap:.5rem;background:#0f172a;color:white;padding:.8rem 1.5rem;border-radius:8px;text-decoration:none;font-weight:600;font-size:.85rem;margin-top:.5rem}
</style></head><body>
<div class="header"><h1><i class="fas fa-check-double"></i> Selenium Automation</h1>
<p>Test Execution Report</p>
<div class="status-badge {}">{}</div></div>
<div class="container">
<div class="stats">
<div class="stat total"><h3>{}</h3><p>Total</p></div>
<div class="stat passed"><h3>{}</h3><p>Passed</p></div>
<div class="stat failed"><h3>{}</h3><p>Failed</p></div>
<div class="stat skipped"><h3>{}</h3><p>Skipped</p></div>
</div>
<div class="card"><h2><i class="fas fa-info-circle"></i> Project Overview</h2>
<p style="font-size:.85rem;color:#475569;margin-bottom:1rem">Automated web testing with Selenium 4, TestNG, Page Object Model against practicesoftwaretesting.com</p>
<div class="tags"><span class="tag">Java</span><span class="tag">Selenium 4</span><span class="tag">TestNG</span><span class="tag">POM</span><span class="tag">Allure</span></div></div>
<div class="card"><h2><i class="fas fa-list"></i> Test Results</h2>
<table><thead><tr><th>Test</th><th>Class</th><th>Status</th><th>Time</th></tr></thead><tbody>
{}
</tbody></table></div>
<a href="https://github.com/kestupinanmicolta/selenium-web-automation" class="src"><i class="fab fa-github"></i> View Source Code</a>
</div><div class="footer">Generated on {} | Karen Paola Estupinan Micolta</div></body></html>'''.format(sclass, status_text, total, passed, failed, skipped, table_rows, ts_str)

with open("public/index.html", "w", encoding="utf-8") as f:
    f.write(html_out)
print("Report: {} tests, {} passed, {} failed, {} skipped".format(total, passed, failed, skipped))
print("File size: {} bytes".format(len(html_out)))
