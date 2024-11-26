function accountChange() {
    // Setting the new account value to filter the transactions
    const accountValue = document.getElementById("accountID").value;

    // Setting the category to filter the transactions
    const categoryValue = document.getElementById("categoryID").value;

    // Calling the method to get transactions - HomeController
    fetch("/update-transactions", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ account_value: accountValue , category_value: categoryValue})
    })
    .then(response => response.json())
    .then(data => {

        const transactionsTableBody = document.querySelector("table tbody");
        // Clear the current table rows
        transactionsTableBody.innerHTML = "";

        // Populate transactions
        data.transactions.forEach(transaction => {
            const row = document.createElement("tr");
            row.classList.add("border-b");

            row.innerHTML = `
                <td class="py-2 px-4">${transaction.account?.name || "N/A"}</td>
                <td class="py-2 px-4">${transaction.amount}</td>
                <td class="py-2 px-4">${transaction.category?.name || "N/A"}</td>
                <td class="py-2 px-4">${transaction.happenedOn}</td>
                <td class="py-2 px-4">${transaction.createdOn}</td>
                <td class="py-2 px-4">
                    <a href="/transaction/edit/${transaction.id}" class="text-blue-600 hover:underline">Editar</a>
                    <a href="/transaction/delete/${transaction.id}" class="text-red-600 hover:underline ml-4">Excluir</a>
                </td>
            `;
            transactionsTableBody.appendChild(row);
        });

    })
    .catch(err => console.error("Error:", err));
}
