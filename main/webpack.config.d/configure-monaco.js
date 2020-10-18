const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin');

config.plugins.push(new MonacoWebpackPlugin());
config.module.rules.push({
    test: /\.css$/,
    use: ['style-loader', 'css-loader']
});
config.module.rules.push({
    test: /\.ttf$/,
    use: ['file-loader']
});
console.log(config.module.rules)
