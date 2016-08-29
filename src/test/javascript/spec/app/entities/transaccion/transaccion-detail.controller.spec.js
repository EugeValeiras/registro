'use strict';

describe('Controller Tests', function() {

    describe('Transaccion Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTransaccion, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTransaccion = jasmine.createSpy('MockTransaccion');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Transaccion': MockTransaccion,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("TransaccionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'registroApp:transaccionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
