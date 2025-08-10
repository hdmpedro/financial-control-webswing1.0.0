window.wsSelector = {
	showAdmin: false,
	adminConsoleUrl: null,
	options: {
		createSelectorPageContent: function(apps, showAdmin, adminConsoleUrl) {
			this.showAdmin = showAdmin;
			this.adminConsoleUrl = adminConsoleUrl;
			return false;
		},
		feedbackUrlLoaded: function(feedbackUrl) {
			if (feedbackUrl == null || feedbackUrl.length == 0) {
				return;
			}
			const feedbackTitle = document.querySelector("#feedbackTitle");
			feedbackTitle.textContent = translate("feedback.title", "We'd love to get some feedback!");
			const feedbackDiv = document.querySelector("#feedbackDiv");
			feedbackDiv.classList.remove('hidden');
			feedbackDiv.addEventListener("click", function() {
				window.open(feedbackUrl, "_blank");
			});
		},
		afterAppsCreated: function() {
			if (this.showAdmin && this.adminConsoleUrl != null) {
				const container = document.querySelector(".ws-selector-container");
				const qsUrl = this.adminConsoleUrl + (this.adminConsoleUrl.endsWith('/') ? "" : "/") + "quickstart";
				const qsApp = '<div class="ws-selector-btn"><a href="' + qsUrl + '" role="button" aria-labelledby="selector-btn-' + 0 + '">'
            		+ '<div class="ws-selector-btn-wrapper">'
					+ '<img src="' + "images/lightning.png" + '" class="ws-selector-btn-thumb"/>'
					+ '</div>'
					+ '<div id="selector-btn-' + 0 + '" class="ws-selector-btn-label">' + translate("quickstart.title", "Quickstart") + '</div>'
					+ '</a></div>';
					container.insertAdjacentHTML("afterbegin", qsApp);
			}
		}
	}
}

function translate(key, defaultString) {
	let translated = defaultString;
	if (wsSelector.options.translate) {
		translated = wsSelector.options.translate(key);
		if (!translated) {
			translated = defaultString;
		}
	}
	return translated;
}