const searchParams = new URLSearchParams(window.location.search);
const blobUrl = searchParams.get('file');
window.location.href = blobUrl;