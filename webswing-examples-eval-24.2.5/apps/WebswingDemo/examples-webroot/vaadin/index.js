function showTable() {
    document.querySelector("#ws-tabs").selectTab(document.querySelector("#table-tab"));
}

function checkVaadinRunning() {
  fetch(window.location.origin + "/vaadin", { method: "HEAD" }).then((res) => {
    let templateId = res.ok ? "#vaadin-available" : "#vaadin-unavailable";
    document.querySelector("#vaadin-content").appendChild(document.querySelector(templateId).content.cloneNode(true));
  });
}

checkVaadinRunning();

document.querySelector("#vaadin-show-table").addEventListener("click", () => {
	showTable();
});