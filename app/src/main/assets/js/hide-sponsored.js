(function() {
  if (window._mpHideSponsoredApplied) return;
  window._mpHideSponsoredApplied = true;

  // Hide the "Sponsored" section block on listing detail pages. It's an ad
  // injected between "Details" and "Seller information" with a leading H2.
  // (This does NOT hide feed-item sponsored ads — those use a span/div label,
  // not an H2 heading, and the feed-grid-fix keeps them aligned 2-wide.)

  function hideSponsoredSections() {
    document.querySelectorAll('[role="main"] h2').forEach(function(h) {
      if (h._mpSponsoredHidden) return;
      if ((h.textContent || '').trim() !== 'Sponsored') return;
      h._mpSponsoredHidden = true;
      var cur = h;
      for (var i = 0; i < 12 && cur && cur.tagName !== 'BODY'; i++) {
        cur = cur.parentElement;
        if (!cur) break;
        if (cur.getBoundingClientRect().height > 150) {
          cur.style.display = 'none';
          return;
        }
      }
    });
  }

  setTimeout(hideSponsoredSections, 500);
  setTimeout(hideSponsoredSections, 1500);
  setTimeout(hideSponsoredSections, 3000);

  var pending = false;
  var observer = new MutationObserver(function() {
    if (pending) return;
    pending = true;
    requestAnimationFrame(function() {
      pending = false;
      hideSponsoredSections();
    });
  });
  function startObserver() {
    var main = document.querySelector('[role="main"]');
    if (!main) { setTimeout(startObserver, 500); return; }
    observer.observe(main, { childList: true, subtree: true });
  }
  startObserver();
})();
