(function() {
    'use strict';

    angular
        .module('registroApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$rootScope', 'Principal', 'LoginService', 'TransaccionService', '$mdSidenav'];

    function HomeController($scope, $rootScope, Principal, LoginService, TransaccionService, $mdSidenav) {
        var vm = this;

        $rootScope.transaccions = []

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.newTransaccion = {}
        vm.selectedTransaccion = []
        vm.isOpen = false;
        vm.tipoDeTransaccion = ""; 


        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        vm.getTransaccions = function getTransaccions() {
            TransaccionService.get(function(data) {
                $rootScope.transaccions = data;
            }, function(error) {
                debugger;
            })
        }

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                if (vm.isAuthenticated) { vm.getTransaccions(); }
            });
        }

        vm.saveTransaccion = function($event) {
            vm.newTransaccion.date = new Date();
            vm.newTransaccion.tipoDeTransaccion = vm.tipoDeTransaccion;
            TransaccionService.save(vm.newTransaccion, 
                function(data) { vm.newTransaccion = {}; vm.getTransaccions() },
                function(error) {});
        };

        vm.removeTransaccion = function(transaccionID) {
            TransaccionService.remove(transaccionID, 
                function(data) {vm.getTransaccions()},
                function(error) {});
        };

        vm.openIngreso = function openIngreso() {
            vm.tipoDeTransaccion = 'INGRESO';
            $mdSidenav('transaccionForm').toggle();
        }

        vm.openEgreso = function openIngreso() {
            vm.tipoDeTransaccion = 'EGRESO';
            $mdSidenav('transaccionForm').toggle();
        }

        vm.validateNewTransaccion = function (){
            return vm.newTransaccion.descripcion === undefined || vm.newTransaccion.descripcion === ''
            || vm.newTransaccion.cantidad === undefined || vm.newTransaccion.cantidad === ''
        }

        vm.simbolo = function simbolo(transaccion){
            switch(transaccion.tipoDeDinero){
                case 'PESO_ARGENTINO':  return '$'
                case 'PESO_URUGUAYO':  return 'U$'
                case 'DOLAR':  return 'U$D'
                default: return '-'
            }
        }

        vm.parseIfIsArgentino = function parseIfIsArgentino(transaccion){
            switch(transaccion.tipoDeDinero){
                case 'PESO_ARGENTINO':  return vm.simbolo(transaccion)+' '+vm.parseExtraccion(transaccion)
                default: return ''
            }
        }

        vm.parseIfIsUruguayo = function parseIfIsUruguayo(transaccion){
            switch(transaccion.tipoDeDinero){
                case 'PESO_URUGUAYO':  return vm.simbolo(transaccion)+' '+vm.parseExtraccion(transaccion)
                default: return ''
            }
        }

        vm.parseIfIsDolar = function parseIfIsDolar(transaccion){
            switch(transaccion.tipoDeDinero){
                case 'DOLAR':  return vm.simbolo(transaccion)+' '+vm.parseExtraccion(transaccion)
                default: return ''
            }
        }

        vm.parseExtraccion = function parseExtraccion(transaccion){
            switch(transaccion.tipoDeTransaccion){
                case 'EGRESO':  return '('+transaccion.cantidad+')'
                case 'INGRESO':  return transaccion.cantidad
                default: return '_'
            }
        }

        $scope.query = {
            order: 'date',
            limit: 10,
            page: 1
        };
    }
})();
