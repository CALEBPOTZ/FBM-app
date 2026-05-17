(function() {
  if (window._mpPreconnectAdded) return;
  window._mpPreconnectAdded = true;

  function add(rel, href) {
    var link = document.createElement('link');
    link.rel = rel;
    link.href = href;
    link.crossOrigin = 'anonymous';
    (document.head || document.documentElement).appendChild(link);
  }

  add('preconnect', 'https://www.facebook.com');
  add('preconnect', 'https://static.xx.fbcdn.net');
  add('dns-prefetch', 'https://scontent.xx.fbcdn.net');
})();
