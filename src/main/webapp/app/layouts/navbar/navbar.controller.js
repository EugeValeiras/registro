(function() {
    'use strict';

    angular
        .module('registroApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$location', '$mdDialog','$state', '$rootScope', '$scope', 'Auth', 'Principal', 'ENV', 'LoginService', 'TransaccionService'];

    function NavbarController ($location, $mdDialog, $state, $scope, $rootScope, Auth, Principal, ENV, LoginService, TransaccionService) {
        var ctrl = this;
        var originatorEv;

        $rootScope.$watch('transaccions', function(){ 
            ctrl.updateCaja();
        })

        ctrl.navCollapsed = true;
        ctrl.isAuthenticated = Principal.isAuthenticated;
        ctrl.inProduction = ENV === 'prod';
        ctrl.login = login;
        ctrl.logout = logout;
        ctrl.$state = $state;

        ctrl.updateCaja = function updateCaja(){
            $scope.pesosArgentinos = TransaccionService.getCantidad($rootScope.transaccions, 'PESO_ARGENTINO');
            $scope.pesosUruguayos = TransaccionService.getCantidad($rootScope.transaccions, 'PESO_URUGUAYO');;
            $scope.dolares = TransaccionService.getCantidad($rootScope.transaccions, 'DOLAR');;
        };


        this.openMenu = function openMenu($mdOpenMenu, ev) {
            originatorEv = ev;
            $mdOpenMenu(ev);
        };

        function login () {
            LoginService.open();
        }

        function logout () {
            Auth.logout();
            $rootScope.transaccions = []
            $state.go('home');
        }
    }
})();