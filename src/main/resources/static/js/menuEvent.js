const menuWrapper = document.querySelector(".menu-wrapper");

menuWrapper.addEventListener("mouseover", () => {
    menuWrapper.style.opacity = "1";
    menuWrapper.style.left = "0";
});

menuWrapper.addEventListener("mouseleave", () => {
    menuWrapper.style.opacity = "0";
    menuWrapper.style.left = "-150px";
});