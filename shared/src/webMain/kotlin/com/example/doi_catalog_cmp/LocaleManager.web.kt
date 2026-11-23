package com.example.doi_catalog_cmp

actual fun setAppLocale(language: String) {
    updateBrowserLocale(language)
}

// Compose reads navigator.languages when it resolves localized resources.
@OptIn(ExperimentalWasmJsInterop::class)
private fun updateBrowserLocale(language: String) {
    js(
        """
        Object.defineProperty(window.navigator, "languages", {
            configurable: true,
            value: Object.freeze([language])
        });
        document.documentElement.lang = language;
        window.dispatchEvent(new Event("languagechange"));
        """
    )
}
