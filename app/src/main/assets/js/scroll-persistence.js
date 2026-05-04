(function() {
  try {
    if (window._mpScrollPersistAdded) return;
    var url = window.location.href;
    var isListingsArea = /\/marketplace(\/|\?|#|$)/.test(url)
      && !/\/marketplace\/item\//.test(url)
      && !/\/product\//.test(url);
    if (!isListingsArea) return;
    window._mpScrollPersistAdded = true;

    var KEY = 'mp_scroll_' + location.pathname + location.search;
    var SKIP_KEY = 'mp_skip_restore';
    var saveTimeout = null;

    function saveScroll() {
      clearTimeout(saveTimeout);
      saveTimeout = setTimeout(function() {
        try { sessionStorage.setItem(KEY, String(window.scrollY)); } catch (e) {}
      }, 200);
    }
    window.addEventListener('scroll', saveScroll, { passive: true });

    var skip = false;
    try {
      if (sessionStorage.getItem(SKIP_KEY)) {
        sessionStorage.removeItem(SKIP_KEY);
        sessionStorage.removeItem(KEY);
        skip = true;
      }
    } catch (e) {}
    if (skip) {
      window._mpDebug && console.log('Skipping scroll restore (refresh)');
      return;
    }

    var saved = null;
    try { saved = sessionStorage.getItem(KEY); } catch (e) {}
    if (saved) {
      var y = parseInt(saved, 10);
      if (!isNaN(y) && y > 0) {
        var attempts = 0;
        var restoreInterval = setInterval(function() {
          attempts++;
          if (document.body && document.body.scrollHeight > y + window.innerHeight) {
            window.scrollTo(0, y);
            clearInterval(restoreInterval);
          } else if (attempts > 30) {
            clearInterval(restoreInterval);
          }
        }, 200);
      }
    }
    window._mpDebug && console.log('Listings scroll persistence applied for ' + KEY);
  } catch (e) {
    window._mpDebug && console.log('Scroll persistence error:', e);
  }
})();
