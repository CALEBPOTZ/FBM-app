(function() {
  try {
    if (window._imageEnhancementsAdded) return;
    window._imageEnhancementsAdded = true;
    window._mpDebug && console.log('Image enhancements loading...');

    var imageStyle = document.createElement('style');
    imageStyle.id = 'marketplace-image-enhancements';
    imageStyle.textContent = `
      .marketplace-image-viewer {
        position: fixed !important;
        top: 0 !important; left: 0 !important;
        width: 100vw !important; height: 100vh !important;
        background: rgba(0, 0, 0, 0.95) !important;
        z-index: 999999 !important;
        display: flex !important;
        align-items: center !important;
        justify-content: center !important;
        overflow: hidden !important;
        touch-action: none !important;
      }
      .marketplace-image-viewer img {
        max-width: 95vw !important;
        max-height: 95vh !important;
        object-fit: contain !important;
        touch-action: none !important;
      }
      .marketplace-image-viewer-close {
        position: absolute !important;
        top: 20px !important; right: 20px !important;
        width: 50px !important; height: 50px !important;
        background: rgba(255, 255, 255, 0.3) !important;
        border-radius: 50% !important;
        display: flex !important;
        align-items: center !important;
        justify-content: center !important;
        cursor: pointer !important;
        font-size: 30px !important;
        color: white !important;
        z-index: 1000000 !important;
      }
      .marketplace-image-controls {
        position: absolute !important;
        bottom: 30px !important;
        left: 50% !important;
        transform: translateX(-50%) !important;
        color: white !important;
        background: rgba(0,0,0,0.5) !important;
        padding: 8px 16px !important;
        border-radius: 20px !important;
        font-size: 14px !important;
        z-index: 1000000 !important;
      }
      .marketplace-image-nav-button {
        position: absolute !important;
        top: 50% !important;
        transform: translateY(-50%) !important;
        width: 50px !important;
        height: 50px !important;
        background: rgba(255, 255, 255, 0.3) !important;
        border-radius: 50% !important;
        display: flex !important;
        align-items: center !important;
        justify-content: center !important;
        cursor: pointer !important;
        font-size: 30px !important;
        color: white !important;
        z-index: 1000000 !important;
      }
      .marketplace-image-nav-left { left: 20px !important; }
      .marketplace-image-nav-right { right: 20px !important; }
    `;
    if (!document.getElementById('marketplace-image-enhancements')) {
      document.head.appendChild(imageStyle);
    }

    window._marketplaceImageViewer = null;
    window._marketplaceImages = [];
    window._marketplaceImageIndex = 0;

    function closeImageViewer() {
      if (window._marketplaceImageViewer) {
        window._marketplaceImageViewer.remove();
        window._marketplaceImageViewer = null;
      }
    }
    window.closeMarketplaceImageViewer = closeImageViewer;

    function showImageViewer(images, index, clickedImg) {
      window._marketplaceImages = images;
      window._marketplaceImageIndex = index;
      window._marketplaceCarouselNav = clickedImg ? findCarouselNav(clickedImg) : null;
      window._marketplaceImageTotal = getImageTotal() || images.length;
      closeImageViewer();

      var viewer = document.createElement('div');
      viewer.className = 'marketplace-image-viewer';
      window._marketplaceImageViewer = viewer;

      var img = document.createElement('img');
      img.src = images[index];

      var closeBtn = document.createElement('div');
      closeBtn.className = 'marketplace-image-viewer-close';
      closeBtn.innerHTML = '&times;';

      var downloadBtn = document.createElement('div');
      downloadBtn.className = 'marketplace-image-viewer-close';
      downloadBtn.style.right = '80px';
      downloadBtn.innerHTML = '&#8615;';
      downloadBtn.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        var src = window._marketplaceImages[window._marketplaceImageIndex];
        if (typeof MarketplaceApp !== 'undefined' && MarketplaceApp.saveImage) {
          MarketplaceApp.saveImage(src);
        }
      });

      var controls = document.createElement('div');
      controls.className = 'marketplace-image-controls';
      var total = window._marketplaceImageTotal || images.length;
      controls.textContent = (index + 1) + ' / ' + total;

      viewer.appendChild(img);
      viewer.appendChild(closeBtn);
      viewer.appendChild(downloadBtn);
      viewer.appendChild(controls);

      if (images.length > 1 || window._marketplaceCarouselNav) {
        var leftBtn = document.createElement('div');
        leftBtn.className = 'marketplace-image-nav-button marketplace-image-nav-left';
        leftBtn.innerHTML = '&#8249;';

        var rightBtn = document.createElement('div');
        rightBtn.className = 'marketplace-image-nav-button marketplace-image-nav-right';
        rightBtn.innerHTML = '&#8250;';

        leftBtn.addEventListener('click', function(e) { e.stopPropagation(); navigateImage(-1); });
        rightBtn.addEventListener('click', function(e) { e.stopPropagation(); navigateImage(1); });

        viewer.appendChild(leftBtn);
        viewer.appendChild(rightBtn);
      }

      closeBtn.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        closeImageViewer();
      });

      viewer.addEventListener('click', function(e) {
        if (e.target === viewer) {
          e.preventDefault();
          e.stopPropagation();
          closeImageViewer();
        }
      });

      var touchStartX = 0;
      var touchStartY = 0;
      var touchEndX = 0;

      img.addEventListener('touchstart', function(e) {
        touchStartX = e.touches[0].clientX;
        touchStartY = e.touches[0].clientY;
      }, { passive: true });

      img.addEventListener('touchend', function(e) {
        touchEndX = e.changedTouches[0].clientX;
        var diffX = touchStartX - touchEndX;
        var diffY = Math.abs(touchStartY - e.changedTouches[0].clientY);
        if (Math.abs(diffX) > 50 && diffY < 100 && (images.length > 1 || window._marketplaceCarouselNav)) {
          if (diffX > 0) navigateImage(1);
          else navigateImage(-1);
        }
      }, { passive: true });

      document.body.appendChild(viewer);
    }

    function navigateImage(dir) {
      var nav = window._marketplaceCarouselNav;
      if (nav) {
        var btn = dir > 0 ? nav.next : nav.prev;
        if (btn) {
          btn.click();
          setTimeout(function() {
            var newImg = findCurrentCarouselImage(nav.container);
            if (newImg) {
              window._marketplaceImageIndex += dir;
              var viewer = window._marketplaceImageViewer;
              if (viewer) {
                var img = viewer.querySelector('img');
                var controls = viewer.querySelector('.marketplace-image-controls');
                if (img) img.src = newImg;
                if (controls) {
                  var total = window._marketplaceImageTotal || '?';
                  controls.textContent = (window._marketplaceImageIndex + 1) + ' / ' + total;
                }
              }
            }
          }, 400);
        }
      } else {
        var images = window._marketplaceImages;
        var index = window._marketplaceImageIndex;
        index += dir;
        if (index < 0) index = images.length - 1;
        else if (index >= images.length) index = 0;
        window._marketplaceImageIndex = index;
        var viewer = window._marketplaceImageViewer;
        if (viewer) {
          var img = viewer.querySelector('img');
          var controls = viewer.querySelector('.marketplace-image-controls');
          if (img) img.src = images[index];
          if (controls) controls.textContent = (index + 1) + ' / ' + images.length;
        }
      }
    }

    function findCarouselNav(img) {
      var parent = img.parentElement;
      for (var i = 0; i < 10 && parent; i++) {
        if (parent.getAttribute && parent.getAttribute('role') === 'main') break;
        var next = parent.querySelector('[aria-label*="Next"], [aria-label*="next"], [data-testid*="next"]');
        var prev = parent.querySelector('[aria-label*="Previous"], [aria-label*="previous"], [aria-label*="Back"], [data-testid*="prev"]');
        if (next || prev) {
          return { container: parent, next: next, prev: prev };
        }
        parent = parent.parentElement;
      }
      return null;
    }

    function findCurrentCarouselImage(container) {
      var imgs = container.querySelectorAll('img');
      var best = null;
      var bestArea = 0;
      for (var i = 0; i < imgs.length; i++) {
        var img = imgs[i];
        if (!img.src || !img.src.includes('scontent')) continue;
        var w = img.offsetWidth || img.naturalWidth || 0;
        var h = img.offsetHeight || img.naturalHeight || 0;
        var area = w * h;
        if (area > bestArea) {
          bestArea = area;
          best = img.src;
        }
      }
      return best;
    }

    function getImageTotal() {
      var indicators = document.querySelectorAll('[aria-label*="photo"], [aria-label*="image"], [aria-label*="Photo"]');
      for (var i = 0; i < indicators.length; i++) {
        var text = indicators[i].textContent || indicators[i].getAttribute('aria-label') || '';
        var match = text.match(/(\d+)\s*(?:of|\/)\s*(\d+)/);
        if (match) return parseInt(match[2]);
      }
      var dots = document.querySelectorAll('[role="tablist"] [role="tab"], .carousel-dots > *, [data-testid*="indicator"] > *');
      if (dots.length > 1) return dots.length;
      return null;
    }

    function findListingImages(clickedImg) {
      var images = [];
      var seen = {};
      var clickedSrc = clickedImg.src;
      var clickedW = clickedImg.offsetWidth || clickedImg.naturalWidth || 0;
      var clickedH = clickedImg.offsetHeight || clickedImg.naturalHeight || 0;
      window._mpDebug && console.log('Finding listing images, clicked size:', clickedW, 'x', clickedH);

      var carousel = null;
      var parent = clickedImg.parentElement;
      for (var i = 0; i < 8 && parent; i++) {
        if (parent.getAttribute && parent.getAttribute('role') === 'main') break;
        var cs = window.getComputedStyle(parent);
        var ox = cs.overflowX;
        var isScrollable = (ox === 'auto' || ox === 'scroll' || ox === 'hidden')
          && parent.scrollWidth > parent.clientWidth + 20;
        var hasSnap = cs.scrollSnapType && cs.scrollSnapType !== 'none';
        if (isScrollable || hasSnap) {
          carousel = parent;
          window._mpDebug && console.log('Found carousel at level', i);
          break;
        }
        parent = parent.parentElement;
      }

      if (carousel) {
        var carouselImgs = carousel.querySelectorAll('img');
        for (var j = 0; j < carouselImgs.length; j++) {
          var cimg = carouselImgs[j];
          var src = cimg.src;
          if (!src || !src.includes('scontent') || seen[src]) continue;
          var w = cimg.offsetWidth || cimg.naturalWidth || 0;
          var h = cimg.offsetHeight || cimg.naturalHeight || 0;
          if (w < 150 || h < 150) continue;
          if (clickedW > 100) {
            var wDiff = Math.abs(w - clickedW) / clickedW;
            var hDiff = Math.abs(h - clickedH) / (clickedH || 1);
            if (wDiff > 0.4 || hDiff > 0.4) continue;
          }
          images.push(src);
          seen[src] = true;
        }
      }

      if (images.length === 0 && clickedSrc) {
        images.push(clickedSrc);
      }

      var clickedIndex = images.indexOf(clickedSrc);
      if (clickedIndex > 0) {
        images.splice(clickedIndex, 1);
        images.unshift(clickedSrc);
      } else if (clickedIndex === -1 && clickedSrc) {
        images.unshift(clickedSrc);
      }

      window._mpDebug && console.log('Found', images.length, 'listing images');
      return images;
    }

    function attachImageClickHandlers() {
      var url = window.location.href;
      var isListingPage = url.includes('/marketplace/item/') || url.includes('/product/');
      var isMarketplace = url.includes('/marketplace');
      if (!isMarketplace) return;

      document.querySelectorAll('img').forEach(function(img) {
        if (img._imageHandlerAttached) return;
        if (!img.src || !img.src.includes('scontent')) return;

        var w = img.offsetWidth || img.naturalWidth || 0;
        var h = img.offsetHeight || img.naturalHeight || 0;

        if (!isListingPage) {
          if (w < 80 || h < 80) return;
          img._imageHandlerAttached = true;
          var link = null;
          var el = img;
          for (var i = 0; i < 10 && el; i++) {
            if (el.tagName === 'A' && el.href && el.href.includes('/marketplace/item/')) {
              link = el;
              break;
            }
            el = el.parentElement;
          }
          if (!link) return;
          img.style.cursor = 'pointer';
          img.addEventListener('click', function(e) {
            e.stopPropagation();
            e.preventDefault();
            if (typeof MarketplaceApp !== 'undefined' && MarketplaceApp.openListing) {
              MarketplaceApp.openListing(link.href);
            } else {
              window.location.href = link.href;
            }
          }, { passive: false });
          return;
        }

        if (w < 200 || h < 200) return;
        img._imageHandlerAttached = true;

        var touchStartX = 0;
        var touchStartY = 0;
        var touchStartTime = 0;
        var touchMoved = false;

        img.addEventListener('touchstart', function(e) {
          touchStartX = e.touches[0].clientX;
          touchStartY = e.touches[0].clientY;
          touchStartTime = Date.now();
          touchMoved = false;
        }, { passive: true });

        img.addEventListener('touchmove', function(e) {
          var dx = Math.abs(e.touches[0].clientX - touchStartX);
          var dy = Math.abs(e.touches[0].clientY - touchStartY);
          if (dx > 10 || dy > 10) touchMoved = true;
        }, { passive: true });

        img.addEventListener('touchend', function(e) {
          var dx = e.changedTouches[0].clientX - touchStartX;
          var dy = Math.abs(e.changedTouches[0].clientY - touchStartY);
          var elapsed = Date.now() - touchStartTime;

          if (Math.abs(dx) > 40 && dy < Math.abs(dx) && touchMoved) {
            e.preventDefault();
            e.stopPropagation();
            var nav = findCarouselNav(img);
            if (nav) {
              var btn = dx < 0 ? nav.next : nav.prev;
              if (btn) btn.click();
            }
            return;
          }

          if (touchMoved || elapsed > 300) return;
          e.preventDefault();
          e.stopPropagation();
          var images = findListingImages(img);
          var index = images.indexOf(img.src);
          if (index === -1) index = 0;
          showImageViewer(images, index, img);
        }, { passive: false });
      });
    }

    attachImageClickHandlers();

    var _imgDebounce = null;
    var observer = new MutationObserver(function() {
      clearTimeout(_imgDebounce);
      _imgDebounce = setTimeout(attachImageClickHandlers, 200);
    });
    observer.observe(document.body, { childList: true, subtree: true });
    window._marketplaceImageObserver = observer;

    window._mpDebug && console.log('Image enhancements applied');
  } catch (e) {
    window._mpDebug && console.log('Image enhancement error:', e);
  }
})();
