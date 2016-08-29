(function() {
    'use strict';
    angular
        .module('registroApp')
        .factory('Transaccion', Transaccion)
        .service('TransaccionService', TransaccionService);

    Transaccion.$inject = ['$resource'];
    TransaccionService.$inject = ['$http'];

    function Transaccion ($resource) {
        var resourceUrl =  'api/transaccions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }

    function TransaccionService ($http){
        this.get = function get(callback, errorCallback){
            $http.get('api/logged/transaccions').success(callback).error(errorCallback)
        }
        this.save = function save(transaccion, callback, errorCallback){
            $http.post('api/logged/transaccions', transaccion).success(callback).error(errorCallback)
        }
        this.remove = function remove(transaccionID, callback, errorCallback){
            $http.delete('api/transaccions/'+transaccionID).success(callback).error(errorCallback)
        }


        var sum = function sum(acum, t) {
            var tAux = t.tipoDeTransaccion === 'EGRESO' ? t.cantidad*-1 : t.cantidad;
            return acum + tAux;
        }        

        this.getCantidad = function getCantidad(transaccions, tipo){
            return transaccions.filter(function(t){return t.tipoDeDinero === tipo}).reduce(sum , 0);
        }
    }
})();