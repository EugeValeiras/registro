(function() {
    'use strict';

    angular
        .module('registroApp')
        .controller('TransaccionController', TransaccionController);

    TransaccionController.$inject = ['$scope', '$state', 'Transaccion'];

    function TransaccionController ($scope, $state, Transaccion) {
        var vm = this;
        vm.transaccions = [];
        vm.loadAll = function() {
            Transaccion.query(function(result) {
                vm.transaccions = result;
            });
        };

        vm.loadAll();
        
    }
})();
