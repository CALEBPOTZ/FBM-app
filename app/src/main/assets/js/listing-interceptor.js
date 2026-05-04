(function() {
  if (window._mpListingInterceptorAdded) return;
  window._mpListingInterceptorAdded = true;

  function isListingHref(href) {
    return href && (href.indexOf('/marketplace/item/') > -1 || href.indexOf('/product/') > -1);
  }

  function intercept(e) {
    var t = e.target;
    var anchor = t && t.closest ? t.closest('a[href]') : null;
    if (!anchor) return;
    var href = anchor.href || '';
    if (!isListingHref(href)) return;
    if (typeof MarketplaceApp === 'undefined' || !MarketplaceApp.openListing) return;
    e.preventDefault();
    e.stopPropagation();
    if (e.stopImmediatePropagation) e.stopImmediatePropagation();
    MarketplaceApp.openListing(href);
  }

  document.addEventListener('click', intercept, true);

  window._mpDebug && console.log('Listing link interceptor applied');
})();
