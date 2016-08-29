(function() {
    'use strict';

    angular
        .module('registroApp')
        .config(themeConfig);

    themeConfig.$inject = ['$mdThemingProvider'];

     function themeConfig($mdThemingProvider){
        $mdThemingProvider
            .theme('default')
            .primaryPalette('blue')
            .accentPalette('indigo');
            //.backgroundPalette('blue')
    }

})();