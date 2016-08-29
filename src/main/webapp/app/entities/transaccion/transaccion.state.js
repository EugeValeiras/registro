(function() {
    'use strict';

    angular
        .module('registroApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transaccion', {
            parent: 'entity',
            url: '/transaccion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Transaccions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaccion/transaccions.html',
                    controller: 'TransaccionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('transaccion-detail', {
            parent: 'entity',
            url: '/transaccion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Transaccion'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaccion/transaccion-detail.html',
                    controller: 'TransaccionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Transaccion', function($stateParams, Transaccion) {
                    return Transaccion.get({id : $stateParams.id});
                }]
            }
        })
        .state('transaccion.new', {
            parent: 'transaccion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaccion/transaccion-dialog.html',
                    controller: 'TransaccionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cantidad: null,
                                tipoDeDinero: null,
                                tipoDeTransaccion: null,
                                descripcion: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transaccion', null, { reload: true });
                }, function() {
                    $state.go('transaccion');
                });
            }]
        })
        .state('transaccion.edit', {
            parent: 'transaccion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaccion/transaccion-dialog.html',
                    controller: 'TransaccionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transaccion', function(Transaccion) {
                            return Transaccion.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaccion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transaccion.delete', {
            parent: 'transaccion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaccion/transaccion-delete-dialog.html',
                    controller: 'TransaccionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Transaccion', function(Transaccion) {
                            return Transaccion.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaccion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
