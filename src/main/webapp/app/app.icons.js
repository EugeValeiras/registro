(function() {
    'use strict';

    angular
        .module('registroApp')
        .config(iconConfig);

    iconConfig.$inject = ['$mdIconProvider'];

    function iconConfig($mdIconProvider){
	   $mdIconProvider
	       .iconSet('social', 'content/fonts/social-icons.svg', 24)
	       .defaultIconSet('content/fonts/core-icons.svg', 24);
	};

})();