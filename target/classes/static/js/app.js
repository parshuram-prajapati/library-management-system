/**
 * app.js
 * Global Application Logic
 * Only put code here that should run on EVERY page (like tooltips or simple UI toggles).
 */

document.addEventListener('DOMContentLoaded', () => {
    console.log('Library App Initialized');

    // 1. Initialize Bootstrap Tooltips (Global UI feature)
    // This finds any element with data-bs-toggle="tooltip" and activates it
    if (typeof bootstrap !== 'undefined') {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }

    // 2. Global Error Handler (Optional)
    window.addEventListener('unhandledrejection', function(event) {
        console.warn("Unhandled promise rejection:", event.reason);
    });
});