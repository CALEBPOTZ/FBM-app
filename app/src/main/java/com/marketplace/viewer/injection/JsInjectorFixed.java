package com.marketplace.viewer.injection;

import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

public final class JsInjectorFixed {
    private static final String TAG = "JsInjector";
    private final WebView webView;

    public JsInjectorFixed(WebView webView) {
        this.webView = webView;
    }

    public final void injectMarketplaceEnhancements() {
        Log.d(TAG, "Injecting marketplace enhancements");
        String script = buildEnhancementScript();
        executeJavaScript(script);
        injectSavedListingsAccess();
        injectImageEnhancements();
        injectSearchEnterKey();
        injectTextareaFix();
        injectScrollToTop();
        injectSideNavigation();
    }

    private String buildEnhancementScript() {
        return "(function() {" +
            "console.log('Marketplace enhancements loading...');" +
            "var style = document.createElement('style');" +
            "style.id = 'marketplace-enhancements';" +
            "style.textContent = `" +
            "[role=\"banner\"] { display: none !important; }" +
            "[data-pagelet=\"LeftRail\"], [aria-label=\"Navigation menu\"] { display: none !important; }" +
            "[data-pagelet=\"RightRail\"] { display: none !important; }" +
            "[role=\"contentinfo\"] { display: none !important; }" +
            "[role=\"main\"] { max-width: 100% !important; margin: 0 !important; padding: 8px !important; }" +
            "[data-testid*=\"app-banner\"], [data-testid*=\"download-banner\"] { display: none !important; }" +
            "[aria-label*=\"Create post\"], [aria-label*=\"Create a post\"], [aria-label*=\"Stories\"] { display: none !important; }" +
            "body { background-color: #f0f2f5 !important; }" +
            // Ensure scrolling works on all pages
            "html, body { " +
            "overflow-y: auto !important; " +
            "overflow-x: hidden !important; " +
            "height: auto !important; " +
            "min-height: 100% !important; " +
            "-webkit-overflow-scrolling: touch !important; " +
            "}" +
            // Fix for listing detail pages - ensure content is scrollable
            "[role=\"main\"], [data-pagelet=\"MarketplaceItemPage\"] { " +
            "overflow-y: visible !important; " +
            "overflow-x: hidden !important; " +
            "height: auto !important; " +
            "max-height: none !important; " +
            "}" +
            // Fix any fixed-height containers that might block scrolling
            "div[style*=\"overflow: hidden\"], div[style*=\"overflow:hidden\"] { " +
            "overflow-y: auto !important; " +
            "}" +
            "`;" +
            "if (!document.getElementById('marketplace-enhancements')) {" +
            "document.head.appendChild(style);" +
            "}" +
            "console.log('Marketplace enhancements applied');" +
            "})();";
    }

    public final void injectAntiDetection() {
        Log.d(TAG, "Injecting anti-detection script");
        executeJavaScript("(function() {" +
            "delete window.ReactNativeWebView;" +
            "delete window.__REACT_DEVTOOLS_GLOBAL_HOOK__;" +
            "console.log('Anti-detection applied');" +
            "})();");
    }

