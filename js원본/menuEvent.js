const menuWrapper = document.querySelector(".menu-wrapper");
const menuBtn = document.getElementById('menu-btn');
const menuCloseBtn = document.getElementById('menu-close-btn');

menuBtn.addEventListener("click", () => {
    menuWrapper.style.opacity = "1";
    menuWrapper.style.left = "0";
});

menuCloseBtn.addEventListener("click", () => {
    menuWrapper.style.opacity = "0";
    menuWrapper.style.left = "-200px";
});

document.addEventListener("click", (event) => {
    const target = event.target;
    const isMenuBtn = target.closest(".menu-btn") === menuBtn;
    const isMenuWrapper = target.closest(".menu-wrapper") === menuWrapper;
    if (!isMenuBtn && !isMenuWrapper ) {
        menuWrapper.style.opacity = "0";
        menuWrapper.style.left = "-200px";
    }
});