document.querySelector("#fc-download").addEventListener("click", () => {
	window.parent.webswingInstance0.getFilesManager().downloadSelectedFile();
});
document.querySelector("#fc-upload").addEventListener("click", () => {
	document.querySelector('#fc-upload-input').click();
});
document.querySelector("#fc-delete").addEventListener("click", () => {
	window.parent.webswingInstance0.getFilesManager().deleteSelectedFile();
});
document.querySelector("#fc-folder").addEventListener("click", () => {
	const name = document.querySelector("#fc-folder-name").value;
	if (name) {
		window.parent.webswingInstance0.performAction({actionName: 'file_explorer_new_folder', data: name});
	}
});
document.querySelector("#fc-upload-input").addEventListener("change", (e) => {
	var files;
	
    if (e.type === 'drop') {
        files = [...e.dataTransfer.files];
    } else {
        files = e.target?.files;
    }

    for (const file of files) {
    	window.parent.webswingInstance0.getFilesManager().uploadFile(file);
    }

    e.target.value = "";
});
