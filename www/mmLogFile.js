var exec = require('cordova/exec');

exports.createLogFile = function (arg0, success, error) {
    exec(success, error, 'mmLogFile', 'createLogFile', [arg0]);
};
