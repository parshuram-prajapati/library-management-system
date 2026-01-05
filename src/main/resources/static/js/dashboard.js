document.addEventListener('DOMContentLoaded', loadDashboard);

async function loadDashboard() {
    try {
        const [books, students, issues] = await Promise.all([
            fetch('/api/books').then(r => r.json()),
            fetch('/api/students').then(r => r.json()),
            fetch('/api/issues').then(r => r.json())
        ]);

        // ---------------- COUNTS ----------------
        document.getElementById('totalBooksCount').innerText = books.length;
        document.getElementById('membersCount').innerText = students.length;

        const issued = issues.filter(i => !i.returnDate);
        const returned = issues.filter(i => i.returnDate);

        document.getElementById('borrowedCount').innerText = issued.length;
        document.getElementById('returnedCount').innerText = returned.length;

        const today = new Date();
        const overdue = issued.filter(i => new Date(i.dueDate) < today);
        document.getElementById('overdueCount').innerText = overdue.length;

        // ---------------- FINE CALCULATION ----------------
        let totalFine = 0;
        overdue.forEach(i => {
            totalFine += calculateFine(i.dueDate);
        });

        document.getElementById('totalFine').innerText = totalFine;

        // ---------------- RENDER UI ----------------
        renderOverdueTable(overdue);
        renderRecentTable(issues);
        renderPieChart(issued.length, returned.length);

    } catch (err) {
        console.error("Dashboard error:", err);
    }
}

// ---------------- FINE LOGIC (₹2 PER DAY) ----------------
function calculateFine(dueDate) {
    const today = new Date();
    const due = new Date(dueDate);

    const diffTime = today - due;
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

    return diffDays > 0 ? diffDays * 2 : 0;
}

// ---------------- OVERDUE TABLE ----------------
function renderOverdueTable(list) {
    const tbody = document.getElementById('overdueTableBody');

    if (!list.length) {
        tbody.innerHTML = `<tr><td class="text-muted">No overdue books</td></tr>`;
        return;
    }

    tbody.innerHTML = list.slice(0, 5).map(i => `
        <tr>
          <td>
            <strong>${i.bookTitle}</strong><br>
            <small class="text-danger">
              Due: ${i.dueDate} | Fine: ₹${calculateFine(i.dueDate)}
            </small>
          </td>
        </tr>
    `).join('');
}

// ---------------- RECENT ACTIVITY ----------------
function renderRecentTable(issues) {
    const tbody = document.getElementById('recentActivityTableBody');

    if (!issues.length) {
        tbody.innerHTML = `<tr><td class="text-muted">No activity</td></tr>`;
        return;
    }

    tbody.innerHTML = issues.slice(-5).reverse().map(i => `
        <tr>
          <td>
            <strong>${i.bookTitle}</strong><br>
            <small>${i.studentName}</small>
          </td>
          <td>
            <span class="badge ${i.returnDate ? 'bg-info' : 'bg-success'}">
              ${i.returnDate ? 'Returned' : 'Issued'}
            </span>
          </td>
        </tr>
    `).join('');
}

// ---------------- PIE CHART ----------------
function renderPieChart(issued, returned) {
    const ctx = document.getElementById('checkoutChart');
    if (!ctx) return;

    if (window.chartInstance) {
        window.chartInstance.destroy();
    }

    window.chartInstance = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['Issued Books', 'Returned Books'],
            datasets: [{
                data: [issued, returned],
                backgroundColor: ['#28c76f', '#0dcaf0']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'bottom' }
            }
        }
    });
}
