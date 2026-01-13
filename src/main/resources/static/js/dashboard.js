document.addEventListener('DOMContentLoaded', loadDashboard);

// ===== ANIMATE NUMBER COUNTER =====
function animateCounter(elementId, finalValue) {
    const element = document.getElementById(elementId);
    if (!element) return;

    const duration = 1200; // milliseconds
    const start = 0;
    const startTime = Date.now();

    function update() {
        const elapsed = Date.now() - startTime;
        const progress = Math.min(elapsed / duration, 1);

        // Easing function for smooth animation
        const easeOutQuad = 1 - Math.pow(1 - progress, 2);
        const currentValue = Math.floor(easeOutQuad * finalValue);

        element.innerText = currentValue;

        if (progress < 1) {
            requestAnimationFrame(update);
        } else {
            element.innerText = finalValue;
        }
    }

    update();
}

// ===== ANIMATE FINE WITH DECIMALS =====
function animateFineCounter(elementId, finalValue) {
    const element = document.getElementById(elementId);
    if (!element) return;

    const duration = 1500;
    const startTime = Date.now();

    function update() {
        const elapsed = Date.now() - startTime;
        const progress = Math.min(elapsed / duration, 1);

        const easeOutQuad = 1 - Math.pow(1 - progress, 2);
        const currentValue = (easeOutQuad * finalValue).toFixed(0);

        element.innerText = currentValue;

        if (progress < 1) {
            requestAnimationFrame(update);
        } else {
            element.innerText = finalValue;
        }
    }

    update();
}

async function loadDashboard() {
    try {
        const [books, students, issues] = await Promise.all([
            fetch('/api/books').then(r => r.json()),
            fetch('/api/students').then(r => r.json()),
            fetch('/api/issues').then(r => r.json())
        ]);

        // ---------------- COUNTS ----------------
        animateCounter('totalBooksCount', books.length);
        animateCounter('membersCount', students.length);

        const issued = issues.filter(i => !i.returnDate);
        const returned = issues.filter(i => i.returnDate);

        animateCounter('borrowedCount', issued.length);
        animateCounter('returnedCount', returned.length);

        const today = new Date();
        const overdue = issued.filter(i => new Date(i.dueDate) < today);
        animateCounter('overdueCount', overdue.length);

        // ---------------- FINE CALCULATION ----------------
        let totalFine = 0;
        overdue.forEach(i => {
            totalFine += calculateFine(i.dueDate);
        });

        animateFineCounter('totalFine', totalFine);

        // Add pulse animation to fine card on load
        const fineCard = document.getElementById('totalFine').closest('.card');
        if (fineCard && totalFine > 0) {
            fineCard.style.animation = 'glow 2s ease-in-out infinite';
        }

        // Add staggered animations to tables
        setTimeout(() => {
            renderOverdueTable(overdue);
        }, 600);

        setTimeout(() => {
            renderRecentTable(issues);
        }, 800);

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

    // Grace period: no fine for first 2 days after due date
    if (diffDays <= 2) return 0;
    return (diffDays - 2) * 2;
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


// ---------------- ADVANCED PIE CHART WITH PERCENTAGES ----------------
function renderPieChart(issued, returned) {
    const ctx = document.getElementById('checkoutChart');
    if (!ctx) return;

    if (window.chartInstance) {
        window.chartInstance.destroy();
    }

    const total = issued + returned;
    const issuedPercent = total > 0 ? Math.round((issued / total) * 100) : 0;
    const returnedPercent = total > 0 ? Math.round((returned / total) * 100) : 0;

    // Update percentage displays
    animatePercentage('issuedPercent', issuedPercent);
    animatePercentage('returnedPercent', returnedPercent);

    window.chartInstance = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Issued Books', 'Returned Books'],
            datasets: [{
                data: [issued, returned],
                backgroundColor: ['#28c76f', '#00d4ff'],
                borderColor: ['#ffffff', '#ffffff'],
                borderWidth: 3,
                borderRadius: 6,
                spacing: 2,
                hoverOffset: 8
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            animation: {
                duration: 2000,
                easing: 'easeInOutQuart',
                animateScale: true,
                animateRotate: true
            },
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    padding: 12,
                    cornerRadius: 8,
                    titleFont: { size: 13, weight: 'bold' },
                    bodyFont: { size: 12 },
                    callbacks: {
                        label: function(context) {
                            const label = context.label || '';
                            const value = context.parsed;
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percent = Math.round((value / total) * 100);
                            return label + ': ' + value + ' (' + percent + '%)';
                        }
                    }
                }
            }
        }
    });
}

// Animate percentage counter
function animatePercentage(elementId, finalValue) {
    const element = document.getElementById(elementId);
    if (!element) return;

    const duration = 1500;
    const startTime = Date.now();

    function update() {
        const elapsed = Date.now() - startTime;
        const progress = Math.min(elapsed / duration, 1);

        const easeOutQuad = 1 - Math.pow(1 - progress, 2);
        const currentValue = Math.floor(easeOutQuad * finalValue);

        element.innerText = currentValue + '%';

        if (progress < 1) {
            requestAnimationFrame(update);
        } else {
            element.innerText = finalValue + '%';
        }
    }

    update();
}
