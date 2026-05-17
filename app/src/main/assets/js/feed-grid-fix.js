(function() {
  if (window._mpFeedGridFixApplied) return;
  window._mpFeedGridFixApplied = true;

  var style = document.createElement('style');
  style.id = 'marketplace-feed-grid-fix';
  style.textContent = `
    /* Force Marketplace feed to a stable 2-column grid.
       Facebook changed their grid template ~mid-May 2026; their new math
       collapses to 3+ tiny columns at our WebView's viewport width. */

    [role="main"] [role="feed"] {
      display: grid !important;
      grid-template-columns: 1fr 1fr !important;
      gap: 8px !important;
      width: 100% !important;
    }

    [role="main"] [role="feed"] > div {
      width: auto !important;
      min-width: 0 !important;
      max-width: none !important;
      grid-column: auto !important;
      grid-row: auto !important;
    }

    /* Fallback: any grid container under main that's not the feed role */
    [role="main"] div[style*="grid-template-columns"] {
      grid-template-columns: 1fr 1fr !important;
    }

    [role="main"] div[style*="grid-template-columns"] > div {
      min-width: 0 !important;
      width: auto !important;
    }

    /* Last-ditch: stop any descendant from forcing column count via inline style */
    [role="main"] [style*="column-count"] {
      column-count: 2 !important;
    }
  `;
  if (!document.getElementById('marketplace-feed-grid-fix')) {
    (document.head || document.documentElement).appendChild(style);
  }

  if (window._mpDebug) {
    var feeds = document.querySelectorAll('[role="main"] [role="feed"]');
    console.log('[feed-grid-fix] role=feed containers found:', feeds.length);
    feeds.forEach(function(f, i) {
      var cs = window.getComputedStyle(f);
      console.log('[feed-grid-fix] feed[' + i + '] display=' + cs.display +
        ' gtc=' + cs.gridTemplateColumns + ' children=' + f.children.length);
    });
    var grids = document.querySelectorAll('[role="main"] div[style*="grid-template-columns"]');
    console.log('[feed-grid-fix] inline grid-template-columns count:', grids.length);
  }
})();
