// dashboardCharts.js

// Parse the JSON data safely
var expensesByCategory, transactionsOverTime;
try {
    expensesByCategory = JSON.parse(expensesByCategoryData);
    transactionsOverTime = JSON.parse(transactionsOverTimeData);
    console.log("Expenses by Category Data:", expensesByCategory);
    console.log("Transactions Over Time Data:", transactionsOverTime);
} catch (error) {
    console.error("Error parsing JSON data:", error);
}

// Function to format a timestamp into a readable date
function formatTimestampToDate(timestamp) {
    var date = new Date(timestamp);
    // Format options can be adjusted as per your preference
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

// Expenses by Category Chart
(function() {
    var categoryLabels = [];
    var categoryAmounts = [];

    expensesByCategory.forEach(function(item) {
        var categoryName = item[0] || 'Sem Categoria';
        var sumAmount = Math.abs(item[1]); // Use absolute value
        categoryLabels.push(categoryName);
        categoryAmounts.push(sumAmount);
    });

    console.log("Category Labels:", categoryLabels);
    console.log("Category Amounts:", categoryAmounts);

    var ctx = document.getElementById('expensesByCategoryChart').getContext('2d');
    var expensesByCategoryChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: categoryLabels,
            datasets: [{
                data: categoryAmounts,
                backgroundColor: [
                    '#FF6384',
                    '#36A2EB',
                    '#FFCE56',
                    '#4BC0C0',
                    '#9966FF',
                    '#FF9F40',
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom',
                },
                title: {
                    display: false,
                }
            }
        }
    });
})();

// Transactions Over Time Chart
(function() {
    if (!transactionsOverTime || transactionsOverTime.length === 0) {
        console.warn("No transactions over time data available.");
        return;
    }

    var timeLabels = [];
    var timeAmounts = [];

    transactionsOverTime.forEach(function(item) {
        var timestamp = item[0];
        var date = formatTimestampToDate(timestamp);
        var sumAmount = item[1];
        console.log("Transaction Date:", date, "Amount:", sumAmount);
        timeLabels.push(date);
        timeAmounts.push(sumAmount);
    });

    console.log("Time Labels:", timeLabels);
    console.log("Time Amounts:", timeAmounts);

    var ctx2 = document.getElementById('transactionsOverTimeChart').getContext('2d');
    var transactionsOverTimeChart = new Chart(ctx2, {
        type: 'line',
        data: {
            labels: timeLabels,
            datasets: [{
                label: 'Saldo Diário',
                data: timeAmounts,
                borderColor: '#36A2EB',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                fill: true,
                tension: 0.1
            }]
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    type: 'category',
                    title: {
                        display: true,
                        text: 'Data'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Montante'
                    }
                }
            },
            plugins: {
                legend: {
                    display: false,
                },
                title: {
                    display: false,
                }
            }
        }
    });
})();