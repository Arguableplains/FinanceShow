function accountRegistrationChange(accountSelect) {
    // Setting the new account value to filter the transactions
    const accountValue = accountSelect.value;

    // Calling the method to get transactions - HomeController
    fetch("/transaction/update-account-cookie", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ account_value: accountValue })
    })
    .then(response => response.json())
    .catch(err => console.error("Error:", err));
}
