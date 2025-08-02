window.onload = () => {
    document.querySelector(".logout-button").addEventListener("click", async () => {
        const response = await fetch("/logout", {
            method: "POST",
        });
        if (!response.ok) {
            console.error("Logout failed");
            return;
        }
        window.location.href = "/login";
    });
}