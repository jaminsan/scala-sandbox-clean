const path = require('path')
const glob = require('glob')
const srcDir = "./src"

const entries =
  glob.sync("**/*.test.js", { cwd: srcDir })
    .map(filePath =>
      [filePath.split("/").join("-").split(".")[0], path.resolve(srcDir, filePath)]
    )

module.exports = {
  mode: 'production',
  entry: Object.fromEntries(entries),
  output: {
    path: path.resolve(__dirname, 'dist'),
    libraryTarget: 'commonjs',
    filename: '[name].bundle.js',
  },
  module: {
    rules: [{ test: /\.js$/, use: 'babel-loader' }],
  },
  target: 'web',
  externals: /k6(\/.*)?/,
};
