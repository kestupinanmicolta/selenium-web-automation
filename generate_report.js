const fs = require('fs');
const path = require('path');

const surefireDir = path.join(process.cwd(), 'target', 'surefire-reports');
const publicDir = path.join(process.cwd(), 'public');

if (!fs.existsSync(publicDir)) fs.mkdirSync(publicDir, { recursive: true });

const REPORT_CSS = '*{margin:0;padding:0;box-sizing:border-box}body{font-family:Inter,sans-serif;background:#f1f5f9;color:#1e293b}.header{background:linear-gradient(135deg,#0f172a,#1e3a5f);color:white;padding:2rem;text-align:center}.header h1{font-size:1.5rem}.header p{color:#60a5fa}.status-badge{display:inline-block;padding:.3rem .8rem;border-radius:20px;font-weight:700;font-size:.85rem;margin-top:.5rem}.status-PASSED{background:#dcfce7;color:#166534}.status-FAILED{background:#fee2e2;color:#991b1b}.stats{display:flex;gap:1rem;justify-content:center;margin:1.5rem 0;flex-wrap:wrap}.stat{background:white;border-radius:12px;padding:1.2rem 1.5rem;box-shadow:0 2px 8px rgba(0,0,0,.08);text-align:center;min-width:120px}.stat h3{font-size:1.8rem;font-weight:700}.stat p{font-size:.75rem;color:#64748b;text-transform:uppercase}.stat.passed h3{color:#16a34a}.stat.failed h3{color:#dc2626}.stat.skipped h3{color:#ca8a04}.stat.total h3{color:#0f172a}.container{max-width:900px;margin:2rem auto;padding:0 1rem}.card{background:white;border-radius:12px;padding:1.5rem;margin-bottom:1rem;box-shadow:0 2px 8px rgba(0,0,0,.08)}.card h2{font-size:1rem;margin-bottom:1rem;color:#0f172a;display:flex;align-items:center;gap:.5rem}.card h2 i{color:#3b82f6}.tags{display:flex;flex-wrap:wrap;gap:.4rem;margin-bottom:1rem}.tag{background:#eff6ff;color:#3b82f6;padding:.25rem .6rem;border-radius:6px;font-size:.75rem;font-weight:600}table{width:100%;border-collapse:collapse;font-size:.85rem}th{text-align:left;padding:.6rem .8rem;background:#f8fafc;color:#475569;font-weight:600;border-bottom:2px solid #e2e8f0}td{padding:.6rem .8rem;border-bottom:1px solid #f1f5f9}.badge{padding:.2rem .5rem;border-radius:4px;font-size:.7rem;font-weight:700;text-transform:uppercase}.badge-passed{background:#dcfce7;color:#166534}.badge-failed{background:#fee2e2;color:#991b1b}.badge-skipped{background:#fef3c7;color:#92400e}.trace{font-size:.75rem;color:#64748b;margin-top:.4rem;max-height:80px;overflow:auto;background:#f8fafc;padding:.4rem;border-radius:4px;font-family:monospace}.footer{text-align:center;padding:1.5rem;font-size:.75rem;color:#94a3b8}a.src{display:inline-flex;align-items:center;gap:.5rem;background:#0f172a;color:white;padding:.8rem 1.5rem;border-radius:8px;text-decoration:none;font-weight:600;font-size:.85rem;margin-top:.5rem}';

function escapeHtml(s) {
    return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/\n/g, '<br>');
}

function parseSurefireXml(xml) {
    const testsuiteMatch = xml.match(/<testsuite[^>]*>/);
    if (!testsuiteMatch) return null;
    const attrs = {};
    const attrRegex = /(\w+)="([^"]*)"/g;
    let m;
    while ((m = attrRegex.exec(testsuiteMatch[0])) !== null) attrs[m[1]] = m[2];

    const testcases = [];
    const tcRegex = /<testcase\s+([^>]*?)\/>|<testcase\s+([^>]*?)>([\s\S]*?)<\/testcase>/g;
    let tc;
    while ((tc = tcRegex.exec(xml)) !== null) {
        const tcAttrs = {};
        const tcStr = tc[1] || tc[2] || '';
        const tcContent = tc[3] || '';
        let am;
        while ((am = attrRegex.exec(tcStr)) !== null) tcAttrs[am[1]] = am[2];
        attrRegex.lastIndex = 0;

        const hasFailure = tcContent.includes('<failure');
        const hasSkipped = tcContent.includes('<skipped');
        let failMsg = '';
        if (hasFailure) {
            const msgMatch = tcContent.match(/message="([^"]*)"/);
            if (msgMatch) failMsg = msgMatch[1].substring(0, 150);
        }
        testcases.push({ name: tcAttrs.name || '?', time: tcAttrs.time || '0', failure: hasFailure, skipped: hasSkipped, failMsg });
    }
    return { attrs, testcases };
}

