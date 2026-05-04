(function() {
  delete window.ReactNativeWebView;
  delete window.__REACT_DEVTOOLS_GLOBAL_HOOK__;
  window._mpDebug && console.log('Anti-detection applied');
})();
