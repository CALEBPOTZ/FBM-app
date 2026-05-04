(function() {
  try {
    if (window._scrollFixAdded) return;
    window._scrollFixAdded = true;
    window._mpDebug && console.log('Scroll fix loading...');

    var style = document.createElement('style');
    style.id = 'marketplace-scroll-fix';
    style.textContent = `
      html, body { overflow-y: auto !important; -webkit-overflow-scrolling: touch !important; height: 100% !important; }
      [role="dialog"], [role="dialog"] > div, [role="complementary"] {
        overflow-y: auto !important;
        -webkit-overflow-scrolling: touch !important;
        overscroll-behavior: contain !important;
      }
      div[style*="position: fixed"], div[style*="position:fixed"] {
        overflow-y: auto !important;
        -webkit-overflow-scrolling: touch !important;
      }
    `;
    if (!document.getElementById('marketplace-scroll-fix')) {
      document.head.appendChild(style);
    }

    window._mpDebug && console.log('Scroll fix applied');
  } catch (e) {
    window._mpDebug && console.log('Scroll fix error:', e);
  }
})();
