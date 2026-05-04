(function() {
  window._mpDebug = __DEBUG__;
  window._mpDebug && console.log('Marketplace enhancements loading...');
  window.addEventListener('beforeunload', function() {
    if (window._marketplaceImageObserver) window._marketplaceImageObserver.disconnect();
    if (window._marketplaceTextareaObserver) window._marketplaceTextareaObserver.disconnect();
  });
  var style = document.createElement('style');
  style.id = 'marketplace-enhancements';
  style.textContent = `
    [role="banner"] { display: none !important; }
    [data-pagelet="LeftRail"], [aria-label="Navigation menu"] { display: none !important; }
    [data-pagelet="RightRail"] { display: none !important; }
    [role="contentinfo"] { display: none !important; }
    [role="main"] { max-width: 100% !important; margin: 0 !important; padding: 8px !important; }
    [data-testid*="app-banner"], [data-testid*="download-banner"] { display: none !important; }
    [aria-label*="Create post"], [aria-label*="Create a post"], [aria-label*="Stories"] { display: none !important; }
    body { background-color: #f0f2f5 !important; }
    @media (prefers-color-scheme: dark) { body { background-color: #18191a !important; } }
  `;
  if (!document.getElementById('marketplace-enhancements')) {
    document.head.appendChild(style);
  }
  window._mpDebug && console.log('Marketplace enhancements applied');
})();
