(function() {
    'use strict';

    angular
        .module('registroApp')
        .controller('TransaccionDialogController', TransaccionDialogController);

    TransaccionDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Transaccion', 'User'];

    function TransaccionDialogController ($scope, $stateParams, $uibModalInstance, entity, Transaccion, User) {
        var vm = this;
        vm.transaccion = entity;
        vm.users = User.query();
        vm.load = function(id) {
            Transaccion.get({id : id}, function(result) {
                vm.transaccion = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('registroApp:transaccionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.transaccion.id !== null) {
                Transaccion.update(vm.transaccion, onSaveSuccess, onSaveError);
            } else {
                Transaccion.save(vm.transaccion, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
