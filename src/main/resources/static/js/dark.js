const darkModeSwitch = document.querySelector("#dark-mode-switch");

darkModeSwitch.addEventListener("click", () => {
    document.body.classList.toggle("dark-mode");
    if (darkModeSwitch.className === "fa fa-toggle-off fa-lg text-black") {
        darkModeSwitch.className = "fa fa-toggle-on fa-lg text-black";
    } else {
        darkModeSwitch.className = "fa fa-toggle-off fa-lg text-black";
    }

    // Save mode on localStorage
    if (document.body.classList.contains("dark-mode")) {
        localStorage.setItem("dark-mode", "true");
    } else {
        localStorage.setItem("dark-mode", "false");
    }
});

// Get current mode
if (localStorage.getItem("dark-mode") === "true") {
    document.body.classList.add("dark-mode");
    darkModeSwitch.classList.add("active");
} else {
    document.body.classList.remove("dark-mode");
    darkModeSwitch.classList.remove("active");
}
