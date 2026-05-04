(function() {
  try {
    if (window._sideNavAdded) return;
    window._sideNavAdded = true;
    window._mpDebug && console.log('Side navigation loading...');

    var navStyle = document.createElement('style');
    navStyle.id = 'marketplace-side-nav';
    navStyle.textContent = `
      .marketplace-nav-overlay {
        position: fixed !important;
        top: 0 !important; left: 0 !important;
        width: 100vw !important; height: 100vh !important;
        background: rgba(0, 0, 0, 0.5) !important;
        z-index: 99998 !important;
        opacity: 0 !important; visibility: hidden !important;
        transition: opacity 0.3s ease, visibility 0.3s ease !important;
      }
      .marketplace-nav-overlay.visible { opacity: 1 !important; visibility: visible !important; }

      .marketplace-side-nav {
        position: fixed !important;
        top: 0 !important; left: 0 !important;
        width: 280px !important; height: 100vh !important;
        background: #ffffff !important;
        z-index: 99999 !important;
        box-shadow: 2px 0 12px rgba(0,0,0,0.3) !important;
        transform: translateX(-100%) !important;
        transition: transform 0.3s ease !important;
        display: flex !important; flex-direction: column !important;
        overflow: hidden !important;
      }
      .marketplace-side-nav.open { transform: translateX(0) !important; }

      .marketplace-nav-header {
        background: #1877f2 !important; color: white !important;
        padding: 20px 16px !important;
        font-size: 20px !important; font-weight: bold !important;
        display: flex !important; align-items: center !important; justify-content: space-between !important;
      }
      .marketplace-nav-close {
        width: 36px !important; height: 36px !important;
        border-radius: 50% !important;
        background: rgba(255,255,255,0.2) !important; color: white !important;
        display: flex !important; align-items: center !important; justify-content: center !important;
        font-size: 24px !important; cursor: pointer !important;
      }

      .marketplace-nav-search {
        padding: 16px !important;
        border-bottom: 1px solid #e4e6eb !important;
      }
      .marketplace-nav-search input {
        width: 100% !important;
        padding: 12px 16px !important;
        border: 1px solid #dddfe2 !important;
        border-radius: 24px !important;
        font-size: 16px !important;
        outline: none !important;
        box-sizing: border-box !important;
      }
      .marketplace-nav-search input:focus { border-color: #1877f2 !important; }

      .marketplace-nav-items {
        flex: 1 !important;
        overflow-y: auto !important;
        padding: 8px 0 !important;
      }
      .marketplace-nav-item {
        display: flex !important; align-items: center !important;
        padding: 14px 16px !important;
        cursor: pointer !important;
        transition: background 0.2s ease !important;
        font-size: 16px !important; color: #1c1e21 !important;
      }
      .marketplace-nav-item:active { background: #f0f2f5 !important; }
      .marketplace-nav-item-icon {
        width: 36px !important; height: 36px !important;
        border-radius: 50% !important;
        background: #e4e6eb !important;
        display: flex !important; align-items: center !important; justify-content: center !important;
        margin-right: 12px !important; font-size: 18px !important;
      }
      .marketplace-nav-divider {
        height: 1px !important;
        background: #e4e6eb !important;
        margin: 8px 16px !important;
      }

      .marketplace-nav-footer {
        padding: 16px !important;
        border-top: 1px solid #e4e6eb !important;
        background: #f7f8fa !important;
      }
      .marketplace-nav-version {
        font-size: 12px !important; color: #65676b !important;
        text-align: center !important; margin-bottom: 8px !important;
      }

      .marketplace-swipe-indicator {
        position: fixed !important;
        top: 50% !important; left: 0 !important;
        width: 4px !important; height: 60px !important;
        transform: translateY(-50%) !important;
        background: #1877f2 !important;
        border-radius: 0 4px 4px 0 !important;
        opacity: 0.5 !important;
        z-index: 9997 !important;
        transition: width 0.2s ease, opacity 0.2s ease !important;
      }
      .marketplace-swipe-indicator:active { width: 8px !important; opacity: 0.8 !important; }

      @media (prefers-color-scheme: dark) {
        .marketplace-side-nav { background: #242526 !important; }
        .marketplace-nav-search { border-color: #3a3b3c !important; }
        .marketplace-nav-search input {
          background: #3a3b3c !important;
          border-color: #3a3b3c !important;
          color: #e4e6eb !important;
        }
        .marketplace-nav-search input::placeholder { color: #b0b3b8 !important; }
        .marketplace-nav-item { color: #e4e6eb !important; }
        .marketplace-nav-item:active { background: #3a3b3c !important; }
        .marketplace-nav-item-icon { background: #3a3b3c !important; }
        .marketplace-nav-divider { background: #3a3b3c !important; }
        .marketplace-nav-footer { background: #242526 !important; border-color: #3a3b3c !important; }
        .marketplace-nav-version { color: #b0b3b8 !important; }
      }
    `;
    if (!document.getElementById('marketplace-side-nav')) {
      document.head.appendChild(navStyle);
    }

    var overlay = document.createElement('div');
    overlay.className = 'marketplace-nav-overlay';

    var sideNav = document.createElement('div');
    sideNav.className = 'marketplace-side-nav';

    var header = document.createElement('div');
    header.className = 'marketplace-nav-header';
    header.innerHTML = '<span>Marketplace</span><div class="marketplace-nav-close">&times;</div>';

    var searchSection = document.createElement('div');
    searchSection.className = 'marketplace-nav-search';
    var searchInput = document.createElement('input');
    searchInput.type = 'text';
    searchInput.placeholder = 'Search Marketplace';
    searchSection.appendChild(searchInput);

    var navItems = document.createElement('div');
    navItems.className = 'marketplace-nav-items';

    var items = [
      { icon: '&#9733;', label: 'Saved Listings', url: 'https://www.facebook.com/marketplace/you/saved', action: null },
      { icon: '&#9776;', label: 'My Listings', url: 'https://www.facebook.com/marketplace/you/selling', action: null },
      { icon: '&#128269;', label: 'Saved Searches', url: 'https://www.facebook.com/marketplace/you/saved_searches', action: null },
      { icon: '&#128279;', label: 'Share This Page', url: null, action: 'share' },
      { icon: '&#8635;', label: 'Check for Updates', url: null, action: 'checkUpdates' }
    ];

    function activate(item) {
      closeNav();
      if (item.action === 'checkUpdates') {
        if (typeof MarketplaceApp !== 'undefined' && MarketplaceApp.checkForUpdates) {
          MarketplaceApp.checkForUpdates();
        }
      } else if (item.action === 'share') {
        if (typeof MarketplaceApp !== 'undefined' && MarketplaceApp.shareListing) {
          MarketplaceApp.shareListing(window.location.href, document.title || 'Marketplace Listing');
        }
      } else if (item.url) {
        window.location.href = item.url;
      }
    }

    items.forEach(function(item) {
      var navItem = document.createElement('div');
      navItem.className = 'marketplace-nav-item';
      navItem.innerHTML = '<div class="marketplace-nav-item-icon">' + item.icon + '</div><span>' + item.label + '</span>';
      navItem.addEventListener('click', function() { activate(item); });
      navItem.addEventListener('touchend', function(e) { e.preventDefault(); activate(item); });
      navItems.appendChild(navItem);
    });

    var footer = document.createElement('div');
    footer.className = 'marketplace-nav-footer';
    var versionText = 'Version: ';
    if (typeof MarketplaceApp !== 'undefined' && MarketplaceApp.getAppVersion) {
      versionText += MarketplaceApp.getAppVersion();
    } else {
      versionText += 'Unknown';
    }
    footer.innerHTML = '<div class="marketplace-nav-version">' + versionText + '</div>';

    sideNav.appendChild(header);
    sideNav.appendChild(searchSection);
    sideNav.appendChild(navItems);
    sideNav.appendChild(footer);

    var swipeIndicator = document.createElement('div');
    swipeIndicator.className = 'marketplace-swipe-indicator';

    function openNav() {
      sideNav.classList.add('open');
      overlay.classList.add('visible');
    }
    function closeNav() {
      sideNav.classList.remove('open');
      overlay.classList.remove('visible');
    }
    window._marketplaceSideNavClose = closeNav;
    window._marketplaceSideNavIsOpen = function() { return sideNav.classList.contains('open'); };

    header.querySelector('.marketplace-nav-close').addEventListener('click', closeNav);
    header.querySelector('.marketplace-nav-close').addEventListener('touchend', function(e) {
      e.preventDefault();
      closeNav();
    });

    overlay.addEventListener('click', closeNav);
    overlay.addEventListener('touchend', function(e) { e.preventDefault(); closeNav(); });

    searchInput.addEventListener('keypress', function(e) {
      if (e.key === 'Enter' || e.keyCode === 13) {
        var query = searchInput.value.trim();
        if (query) {
          closeNav();
          window.location.href = 'https://www.facebook.com/marketplace/search?query=' + encodeURIComponent(query);
        }
      }
    });

    var touchStartX = 0;
    var touchStartY = 0;
    var touchMoveX = 0;
    var isSwipingNav = false;
    var swipeThreshold = 50;
    var edgeWidth = 30;

    document.addEventListener('touchstart', function(e) {
      if (sideNav.classList.contains('open')) return;
      var touch = e.touches[0];
      if (touch.clientX <= edgeWidth) {
        touchStartX = touch.clientX;
        touchStartY = touch.clientY;
        isSwipingNav = true;
      }
    }, { passive: true });

    document.addEventListener('touchmove', function(e) {
      if (!isSwipingNav) return;
      var touch = e.touches[0];
      touchMoveX = touch.clientX - touchStartX;
      var touchMoveY = Math.abs(touch.clientY - touchStartY);
      if (touchMoveY > Math.abs(touchMoveX)) {
        isSwipingNav = false;
        return;
      }
      if (touchMoveX > 0) {
        var progress = Math.min(touchMoveX / 200, 1);
        sideNav.style.transform = 'translateX(' + (-100 + (progress * 100)) + '%)';
        sideNav.style.transition = 'none';
        overlay.style.opacity = progress;
        overlay.style.visibility = 'visible';
        overlay.style.transition = 'none';
      }
    }, { passive: true });

    document.addEventListener('touchend', function() {
      if (!isSwipingNav) return;
      isSwipingNav = false;
      sideNav.style.transition = '';
      sideNav.style.transform = '';
      overlay.style.transition = '';
      overlay.style.opacity = '';
      if (touchMoveX > swipeThreshold) {
        openNav();
      } else {
        closeNav();
      }
      touchMoveX = 0;
    }, { passive: true });

    swipeIndicator.addEventListener('click', openNav);
    swipeIndicator.addEventListener('touchend', function(e) { e.preventDefault(); openNav(); });

    var navTouchStartX = 0;
    sideNav.addEventListener('touchstart', function(e) {
      navTouchStartX = e.touches[0].clientX;
    }, { passive: true });
    sideNav.addEventListener('touchend', function(e) {
      var navTouchEndX = e.changedTouches[0].clientX;
      if (navTouchStartX - navTouchEndX > 50) closeNav();
    }, { passive: true });

    setTimeout(function() {
      if (!document.querySelector('.marketplace-side-nav')) {
        document.body.appendChild(overlay);
        document.body.appendChild(sideNav);
        document.body.appendChild(swipeIndicator);
      }
    }, 1500);

    window._mpDebug && console.log('Side navigation applied');
  } catch (e) {
    window._mpDebug && console.log('Side navigation error:', e);
  }
})();
