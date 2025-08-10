const searchParams = new URLSearchParams(window.location.search);
const blobId = searchParams.get('file');
console.log("blobId", blobId)

document.addEventListener("DOMContentLoaded", function(event) {
	const operation = searchParams.get('operation');
	if(operation){
		document.getElementById('operation').innerText=operation;
	}
	
	var fileContent = document.getElementById('filecontent');
	fileContent.value = 'Loading file content ...';
	
	fetch(blobId)
	  .then(response => response.blob())
	  .then(blob => {
	    const reader = new FileReader();
	    reader.readAsText(blob);
	    reader.onloadend = function() {
	    	const rows = reader.result.split("\n");

	    	let table = "<table><thead><tr>";
	    	rows[0].split(";").forEach(header => {
	    	  table += `<th>${header}</th>`;
	    	});
	    	table += "</tr></thead><tbody>";

	    	for(let i = 1; i < rows.length; i++) {
	    	  const rowData = rows[i].split(";");
	    	  table += "<tr>";
	    	  rowData.forEach(cell => {
	    	    table += `<td>${cell}</td>`;
	    	  });
	    	  table += "</tr>";
	    	}
	    	table += "</tbody></table>";

	    	document.getElementById("filecontent").innerHTML = table;
	    };
	  })
	  .catch(error => {
		  fileContent.value = "Blob error " + error
		  console.error('blob error', error)
	  });
});