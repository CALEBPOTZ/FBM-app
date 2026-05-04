(function() {
  try {
    if (window._scrollToTopAdded) return;
    window._scrollToTopAdded = true;
    window._mpDebug && console.log('Scroll to top button loading...');

    var scrollStyle = document.createElement('style');
    scrollStyle.id = 'marketplace-scroll-to-top';
    scrollStyle.textContent = `
      .marketplace-scroll-top {
        position: fixed !important;
        bottom: 80px !important;
        right: 20px !important;
        width: 50px !important;
        height: 50px !important;
        border-radius: 50% !important;
        background: #1877f2 !important;
        color: white !important;
        display: flex !important;
        align-items: center !important;
        justify-content: center !important;
        box-shadow: 0 4px 12px rgba(0,0,0,0.3) !important;
        cursor: pointer !important;
        z-index: 9998 !important;
        font-size: 24px !important;
        opacity: 0 !important;
        visibility: hidden !important;
        transition: opacity 0.3s ease, visibility 0.3s ease, transform 0.2s ease !important;
        user-select: none !important;
        touch-action: manipulation !important;
      }
      .marketplace-scroll-top.visible {
        opacity: 1 !important;
        visibility: visible !important;
      }
      .marketplace-scroll-top:active {
        transform: scale(0.9) !important;
      }
    `;
    if (!document.getElementById('marketplace-scroll-to-top')) {
      document.head.appendChild(scrollStyle);
    }

    var scrollBtn = document.createElement('div');
    scrollBtn.className = 'marketplace-scroll-top';
    scrollBtn.innerHTML = '&#8679;';
    scrollBtn.title = 'Scroll to top';

    var scrollThreshold = 500;

    function checkScroll() {
      var scrollY = window.scrollY || window.pageYOffset || document.documentElement.scrollTop;
      if (scrollY > scrollThreshold) {
        scrollBtn.classList.add('visible');
      } else {
        scrollBtn.classList.remove('visible');
      }
    }

    scrollBtn.addEventListener('click', function(e) {
      e.preventDefault();
      e.stopPropagation();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    });

    scrollBtn.addEventListener('touchend', function(e) {
      e.preventDefault();
      e.stopPropagation();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    });

    window.addEventListener('scroll', checkScroll, { passive: true });

    setTimeout(function() {
      if (!document.querySelector('.marketplace-scroll-top')) {
        document.body.appendChild(scrollBtn);
      }
      checkScroll();
    }, 1500);

    window._mpDebug && console.log('Scroll to top button applied');
  } catch (e) {
    window._mpDebug && console.log('Scroll to top error:', e);
  }
})();
