(function() {
    'use strict';

    angular
        .module('registroApp')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$rootScope', '$state', '$timeout', 'Auth', '$mdDialog', '$mdToast'];

    function LoginController ($rootScope, $state, $timeout, Auth, $mdDialog, $mdToast) {
        var ctrl = this;

        ctrl.authenticationError = false;
        ctrl.cancel = cancel;
        ctrl.credentials = {};
        ctrl.login = login;
        ctrl.password = null;
        ctrl.register = register;
        ctrl.rememberMe = true;
        ctrl.requestResetPassword = requestResetPassword;
        ctrl.username = null;

        $timeout(function (){angular.element('[ng-model="ctrl.username"]').focus();});

        function cancel () {
            ctrl.credentials = {
                username: null,
                password: null,
                rememberMe: true
            };
            ctrl.authenticationError = false;
            $mdDialog.cancel();
        }

        function login (event) {
            event.preventDefault();
            Auth.login({
                username: ctrl.username,
                password: ctrl.password,
                rememberMe: ctrl.rememberMe
            }).then(function () {
                $mdDialog.hide();
                ctrl.authenticationError = false;
                // If we're redirected to login, our
                // previousState is already set in the authExpiredInterceptor. When login succesful go to stored state
                if ($rootScope.redirected && $rootScope.previousStateName) {
                    $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
                    $rootScope.redirected = false;
                } else {
                    $rootScope.$broadcast('authenticationSuccess');
                }
            }).catch(function () {
                
                var toast = $mdToast.simple()
                  .textContent('Error en la Authentificacion')
                  .action('X')
                  .theme("error")
                  .position("top right")
                  .hideDelay(3000);
                $mdToast.show(toast);
            });
        }

        function register () {
            $mdDialog.cancel();
            $state.go('register');
        }

        function requestResetPassword () {
            $mdDialog.cancel();
            $state.go('requestReset');
        }
    }
})();
