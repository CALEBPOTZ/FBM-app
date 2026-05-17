(function() {
  if (window._mpFeedGridFixApplied) return;
  window._mpFeedGridFixApplied = true;

  // Facebook Marketplace changed their feed layout: it's now a flex-wrap
  // container with flex-basis:0 children. When a row contains many slots
  // (some empty / display:none), the visible children collapse to tiny
  // widths instead of wrapping. The fix forces every real flex child to
  // exactly 50% width so the feed stays 2-wide regardless of slot count.

  function fixContainer(el) {
    if (!el || el._mpFeedFixed) return false;
    var cs = window.getComputedStyle(el);
    if (cs.display !== 'flex') return false;
    if (cs.flexWrap !== 'wrap') return false;
    if (cs.flexDirection !== 'row') return false;
    if (el.children.length < 2) return false;

    // Only fix containers that actually hold marketplace listings.
    var realChildren = 0;
    for (var k = 0; k < el.children.length; k++) {
      var ch = el.children[k];
      if (ch.tagName === 'STYLE') continue;
      if (ch.querySelector && ch.querySelector('a[href*="/marketplace/"]')) {
        realChildren++;
        if (realChildren >= 2) break;
      }
    }
    if (realChildren < 2) return false;

    for (var i = 0; i < el.children.length; i++) {
      var c = el.children[i];
      if (c.tagName === 'STYLE') continue;
      c.style.flex = '0 0 calc(50% - 4px)';
      c.style.maxWidth = 'calc(50% - 4px)';
      c.style.minWidth = '0';
    }
    el.style.gap = '8px';
    el._mpFeedFixed = true;
    return true;
  }

  function scan(root) {
    var main = (root || document).querySelector ? (root || document).querySelector('[role="main"]') : null;
    if (!main) main = document.querySelector('[role="main"]');
    if (!main) return 0;
    var count = 0;
    main.querySelectorAll('div').forEach(function(el) {
      if (fixContainer(el)) count++;
    });
    return count;
  }

  // Initial passes — content streams in over the first few seconds
  setTimeout(scan, 500);
  setTimeout(scan, 1500);
  setTimeout(scan, 3000);

  // Watch for newly added feed sections and apply the fix to them too.
  var pending = false;
  var observer = new MutationObserver(function() {
    if (pending) return;
    pending = true;
    requestAnimationFrame(function() {
      pending = false;
      scan();
    });
  });
  function startObserver() {
    var main = document.querySelector('[role="main"]');
    if (!main) { setTimeout(startObserver, 500); return; }
    observer.observe(main, { childList: true, subtree: true });
  }
  startObserver();

  window._mpRescanFeed = scan;

  if (window._mpDebug) {
    console.log('[feed-grid-fix] installed');
  }
})();
