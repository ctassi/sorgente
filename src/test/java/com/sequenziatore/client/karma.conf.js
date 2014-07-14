    module.exports = function(config){
        config.set({

            files : [
                'lib/angular/angular.js',
                'lib/angular/angular-route.min.js',
                'lib/angular/angular-cookies.min.js',
                'lib/angular/angular-local-storage.min.js',
                'lib/angular/angular-file-upload.js',
                'lib/angular/angular-pickadate.js',
                'lib/angular/angular-mocks.js',
                'lib/angular/ng-map.min.js',
                'lib/angular/angular-easyfb.js',
                'lib/jquery/jquery.min.js',
                '../../../../../main/webapp/applicationContent/com/sequenziatore/client/**/*.js',
                'unit/**/*Spec.js'
            ],

            autoWatch : true,

            frameworks: ['jasmine'],

            // browsers : ['Chrome', 'Firefox', 'Safari'],
            browsers : ['Chrome'],

            plugins : [
                'karma-chrome-launcher',
                'karma-firefox-launcher',
                'karma-jasmine',
                'karma-coverage'
            ],

            reporters: ["coverage"],
            preprocessors: {
                "../../../../../main/webapp/applicationContent/com/sequenziatore/client/**/*.js": "coverage"
            }

        });
    };