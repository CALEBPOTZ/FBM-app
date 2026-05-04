(function() {
  if (window._searchEnterAdded) return;
  window._searchEnterAdded = true;
  window._mpDebug && console.log('Search enter key support loading...');

  document.addEventListener('keypress', function(e) {
    if (e.key === 'Enter' || e.keyCode === 13) {
      var target = e.target;
      if (target.tagName === 'INPUT'
          && target.getAttribute('placeholder')
          && target.getAttribute('placeholder').toLowerCase().includes('search')) {
        e.preventDefault();
        var searchValue = target.value.trim();
        if (searchValue) {
          window.location.href = 'https://www.facebook.com/marketplace/search?query='
            + encodeURIComponent(searchValue);
        }
      }
    }
  });

  window._mpDebug && console.log('Search enter key support applied');
})();
