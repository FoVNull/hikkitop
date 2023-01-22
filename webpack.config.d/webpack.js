config.resolve.modules.push("../../processedResources/frontend/main");
config.resolve.modules.h

// config.devServer.allowedHosts = "all"
if (config.devServer) {
    config.devServer.allowedHosts = "all"
    config.devServer.hot = true;
    config.devtool = 'eval-cheap-source-map';
} else {
    config.devtool = undefined;
}

// disable bundle size warning
config.performance = {
    assetFilter: function (assetFilename) {
      return !assetFilename.endsWith('.js');
    },
};