    private void executeJavaScript(String script) {
        this.webView.evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String result) {
                Log.d(TAG, "Script execution result: " + result);
            }
        });
    }

    public final void injectCustomScript(String script) {
        Log.d(TAG, "Injecting custom script");
        executeJavaScript(script);
    }

    // FIX 1 & 2: Enhanced image viewer with pinch-to-zoom
    private void injectImageEnhancements() {
        Log.d(TAG, "Injecting image enhancements");
        String script = "(function() {" +
            "try {" +
            "if (window._imageEnhancementsAdded) return;" +
            "window._imageEnhancementsAdded = true;" +
            "console.log('Image enhancements loading...');" +
            
            "var imageStyle = document.createElement('style');" +
            "imageStyle.id = 'marketplace-image-enhancements';" +
            "imageStyle.textContent = `" +
            ".marketplace-image-viewer {" +
            "position: fixed !important; top: 0 !important; left: 0 !important;" +
            "width: 100vw !important; height: 100vh !important;" +
            "background: rgba(0, 0, 0, 0.95) !important; z-index: 999999 !important;" +
            "display: flex !important; align-items: center !important; justify-content: center !important;" +
            "overflow: hidden !important; touch-action: none !important;" +
            "}" +
            ".marketplace-image-viewer img {" +
            "max-width: 95vw !important; max-height: 95vh !important;" +
            "object-fit: contain !important; touch-action: none !important;" +
            "}" +
            ".marketplace-image-viewer-close {" +
            "position: absolute !important; top: 20px !important; right: 20px !important;" +
            "width: 50px !important; height: 50px !important;" +
            "background: rgba(255, 255, 255, 0.3) !important; border-radius: 50% !important;" +
            "display: flex !important; align-items: center !important; justify-content: center !important;" +
            "cursor: pointer !important; font-size: 30px !important; color: white !important; z-index: 1000000 !important;" +
            "}" +
            ".marketplace-image-controls {" +
            "position: absolute !important; bottom: 30px !important; left: 50% !important;" +
            "transform: translateX(-50%) !important; color: white !important;" +
            "background: rgba(0,0,0,0.5) !important; padding: 8px 16px !important;" +
            "border-radius: 20px !important; font-size: 14px !important; z-index: 1000000 !important;" +
            "}" +
            ".marketplace-image-nav-button {" +
            "position: absolute !important; top: 50% !important; transform: translateY(-50%) !important;" +
            "width: 50px !important; height: 50px !important;" +
            "background: rgba(255, 255, 255, 0.3) !important; border-radius: 50% !important;" +
            "display: flex !important; align-items: center !important; justify-content: center !important;" +
            "cursor: pointer !important; font-size: 30px !important; color: white !important; z-index: 1000000 !important;" +
            "}" +
            ".marketplace-image-nav-left { left: 20px !important; }" +
            ".marketplace-image-nav-right { right: 20px !important; }" +
            "`;" +
            "if (!document.getElementById('marketplace-image-enhancements')) {" +
            "document.head.appendChild(imageStyle);" +
            "}" +
            
            // Store image viewer reference globally so we can close it on back button
            "window._marketplaceImageViewer = null;" +
            "window._marketplaceImages = [];" +
            "window._marketplaceImageIndex = 0;" +
            
            "function closeImageViewer() {" +
            "if (window._marketplaceImageViewer) {" +
            "window._marketplaceImageViewer.remove();" +
            "window._marketplaceImageViewer = null;" +
            "}" +
            "}" +
            
            // Expose close function globally for back button handling
            "window.closeMarketplaceImageViewer = closeImageViewer;" +
            
            "function showImageViewer(images, index) {" +
            "window._marketplaceImages = images;" +
            "window._marketplaceImageIndex = index;" +
            "closeImageViewer();" +
            
            "var viewer = document.createElement('div');" +
            "viewer.className = 'marketplace-image-viewer';" +
            "window._marketplaceImageViewer = viewer;" +
            
            "var img = document.createElement('img');" +
            "img.src = images[index];" +
            
            "var closeBtn = document.createElement('div');" +
            "closeBtn.className = 'marketplace-image-viewer-close';" +
            "closeBtn.innerHTML = '&times;';" +
            
            "var controls = document.createElement('div');" +
            "controls.className = 'marketplace-image-controls';" +
            "controls.textContent = (index + 1) + ' / ' + images.length;" +
            
            "viewer.appendChild(img);" +
            "viewer.appendChild(closeBtn);" +
            "viewer.appendChild(controls);" +
            
            // Add navigation buttons if multiple images
            "if (images.length > 1) {" +
            "var leftBtn = document.createElement('div');" +
            "leftBtn.className = 'marketplace-image-nav-button marketplace-image-nav-left';" +
            "leftBtn.innerHTML = '&#8249;';" +
            
            "var rightBtn = document.createElement('div');" +
            "rightBtn.className = 'marketplace-image-nav-button marketplace-image-nav-right';" +
            "rightBtn.innerHTML = '&#8250;';" +
            
            "leftBtn.addEventListener('click', function(e) { e.stopPropagation(); navigateImage(-1); });" +
            "rightBtn.addEventListener('click', function(e) { e.stopPropagation(); navigateImage(1); });" +
            
            "viewer.appendChild(leftBtn);" +
            "viewer.appendChild(rightBtn);" +
            "}" +
            
            // Close button click - only closes viewer, doesn't navigate away
            "closeBtn.addEventListener('click', function(e) {" +
            "e.preventDefault(); e.stopPropagation();" +
            "closeImageViewer();" +
            "});" +
            
            // Tap on background to close
            "viewer.addEventListener('click', function(e) {" +
            "if (e.target === viewer) {" +
            "e.preventDefault(); e.stopPropagation();" +
            "closeImageViewer();" +
            "}" +
            "});" +
            
            // Swipe detection for image navigation
            "var touchStartX = 0;" +
            "var touchStartY = 0;" +
            "var touchEndX = 0;" +
            
            "img.addEventListener('touchstart', function(e) {" +
            "touchStartX = e.touches[0].clientX;" +
            "touchStartY = e.touches[0].clientY;" +
            "}, { passive: true });" +
            
            "img.addEventListener('touchend', function(e) {" +
            "touchEndX = e.changedTouches[0].clientX;" +
            "var diffX = touchStartX - touchEndX;" +
            "var diffY = Math.abs(touchStartY - e.changedTouches[0].clientY);" +
            // Only navigate if horizontal swipe and not too much vertical movement
            "if (Math.abs(diffX) > 50 && diffY < 100 && images.length > 1) {" +
            "if (diffX > 0) { navigateImage(1); }" +
            "else { navigateImage(-1); }" +
            "}" +
            "}, { passive: true });" +
            
            "document.body.appendChild(viewer);" +
            "}" +
            
            "function navigateImage(dir) {" +
            "var images = window._marketplaceImages;" +
            "var index = window._marketplaceImageIndex;" +
            "index += dir;" +
            "if (index < 0) index = images.length - 1;" +
            "else if (index >= images.length) index = 0;" +
            "window._marketplaceImageIndex = index;" +
            
            "var viewer = window._marketplaceImageViewer;" +
            "if (viewer) {" +
            "var img = viewer.querySelector('img');" +
            "var controls = viewer.querySelector('.marketplace-image-controls');" +
            "if (img) img.src = images[index];" +
            "if (controls) controls.textContent = (index + 1) + ' / ' + images.length;" +
            "}" +
            "}" +
            
            "function findListingImages(clickedImg) {" +
            "var images = [];" +
            "var seen = {};" +
            "console.log('Finding listing images...');" +
            
            // Get the clicked image src
            "var clickedSrc = clickedImg.src;" +
            "var clickedWidth = clickedImg.offsetWidth || clickedImg.naturalWidth || 0;" +
            "var clickedHeight = clickedImg.offsetHeight || clickedImg.naturalHeight || 0;" +
            "console.log('Clicked image size:', clickedWidth, 'x', clickedHeight);" +
            
            // Strategy: Find the carousel/gallery container that holds the listing images
            // Facebook uses a horizontal scrollable div or a swipeable container for product images
            
            // First, find the closest scrollable/carousel parent
            "var carouselContainer = null;" +
            "var parent = clickedImg.parentElement;" +
            "for (var i = 0; i < 15 && parent; i++) {" +
            "var style = window.getComputedStyle(parent);" +
            // Check for scrollable container (horizontal scroll for image carousels)
            "var hasHScroll = parent.scrollWidth > parent.clientWidth + 10;" +
            "var isFlexRow = style.display === 'flex' && (style.flexDirection === 'row' || style.flexDirection === '');" +
            "var hasSnapScroll = style.scrollSnapType && style.scrollSnapType !== 'none';" +
            // Check for navigation arrows nearby (common in carousels)
            "var hasNavArrows = parent.querySelector('[aria-label*=\"Next\"]') || parent.querySelector('[aria-label*=\"Previous\"]') || " +
            "parent.querySelector('[aria-label*=\"next\"]') || parent.querySelector('[aria-label*=\"previous\"]');" +
            
            "if (hasHScroll || hasSnapScroll || hasNavArrows || (isFlexRow && parent.children.length > 1)) {" +
            "carouselContainer = parent;" +
            "console.log('Found potential carousel at level', i);" +
            "break;" +
            "}" +
            "parent = parent.parentElement;" +
            "}" +
            
            // If we found a carousel, get images from it
            "if (carouselContainer) {" +
            "var carouselImgs = carouselContainer.querySelectorAll('img');" +
            "console.log('Carousel has', carouselImgs.length, 'images');" +
            "for (var j = 0; j < carouselImgs.length; j++) {" +
            "var img = carouselImgs[j];" +
            "var src = img.src;" +
            "if (!src || !src.includes('scontent')) continue;" +
            "if (seen[src]) continue;" +
            
            // Size check - product images are typically large
            "var w = img.offsetWidth || img.naturalWidth || 0;" +
            "var h = img.offsetHeight || img.naturalHeight || 0;" +
            
            // Skip tiny images (icons, dots, etc)
            "if (w < 80 || h < 80) continue;" +
            
            // Skip images with very different aspect ratios from clicked image (likely ads)
            "var clickedRatio = clickedWidth / (clickedHeight || 1);" +
            "var imgRatio = w / (h || 1);" +
            "var ratioDiff = Math.abs(clickedRatio - imgRatio);" +
            // If clicked image is reasonably sized, check aspect ratio similarity
            "if (clickedWidth > 100 && ratioDiff > 1.5) {" +
            "console.log('Skipping image with different ratio:', ratioDiff);" +
            "continue;" +
            "}" +
            
            "images.push(src);" +
            "seen[src] = true;" +
            "}" +
            "}" +
            
            // Fallback: if carousel search didn't find enough, use the close-parent approach
            "if (images.length <= 1) {" +
            "console.log('Fallback: searching nearby parents...');" +
            "images = [];" +
            "seen = {};" +
            
            // Always include clicked image first
            "if (clickedSrc && clickedSrc.includes('scontent')) {" +
            "images.push(clickedSrc);" +
            "seen[clickedSrc] = true;" +
            "}" +
            
            // Search parent containers but be more selective
            "var container = clickedImg.parentElement;" +
            "for (var i = 0; i < 6 && container; i++) {" +
            "var imgs = container.querySelectorAll('img');" +
            "for (var j = 0; j < imgs.length; j++) {" +
            "var img = imgs[j];" +
            "var src = img.src;" +
            "if (!src || !src.includes('scontent') || seen[src]) continue;" +
            
            "var w = img.offsetWidth || img.naturalWidth || 0;" +
            "var h = img.offsetHeight || img.naturalHeight || 0;" +
            
            // Must be reasonably large (not icons/thumbnails)
            "if (w < 100 || h < 100) continue;" +
            
            // Check that it's similar size to clicked image (within 50%)
            "var sizeDiffW = Math.abs(w - clickedWidth) / (clickedWidth || 1);" +
            "var sizeDiffH = Math.abs(h - clickedHeight) / (clickedHeight || 1);" +
            "if (clickedWidth > 100 && (sizeDiffW > 0.5 || sizeDiffH > 0.5)) continue;" +
            
            "images.push(src);" +
            "seen[src] = true;" +
            "}" +
            "container = container.parentElement;" +
            // Stop if we found a reasonable number of images
            "if (images.length >= 3) break;" +
            "}" +
            "}" +
            
            // Make sure clicked image is first
            "var clickedIndex = images.indexOf(clickedSrc);" +
            "if (clickedIndex > 0) {" +
            "images.splice(clickedIndex, 1);" +
            "images.unshift(clickedSrc);" +
            "} else if (clickedIndex === -1 && clickedSrc) {" +
            "images.unshift(clickedSrc);" +
            "}" +
            
            "console.log('Found', images.length, 'listing images');" +
            "return images;" +
            "}" +
            
            // Click handler for images
            "document.addEventListener('click', function(e) {" +
            "var img = e.target;" +
            "if (img.tagName !== 'IMG') img = e.target.closest('img');" +
            "if (!img || !img.src || !img.src.includes('scontent')) return;" +
            
            "var url = window.location.href;" +
            "if (url.includes('/marketplace/item/') || url.includes('/product/')) {" +
            "e.preventDefault();" +
            "e.stopPropagation();" +
            "var images = findListingImages(img);" +
            "var index = images.indexOf(img.src);" +
            "if (index === -1) index = 0;" +
            "showImageViewer(images, index);" +
            "}" +
            "}, true);" +
            
            "console.log('Image enhancements applied');" +
            "} catch(e) { console.log('Image enhancement error:', e); }" +
            "})();";
        
        executeJavaScript(script);
    }

    // FIX 1: Draggable saved listings button
    private void injectSavedListingsAccess() {
        Log.d(TAG, "Injecting saved listings access");
        String script = "(function() {" +
            "if (window._savedButtonAdded) return;" +
            "window._savedButtonAdded = true;" +
            "console.log('Saved listings access loading...');" +
            
            "var savedPos = localStorage.getItem('marketplace_saved_button_pos');" +
            "var position = savedPos ? JSON.parse(savedPos) : { top: 70, left: 10 };" +
            
            "var savedStyle = document.createElement('style');" +
            "savedStyle.id = 'marketplace-saved-button';" +
            "savedStyle.textContent = `" +
            ".marketplace-saved-fab {" +
            "position: fixed !important; width: 56px !important; height: 56px !important;" +
            "border-radius: 50% !important; background: #1877f2 !important; color: white !important;" +
            "display: flex !important; align-items: center !important; justify-content: center !important;" +
            "box-shadow: 0 4px 8px rgba(0,0,0,0.3) !important; cursor: move !important; z-index: 9999 !important;" +
            "font-size: 24px !important; transition: box-shadow 0.2s ease !important;" +
            "user-select: none !important; touch-action: none !important;" +
            "}" +
            ".marketplace-saved-fab.dragging { box-shadow: 0 8px 16px rgba(0,0,0,0.5) !important; opacity: 0.8 !important; }" +
            "`;" +
            "if (!document.getElementById('marketplace-saved-button')) { document.head.appendChild(savedStyle); }" +
            
            "var savedButton = document.createElement('div');" +
            "savedButton.className = 'marketplace-saved-fab';" +
            "savedButton.innerHTML = '★';" +
            "savedButton.title = 'View Saved Listings (Long press to drag)';" +
            "savedButton.style.top = position.top + 'px';" +
            "savedButton.style.left = position.left + 'px';" +
            
            "var isDragging = false; var wasDragged = false;" +
            "var startX = 0; var startY = 0;" +
            "var currentX = position.left; var currentY = position.top;" +
            "var longPressTimer = null;" +
            
            "function savePosition() { localStorage.setItem('marketplace_saved_button_pos', JSON.stringify({ top: currentY, left: currentX })); }" +
            
            "savedButton.addEventListener('touchstart', function(e) {" +
            "wasDragged = false;" +
            "var touch = e.touches[0];" +
            "startX = touch.clientX - currentX;" +
            "startY = touch.clientY - currentY;" +
            "longPressTimer = setTimeout(function() { isDragging = true; savedButton.classList.add('dragging'); }, 500);" +
            "e.preventDefault();" +
            "});" +
            
            "savedButton.addEventListener('touchmove', function(e) {" +
            "if (isDragging) {" +
            "wasDragged = true; var touch = e.touches[0];" +
            "currentX = touch.clientX - startX; currentY = touch.clientY - startY;" +
            "var maxX = window.innerWidth - 56; var maxY = window.innerHeight - 56;" +
            "currentX = Math.max(0, Math.min(maxX, currentX));" +
            "currentY = Math.max(0, Math.min(maxY, currentY));" +
            "savedButton.style.left = currentX + 'px';" +
            "savedButton.style.top = currentY + 'px';" +
            "e.preventDefault();" +
            "}" +
            "});" +
            
            "savedButton.addEventListener('touchend', function(e) {" +
            "clearTimeout(longPressTimer);" +
            "if (isDragging) { isDragging = false; savedButton.classList.remove('dragging'); savePosition(); }" +
            "else if (!wasDragged) { window.location.href = 'https://www.facebook.com/marketplace/you/saved'; }" +
            "e.preventDefault();" +
            "});" +
            
            "savedButton.addEventListener('touchcancel', function() { clearTimeout(longPressTimer); isDragging = false; savedButton.classList.remove('dragging'); });" +
            
            "setTimeout(function() {" +
            "if (!document.querySelector('.marketplace-saved-fab')) { document.body.appendChild(savedButton); }" +
            "}, 2000);" +
            
            "console.log('Saved listings access applied');" +
            "})();";
        
        executeJavaScript(script);
    }

    // FIX 3: Search enter key support
    private void injectSearchEnterKey() {
        Log.d(TAG, "Injecting search enter key support");
        String script = "(function() {" +
            "if (window._searchEnterAdded) return;" +
            "window._searchEnterAdded = true;" +
            "console.log('Search enter key support loading...');" +
            
            "document.addEventListener('keypress', function(e) {" +
            "if (e.key === 'Enter' || e.keyCode === 13) {" +
            "var target = e.target;" +
            "if (target.tagName === 'INPUT' && target.getAttribute('placeholder') && target.getAttribute('placeholder').toLowerCase().includes('search')) {" +
            "e.preventDefault();" +
            "var searchValue = target.value.trim();" +
            "if (searchValue) {" +
            "window.location.href = 'https://www.facebook.com/marketplace/search?query=' + encodeURIComponent(searchValue);" +
            "}" +
            "}" +
            "}" +
            "});" +
            
            "console.log('Search enter key support applied');" +
            "})();";
        
        executeJavaScript(script);
    }

    // FIX 4: Textarea overflow fix for create listing
    private void injectTextareaFix() {
        Log.d(TAG, "Injecting textarea fix");
        String script = "(function() {" +
            "if (window._textareaFixAdded) return;" +
            "window._textareaFixAdded = true;" +
            "console.log('Textarea fix loading...');" +
            
            "var textareaStyle = document.createElement('style');" +
            "textareaStyle.id = 'marketplace-textarea-fix';" +
            "textareaStyle.textContent = `" +
            "textarea, [contenteditable=\"true\"] {" +
            "overflow-y: auto !important;" +
            "word-wrap: break-word !important;" +
            "white-space: pre-wrap !important;" +
            "min-height: 60px !important;" +
            "max-height: 300px !important;" +
            "resize: vertical !important;" +
            "}" +
            "[role=\"dialog\"] textarea, [role=\"dialog\"] [contenteditable=\"true\"] {" +
            "box-sizing: border-box !important;" +
            "width: 100% !important;" +
            "}" +
            "[role=\"dialog\"] form > div, [role=\"dialog\"] [data-pagelet] > div {" +
            "overflow: visible !important;" +
            "display: flex !important;" +
            "flex-direction: column !important;" +
            "}" +
            "`;" +
            "if (!document.getElementById('marketplace-textarea-fix')) { document.head.appendChild(textareaStyle); }" +
            
            "function autoResizeTextarea(textarea) {" +
            "textarea.style.height = 'auto';" +
            "textarea.style.height = Math.min(textarea.scrollHeight, 300) + 'px';" +
            "}" +
            
            "var observer = new MutationObserver(function(mutations) {" +
            "document.querySelectorAll('textarea').forEach(function(textarea) {" +
            "if (!textarea._autoResizeAdded) {" +
            "textarea._autoResizeAdded = true;" +
            "textarea.addEventListener('input', function() { autoResizeTextarea(textarea); });" +
            "autoResizeTextarea(textarea);" +
            "}" +
            "});" +
            "});" +
            
            "observer.observe(document.body, { childList: true, subtree: true });" +
            
            "document.querySelectorAll('textarea').forEach(function(textarea) {" +
            "if (!textarea._autoResizeAdded) {" +
            "textarea._autoResizeAdded = true;" +
            "textarea.addEventListener('input', function() { autoResizeTextarea(textarea); });" +
            "autoResizeTextarea(textarea);" +
            "}" +
            "});" +
            
            "console.log('Textarea fix applied');" +
            "})();";
        
        executeJavaScript(script);
    }

    // Scroll to top button that appears when user scrolls down
    private void injectScrollToTop() {
        Log.d(TAG, "Injecting scroll to top button");
        String script = "(function() {" +
            "try {" +
            "if (window._scrollToTopAdded) return;" +
            "window._scrollToTopAdded = true;" +
            "console.log('Scroll to top button loading...');" +
            
            "var scrollStyle = document.createElement('style');" +
            "scrollStyle.id = 'marketplace-scroll-to-top';" +
            "scrollStyle.textContent = `" +
            ".marketplace-scroll-top {" +
            "position: fixed !important; bottom: 80px !important; right: 20px !important;" +
            "width: 50px !important; height: 50px !important;" +
            "border-radius: 50% !important; background: #1877f2 !important; color: white !important;" +
            "display: flex !important; align-items: center !important; justify-content: center !important;" +
            "box-shadow: 0 4px 12px rgba(0,0,0,0.3) !important; cursor: pointer !important; z-index: 9998 !important;" +
            "font-size: 24px !important; opacity: 0 !important; visibility: hidden !important;" +
            "transition: opacity 0.3s ease, visibility 0.3s ease, transform 0.2s ease !important;" +
            "user-select: none !important; touch-action: manipulation !important;" +
            "}" +
            ".marketplace-scroll-top.visible { opacity: 1 !important; visibility: visible !important; }" +
            ".marketplace-scroll-top:active { transform: scale(0.9) !important; }" +
            "`;" +
            "if (!document.getElementById('marketplace-scroll-to-top')) { document.head.appendChild(scrollStyle); }" +
            
            "var scrollBtn = document.createElement('div');" +
            "scrollBtn.className = 'marketplace-scroll-top';" +
            "scrollBtn.innerHTML = '&#8679;';" +
            "scrollBtn.title = 'Scroll to top';" +
            
            "var scrollThreshold = 500;" +
            
            "function checkScroll() {" +
            "var scrollY = window.scrollY || window.pageYOffset || document.documentElement.scrollTop;" +
            "if (scrollY > scrollThreshold) {" +
            "scrollBtn.classList.add('visible');" +
            "} else {" +
            "scrollBtn.classList.remove('visible');" +
            "}" +
            "}" +
            
            "scrollBtn.addEventListener('click', function(e) {" +
            "e.preventDefault(); e.stopPropagation();" +
            "window.scrollTo({ top: 0, behavior: 'smooth' });" +
            "});" +
            
            "scrollBtn.addEventListener('touchend', function(e) {" +
            "e.preventDefault(); e.stopPropagation();" +
            "window.scrollTo({ top: 0, behavior: 'smooth' });" +
            "});" +
            
            "window.addEventListener('scroll', checkScroll, { passive: true });" +
            
            "setTimeout(function() {" +
            "if (!document.querySelector('.marketplace-scroll-top')) { document.body.appendChild(scrollBtn); }" +
            "checkScroll();" +
            "}, 1500);" +
            
            "console.log('Scroll to top button applied');" +
            "} catch(e) { console.log('Scroll to top error:', e); }" +
            "})();";
        
        executeJavaScript(script);
    }

    // Side navigation drawer with swipe gesture
    private void injectSideNavigation() {
        Log.d(TAG, "Injecting side navigation");
        String script = "(function() {" +
            "try {" +
            "if (window._sideNavAdded) return;" +
            "window._sideNavAdded = true;" +
            "console.log('Side navigation loading...');" +
            
            "var navStyle = document.createElement('style');" +
            "navStyle.id = 'marketplace-side-nav';" +
            "navStyle.textContent = `" +
            ".marketplace-nav-overlay {" +
            "position: fixed !important; top: 0 !important; left: 0 !important;" +
            "width: 100vw !important; height: 100vh !important;" +
            "background: rgba(0, 0, 0, 0.5) !important; z-index: 99998 !important;" +
            "opacity: 0 !important; visibility: hidden !important;" +
            "transition: opacity 0.3s ease, visibility 0.3s ease !important;" +
            "}" +
            ".marketplace-nav-overlay.visible { opacity: 1 !important; visibility: visible !important; }" +
            
            ".marketplace-side-nav {" +
            "position: fixed !important; top: 0 !important; left: 0 !important;" +
            "width: 280px !important; height: 100vh !important;" +
            "background: #ffffff !important; z-index: 99999 !important;" +
            "box-shadow: 2px 0 12px rgba(0,0,0,0.3) !important;" +
            "transform: translateX(-100%) !important;" +
            "transition: transform 0.3s ease !important;" +
            "display: flex !important; flex-direction: column !important;" +
            "overflow: hidden !important;" +
            "}" +
            ".marketplace-side-nav.open { transform: translateX(0) !important; }" +
            
            ".marketplace-nav-header {" +
            "background: #1877f2 !important; color: white !important;" +
            "padding: 20px 16px !important; font-size: 20px !important; font-weight: bold !important;" +
            "display: flex !important; align-items: center !important; justify-content: space-between !important;" +
            "}" +
            ".marketplace-nav-close {" +
            "width: 36px !important; height: 36px !important; border-radius: 50% !important;" +
            "background: rgba(255,255,255,0.2) !important; color: white !important;" +
            "display: flex !important; align-items: center !important; justify-content: center !important;" +
            "font-size: 24px !important; cursor: pointer !important;" +
            "}" +
            
            ".marketplace-nav-search {" +
            "padding: 16px !important; border-bottom: 1px solid #e4e6eb !important;" +
            "}" +
            ".marketplace-nav-search input {" +
            "width: 100% !important; padding: 12px 16px !important;" +
            "border: 1px solid #dddfe2 !important; border-radius: 24px !important;" +
            "font-size: 16px !important; outline: none !important;" +
            "box-sizing: border-box !important;" +
            "}" +
            ".marketplace-nav-search input:focus { border-color: #1877f2 !important; }" +
            
            ".marketplace-nav-items {" +
            "flex: 1 !important; overflow-y: auto !important; padding: 8px 0 !important;" +
            "}" +
            ".marketplace-nav-item {" +
            "display: flex !important; align-items: center !important; padding: 14px 16px !important;" +
            "cursor: pointer !important; transition: background 0.2s ease !important;" +
            "font-size: 16px !important; color: #1c1e21 !important;" +
            "}" +
            ".marketplace-nav-item:active { background: #f0f2f5 !important; }" +
            ".marketplace-nav-item-icon {" +
            "width: 36px !important; height: 36px !important; border-radius: 50% !important;" +
            "background: #e4e6eb !important; display: flex !important; align-items: center !important;" +
            "justify-content: center !important; margin-right: 12px !important; font-size: 18px !important;" +
            "}" +
            ".marketplace-nav-divider { height: 1px !important; background: #e4e6eb !important; margin: 8px 16px !important; }" +
            
            ".marketplace-nav-footer {" +
            "padding: 16px !important; border-top: 1px solid #e4e6eb !important;" +
            "background: #f7f8fa !important;" +
            "}" +
            ".marketplace-nav-version {" +
            "font-size: 12px !important; color: #65676b !important; text-align: center !important;" +
            "margin-bottom: 8px !important;" +
            "}" +
            
            ".marketplace-swipe-indicator {" +
            "position: fixed !important; top: 50% !important; left: 0 !important;" +
            "width: 4px !important; height: 60px !important; transform: translateY(-50%) !important;" +
            "background: #1877f2 !important; border-radius: 0 4px 4px 0 !important;" +
            "opacity: 0.5 !important; z-index: 9997 !important;" +
            "transition: width 0.2s ease, opacity 0.2s ease !important;" +
            "}" +
            ".marketplace-swipe-indicator:active { width: 8px !important; opacity: 0.8 !important; }" +
            "`;" +
            "if (!document.getElementById('marketplace-side-nav')) { document.head.appendChild(navStyle); }" +
            
            // Create overlay
            "var overlay = document.createElement('div');" +
            "overlay.className = 'marketplace-nav-overlay';" +
            
            // Create side nav
            "var sideNav = document.createElement('div');" +
            "sideNav.className = 'marketplace-side-nav';" +
            
            // Header
            "var header = document.createElement('div');" +
            "header.className = 'marketplace-nav-header';" +
            "header.innerHTML = '<span>Marketplace</span><div class=\"marketplace-nav-close\">&times;</div>';" +
            
            // Search section
            "var searchSection = document.createElement('div');" +
            "searchSection.className = 'marketplace-nav-search';" +
            "var searchInput = document.createElement('input');" +
            "searchInput.type = 'text';" +
            "searchInput.placeholder = 'Search Marketplace';" +
            "searchSection.appendChild(searchInput);" +
            
            // Nav items container
            "var navItems = document.createElement('div');" +
            "navItems.className = 'marketplace-nav-items';" +
            
            // Create nav items
            "var items = [" +
            "{ icon: '&#9733;', label: 'Saved Listings', url: 'https://www.facebook.com/marketplace/you/saved', action: null }," +
            "{ icon: '&#9776;', label: 'My Listings', url: 'https://www.facebook.com/marketplace/you/selling', action: null }," +
            "{ icon: '&#128269;', label: 'Saved Searches', url: 'https://www.facebook.com/marketplace/you/saved_searches', action: null }," +
            "{ icon: '&#8635;', label: 'Check for Updates', url: null, action: 'checkUpdates' }" +
            "];" +
            
            "items.forEach(function(item) {" +
            "var navItem = document.createElement('div');" +
            "navItem.className = 'marketplace-nav-item';" +
            "navItem.innerHTML = '<div class=\"marketplace-nav-item-icon\">' + item.icon + '</div><span>' + item.label + '</span>';" +
            "navItem.addEventListener('click', function() {" +
            "closeNav();" +
            "if (item.action === 'checkUpdates') {" +
            "if (typeof MarketplaceApp !== 'undefined' && MarketplaceApp.checkForUpdates) {" +
            "MarketplaceApp.checkForUpdates();" +
            "} else { console.log('MarketplaceApp interface not available'); }" +
            "} else if (item.url) { window.location.href = item.url; }" +
            "});" +
            "navItem.addEventListener('touchend', function(e) {" +
            "e.preventDefault(); closeNav();" +
            "if (item.action === 'checkUpdates') {" +
            "if (typeof MarketplaceApp !== 'undefined' && MarketplaceApp.checkForUpdates) {" +
            "MarketplaceApp.checkForUpdates();" +
            "} else { console.log('MarketplaceApp interface not available'); }" +
            "} else if (item.url) { window.location.href = item.url; }" +
            "});" +
            "navItems.appendChild(navItem);" +
            "});" +
            
            // Create footer with version info
            "var footer = document.createElement('div');" +
            "footer.className = 'marketplace-nav-footer';" +
            "var versionText = 'Version: ';" +
            "if (typeof MarketplaceApp !== 'undefined' && MarketplaceApp.getAppVersion) {" +
            "versionText += MarketplaceApp.getAppVersion();" +
            "} else { versionText += 'Unknown'; }" +
            "footer.innerHTML = '<div class=\"marketplace-nav-version\">' + versionText + '</div>';" +
            
            // Assemble side nav
            "sideNav.appendChild(header);" +
            "sideNav.appendChild(searchSection);" +
            "sideNav.appendChild(navItems);" +
            "sideNav.appendChild(footer);" +
            
            // Create swipe indicator on left edge
            "var swipeIndicator = document.createElement('div');" +
            "swipeIndicator.className = 'marketplace-swipe-indicator';" +
            
            // Functions to open/close nav
            "function openNav() { sideNav.classList.add('open'); overlay.classList.add('visible'); }" +
            "function closeNav() { sideNav.classList.remove('open'); overlay.classList.remove('visible'); }" +
            
            // Close button handler
            "header.querySelector('.marketplace-nav-close').addEventListener('click', closeNav);" +
            "header.querySelector('.marketplace-nav-close').addEventListener('touchend', function(e) { e.preventDefault(); closeNav(); });" +
            
            // Overlay click to close
            "overlay.addEventListener('click', closeNav);" +
            "overlay.addEventListener('touchend', function(e) { e.preventDefault(); closeNav(); });" +
            
            // Search input handler
            "searchInput.addEventListener('keypress', function(e) {" +
            "if (e.key === 'Enter' || e.keyCode === 13) {" +
            "var query = searchInput.value.trim();" +
            "if (query) { closeNav(); window.location.href = 'https://www.facebook.com/marketplace/search?query=' + encodeURIComponent(query); }" +
            "}" +
            "});" +
            
            // Swipe detection variables
            "var touchStartX = 0;" +
            "var touchStartY = 0;" +
            "var touchMoveX = 0;" +
            "var isSwipingNav = false;" +
            "var swipeThreshold = 50;" +
            "var edgeWidth = 30;" +
            
            // Swipe from left edge to open
            "document.addEventListener('touchstart', function(e) {" +
            "if (sideNav.classList.contains('open')) return;" +
            "var touch = e.touches[0];" +
            "if (touch.clientX <= edgeWidth) {" +
            "touchStartX = touch.clientX;" +
            "touchStartY = touch.clientY;" +
            "isSwipingNav = true;" +
            "}" +
            "}, { passive: true });" +
            
            "document.addEventListener('touchmove', function(e) {" +
            "if (!isSwipingNav) return;" +
            "var touch = e.touches[0];" +
            "touchMoveX = touch.clientX - touchStartX;" +
            "var touchMoveY = Math.abs(touch.clientY - touchStartY);" +
            // Only track horizontal swipes
            "if (touchMoveY > Math.abs(touchMoveX)) { isSwipingNav = false; return; }" +
            "if (touchMoveX > 0) {" +
            "var progress = Math.min(touchMoveX / 200, 1);" +
            "sideNav.style.transform = 'translateX(' + (-100 + (progress * 100)) + '%)';" +
            "sideNav.style.transition = 'none';" +
            "overlay.style.opacity = progress;" +
            "overlay.style.visibility = 'visible';" +
            "overlay.style.transition = 'none';" +
            "}" +
            "}, { passive: true });" +
            
            "document.addEventListener('touchend', function(e) {" +
            "if (!isSwipingNav) return;" +
            "isSwipingNav = false;" +
            "sideNav.style.transition = '';" +
            "sideNav.style.transform = '';" +
            "overlay.style.transition = '';" +
            "overlay.style.opacity = '';" +
            "if (touchMoveX > swipeThreshold) { openNav(); } else { closeNav(); }" +
            "touchMoveX = 0;" +
            "}, { passive: true });" +
            
            // Swipe indicator tap to open
            "swipeIndicator.addEventListener('click', openNav);" +
            "swipeIndicator.addEventListener('touchend', function(e) { e.preventDefault(); openNav(); });" +
            
            // Swipe on nav to close
            "var navTouchStartX = 0;" +
            "sideNav.addEventListener('touchstart', function(e) {" +
            "navTouchStartX = e.touches[0].clientX;" +
            "}, { passive: true });" +
            
            "sideNav.addEventListener('touchend', function(e) {" +
            "var navTouchEndX = e.changedTouches[0].clientX;" +
            "if (navTouchStartX - navTouchEndX > 50) { closeNav(); }" +
            "}, { passive: true });" +
            
            // Append elements to DOM
            "setTimeout(function() {" +
            "if (!document.querySelector('.marketplace-side-nav')) {" +
            "document.body.appendChild(overlay);" +
            "document.body.appendChild(sideNav);" +
            "document.body.appendChild(swipeIndicator);" +
            "}" +
            "}, 1500);" +
            
            "console.log('Side navigation applied');" +
            "} catch(e) { console.log('Side navigation error:', e); }" +
            "})();";
        
        executeJavaScript(script);
    }
}
