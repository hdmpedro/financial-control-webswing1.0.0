function sendMsg(msg) {
	webswingWindow.performAction({actionName: "showMsg", data: msg});
}

document.querySelector("#inj-send-btn").addEventListener("click", () => {
	sendMsg(document.querySelector('#inj-msg').value);
});