if (!fs.existsSync(surefireDir)) {
    console.log('surefire-reports does NOT exist');
    process.exit(1);
}

const files = fs.readdirSync(surefireDir).filter(f => f.startsWith('TEST-') && f.endsWith('.xml'));
console.log('Found ' + files.length + ' TEST XML files');

let total = 0, passed = 0, failed = 0, skipped = 0;
const rows = [];

for (const file of files) {
    const xml = fs.readFileSync(path.join(surefireDir, file), 'utf8');
    const result = parseSurefireXml(xml);
    if (!result) { console.log('Error parsing ' + file); continue; }

    const t = parseInt(result.attrs.tests || '0');
    const f = parseInt(result.attrs.failures || '0');
    const e = parseInt(result.attrs.errors || '0');
    const s = parseInt(result.attrs.skipped || '0');
    total += t; failed += f + e; skipped += s; passed += t - f - e - s;

    const classname = file.replace('TEST-', '').replace('.xml', '');
    for (const tc of result.testcases) {
        const timeMs = Math.round(parseFloat(tc.time) * 1000);
        let status, badge;
        if (tc.failure) { status = 'FAILED'; badge = 'badge-failed'; }
        else if (tc.skipped) { status = 'SKIPPED'; badge = 'badge-skipped'; }
        else { status = 'PASSED'; badge = 'badge-passed'; }
        const trace = tc.failMsg ? '<div class="trace">' + escapeHtml(tc.failMsg) + '</div>' : '';
        rows.push('<tr><td><strong>' + escapeHtml(tc.name) + '</strong>' + trace + '</td><td style="font-size:.8rem;color:#64748b">' + escapeHtml(classname) + '</td><td><span class="badge ' + badge + '">' + status + '</span></td><td style="font-size:.8rem;color:#64748b">' + timeMs + 'ms</td></tr>');
    }
}

const statusText = failed > 0 ? 'FAILED' : (total > 0 ? 'PASSED' : 'NO RESULTS');
const sclass = (failed > 0 || total === 0) ? 'status-FAILED' : 'status-PASSED';
const ts = new Date().toISOString().replace('T', ' ').replace(/\..+/, ' UTC');

const html = '<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Selenium Test Report</title>' +
    '<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">' +
    '<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">' +
    '<style>' + REPORT_CSS + '</style></head><body>' +
    '<div class="header"><h1><i class="fas fa-check-double"></i> Selenium Automation</h1>' +
    '<p>Test Execution Report</p>' +
    '<div class="status-badge ' + sclass + '">' + statusText + '</div></div>' +
    '<div class="container">' +
    '<div class="stats">' +
    '<div class="stat total"><h3>' + total + '</h3><p>Total</p></div>' +
    '<div class="stat passed"><h3>' + passed + '</h3><p>Passed</p></div>' +
    '<div class="stat failed"><h3>' + failed + '</h3><p>Failed</p></div>' +
    '<div class="stat skipped"><h3>' + skipped + '</h3><p>Skipped</p></div>' +
    '</div>' +
    '<div class="card"><h2><i class="fas fa-info-circle"></i> Project Overview</h2>' +
    '<p style="font-size:.85rem;color:#475569;margin-bottom:1rem">Automated web testing with Selenium 4, TestNG, Page Object Model against practicesoftwaretesting.com</p>' +
    '<div class="tags"><span class="tag">Java</span><span class="tag">Selenium 4</span><span class="tag">TestNG</span><span class="tag">POM</span><span class="tag">Allure</span></div></div>' +
    '<div class="card"><h2><i class="fas fa-list"></i> Test Results</h2>' +
    '<table><thead><tr><th>Test</th><th>Class</th><th>Status</th><th>Time</th></tr></thead><tbody>' +
    rows.join('\n') +
    '</tbody></table></div>' +
    '<a href="https://github.com/kestupinanmicolta/selenium-web-automation" class="src"><i class="fab fa-github"></i> View Source Code</a>' +
    '</div><div class="footer">Generated on ' + ts + ' | Karen Paola Estupinan Micolta</div></body></html>';

fs.writeFileSync(path.join(publicDir, 'index.html'), html, 'utf8');
console.log('Report: ' + total + ' tests, ' + passed + ' passed, ' + failed + ' failed, ' + skipped + ' skipped');
console.log('File size: ' + Buffer.byteLength(html) + ' bytes');
