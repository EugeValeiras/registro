(function() {
    'use strict';

    angular
        .module('registroApp')
        .controller('TransaccionDeleteController',TransaccionDeleteController);

    TransaccionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Transaccion'];

    function TransaccionDeleteController($uibModalInstance, entity, Transaccion) {
        var vm = this;
        vm.transaccion = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Transaccion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
