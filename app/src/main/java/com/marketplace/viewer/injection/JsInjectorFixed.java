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
            "overflow: hidden !important;" +
            "}" +
            ".marketplace-image-viewer img {" +
            "max-width: 95vw !important; max-height: 95vh !important;" +
            "object-fit: contain !important; transition: transform 0.2s ease !important;" +
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
            ".marketplace-nav-button {" +
            "position: absolute !important; top: 50% !important; transform: translateY(-50%) !important;" +
            "width: 50px !important; height: 50px !important;" +
            "background: rgba(255, 255, 255, 0.3) !important; border-radius: 50% !important;" +
            "display: flex !important; align-items: center !important; justify-content: center !important;" +
            "cursor: pointer !important; font-size: 30px !important; color: white !important; z-index: 1000000 !important;" +
            "}" +
            ".marketplace-nav-left { left: 20px !important; }" +
            ".marketplace-nav-right { right: 20px !important; }" +
            "`;" +
            "if (!document.getElementById('marketplace-image-enhancements')) {" +
            "document.head.appendChild(imageStyle);" +
            "}" +
            
            "var currentImages = []; var currentIndex = 0; var imageViewer = null;" +
            "var scale = 1; var translateX = 0; var translateY = 0;" +
            "var lastDistance = 0; var isDragging = false; var startX = 0; var startY = 0;" +
            
            "function showImageViewer(images, index) {" +
            "currentImages = images; currentIndex = index; scale = 1; translateX = 0; translateY = 0;" +
            "if (imageViewer) imageViewer.remove();" +
            
            "imageViewer = document.createElement('div');" +
            "imageViewer.className = 'marketplace-image-viewer';" +
            "var img = document.createElement('img');" +
            "img.src = images[index]; img.style.transformOrigin = 'center center';" +
            
            "var closeBtn = document.createElement('div');" +
            "closeBtn.className = 'marketplace-image-viewer-close'; closeBtn.textContent = '×';" +
            "closeBtn.ontouchstart = function(e) { e.stopPropagation(); imageViewer.remove(); imageViewer = null; };" +
            "closeBtn.onclick = function() { imageViewer.remove(); imageViewer = null; };" +
            
            "var controls = document.createElement('div');" +
            "controls.className = 'marketplace-image-controls';" +
            "controls.textContent = (index + 1) + ' / ' + images.length;" +
            
            "imageViewer.appendChild(img); imageViewer.appendChild(closeBtn); imageViewer.appendChild(controls);" +
            
            "if (images.length > 1) {" +
            "var leftBtn = document.createElement('div');" +
            "leftBtn.className = 'marketplace-nav-button marketplace-nav-left'; leftBtn.textContent = '‹';" +
            "leftBtn.ontouchstart = function(e) { e.stopPropagation(); navigateImage(-1); };" +
            "leftBtn.onclick = function() { navigateImage(-1); };" +
            "var rightBtn = document.createElement('div');" +
            "rightBtn.className = 'marketplace-nav-button marketplace-nav-right'; rightBtn.textContent = '›';" +
            "rightBtn.ontouchstart = function(e) { e.stopPropagation(); navigateImage(1); };" +
            "rightBtn.onclick = function() { navigateImage(1); };" +
            "imageViewer.appendChild(leftBtn); imageViewer.appendChild(rightBtn);" +
            "}" +
            
            "img.addEventListener('touchstart', handleTouchStart);" +
            "img.addEventListener('touchmove', handleTouchMove);" +
            "img.addEventListener('touchend', handleTouchEnd);" +
            "document.body.appendChild(imageViewer);" +
            "}" +
            
            "function updateTransform() {" +
            "var img = imageViewer.querySelector('img');" +
            "img.style.transform = 'translate(' + translateX + 'px, ' + translateY + 'px) scale(' + scale + ')';" +
            "}" +
            
            "function handleTouchStart(e) {" +
            "e.preventDefault();" +
            "if (e.touches.length === 2) { lastDistance = getDistance(e.touches); }" +
            "else if (e.touches.length === 1) { isDragging = true; startX = e.touches[0].clientX - translateX; startY = e.touches[0].clientY - translateY; }" +
            "}" +
            
            "function handleTouchMove(e) {" +
            "e.preventDefault();" +
            "if (e.touches.length === 2) {" +
            "var distance = getDistance(e.touches);" +
            "if (lastDistance > 0) { scale *= distance / lastDistance; scale = Math.max(1, Math.min(scale, 5)); updateTransform(); }" +
            "lastDistance = distance;" +
            "} else if (e.touches.length === 1 && isDragging && scale > 1) {" +
            "translateX = e.touches[0].clientX - startX; translateY = e.touches[0].clientY - startY; updateTransform();" +
            "}" +
            "}" +
            
            "function handleTouchEnd(e) {" +
            "if (e.touches.length === 0) {" +
            "isDragging = false; lastDistance = 0;" +
            "if (scale === 1) { translateX = 0; translateY = 0; updateTransform(); }" +
            "}" +
            "}" +
            
            "function getDistance(touches) { var dx = touches[0].clientX - touches[1].clientX; var dy = touches[0].clientY - touches[1].clientY; return Math.sqrt(dx * dx + dy * dy); }" +
            
            "function navigateImage(dir) {" +
            "currentIndex += dir; if (currentIndex < 0) currentIndex = currentImages.length - 1;" +
            "else if (currentIndex >= currentImages.length) currentIndex = 0;" +
            "scale = 1; translateX = 0; translateY = 0;" +
            "var img = imageViewer.querySelector('img'); img.src = currentImages[currentIndex];" +
            "var controls = imageViewer.querySelector('.marketplace-image-controls');" +
            "controls.textContent = (currentIndex + 1) + ' / ' + currentImages.length;" +
            "updateTransform();" +
            "}" +
            
            "function findListingImages(clickedImg) {" +
            "var images = [];" +
            "console.log('Finding listing images...');" +
            
            // Method 1: Look for all scontent images in the main listing area
            "var mainContent = document.querySelector('[role=\"main\"]') || document.body;" +
            "var allImgs = mainContent.querySelectorAll('img[src*=\"scontent\"]');" +
            "console.log('Found ' + allImgs.length + ' scontent images');" +
            
            // Filter to likely listing images (larger ones, not profile pics/icons)
            "for (var i = 0; i < allImgs.length; i++) {" +
            "var img = allImgs[i];" +
            "var dominated = img.closest('[data-pagelet*=\"FeedUnit\"], [data-pagelet*=\"Related\"]');" +
            "if (dominated) continue;" + // Skip images in related listings
            "var rect = img.getBoundingClientRect();" +
            "if (rect.width >= 100 || img.naturalWidth >= 100) {" +
            "var src = img.src;" +
            // Get highest resolution version
            "src = src.replace(/\\/[sp]\\d+x\\d+\\//g, '/').replace(/\\?.*$/, '');" +
            "if (images.indexOf(src) === -1) {" +
            "images.push(src);" +
            "console.log('Added image: ' + src.substring(0, 60) + '...');" +
            "}" +
            "}" +
            "}" +
            
            // Method 2: Look for image URLs in data attributes and background images
            "if (images.length < 2) {" +
            "var elements = mainContent.querySelectorAll('[style*=\"background-image\"], [data-src*=\"scontent\"]');" +
            "for (var j = 0; j < elements.length; j++) {" +
            "var el = elements[j];" +
            "var bgImg = el.style.backgroundImage;" +
            "if (bgImg && bgImg.includes('scontent')) {" +
            "var match = bgImg.match(/url\\([\"']?([^\"')]+)[\"']?\\)/);" +
            "if (match && match[1]) {" +
            "var src = match[1].replace(/\\/[sp]\\d+x\\d+\\//g, '/').replace(/\\?.*$/, '');" +
            "if (images.indexOf(src) === -1) images.push(src);" +
            "}" +
            "}" +
            "var dataSrc = el.getAttribute('data-src');" +
            "if (dataSrc && dataSrc.includes('scontent')) {" +
            "var src = dataSrc.replace(/\\/[sp]\\d+x\\d+\\//g, '/').replace(/\\?.*$/, '');" +
            "if (images.indexOf(src) === -1) images.push(src);" +
            "}" +
            "}" +
            "}" +
            
            // Fallback: just use the clicked image
            "if (images.length === 0) {" +
            "var src = clickedImg.src.replace(/\\/[sp]\\d+x\\d+\\//g, '/').replace(/\\?.*$/, '');" +
            "images.push(src);" +
            "}" +
            
            "console.log('Total images found: ' + images.length);" +
            "return images;" +
            "}" +
            
            "document.addEventListener('click', function(e) {" +
            "var img = e.target.closest('img');" +
            "if (img && img.src && img.src.includes('scontent')) {" +
            "var url = window.location.href;" +
            "if (url.includes('/marketplace/item/') || url.includes('/product/')) {" +
            "e.preventDefault(); e.stopPropagation();" +
            "var images = findListingImages(img);" +
            "var clickedSrc = img.src.replace(/\\/[sp]\\d+x\\d+\\//g, '/').replace(/\\?.*$/, '');" +
            "var index = images.indexOf(clickedSrc); if (index === -1) index = 0;" +
            "showImageViewer(images, index);" +
            "return false;" +
            "}" +
            "}" +
            "}, true);" +
            
            "console.log('Image enhancements applied');" +
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
}
