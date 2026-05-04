(function() {
  if (window._textareaFixAdded) return;
  window._textareaFixAdded = true;
  window._mpDebug && console.log('Textarea fix loading...');

  var textareaStyle = document.createElement('style');
  textareaStyle.id = 'marketplace-textarea-fix';
  textareaStyle.textContent = `
    textarea, [contenteditable="true"] {
      overflow-y: auto !important;
      word-wrap: break-word !important;
      white-space: pre-wrap !important;
      min-height: 60px !important;
      max-height: 300px !important;
      resize: vertical !important;
    }
    [role="dialog"] textarea, [role="dialog"] [contenteditable="true"] {
      box-sizing: border-box !important;
      width: 100% !important;
    }
    [role="dialog"] form > div, [role="dialog"] [data-pagelet] > div {
      overflow: visible !important;
      display: flex !important;
      flex-direction: column !important;
    }
  `;
  if (!document.getElementById('marketplace-textarea-fix')) {
    document.head.appendChild(textareaStyle);
  }

  function autoResizeTextarea(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = Math.min(textarea.scrollHeight, 300) + 'px';
  }

  var _taDebounce = null;
  var observer = new MutationObserver(function() {
    clearTimeout(_taDebounce);
    _taDebounce = setTimeout(function() {
      document.querySelectorAll('textarea').forEach(function(textarea) {
        if (!textarea._autoResizeAdded) {
          textarea._autoResizeAdded = true;
          textarea.addEventListener('input', function() { autoResizeTextarea(textarea); });
          autoResizeTextarea(textarea);
        }
      });
    }, 200);
  });
  observer.observe(document.body, { childList: true, subtree: true });
  window._marketplaceTextareaObserver = observer;

  document.querySelectorAll('textarea').forEach(function(textarea) {
    if (!textarea._autoResizeAdded) {
      textarea._autoResizeAdded = true;
      textarea.addEventListener('input', function() { autoResizeTextarea(textarea); });
      autoResizeTextarea(textarea);
    }
  });

  window._mpDebug && console.log('Textarea fix applied');
})();
