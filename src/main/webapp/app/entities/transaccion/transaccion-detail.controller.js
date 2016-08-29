(function() {
    'use strict';

    angular
        .module('registroApp')
        .controller('TransaccionDetailController', TransaccionDetailController);

    TransaccionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Transaccion', 'User'];

    function TransaccionDetailController($scope, $rootScope, $stateParams, entity, Transaccion, User) {
        var vm = this;
        vm.transaccion = entity;
        vm.load = function (id) {
            Transaccion.get({id: id}, function(result) {
                vm.transaccion = result;
            });
        };
        var unsubscribe = $rootScope.$on('registroApp:transaccionUpdate', function(event, result) {
            vm.transaccion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
