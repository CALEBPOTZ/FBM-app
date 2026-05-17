(function() {
  if (window._mpFeedGridFixApplied) return;
  window._mpFeedGridFixApplied = true;

  // Facebook Marketplace changed their feed layout: each section is now a
  // flex-wrap:wrap row container whose direct children have flex-basis:0
  // and flex-grow:1, sharing available width equally. When a row contains
  // sponsored placeholders or invisible slots, the visible items collapse
  // to tiny widths instead of wrapping cleanly.
  //
  // Detection: feed sections have a distinctive alternating <style>/<div>
  // child pattern (FB injects a per-item <style> tag before each item).
  // We require at least 2 <style> and 2 <div> children, with the counts
  // roughly balanced, and at least one child containing a link. This
  // matches both "Today's picks" and the sponsored/mixed feed sections,
  // and doesn't match other flex containers (category carousels, nav, etc.).
  //
  // Fix: force each non-<style> child to exactly 50% width.

  function fixContainer(el) {
    if (!el || el._mpFeedFixed) return false;
    var cs = window.getComputedStyle(el);
    if (cs.display !== 'flex') return false;
    if (cs.flexWrap !== 'wrap') return false;
    if (cs.flexDirection !== 'row') return false;
    if (el.children.length < 4) return false;

    var styleCount = 0, divCount = 0, linkCount = 0;
    for (var k = 0; k < el.children.length; k++) {
      var ch = el.children[k];
      if (ch.tagName === 'STYLE') styleCount++;
      else if (ch.tagName === 'DIV') divCount++;
      if (ch.querySelector && ch.querySelector('a[href]')) linkCount++;
    }

    if (styleCount < 2 || divCount < 2) return false;
    if (Math.abs(styleCount - divCount) > 1) return false;
    if (linkCount < 1) return false;

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

  function scan() {
    var main = document.querySelector('[role="main"]');
    if (!main) return 0;
    var count = 0;
    main.querySelectorAll('div').forEach(function(el) {
      if (fixContainer(el)) count++;
    });
    return count;
  }

  setTimeout(scan, 500);
  setTimeout(scan, 1500);
  setTimeout(scan, 3000);

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
