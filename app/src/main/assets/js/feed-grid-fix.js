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
  //
  // Infinite scroll appends new children to containers we've already fixed,
  // so matched containers are remembered and re-fixed on every scan; only
  // individual children are marked done. FB's SPA can also replace the
  // [role="main"] node, orphaning the MutationObserver — ensureObserver()
  // re-attaches whenever the node changes.

  var matchedContainers = new WeakSet();

  function looksLikeFeedContainer(el) {
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
    return true;
  }

  function fixChildren(el) {
    var fixed = 0;
    for (var i = 0; i < el.children.length; i++) {
      var c = el.children[i];
      if (c.tagName === 'STYLE') continue;
      if (c._mpFeedItemFixed) continue;
      c.style.flex = '0 0 calc(50% - 4px)';
      c.style.maxWidth = 'calc(50% - 4px)';
      c.style.minWidth = '0';
      c._mpFeedItemFixed = true;
      fixed++;
    }
    if (!el._mpFeedGapSet) {
      el.style.gap = '8px';
      el._mpFeedGapSet = true;
    }
    return fixed;
  }

  function scan() {
    var main = document.querySelector('[role="main"]');
    if (!main) return 0;
    ensureObserver(main);
    var count = 0;
    main.querySelectorAll('div').forEach(function(el) {
      if (matchedContainers.has(el) || looksLikeFeedContainer(el)) {
        matchedContainers.add(el);
        count += fixChildren(el);
      }
    });
    return count;
  }

  var pending = false;
  var observer = new MutationObserver(function() {
    if (pending) return;
    pending = true;
    requestAnimationFrame(function() {
      pending = false;
      scan();
    });
  });

  var observedMain = null;
  function ensureObserver(main) {
    if (observedMain === main) return;
    observer.disconnect();
    observer.observe(main, { childList: true, subtree: true });
    observedMain = main;
  }

  setTimeout(scan, 500);
  setTimeout(scan, 1500);
  setTimeout(scan, 3000);

  // Safety net: if the SPA swaps [role="main"] the observer goes quiet;
  // notice the swap and rescan (which also re-attaches the observer).
  setInterval(function() {
    var main = document.querySelector('[role="main"]');
    if (main && main !== observedMain) scan();
  }, 2000);

  window._mpRescanFeed = scan;

  if (window._mpDebug) {
    console.log('[feed-grid-fix] installed');
  }
})();
