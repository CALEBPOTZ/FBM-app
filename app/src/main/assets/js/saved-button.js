(function() {
  if (window._savedButtonAdded) return;
  window._savedButtonAdded = true;
  window._mpDebug && console.log('Saved listings access loading...');

  var savedPos = localStorage.getItem('marketplace_saved_button_pos');
  var position = savedPos ? JSON.parse(savedPos) : { top: 70, left: 10 };

  var savedStyle = document.createElement('style');
  savedStyle.id = 'marketplace-saved-button';
  savedStyle.textContent = `
    .marketplace-saved-fab {
      position: fixed !important;
      width: 56px !important;
      height: 56px !important;
      border-radius: 50% !important;
      background: #1877f2 !important;
      color: white !important;
      display: flex !important;
      align-items: center !important;
      justify-content: center !important;
      box-shadow: 0 4px 8px rgba(0,0,0,0.3) !important;
      cursor: move !important;
      z-index: 9999 !important;
      font-size: 24px !important;
      transition: box-shadow 0.2s ease !important;
      user-select: none !important;
      touch-action: none !important;
    }
    .marketplace-saved-fab.dragging {
      box-shadow: 0 8px 16px rgba(0,0,0,0.5) !important;
      opacity: 0.8 !important;
    }
  `;
  if (!document.getElementById('marketplace-saved-button')) {
    document.head.appendChild(savedStyle);
  }

  var savedButton = document.createElement('div');
  savedButton.className = 'marketplace-saved-fab';
  savedButton.innerHTML = '★';
  savedButton.title = 'View Saved Listings (Long press to drag)';
  savedButton.style.top = position.top + 'px';
  savedButton.style.left = position.left + 'px';

  var isDragging = false;
  var wasDragged = false;
  var startX = 0;
  var startY = 0;
  var currentX = position.left;
  var currentY = position.top;
  var longPressTimer = null;

  function savePosition() {
    localStorage.setItem(
      'marketplace_saved_button_pos',
      JSON.stringify({ top: currentY, left: currentX })
    );
  }

  savedButton.addEventListener('touchstart', function(e) {
    wasDragged = false;
    var touch = e.touches[0];
    startX = touch.clientX - currentX;
    startY = touch.clientY - currentY;
    longPressTimer = setTimeout(function() {
      isDragging = true;
      savedButton.classList.add('dragging');
    }, 500);
    e.preventDefault();
  });

  savedButton.addEventListener('touchmove', function(e) {
    if (isDragging) {
      wasDragged = true;
      var touch = e.touches[0];
      currentX = touch.clientX - startX;
      currentY = touch.clientY - startY;
      var maxX = window.innerWidth - 56;
      var maxY = window.innerHeight - 56;
      currentX = Math.max(0, Math.min(maxX, currentX));
      currentY = Math.max(0, Math.min(maxY, currentY));
      savedButton.style.left = currentX + 'px';
      savedButton.style.top = currentY + 'px';
      e.preventDefault();
    }
  });

  savedButton.addEventListener('touchend', function(e) {
    clearTimeout(longPressTimer);
    if (isDragging) {
      isDragging = false;
      savedButton.classList.remove('dragging');
      savePosition();
    } else if (!wasDragged) {
      window.location.href = 'https://www.facebook.com/marketplace/you/saved';
    }
    e.preventDefault();
  });

  savedButton.addEventListener('touchcancel', function() {
    clearTimeout(longPressTimer);
    isDragging = false;
    savedButton.classList.remove('dragging');
  });

  setTimeout(function() {
    if (!document.querySelector('.marketplace-saved-fab')) {
      document.body.appendChild(savedButton);
    }
  }, 2000);

  window._mpDebug && console.log('Saved listings access applied');
})();
