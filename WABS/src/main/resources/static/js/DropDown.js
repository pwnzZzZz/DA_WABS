$(document).ready(function () {
    $("#navbarContainer").load("/static/css/navbarEmployee.html");
});
//alert("JavaScript-Code wird ausgef�hrt!");

// Adjust dropdown width when expanded
let dropdowns = document.querySelectorAll(".dropdown");
dropdowns.forEach((dropdown) => {
    let content = dropdown.querySelector(".dropdown-content");
    let links = content.querySelectorAll("a");
    let width = 0;
    links.forEach((link) => {
        width = Math.max(width, link.offsetWidth);
    });
    content.style.width = width + "px";
});

// Toggle dropdown content on click
let dropdownToggles = document.querySelectorAll(".dropdown > a");
dropdownToggles.forEach((toggle) => {
    let content = toggle.nextElementSibling;
    toggle.addEventListener("click", (event) => {
        event.preventDefault();
        content.classList.toggle("open");
    });
});

// Datepicker initialisieren
$(function () {
    $("#startDate").datepicker({
        dateFormat: "dd-mm-yy",
        onSelect: function (selectedDate) {
            $("#endDate").datepicker("option", "minDate", selectedDate);
        }
    });
    $("#endDate").datepicker({
        dateFormat: "dd-mm-yy",
        onSelect: function (selectedDate) {
            $("#startDate").datepicker("option", "maxDate", selectedDate);
        }
    });
});

// Begrenze die Uhrzeit auf volle Stunden
$(function () {
    $("#startTime").on("change", function () {
        var selectedHour = $(this).val().split(":")[0];
        $("#endTime option").prop("disabled", false);
        $("#endTime option").each(function () {
            var hour = $(this).val().split(":")[0];
            if (parseInt(hour) <= parseInt(selectedHour)) {
                $(this).prop("disabled", true);
            }
        });
    });
});