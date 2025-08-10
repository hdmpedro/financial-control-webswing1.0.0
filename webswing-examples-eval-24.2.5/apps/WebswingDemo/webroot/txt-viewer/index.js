const searchParams = new URLSearchParams(window.location.search);
const blobId = searchParams.get('file');
console.log("blobId", blobId)

document.addEventListener("DOMContentLoaded", function(event) {
	var fileContent = document.getElementById('filecontent');
	fileContent.value = 'Loading file content ...';
	
	fetch(blobId)
	  .then(response => response.blob())
	  .then(blob => {
	    const reader = new FileReader();
	    reader.readAsText(blob);
	    reader.onloadend = function() {
	      fileContent.value = reader.result;
	    };
	  })
	  .catch(error => {
		  fileContent.value = "Blob error " + error
		  console.error('blob error', error)
	  });
});