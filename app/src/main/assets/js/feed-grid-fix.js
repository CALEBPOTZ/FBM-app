(function() {
  if (window._mpFeedGridFixApplied) return;
  window._mpFeedGridFixApplied = true;

  var style = document.createElement('style');
  style.id = 'marketplace-feed-grid-fix';
  style.textContent = `
    /* Aggressive grid reset: force ALL grids under main to 2 columns
       and reset any child spans. Facebook changed their feed grid such
       that sponsored ads have larger column spans, and items after
       them flow into narrow gaps via grid-auto-flow: dense. */

    [role="main"] [role="feed"],
    [role="main"] [role="feed"] > div[style*="grid"],
    [role="main"] div[style*="display: grid"],
    [role="main"] div[style*="display:grid"],
    [role="main"] div[style*="grid-template-columns"] {
      display: grid !important;
      grid-template-columns: 1fr 1fr !important;
      grid-auto-flow: row !important;
      grid-auto-rows: auto !important;
      gap: 8px !important;
    }

    [role="main"] [role="feed"] > *,
    [role="main"] [role="feed"] > div > *,
    [role="main"] div[style*="grid-column"],
    [role="main"] div[style*="grid-row"] {
      grid-column: span 1 !important;
      grid-row: auto !important;
      width: auto !important;
      min-width: 0 !important;
      max-width: none !important;
    }
  `;
  if (!document.getElementById('marketplace-feed-grid-fix')) {
    (document.head || document.documentElement).appendChild(style);
  }

  // Floating diagnostic panel — shows what grids exist under [role="main"]
  function buildPanel() {
    var panel = document.getElementById('mp-grid-diag');
    if (panel) panel.remove();
    panel = document.createElement('div');
    panel.id = 'mp-grid-diag';
    panel.style.cssText = 'position:fixed;left:0;right:0;bottom:0;max-height:50vh;overflow:auto;background:rgba(0,0,0,0.92);color:#0f0;font:11px/1.3 monospace;padding:6px;z-index:2147483647;white-space:pre-wrap;border-top:2px solid #0f0';
    var close = document.createElement('button');
    close.textContent = 'X';
    close.style.cssText = 'position:absolute;top:2px;right:4px;background:#c00;color:#fff;border:0;padding:2px 8px;font:bold 12px monospace;cursor:pointer';
    close.onclick = function() { panel.remove(); };
    panel.appendChild(close);

    var out = ['== Grid diagnostic =='];
    var main = document.querySelector('[role="main"]');
    if (!main) { out.push('NO [role="main"] FOUND'); panel.appendChild(document.createTextNode(out.join('\n'))); document.body.appendChild(panel); return; }

    // Walk descendants; find anything that is display:grid
    var all = main.querySelectorAll('*');
    var grids = [];
    for (var i = 0; i < all.length && grids.length < 12; i++) {
      var el = all[i];
      var cs = window.getComputedStyle(el);
      if (cs.display === 'grid' || cs.display === 'inline-grid') {
        grids.push({ el: el, cs: cs });
      }
    }
    out.push('Total descendants: ' + all.length);
    out.push('Grid containers found: ' + grids.length);

    function describe(el) {
      var parts = [el.tagName.toLowerCase()];
      if (el.id) parts.push('#' + el.id);
      var role = el.getAttribute('role'); if (role) parts.push('[role=' + role + ']');
      var aria = el.getAttribute('aria-label'); if (aria) parts.push('[aria=' + aria.slice(0, 30) + ']');
      var dpl = el.getAttribute('data-pagelet'); if (dpl) parts.push('[pagelet=' + dpl + ']');
      var dt = el.getAttribute('data-testid'); if (dt) parts.push('[testid=' + dt + ']');
      var cls = (el.className && typeof el.className === 'string') ? el.className.slice(0, 50) : '';
      if (cls) parts.push('.' + cls.replace(/\s+/g, '.'));
      return parts.join('');
    }

    grids.forEach(function(g, i) {
      out.push('--- grid[' + i + '] ---');
      out.push(' el: ' + describe(g.el));
      out.push(' gtc: ' + g.cs.gridTemplateColumns);
      out.push(' flow: ' + g.cs.gridAutoFlow);
      out.push(' children: ' + g.el.children.length);
      for (var j = 0; j < Math.min(3, g.el.children.length); j++) {
        var c = g.el.children[j];
        var ccs = window.getComputedStyle(c);
        out.push('  child[' + j + ']: gc=' + ccs.gridColumn + ' gr=' + ccs.gridRow + ' w=' + Math.round(c.getBoundingClientRect().width) + 'px ' + describe(c));
      }
    });

    panel.appendChild(document.createTextNode(out.join('\n')));
    document.body.appendChild(panel);
  }

  // Run after page settles
  setTimeout(buildPanel, 2500);
  setTimeout(buildPanel, 6000);
  window._mpRebuildGridDiag = buildPanel;
})();
